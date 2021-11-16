package bih.nic.in.policesoft.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.UserHomeListener;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.UserDetails;
import bih.nic.in.policesoft.ui.changePassword.ChangePasswordFragment;
import bih.nic.in.policesoft.ui.home.HomeFragment;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;

public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserHomeListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Toolbar toolbar;
    Toolbar toolbar1;

    private ProgressDialog dialog;
    HomeFragment homeFrag;
    SQLiteDatabase db;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        TextView navMobileNum = (TextView) headerView.findViewById(R.id.nav_mobile_no);

        UserDetails user = CommonPref.getUserDetails(getApplicationContext());
        navUsername.setText(user.getUserName());
        navMobileNum.setText(user.getMobileNo().equals("anyType{}") ? "NA" : user.getMobileNo());

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_profile, R.id.nav_sync, R.id.nav_view_incentive_report, R.id.nav_change_password, R.id.nav_logOut).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem profile = menu.findItem(R.id.nav_profile);


        homeFrag = new HomeFragment();
        //homeFrag = new HomeFragment(this);
        //displaySelectedFragment(homeFrag);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                toolbar.setTitle("Police Soft Home");
                homeFrag = new HomeFragment();
                //homeFrag = new HomeFragment(this);
                displaySelectedFragment(homeFrag);
                break;
            case R.id.nav_sync:

                syncData();
                break;

            case R.id.nav_change_password:
                toolbar.setTitle("Change Password");
                displaySelectedFragment(new ChangePasswordFragment());
                break;
            case R.id.nav_logOut:
                logout();
                break;
            case R.id.nav_profile:
                if (CommonPref.getUserDetails(UserHomeActivity.this).getUserrole().equals("ASHA")) {
                    if (Utiilties.isOnline(UserHomeActivity.this)) {
//                        Intent i = new Intent(UserHomeActivity.this, ProfileActivity.class);
//                        startActivity(i);
                    } else {
                        Utiilties.showInternetAlert(this);
                    }
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    private void confirmLogout() {
        SplashActivity.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = SplashActivity.prefs.edit();
        editor.putBoolean("username", false);
        editor.putBoolean("password", false);
        editor.commit();

        UserDetails userInfo = CommonPref.getUserDetails(getApplicationContext());
        userInfo.setAuthenticated(false);
        CommonPref.setUserDetails(this, userInfo);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setIcon(R.drawable.logo)
                .setMessage("क्या आप सुनिश्चित हैं कि आप ऐप से लॉगआउट करना चाहते हैं??")
                .setCancelable(false)
                .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmLogout();
                    }
                })
                .setNegativeButton("नहीं", null)
                .show();
    }

    @Override
    public void onSyncMasterData() {
        syncData();
    }

    private void syncData() {
        if (Utiilties.isOnline(this)) {

        } else {
            Utiilties.showInternetAlert(this);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do something on back.
            // Display alert message when back button has been pressed
            //moveTaskToBack(true);
            backButtonHandler();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserHomeActivity.this);
        alertDialog.setTitle("Exit?");
        alertDialog.setMessage("Do you want to exit the app ?");
        alertDialog.setPositiveButton("[NO]", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("[YES]", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                Intent i=new Intent(HomeActivity.this,PreLoginActivity.class);
//                startActivity(i);

                finish();

            }
        });
        alertDialog.show();
    }


}
