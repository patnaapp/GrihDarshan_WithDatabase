package bih.nic.in.policesoft.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.UserHomeListener;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.databinding.ActivityAddMajorUtilitiesBinding;
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
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        ModifyTable();
        CREATE_Block_TABLEIFNOTEXIST();
        CREATE_CourtSubType_TABLEIFNOTEXIST();
        CREATE_OfficeGps_TABLEIFNOTEXIST();
        CREATE_OfficesUnderPs_TABLEIFNOTEXIST();
        CREATE_Imp_Contacts_TABLEIFNOTEXIST();
        CREATE_MajorUtilities_TABLEIFNOTEXIST();
        CREATE_PrisonMaster_TABLEIFNOTEXIST();
        CREATE_Contact_Type_TABLEIFNOTEXIST();
        CREATE_CourtType_TABLEIFNOTEXIST();
        CREATE_OfficeName_TABLEIFNOTEXIST();
        CREATE_OfficeType_TABLEIFNOTEXIST();
        CREATE_MajorUtilEntry_TABLEIFNOTEXIST();
        CREATE_MajorUtil_GpsList_TABLEIFNOTEXIST();
        CREATE_MajorUtil_Other_Facility_TABLEIFNOTEXIST();
        CREATE_FireType_TABLEIFNOTEXIST();
        CREATE_Hydrant_List_TABLEIFNOTEXIST();

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
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
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
        editor.putString("isLogin", "");
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


    public void CREATE_Block_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS BlockList (Block_Code TEXT,Block_Name TEXT, Dist_Code TEXT )");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "BlockList");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "BlockList");
        }
    }

    public void CREATE_CourtSubType_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS CourtSubType (Court_SubType_Id TEXT,Court_SubType_Name TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "CourtSubType");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "CourtSubType");
        }
    }

    public void CREATE_OfficeGps_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS InsertOfficeGps_List (Latitude TEXT, Longitude TEXT, Insp_Id TEXT, EntryBy TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "InsertOfficeGps_List");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "InsertOfficeGps_List");
        }
    }

    public void CREATE_OfficesUnderPs_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS InsertOfficesUnderPs (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, OfficeType_Code TEXT, OfficeType_Name TEXT, Office_Code TEXT, Office_Name TEXT, PoliceOwnBuild_Code TEXT, PoliceOwnBuild_Name TEXT, Khata_Num TEXT, Khesra_Num TEXT, Total_Area_Land TEXT, Other_Offices TEXT, Other_Office_Name TEXT, Address TEXT, Remarks TEXT, Houseing_Faci TEXT, LsQuarter TEXT, UsQuarter TEXT, Male_Barrack TEXT, Female_Barrack TEXT, Armoury_Magazine TEXT, Ongoing_CivilWork TEXT, Office_In_Charge TEXT, Designation TEXT, Mobile_No TEXT, Landline_No TEXT, Establish_Year TEXT, Email_id TEXT, TrainingCourseName TEXT, TrainingCourseCapacity TEXT, Sanction_Strength TEXT, Working_Strength TEXT, Division_Fun TEXT, Major_Devices_Equi TEXT, Photo TEXT, Latitude TEXT, Longitude TEXT, stateOfficeName TEXT, prosecutionOfficelevel TEXT, courtCategId TEXT, courtTypeId TEXT, courtSubTypeId TEXT, HGOfficeLevel_ID TEXT, HGStateOffice TEXT, HGDistOffice TEXT, HG_regular_Male TEXT, HG_regular_Female TEXT, HG_regular_Others TEXT, HG_volunatry_Male TEXT, HG_volunatry_Female TEXT, HG_volunatry_Others TEXT, Entry_By TEXT, Entry_Date TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "InsertOfficesUnderPs");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "InsertOfficesUnderPs");
        }
    }


    public void CREATE_Imp_Contacts_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS Insert_Imp_Contacts (Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Cntct_Type_code TEXT, Cntct_Type_Name TEXT, Officer_Name TEXT, email TEXT, Photo TEXT, Lat TEXT, Long TEXT, Block_code TEXT, Block_name TEXT, Dist_Code TEXT, Dist_name TEXT, Thana_code TEXT, Thana_name TEXT, Range_Name TEXT, Range_Code TEXT, Sub_div_code TEXT, Sub_div_name TEXT, PostOffice_Name TEXT, PostOffice_Address TEXT, Hospital_type_code TEXT, Hospital_name TEXT, Beds_capacity TEXT, Hospital_address TEXT, BusStand_type TEXT, BusStand_name TEXT, BusStand_Address TEXT, Entry_By TEXT, Officer_Contact TEXT, HospitalContact TEXT, PostOffice_Contact TEXT, School_contact TEXT, School_Address TEXT, School_Name TEXT, School_Type_code TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "Insert_Imp_Contacts");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "Insert_Imp_Contacts");
        }
    }

    public void CREATE_MajorUtilities_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS MajorUtilities_List (Util_Code TEXT, Util_Name TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "MajorUtilities_List");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "MajorUtilities_List");
        }
    }

    public void CREATE_PrisonMaster_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS PrisonMaster_List (PrisionName TEXT, PrisonCode TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "PrisonMaster_List");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "PrisonMaster_List");
        }
    }
    public void CREATE_Contact_Type_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS mst_Contact_Type (Contact_Type_Code TEXT, Contact_Type_Name TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "mst_Contact_Type");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "mst_Contact_Type");
        }
    }

    public void CREATE_CourtType_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS mst_CourtType (CourtType_Id TEXT, CourtType_Name TEXT, CourtCateg_ID TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "mst_CourtType");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "mst_CourtType");
        }
    }

    public void CREATE_OfficeName_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS mst_OfficeName (Office_Code TEXT, Officce_Name TEXT, Dist_Code TEXT, OfficeType_Code TEXT, Range_Code TEXT, SubDiv_Code TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "mst_OfficeName");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "mst_OfficeName");
        }
    }

    public void CREATE_OfficeType_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS mst_OfficeType (OfficeType_code TEXT, OfficeType_Name TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "mst_OfficeType");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "mst_OfficeType");
        }
    }

    public void CREATE_MajorUtilEntry_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS MajorUtilEntry (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, password TEXT, Range_Code TEXT, SubDiv_Code TEXT, Dist_Code TEXT, Thana_code TEXT, Major_UtilCode TEXT, Major_Crime_HeadCode TEXT, Major_Crime_HeadAddress TEXT, Chronic_Land_DistributeCode TEXT, Chronic_Land_Add TEXT, Kabrishtan_Name TEXT, Kabrishtan_VillName TEXT, Land_DetailCode TEXT, Boundary_StatusCode TEXT, Jail_TypeCode TEXT, Jail_Name TEXT, Jail_Address TEXT, Started_Year TEXT, Jail_Capacity TEXT, Type_Court_Code TEXT, Name_Of_Court TEXT, Court_Address TEXT, Fair_Festival_Name TEXT, Fair_Festival_Address TEXT, Historical_Place_Name TEXT, Historical_Place_Address TEXT, Remarks TEXT, Photo TEXT, Latitude TEXT, Longitude TEXT, Religious_PlaceType TEXT, Religious_PlaceName TEXT, Historical_Imp_Prison TEXT, Best_Practices_Prison TEXT, Reform_Activities_Prison TEXT, Fire_TypeCode TEXT, Hydrant_Type_Code TEXT, Hydrant_Name TEXT, Fire_Prone_Name TEXT, Fire_Status TEXT, Fire_Prone_Address TEXT, PerisonMale_Capcity TEXT, PerisonFemale_Capcity TEXT, PerisonOther_Capcity TEXT, Under_Trial_Male TEXT, Under_Trial_Female TEXT, Under_Trial_Other TEXT, Convicted_Male TEXT, Convicted_Female TEXT, Convicted_Other TEXT, Transit_Male TEXT, Transit_Female TEXT, Transit_Other TEXT, Male_Under_Eighteen TEXT, Female_Under_Eighteen TEXT, Other_Under_Eighteen TEXT, Male_Over_Eighteen TEXT, Female_Over_Eighteen TEXT, Other_Over_Eighteen TEXT, Male_Foreigner TEXT, Female_Foreigner TEXT, Other_Foreigner TEXT, Jail_Hospital TEXT, Jail_Kitchen TEXT, Jail_Dormitory TEXT, User_Id TEXT, Major_UtilName TEXT, Jail_Toilet TEXT, Major_UtilAddress TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "MajorUtilEntry");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "MajorUtilEntry");
        }
    }



    public void CREATE_MajorUtil_GpsList_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS MajorUtil_GpsList (Latitude TEXT, Longitude TEXT, Insp_Id TEXT, EntryBy TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "MajorUtil_GpsList");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "MajorUtil_GpsList");
        }
    }

    public void CREATE_MajorUtil_Other_Facility_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS Other_Facility (Text_Facility TEXT, Insp_Id TEXT, User_Id TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "Other_Facility");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "Other_Facility");
        }
    }




    public void CREATE_FireType_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS FireType (FireType_Name TEXT, FireType_Code TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "FireType");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "FireType");
        }
    }

    public void CREATE_Hydrant_List_TABLEIFNOTEXIST()
    {
        db = dataBaseHelper.getWritableDatabase();
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS Hydrant_List (Hydrant_Type TEXT, Hydrant_Code TEXT)");
            dataBaseHelper.getWritableDatabase().close();
            Log.e("CREATE SUCCESS ", "Hydrant_List");
            dataBaseHelper.getWritableDatabase().close();
            //db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("CREATE Failed ", "Hydrant_List");
        }
    }

    public void ModifyTable() {


        if (isColumnExists("MajorUtilities_List", "UserRole") == false) {
            AlterTable("MajorUtilities_List", "UserRole");
        }
        if (isColumnExists("mst_OfficeType", "UserRole") == false) {
            AlterTable("mst_OfficeType", "UserRole");
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
