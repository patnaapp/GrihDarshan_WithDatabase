package bih.nic.in.ashwin.ui.activity;

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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.UserHomeListener;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_Type_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.Block_List;
import bih.nic.in.ashwin.entity.Centralamount_entity;
import bih.nic.in.ashwin.entity.District_list;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.entity.Panchayat_List;
import bih.nic.in.ashwin.entity.RegisteMappingEbtity;
import bih.nic.in.ashwin.entity.RegisterDetailsEntity;
import bih.nic.in.ashwin.entity.Stateamount_entity;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.ui.changePassword.ChangePasswordFragment;
import bih.nic.in.ashwin.ui.home.HomeFragment;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserHomeListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Toolbar toolbar;

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
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ModifyTable();
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        TextView navMobileNum = (TextView) headerView.findViewById(R.id.nav_mobile_no);

        UserDetails user = CommonPref.getUserDetails(getApplicationContext());
        navUsername.setText(user.getUserName());
        navMobileNum.setText(user.getMobileNo().equals("anyType{}")? "NA" : user.getMobileNo());

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_profile, R.id.nav_sync, R.id.nav_view_incentive_report,R.id.nav_change_password, R.id.nav_logOut).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem profile = menu.findItem(R.id.nav_profile);

        if(user.getUserrole().equals("ASHA"))
        {
            profile.setVisible(true);
        }
        else
            {
            profile.setVisible(false);
        }

        homeFrag = new HomeFragment();
        //homeFrag = new HomeFragment(this);
        displaySelectedFragment(homeFrag);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                toolbar.setTitle("Ashwin Home");
                homeFrag = new HomeFragment();
                //homeFrag = new HomeFragment(this);
                displaySelectedFragment(homeFrag);
                break;
            case R.id.nav_sync:

                syncData();
                break;

            case R.id.nav_view_incentive_report:
                if(Utiilties.isOnline(UserHomeActivity.this)) {
                    Intent i = new Intent(UserHomeActivity.this, IncentiveReportActivity.class);

                    startActivity(i);
                }else{
                    Utiilties.showInternetAlert(this);
                }
                break;
            case R.id.nav_change_password:
                toolbar.setTitle("Change Password");
                displaySelectedFragment(new ChangePasswordFragment());
                break;
            case R.id.nav_logOut:
                logout();
                break;
            case R.id.nav_profile:
                if (CommonPref.getUserDetails(UserHomeActivity.this).getUserrole().equals("ASHA"))
                {
                    if(Utiilties.isOnline(UserHomeActivity.this)) {
                        Intent i = new Intent(UserHomeActivity.this, ProfileActivity.class);
                        startActivity(i);
                    }else{
                        Utiilties.showInternetAlert(this);
                    }
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void
    displaySelectedFragment(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    private void confirmLogout()
    {
        SplashActivity.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = SplashActivity.prefs.edit();
        editor.putBoolean("username", false);
        editor.putBoolean("password", false);
        editor.commit();

        UserDetails userInfo = CommonPref.getUserDetails(getApplicationContext());
        userInfo.setAuthenticated(false);
        CommonPref.setUserDetails(this, userInfo);

        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void logout()
    {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setIcon(R.drawable.asha)
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

    private void syncData()
    {
        if(Utiilties.isOnline(this)){
            new GetFinYear().execute();
        }else{
            Utiilties.showInternetAlert(this);
        }

    }


    private class GetFinYear extends AsyncTask<String, Void, ArrayList<Financial_Year>> {

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading financial year...");
            dialog.show();
        }

        @Override
        protected ArrayList<Financial_Year> doInBackground(String... param)
        {
            return WebServiceHelper.getFinancialYear();
        }

        @Override
        protected void onPostExecute(ArrayList<Financial_Year> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());


                long i = helper.setFinyr_Local(result);
                if (i > 0) {
                    new GetFinMonth().execute();
                    Toast.makeText(getApplicationContext(), "Financial year loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetFinMonth extends AsyncTask<String, Void, ArrayList<Financial_Month>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading financial month...");
            dialog.show();
        }

        @Override
        protected ArrayList<Financial_Month> doInBackground(String... param) {

            return WebServiceHelper.getFinancialMonth();
        }

        @Override
        protected void onPostExecute(ArrayList<Financial_Month> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setFinMonth_Local(result);
                if (i > 0) {

                    new GetActivityList().execute();
                    Toast.makeText(getApplicationContext(), "Financial month loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetActivityList extends AsyncTask<String, Void, ArrayList<Activity_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Activity list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param) {

            return WebServiceHelper.getActivityList();
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setActivityList_Local(result);
                if (i > 0) {

                    new GetActivityCategoryList().execute();
                    Toast.makeText(getApplicationContext(), "Activity List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetActivityCategoryList extends AsyncTask<String, Void, ArrayList<ActivityCategory_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Category list...");
            dialog.show();
        }

        @Override
        protected ArrayList<ActivityCategory_entity> doInBackground(String... param) {

            return WebServiceHelper.getActivityCAtegoryList();
        }

        @Override
        protected void onPostExecute(ArrayList<ActivityCategory_entity> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setActivityCategoryList_Local(result);
                if (i > 0) {

                    new GetActivityTypeList().execute();
                    Toast.makeText(getApplicationContext(), "Activity Category List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    new GetActivityTypeList().execute();

                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetActivityTypeList extends AsyncTask<String, Void, ArrayList<Activity_Type_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Activity Type list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_Type_entity> doInBackground(String... param) {

            return WebServiceHelper.getActivityTypeList();
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_Type_entity> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setActivityType_Local(result);
                if (i > 0) {

                    new GetDistrictList().execute();
                    Toast.makeText(getApplicationContext(), "Activity Type List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetDistrictList extends AsyncTask<String, Void, ArrayList<District_list>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading District list...");
            dialog.show();
        }

        @Override
        protected ArrayList<District_list> doInBackground(String... param) {

            return WebServiceHelper.getDistrictList();
        }

        @Override
        protected void onPostExecute(ArrayList<District_list> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setDistrictList_Local(result);
                if (i > 0) {

                    new GetBLOCKTDATA().execute();
                    Toast.makeText(getApplicationContext(), "District List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetBLOCKTDATA extends AsyncTask<String, Void, ArrayList<Block_List>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Panchayat...");
            dialog.show();
        }

        @Override
        protected ArrayList<Block_List> doInBackground(String... param) {

            return WebServiceHelper.getBlockList(CommonPref.getUserDetails(getApplicationContext()).getDistrictCode());
        }

        @Override
        protected void onPostExecute(ArrayList<Block_List> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setBlockLocal(result,CommonPref.getUserDetails(getApplicationContext()).getDistrictCode());
                if (i > 0) {
                    new GetPANCHAYATDATA().execute();

                    Toast.makeText(getApplicationContext(), "Block loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetPANCHAYATDATA extends AsyncTask<String, Void, ArrayList<Panchayat_List>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Panchayat...");
            dialog.show();
        }

        @Override
        protected ArrayList<Panchayat_List> doInBackground(String... param) {

            return WebServiceHelper.getPanchayatName(CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
        }

        @Override
        protected void onPostExecute(ArrayList<Panchayat_List> result)
        {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setPanchayatName(result,CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
                if (i > 0)
                {
                    new GetRegisterActMappingDetails().execute();
                    Toast.makeText(getApplicationContext(), "Panchayat loaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                    new GetRegisterDetails().execute();
                }

            }
        }
    }

    private class GetRegisterActMappingDetails extends AsyncTask<String, Void, ArrayList<RegisteMappingEbtity>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Register Mapping details...");
            dialog.show();
        }

        @Override
        protected ArrayList<RegisteMappingEbtity> doInBackground(String... param) {

            return WebServiceHelper.getregisterActMappingDetails();
        }

        @Override
        protected void onPostExecute(ArrayList<RegisteMappingEbtity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setregisterMapping_Local(result);
                if (i > 0) {
                    new GetRegisterDetails().execute();
                    Toast.makeText(getApplicationContext(), "Register Mapping details loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetRegisterDetails extends AsyncTask<String, Void, ArrayList<RegisterDetailsEntity>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Register details...");
            dialog.show();
        }

        @Override
        protected ArrayList<RegisterDetailsEntity> doInBackground(String... param) {

            return WebServiceHelper.getregisterDetails();
        }

        @Override
        protected void onPostExecute(ArrayList<RegisterDetailsEntity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setregisterDetails_Local(result);
                if (i > 0) {
                    new GetStateAmount().execute();
                    Toast.makeText(getApplicationContext(), "Register details loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetStateAmount extends AsyncTask<String, Void, ArrayList<Stateamount_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading state amount details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Stateamount_entity> doInBackground(String... param) {

            return WebServiceHelper.getstateamount();
        }

        @Override
        protected void onPostExecute(ArrayList<Stateamount_entity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setstateamount_Local(result);
                if (i > 0) {

                    new GetCentreAmount().execute();


                    Toast.makeText(getApplicationContext(), "state amount details loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetCentreAmount extends AsyncTask<String, Void, ArrayList<Centralamount_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading central amount details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Centralamount_entity> doInBackground(String... param) {

            return WebServiceHelper.getcentralamount();
        }

        @Override
        protected void onPostExecute(ArrayList<Centralamount_entity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setcentreamount_Local(result);
                if (i > 0) {

                    if (CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("HSC"))
                    {
                        new GetAshaWorkersList().execute();
                    }
                    if (CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("BLKBCM") || CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("ASHAFC"))
                    {
                        new GetHScList().execute();
                    }
                    else{
                        if(dialog.isShowing())
                            dialog.dismiss();

                        if(homeFrag.isVisible())
                            homeFrag.setFYearSpinner();
                    }

                    Toast.makeText(getApplicationContext(), "centre amount details loaded", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetAshaWorkersList extends AsyncTask<String, Void, ArrayList<AshaWoker_Entity>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Asha details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWoker_Entity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkerList(CommonPref.getUserDetails(getApplicationContext()).getDistrictCode(),CommonPref.getUserDetails(getApplicationContext()).getBlockCode(),CommonPref.getUserDetails(getApplicationContext()).getHSCCode());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWoker_Entity> result) {


            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());


                long i = helper.setAshaWorkerList_Local(result,CommonPref.getUserDetails(getApplicationContext()).getHSCCode(),CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
                if (i > 0) {

                    if (CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("HSC")||CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("BLKBCM")){
                        new GetAshaFacilitatorList().execute();
                    }else{
                        if(dialog.isShowing())
                            dialog.dismiss();
                        //refreshFragment();
                    }
                    Toast.makeText(getApplicationContext(), "Asha worker list loaded", Toast.LENGTH_SHORT).show();

                } else {
                    if (CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("HSC")||CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("BLKBCM")){
                        new GetAshaFacilitatorList().execute();
                    }else{
                        if(dialog.isShowing())
                            dialog.dismiss();
                        //refreshFragment();
                    }
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetAshaFacilitatorList extends AsyncTask<String, Void, ArrayList<AshaFacilitator_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Facilitator details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaFacilitator_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getFacilitatorList(CommonPref.getUserDetails(getApplicationContext()).getDistrictCode(),CommonPref.getUserDetails(getApplicationContext()).getBlockCode(),CommonPref.getUserDetails(getApplicationContext()).getHSCCode());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaFacilitator_Entity> result) {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setFacilitatorList_Local(result,CommonPref.getUserDetails(getApplicationContext()).getHSCCode(),CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
                if (i > 0) {

                    Toast.makeText(getApplicationContext(), "Facilitator list loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

                if(homeFrag.isVisible())
                    homeFrag.setFYearSpinner();
            }
        }
    }

    private class GetHScList extends AsyncTask<String, Void, ArrayList<HscList_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Hsc details...");
            dialog.show();
        }

        @Override
        protected ArrayList<HscList_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getHscList(CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
        }

        @Override
        protected void onPostExecute(ArrayList<HscList_Entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setHscList_Local(result,CommonPref.getUserDetails(getApplicationContext()).getUserID());
                if (i > 0)
                {
                    Toast.makeText(getApplicationContext(), "Hsc list loaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

                if(homeFrag.isVisible())
                    homeFrag.setFYearSpinner();
            }
        }
    }

//    public void refreshFragment(){
//        homeFrag.setFYearSpinner();
//        //f.setFYearSpinner();
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
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


    public void ModifyTable() {


        if (isColumnExists("ActivityList_Master", "MinRange") == false) {
            AlterTable("ActivityList_Master", "MinRange");
        }
        if (isColumnExists("ActivityList_Master", "MaxRange") == false) {
            AlterTable("ActivityList_Master", "MaxRange");
        }
        if (isColumnExists("ActivityList_Master", "FieldName") == false) {
            AlterTable("ActivityList_Master", "FieldName");
        }

    }

    public boolean isColumnExists(String table, String column) {
        db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (column.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        dataBaseHelper.getReadableDatabase().close();
        return false;
    }

    public void AlterTable(String tableName, String columnName) {
        try {
            db = dataBaseHelper.getWritableDatabase();
            db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT");
            Log.e("ALTER Done", tableName + "-" + columnName);
            dataBaseHelper.getWritableDatabase().close();
        } catch (Exception e) {
            Log.e("ALTER Failed", tableName + "-" + columnName);
        }
    }
}
