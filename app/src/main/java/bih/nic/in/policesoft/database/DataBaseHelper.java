package bih.nic.in.policesoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import bih.nic.in.policesoft.entity.BlockList;
import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.ContactDetailsFromServer;
import bih.nic.in.policesoft.entity.CourtSubType_Entity;
import bih.nic.in.policesoft.entity.CourtType_Entity;
import bih.nic.in.policesoft.entity.FireTypeServer;
import bih.nic.in.policesoft.entity.GetPrisionMasterServer;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.GetPrisionypeServer;
import bih.nic.in.policesoft.entity.GetTypeOfHydrantServer;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
import bih.nic.in.policesoft.entity.OfficeListFromServer;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;
import bih.nic.in.policesoft.entity.Office_Name_List_Modal;
import bih.nic.in.policesoft.entity.OtherFacility;
import bih.nic.in.policesoft.entity.OtherFacilityModel;
import bih.nic.in.policesoft.entity.UserDetails;
import bih.nic.in.policesoft.ui.activities.AddMajorUtilitiesActivity;


public class DataBaseHelper extends SQLiteOpenHelper {
    //private static String DB_PATH = "";
    private static String DB_PATH = "";
    //private static String DB_NAME = "eCountingAC.sqlite";
    private static String DB_NAME = "PACSDB1";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    SQLiteDatabase db;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 2);
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist


        } else {
            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            //this.getReadableDatabase();

            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;

    }

    public boolean databaseExist() {

        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        this.getReadableDatabase().close();
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();


    }

    public void openDataBase() throws SQLException {
        // Open the database
        this.getReadableDatabase();
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long InsertNewEntry(AddMajorUtilitiesActivity newEntryActivity, MajorUtilEntry result,String user_id) {
        long c = -1;

        try {
            DataBaseHelper placeData = new DataBaseHelper(newEntryActivity);
            SQLiteDatabase db = placeData.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("password", result.getPassword());
            values.put("Range_Code", result.getRange_Code());
            values.put("SubDiv_Code", result.getSubDiv_Code());
            values.put("Dist_Code", result.getDist_Code());
            values.put("Thana_code", result.getThana_code());
            values.put("Major_UtilCode", result.getMajor_UtilCode());
            values.put("Major_UtilName", result.getMajor_UtilName());
            values.put("Major_Crime_HeadCode", result.getMajor_Crime_HeadCode());
            values.put("Major_Crime_HeadAddress", result.getMajor_Crime_HeadAddress());
            values.put("Chronic_Land_DistributeCode", result.getChronic_Land_DistributeCode());
            values.put("Chronic_Land_Add", result.getChronic_Land_Add());
           // values.put("Kabrishtan_Name", result.getKabrishtan_Name());
            //values.put("Kabrishtan_VillName", result.getKabrishtan_VillName());
            values.put("Land_DetailCode", result.getLand_DetailCode());
            values.put("Boundary_StatusCode", result.getBoundary_StatusCode());
            values.put("Jail_TypeCode", result.getJail_TypeCode());
            values.put("Jail_Name", result.getJail_Name());
            values.put("Jail_Address", result.getJail_Address());
            values.put("Started_Year", result.getStarted_Year());
            values.put("Jail_Capacity", result.getJail_Capacity());
            values.put("Type_Court_Code", result.getType_Court_Code());
            values.put("Name_Of_Court", result.getName_Of_Court());
            values.put("Court_Address", result.getCourt_Address());
            values.put("Fair_Festival_Name", result.getFair_Festival_Name());
            values.put("Fair_Festival_Address", result.getFair_Festival_Address());
            values.put("Historical_Place_Name", result.getHistorical_Place_Name());
            values.put("Historical_Place_Address", result.getHistorical_Place_Address());
            values.put("Remarks", result.getRemarks());
            values.put("Photo", result.getPhoto());
            values.put("Latitude", result.getLatitude());
            values.put("Longitude", result.getLongitude());
            values.put("Religious_PlaceType", result.getReligious_PlaceType());
            values.put("Religious_PlaceName", result.getReligious_PlaceName());
            values.put("Historical_Imp_Prison", result.getHistorical_Imp_Prison());
            values.put("Best_Practices_Prison", result.getBest_Practices_Prison());
            values.put("Reform_Activities_Prison", result.getReform_Activities_Prison());
            values.put("Fire_TypeCode", result.getFire_TypeCode());
            values.put("Hydrant_Type_Code", result.getHydrant_Type_Code());
            values.put("Hydrant_Name", result.getHydrant_Name());
            values.put("Fire_Prone_Name", result.getFire_Prone_Name());
            values.put("Fire_Status", result.getFire_Status());
            values.put("Fire_Prone_Address", result.getFire_Prone_Address());
            values.put("PerisonMale_Capcity", result.getPerisonMale_Capcity());
            values.put("PerisonFemale_Capcity", result.getPerisonFemale_Capcity());
            values.put("PerisonOther_Capcity", result.getPerisonOther_Capcity());
            values.put("Under_Trial_Male", result.getUnder_Trial_Male());
            values.put("Under_Trial_Female", result.getUnder_Trial_Female());
            values.put("Under_Trial_Other", result.getUnder_Trial_Other());
            values.put("Convicted_Male", result.getConvicted_Male());
            values.put("Convicted_Female", result.getConvicted_Female());
            values.put("Convicted_Other", result.getConvicted_Other());
            values.put("Transit_Male", result.getTransit_Male());
            values.put("Transit_Female", result.getTransit_Female());
            values.put("Transit_Other", result.getTransit_Other());
            values.put("Male_Under_Eighteen", result.getMale_Under_Eighteen());
            values.put("Female_Under_Eighteen", result.getFemale_Under_Eighteen());
            values.put("Other_Under_Eighteen", result.getOther_Under_Eighteen());
            values.put("Male_Over_Eighteen", result.getMale_Over_Eighteen());
            values.put("Female_Over_Eighteen", result.getFemale_Over_Eighteen());
            values.put("Other_Over_Eighteen", result.getOther_Over_Eighteen());
            values.put("Male_Foreigner", result.getMale_Foreigner());
            values.put("Female_Foreigner", result.getFemale_Foreigner());
            values.put("Other_Foreigner", result.getOther_Foreigner());
            values.put("Jail_Hospital", result.getJail_Hospital());
            values.put("Jail_Kitchen", result.getJail_Kitchen());
            values.put("Jail_Dormitory", result.getJail_Dormitory());
            values.put("Jail_Toilet", result.getJail_Toilet());
            values.put("User_Id", user_id);

            c = db.insert("MajorUtilEntry", null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            return c;
        }

        return c;
    }


    //MajorUtilitiesLocal
    public long setMajorUtilitiesLocal(ArrayList<MajorUtilitiesFromServer> list) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<MajorUtilitiesFromServer> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("MajorUtilities_List", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Util_Code", info.get(i).getUtil_Code());
                    values.put("Util_Name", info.get(i).getUtil_Name());


                    String[] whereArgs = new String[]{info.get(i).getUtil_Code()};

                    c = db.update("MajorUtilities_List", values, "Util_Code=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("MajorUtilities_List", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }


    public ArrayList<MajorUtilitiesFromServer> getMajorUtilLocal() {
        ArrayList<MajorUtilitiesFromServer> bdetail = new ArrayList<MajorUtilitiesFromServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from MajorUtilities_List order by Util_Code", null);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                MajorUtilitiesFromServer financial_year = new MajorUtilitiesFromServer();
                financial_year.setUtil_Code(cur.getString(cur.getColumnIndex("Util_Code")));
                financial_year.setUtil_Name((cur.getString(cur.getColumnIndex("Util_Name"))));
                bdetail.add(financial_year);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }

    //PrisonMasterLocal
    public long setPrisonMasterLocal(ArrayList<GetPrisionMasterServer> list, String distcode, String jailtype) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<GetPrisionMasterServer> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("PrisonMaster_List", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("PrisionName", info.get(i).getJail_Name());
                    values.put("PrisonCode", info.get(i).getJail_Code());
                    values.put("Distcode", distcode);
                    values.put("JailType_Code", jailtype);

                    String[] whereArgs = new String[]{info.get(i).getJail_Code()};

                    c = db.update("PrisonMaster_List", values, "PrisonCode=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("PrisonMaster_List", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;


    }


    public ArrayList<GetPrisionMasterServer> getPrisonMasterLocal(String distcode, String jailtype_code) {
        ArrayList<GetPrisionMasterServer> bdetail = new ArrayList<GetPrisionMasterServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] whereArgs = new String[]{distcode, jailtype_code};
            Cursor cur = db.rawQuery("select * from PrisonMaster_List where Distcode=? AND JailType_Code=? order by PrisonCode", whereArgs);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                GetPrisionMasterServer prisionMaster = new GetPrisionMasterServer();
                prisionMaster.setJail_Name(cur.getString(cur.getColumnIndex("PrisionName")));
                prisionMaster.setJail_Code((cur.getString(cur.getColumnIndex("PrisonCode"))));
                bdetail.add(prisionMaster);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }


    public long setOfficeTypeLocal(ArrayList<OfficeListFromServer> list) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<OfficeListFromServer> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("mst_OfficeType", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("OfficeType_code", info.get(i).getOffice_Code());
                    values.put("OfficeType_Name", info.get(i).getOffice_Name());


                    String[] whereArgs = new String[]{info.get(i).getOffice_Code()};

                    c = db.update("mst_OfficeType", values, "OfficeType_code=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("mst_OfficeType", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }

    public ArrayList<OfficeListFromServer> getOfficeTypeLocal() {
        ArrayList<OfficeListFromServer> bdetail = new ArrayList<OfficeListFromServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from mst_OfficeType order by OfficeType_code", null);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                OfficeListFromServer financial_year = new OfficeListFromServer();
                financial_year.setOffice_Code(cur.getString(cur.getColumnIndex("OfficeType_code")));
                financial_year.setOffice_Name((cur.getString(cur.getColumnIndex("OfficeType_Name"))));
                bdetail.add(financial_year);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }

    public ArrayList<CourtType_Entity> getCourtTypeLocal(String courtcateg) {
        ArrayList<CourtType_Entity> bdetail = new ArrayList<CourtType_Entity>();
        try {
            String[] Whereargs = {courtcateg};
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from mst_CourtType where CourtCateg_ID=?", Whereargs);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                CourtType_Entity financial_year = new CourtType_Entity();
                financial_year.set_CourtId(cur.getString(cur.getColumnIndex("CourtType_Id")));
                financial_year.set_CourtName((cur.getString(cur.getColumnIndex("CourtType_Name"))));
                bdetail.add(financial_year);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }


    public long setCourtLocal(ArrayList<CourtType_Entity> list, String courtcateg) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<CourtType_Entity> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("mst_CourtType", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("CourtType_Id", info.get(i).get_CourtId());
                    values.put("CourtType_Name", info.get(i).get_CourtName());
                    values.put("CourtCateg_ID", courtcateg);

                    String[] whereArgs = new String[]{info.get(i).get_CourtId()};

                    c = db.update("mst_CourtType", values, "CourtType_Id=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("mst_CourtType", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }

    public long setOfficeNameMaster(ArrayList<Office_Name_List_Modal> list) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<Office_Name_List_Modal> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("mst_OfficeName", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Office_Code", info.get(i).getOfficeCode());
                    values.put("Officce_Name", info.get(i).getOfficeName());
                    values.put("Dist_Code", info.get(i).getDistrict_Code());
                    values.put("OfficeType_Code", info.get(i).getOffice_Type_Code());
                    values.put("Range_Code", info.get(i).getRange_Code());
                    values.put("SubDiv_Code", info.get(i).getSubdivision_Code());

                    String[] whereArgs = new String[]{info.get(i).getOfficeCode()};

                    c = db.update("mst_OfficeName", values, "Office_Code=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("mst_OfficeName", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }

    public ArrayList<Office_Name_List_Modal> getOfficeNameLocal(String officeType, String rangecode) {
        ArrayList<Office_Name_List_Modal> bdetail = new ArrayList<Office_Name_List_Modal>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] whereArgs = new String[]{officeType, rangecode};
            Cursor cur = db.rawQuery("select * from mst_OfficeName where OfficeType_Code=? or Range_Code=?", whereArgs);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                Office_Name_List_Modal financial_year = new Office_Name_List_Modal();
                financial_year.setOfficeCode(cur.getString(cur.getColumnIndex("Office_Code")));
                financial_year.setOfficeName((cur.getString(cur.getColumnIndex("Officce_Name"))));
                financial_year.setOffice_Type_Code((cur.getString(cur.getColumnIndex("OfficeType_Code"))));
                financial_year.setDistrict_Code((cur.getString(cur.getColumnIndex("Dist_Code"))));
                financial_year.setRange_Code((cur.getString(cur.getColumnIndex("Range_Code"))));
                financial_year.setSubdivision_Code((cur.getString(cur.getColumnIndex("SubDiv_Code"))));
                bdetail.add(financial_year);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }


    public long setCourtSubTypeMaster(ArrayList<CourtSubType_Entity> list) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<CourtSubType_Entity> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("CourtSubType", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Court_SubType_Id", info.get(i).get_Court_Sub_Type_Id());
                    values.put("Court_SubType_Name", info.get(i).get_Court_Sub_Type_Name());

                    String[] whereArgs = new String[]{info.get(i).get_Court_Sub_Type_Id()};

                    c = db.update("CourtSubType", values, "Court_SubType_Id=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("CourtSubType", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }


    public ArrayList<CourtSubType_Entity> getCourtSubTypeLocal() {
        ArrayList<CourtSubType_Entity> bdetail = new ArrayList<CourtSubType_Entity>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cur = db.rawQuery("select * from CourtSubType order by Court_SubType_Id", null);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                CourtSubType_Entity financial_year = new CourtSubType_Entity();
                financial_year.set_Court_Sub_Type_Id(cur.getString(cur.getColumnIndex("Court_SubType_Id")));
                financial_year.set_Court_Sub_Type_Name((cur.getString(cur.getColumnIndex("Court_SubType_Name"))));

                bdetail.add(financial_year);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }


    //PrisonTypeLocal
    public long setPrisonTypeLocal(ArrayList<GetPrisionypeServer> list) {
        long c = -1;
        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<GetPrisionypeServer> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("PrisonType_List", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Jail_Type", info.get(i).getJail_Type());
                    values.put("Jail_Type_Code", info.get(i).getJail_Type_Code());

                    String[] whereArgs = new String[]{info.get(i).getJail_Type()};

                    c = db.update("PrisonType_List", values, "Jail_Type=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("PrisonType_List", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;


    }


    public ArrayList<GetPrisionypeServer> getPrisonTypeLocal() {
        ArrayList<GetPrisionypeServer> bdetail = new ArrayList<GetPrisionypeServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from PrisonType_List order by Jail_Type_Code", null);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                GetPrisionypeServer prisionType = new GetPrisionypeServer();
                prisionType.setJail_Type_Code(cur.getString(cur.getColumnIndex("Jail_Type_Code")));
                prisionType.setJail_Type((cur.getString(cur.getColumnIndex("Jail_Type"))));
                bdetail.add(prisionType);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }

    //FireTypeLocal
    public long setFireTypeLocal(ArrayList<FireTypeServer> list) {
        long c = -1;
        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<FireTypeServer> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("FireType", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("FireType_Name", info.get(i).getFireType_Name());
                    values.put("FireType_Code", info.get(i).getFireType_Code());

                    String[] whereArgs = new String[]{info.get(i).getFireType_Name()};

                    c = db.update("FireType", values, "FireType_Name=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("FireType", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;


    }

    public ArrayList<FireTypeServer> getFireTypeLocal() {
        ArrayList<FireTypeServer> bdetail = new ArrayList<FireTypeServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from FireType order by FireType_Code", null);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                FireTypeServer FireType = new FireTypeServer();
                FireType.setFireType_Code(cur.getString(cur.getColumnIndex("FireType_Code")));
                FireType.setFireType_Name((cur.getString(cur.getColumnIndex("FireType_Name"))));
                bdetail.add(FireType);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }

    // TypeOfHydrantLocal
    public long setTypeOfHydrantLocal(ArrayList<GetTypeOfHydrantServer> list) {
        long c = -1;
        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<GetTypeOfHydrantServer> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("Hydrant_List", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Hydrant_Type", info.get(i).getHydrant_Type());
                    values.put("Hydrant_Code", info.get(i).getHydrant_Code());

                    String[] whereArgs = new String[]{info.get(i).getHydrant_Type()};

                    c = db.update("Hydrant_List", values, "Hydrant_Type=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("Hydrant_List", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;


    }

    public ArrayList<GetTypeOfHydrantServer> getTypeofHydrantLocal() {
        ArrayList<GetTypeOfHydrantServer> bdetail = new ArrayList<GetTypeOfHydrantServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from Hydrant_List order by Hydrant_Code", null);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                GetTypeOfHydrantServer hydrantServer = new GetTypeOfHydrantServer();
                hydrantServer.setHydrant_Type(cur.getString(cur.getColumnIndex("Hydrant_Type")));
                hydrantServer.setHydrant_Code((cur.getString(cur.getColumnIndex("Hydrant_Code"))));
                bdetail.add(hydrantServer);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }


    public long InsertOfficeDetails(OfficeUnderPsEntity result, String userid) {

        long c = 0;
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("OfficeType_Code", result.getOfficeType_Code());
            values.put("OfficeType_Name", result.getOfficeType_Name());
            values.put("Office_Code", result.getOffice_Code());
            values.put("Office_Name", result.getOffice_Name());
            values.put("PoliceOwnBuild_Code", result.getPoliceOwnBuild_Code());
            values.put("PoliceOwnBuild_Name", result.getPoliceOwnBuild_Name());
            values.put("Khata_Num", result.getKhata_Num());
            values.put("Khesra_Num", result.getKhesra_Num());
            values.put("Total_Area_Land", result.getTotal_Area_Land());
            values.put("Other_Offices", result.getOther_Offices());
            values.put("Other_Office_Name", result.getOther_Office_Name());
            values.put("Address", result.getAddress());
            values.put("Remarks", result.getRemarks());
            values.put("Houseing_Faci", result.getHouseing_Faci());
            values.put("LsQuarter", result.getLsQuarter());
            values.put("UsQuarter", result.getUsQuarter());
            values.put("Male_Barrack", result.getMale_Barrack());
            values.put("Female_Barrack", result.getFemale_Barrack());
            values.put("Armoury_Magazine", result.getArmoury_Magazine());
            values.put("Ongoing_CivilWork", result.getOngoing_CivilWork());
            values.put("Office_In_Charge", result.getOffice_In_Charge());
            values.put("Designation", result.getDesignation());
            values.put("Mobile_No", result.getMobile_No());
            values.put("Landline_No", result.getLandline_No());
            values.put("Establish_Year", result.getEstablish_Year());
            values.put("Email_id", result.getEmail_id());
            values.put("TrainingCourseName", result.getTrainingCourseName());
            values.put("TrainingCourseCapacity", result.getTrainingCourseCapacity());
            values.put("Sanction_Strength", result.getSanction_Strength());
            values.put("Working_Strength", result.getWorking_Strength());
            values.put("Division_Fun", result.getDivision_Fun());
            values.put("Major_Devices_Equi", result.getMajor_Devices_Equi());
            values.put("Photo", result.getPhoto());
            values.put("Latitude", result.getLatitude());
            values.put("Longitude", result.getLongitude());
            values.put("stateOfficeName", result.getStateOfficeName());
            values.put("prosecutionOfficelevel", result.getProsecutionOfficelevel());
            values.put("courtCategId", result.getCourtCategId());

            values.put("courtTypeId", result.getCourtTypeId());
            values.put("courtSubTypeId", result.getCourtSubTypeId());
            values.put("HGOfficeLevel_ID", result.getHGOfficeLevel_ID());

            values.put("HGStateOffice", result.getHGStateOffice());
            values.put("HGDistOffice", result.getHGDistOffice());

           // Log.e("HG_regular_Male", result.getHG_regular_Male());
            values.put("HG_regular_Male", result.getHG_regular_Male());
            values.put("HG_regular_Female", result.getHG_regular_Female());
            values.put("HG_regular_Others", result.getHG_regular_Others());

            values.put("HG_volunatry_Male", result.getHG_volunatry_Male());
            values.put("HG_volunatry_Female", result.getHG_volunatry_Female());
            values.put("HG_volunatry_Others", result.getHG_volunatry_Others());
            values.put("Entry_By", userid);


            c = db.insert("InsertOfficesUnderPs", null, values);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return c;
        }
        return c;

    }

    //getAllMajorUtilEntryDetail
    public ArrayList<MajorUtilEntry> getAllMajorUtilEntryDetail(String Userid) {
        ArrayList<MajorUtilEntry> majorUtil_basic_details = new ArrayList<MajorUtilEntry>();
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid};
            Cursor cursor =  sqLiteDatabase.rawQuery("Select *  from MajorUtilEntry where User_Id=? ORDER BY Id  DESC", args);
                    int x = cursor.getCount();
            while (cursor.moveToNext()){
                MajorUtilEntry majorUtilInfo = new MajorUtilEntry();
                String rowID = cursor.getString(cursor.getColumnIndex("id"));
                majorUtilInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
                majorUtilInfo.setUser_Id(cursor.getString(cursor.getColumnIndex("User_Id")));
                majorUtilInfo.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                majorUtilInfo.setRange_Code(cursor.getString(cursor.getColumnIndex("Range_Code")));
                majorUtilInfo.setSubDiv_Code(cursor.getString(cursor.getColumnIndex("SubDiv_Code")));
                majorUtilInfo.setDist_Code(cursor.getString(cursor.getColumnIndex("Dist_Code")));
                majorUtilInfo.setThana_code(cursor.getString(cursor.getColumnIndex("Thana_code")));
                majorUtilInfo.setMajor_UtilCode(cursor.getString(cursor.getColumnIndex("Major_UtilCode")));
                majorUtilInfo.setMajor_UtilName(cursor.getString(cursor.getColumnIndex("Major_UtilName")));
                majorUtilInfo.setMajor_Crime_HeadCode(cursor.getString(cursor.getColumnIndex("Major_Crime_HeadCode")));
                majorUtilInfo.setMajor_Crime_HeadAddress(cursor.getString(cursor.getColumnIndex("Major_Crime_HeadAddress")));
                majorUtilInfo.setChronic_Land_DistributeCode(cursor.getString(cursor.getColumnIndex("Chronic_Land_DistributeCode")));
                majorUtilInfo.setChronic_Land_Add(cursor.getString(cursor.getColumnIndex("Chronic_Land_Add")));
                majorUtilInfo.setKabrishtan_Name(cursor.getString(cursor.getColumnIndex("Kabrishtan_Name")));
                majorUtilInfo.setKabrishtan_VillName(cursor.getString(cursor.getColumnIndex("Kabrishtan_VillName")));
                majorUtilInfo.setLand_DetailCode(cursor.getString(cursor.getColumnIndex("Land_DetailCode")));
                majorUtilInfo.setBoundary_StatusCode(cursor.getString(cursor.getColumnIndex("Boundary_StatusCode")));
                majorUtilInfo.setJail_TypeCode(cursor.getString(cursor.getColumnIndex("Jail_TypeCode")));
                majorUtilInfo.setJail_Name(cursor.getString(cursor.getColumnIndex("Jail_Name")));
                majorUtilInfo.setJail_Address(cursor.getString(cursor.getColumnIndex("Jail_Address")));
                majorUtilInfo.setStarted_Year(cursor.getString(cursor.getColumnIndex("Started_Year")));
                majorUtilInfo.setJail_Capacity(cursor.getString(cursor.getColumnIndex("Jail_Capacity")));
                majorUtilInfo.setType_Court_Code(cursor.getString(cursor.getColumnIndex("Type_Court_Code")));
                majorUtilInfo.setName_Of_Court(cursor.getString(cursor.getColumnIndex("Name_Of_Court")));
                majorUtilInfo.setCourt_Address(cursor.getString(cursor.getColumnIndex("Court_Address")));
                majorUtilInfo.setFair_Festival_Name(cursor.getString(cursor.getColumnIndex("Fair_Festival_Name")));
                majorUtilInfo.setFair_Festival_Address(cursor.getString(cursor.getColumnIndex("Fair_Festival_Address")));
                majorUtilInfo.setHistorical_Place_Name(cursor.getString(cursor.getColumnIndex("Historical_Place_Name")));
                majorUtilInfo.setHistorical_Place_Address(cursor.getString(cursor.getColumnIndex("Historical_Place_Address")));
                majorUtilInfo.setRemarks(cursor.getString(cursor.getColumnIndex("Remarks")));
               // majorUtilInfo.setPhoto(cursor.getString(cursor.getColumnIndex("Photo")));
                majorUtilInfo.setLatitude(cursor.getString(cursor.getColumnIndex("Latitude")));
                majorUtilInfo.setLongitude(cursor.getString(cursor.getColumnIndex("Longitude")));
                majorUtilInfo.setReligious_PlaceType(cursor.getString(cursor.getColumnIndex("Religious_PlaceType")));
                majorUtilInfo.setReligious_PlaceName(cursor.getString(cursor.getColumnIndex("Religious_PlaceName")));
                majorUtilInfo.setHistorical_Imp_Prison(cursor.getString(cursor.getColumnIndex("Historical_Imp_Prison")));
                majorUtilInfo.setBest_Practices_Prison(cursor.getString(cursor.getColumnIndex("Best_Practices_Prison")));
                majorUtilInfo.setReform_Activities_Prison(cursor.getString(cursor.getColumnIndex("Reform_Activities_Prison")));
                majorUtilInfo.setFire_TypeCode(cursor.getString(cursor.getColumnIndex("Fire_TypeCode")));
                majorUtilInfo.setHydrant_Type_Code(cursor.getString(cursor.getColumnIndex("Hydrant_Type_Code")));
                majorUtilInfo.setHydrant_Name(cursor.getString(cursor.getColumnIndex("Hydrant_Name")));
                majorUtilInfo.setFire_Prone_Name(cursor.getString(cursor.getColumnIndex("Fire_Prone_Name")));
                majorUtilInfo.setFire_Status(cursor.getString(cursor.getColumnIndex("Fire_Status")));
                majorUtilInfo.setFire_Prone_Address(cursor.getString(cursor.getColumnIndex("Fire_Prone_Address")));
                majorUtilInfo.setPerisonMale_Capcity(cursor.getString(cursor.getColumnIndex("PerisonMale_Capcity")));
                majorUtilInfo.setPerisonFemale_Capcity(cursor.getString(cursor.getColumnIndex("PerisonFemale_Capcity")));
                majorUtilInfo.setPerisonOther_Capcity(cursor.getString(cursor.getColumnIndex("PerisonOther_Capcity")));
                majorUtilInfo.setUnder_Trial_Male(cursor.getString(cursor.getColumnIndex("Under_Trial_Male")));
                majorUtilInfo.setUnder_Trial_Female(cursor.getString(cursor.getColumnIndex("Under_Trial_Female")));
                majorUtilInfo.setUnder_Trial_Other(cursor.getString(cursor.getColumnIndex("Under_Trial_Other")));
                majorUtilInfo.setConvicted_Male(cursor.getString(cursor.getColumnIndex("Convicted_Male")));
                majorUtilInfo.setConvicted_Female(cursor.getString(cursor.getColumnIndex("Convicted_Female")));
                majorUtilInfo.setConvicted_Other(cursor.getString(cursor.getColumnIndex("Convicted_Other")));
                majorUtilInfo.setTransit_Male(cursor.getString(cursor.getColumnIndex("Transit_Male")));
                majorUtilInfo.setTransit_Female(cursor.getString(cursor.getColumnIndex("Transit_Female")));
                majorUtilInfo.setTransit_Other(cursor.getString(cursor.getColumnIndex("Transit_Other")));
                majorUtilInfo.setMale_Under_Eighteen(cursor.getString(cursor.getColumnIndex("Male_Under_Eighteen")));
                majorUtilInfo.setFemale_Under_Eighteen(cursor.getString(cursor.getColumnIndex("Female_Under_Eighteen")));
                majorUtilInfo.setOther_Under_Eighteen(cursor.getString(cursor.getColumnIndex("Other_Under_Eighteen")));
                majorUtilInfo.setMale_Over_Eighteen(cursor.getString(cursor.getColumnIndex("Male_Over_Eighteen")));
                majorUtilInfo.setFemale_Over_Eighteen(cursor.getString(cursor.getColumnIndex("Female_Over_Eighteen")));
                majorUtilInfo.setOther_Over_Eighteen(cursor.getString(cursor.getColumnIndex("Other_Over_Eighteen")));
                majorUtilInfo.setMale_Foreigner(cursor.getString(cursor.getColumnIndex("Male_Foreigner")));
                majorUtilInfo.setFemale_Foreigner(cursor.getString(cursor.getColumnIndex("Female_Foreigner")));
                majorUtilInfo.setOther_Foreigner(cursor.getString(cursor.getColumnIndex("Other_Foreigner")));
                majorUtilInfo.setJail_Hospital(cursor.getString(cursor.getColumnIndex("Jail_Hospital")));
                majorUtilInfo.setJail_Kitchen(cursor.getString(cursor.getColumnIndex("Jail_Kitchen")));
                majorUtilInfo.setJail_Dormitory(cursor.getString(cursor.getColumnIndex("Jail_Dormitory")));


                String[] args2 = {rowID};
                String selectSQL = "select Photo From MajorUtilEntry where Id=? ORDER BY Id  DESC";
                Cursor cursor1 = sqLiteDatabase.rawQuery(selectSQL, args2);
                Log.e("USERID", Userid);
                while (cursor1.moveToNext()) {
                    majorUtilInfo.setPhoto((cursor1.getString(cursor1.getColumnIndex("Photo"))));

                }

                //basicdetail.add(basicInfo);

                majorUtil_basic_details.add(majorUtilInfo);
                cursor1.close();
            }
            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();


        } catch (Exception e) {
            e.printStackTrace();
            majorUtil_basic_details = null;
            // TODO: handle exception

        }
        return majorUtil_basic_details;
    }
    //getAllMajorDetailsingle
    public MajorUtilEntry getAllMajorDetailsingle(String Userid, String rowid) {
        //ArrayList<MajorUtilEntry> majorUtil_basic_details = new ArrayList<MajorUtilEntry>();

        MajorUtilEntry majorUtilInfo = new MajorUtilEntry();

        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid,rowid};
            Cursor cursor =  sqLiteDatabase.rawQuery("Select * from MajorUtilEntry where User_Id=? AND id=?  ORDER BY id  DESC", args);
            int x = cursor.getCount();
            while (cursor.moveToNext()){
                String rowID = cursor.getString(cursor.getColumnIndex("id"));
                majorUtilInfo.setId((cursor.getString(cursor.getColumnIndex("id"))));
                majorUtilInfo.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                majorUtilInfo.setRange_Code(cursor.getString(cursor.getColumnIndex("Range_Code")));
                majorUtilInfo.setSubDiv_Code(cursor.getString(cursor.getColumnIndex("SubDiv_Code")));
                majorUtilInfo.setDist_Code(cursor.getString(cursor.getColumnIndex("Dist_Code")));
                majorUtilInfo.setThana_code(cursor.getString(cursor.getColumnIndex("Thana_code")));
                majorUtilInfo.setMajor_UtilCode(cursor.getString(cursor.getColumnIndex("Major_UtilCode")));
                majorUtilInfo.setMajor_UtilName(cursor.getString(cursor.getColumnIndex("Major_UtilName")));
                majorUtilInfo.setMajor_Crime_HeadCode(cursor.getString(cursor.getColumnIndex("Major_Crime_HeadCode")));
                majorUtilInfo.setMajor_Crime_HeadAddress(cursor.getString(cursor.getColumnIndex("Major_Crime_HeadAddress")));
                majorUtilInfo.setChronic_Land_DistributeCode(cursor.getString(cursor.getColumnIndex("Chronic_Land_DistributeCode")));
                majorUtilInfo.setChronic_Land_Add(cursor.getString(cursor.getColumnIndex("Chronic_Land_Add")));
                majorUtilInfo.setKabrishtan_Name(cursor.getString(cursor.getColumnIndex("Kabrishtan_Name")));
                majorUtilInfo.setKabrishtan_VillName(cursor.getString(cursor.getColumnIndex("Kabrishtan_VillName")));
                majorUtilInfo.setLand_DetailCode(cursor.getString(cursor.getColumnIndex("Land_DetailCode")));
                majorUtilInfo.setBoundary_StatusCode(cursor.getString(cursor.getColumnIndex("Boundary_StatusCode")));
                majorUtilInfo.setJail_TypeCode(cursor.getString(cursor.getColumnIndex("Jail_TypeCode")));
                majorUtilInfo.setJail_Name(cursor.getString(cursor.getColumnIndex("Jail_Name")));
                majorUtilInfo.setJail_Address(cursor.getString(cursor.getColumnIndex("Jail_Address")));
                majorUtilInfo.setStarted_Year(cursor.getString(cursor.getColumnIndex("Started_Year")));
                majorUtilInfo.setJail_Capacity(cursor.getString(cursor.getColumnIndex("Jail_Capacity")));
                majorUtilInfo.setType_Court_Code(cursor.getString(cursor.getColumnIndex("Type_Court_Code")));
                majorUtilInfo.setName_Of_Court(cursor.getString(cursor.getColumnIndex("Name_Of_Court")));
                majorUtilInfo.setCourt_Address(cursor.getString(cursor.getColumnIndex("Court_Address")));
                majorUtilInfo.setFair_Festival_Name(cursor.getString(cursor.getColumnIndex("Fair_Festival_Name")));
                majorUtilInfo.setFair_Festival_Address(cursor.getString(cursor.getColumnIndex("Fair_Festival_Address")));
                majorUtilInfo.setHistorical_Place_Name(cursor.getString(cursor.getColumnIndex("Historical_Place_Name")));
                majorUtilInfo.setHistorical_Place_Address(cursor.getString(cursor.getColumnIndex("Historical_Place_Address")));
                majorUtilInfo.setRemarks(cursor.getString(cursor.getColumnIndex("Remarks")));
                // majorUtilInfo.setPhoto(cursor.getString(cursor.getColumnIndex("Photo")));
                majorUtilInfo.setLatitude(cursor.getString(cursor.getColumnIndex("Latitude")));
                majorUtilInfo.setLongitude(cursor.getString(cursor.getColumnIndex("Longitude")));
                majorUtilInfo.setReligious_PlaceType(cursor.getString(cursor.getColumnIndex("Religious_PlaceType")));
                majorUtilInfo.setReligious_PlaceName(cursor.getString(cursor.getColumnIndex("Religious_PlaceName")));
                majorUtilInfo.setHistorical_Imp_Prison(cursor.getString(cursor.getColumnIndex("Historical_Imp_Prison")));
                majorUtilInfo.setBest_Practices_Prison(cursor.getString(cursor.getColumnIndex("Best_Practices_Prison")));
                majorUtilInfo.setReform_Activities_Prison(cursor.getString(cursor.getColumnIndex("Reform_Activities_Prison")));
                majorUtilInfo.setFire_TypeCode(cursor.getString(cursor.getColumnIndex("Fire_TypeCode")));
                majorUtilInfo.setHydrant_Type_Code(cursor.getString(cursor.getColumnIndex("Hydrant_Type_Code")));
                majorUtilInfo.setHydrant_Name(cursor.getString(cursor.getColumnIndex("Hydrant_Name")));
                majorUtilInfo.setFire_Prone_Name(cursor.getString(cursor.getColumnIndex("Fire_Prone_Name")));
                majorUtilInfo.setFire_Status(cursor.getString(cursor.getColumnIndex("Fire_Status")));
                majorUtilInfo.setFire_Prone_Address(cursor.getString(cursor.getColumnIndex("Fire_Prone_Address")));
                majorUtilInfo.setPerisonMale_Capcity(cursor.getString(cursor.getColumnIndex("PerisonMale_Capcity")));
                majorUtilInfo.setPerisonFemale_Capcity(cursor.getString(cursor.getColumnIndex("PerisonFemale_Capcity")));
                majorUtilInfo.setPerisonOther_Capcity(cursor.getString(cursor.getColumnIndex("PerisonOther_Capcity")));
                majorUtilInfo.setUnder_Trial_Male(cursor.getString(cursor.getColumnIndex("Under_Trial_Male")));
                majorUtilInfo.setUnder_Trial_Female(cursor.getString(cursor.getColumnIndex("Under_Trial_Female")));
                majorUtilInfo.setUnder_Trial_Other(cursor.getString(cursor.getColumnIndex("Under_Trial_Other")));
                majorUtilInfo.setConvicted_Male(cursor.getString(cursor.getColumnIndex("Convicted_Male")));
                majorUtilInfo.setConvicted_Female(cursor.getString(cursor.getColumnIndex("Convicted_Female")));
                majorUtilInfo.setConvicted_Other(cursor.getString(cursor.getColumnIndex("Convicted_Other")));
                majorUtilInfo.setTransit_Male(cursor.getString(cursor.getColumnIndex("Transit_Male")));
                majorUtilInfo.setTransit_Female(cursor.getString(cursor.getColumnIndex("Transit_Female")));
                majorUtilInfo.setTransit_Other(cursor.getString(cursor.getColumnIndex("Transit_Other")));
                majorUtilInfo.setMale_Under_Eighteen(cursor.getString(cursor.getColumnIndex("Male_Under_Eighteen")));
                majorUtilInfo.setFemale_Under_Eighteen(cursor.getString(cursor.getColumnIndex("Female_Under_Eighteen")));
                majorUtilInfo.setOther_Under_Eighteen(cursor.getString(cursor.getColumnIndex("Other_Under_Eighteen")));
                majorUtilInfo.setMale_Over_Eighteen(cursor.getString(cursor.getColumnIndex("Male_Over_Eighteen")));
                majorUtilInfo.setFemale_Over_Eighteen(cursor.getString(cursor.getColumnIndex("Female_Over_Eighteen")));
                majorUtilInfo.setOther_Over_Eighteen(cursor.getString(cursor.getColumnIndex("Other_Over_Eighteen")));
                majorUtilInfo.setMale_Foreigner(cursor.getString(cursor.getColumnIndex("Male_Foreigner")));
                majorUtilInfo.setFemale_Foreigner(cursor.getString(cursor.getColumnIndex("Female_Foreigner")));
                majorUtilInfo.setOther_Foreigner(cursor.getString(cursor.getColumnIndex("Other_Foreigner")));
                majorUtilInfo.setJail_Hospital(cursor.getString(cursor.getColumnIndex("Jail_Hospital")));
                majorUtilInfo.setJail_Kitchen(cursor.getString(cursor.getColumnIndex("Jail_Kitchen")));
                majorUtilInfo.setJail_Dormitory(cursor.getString(cursor.getColumnIndex("Jail_Dormitory")));
                majorUtilInfo.setJail_Toilet(cursor.getString(cursor.getColumnIndex("Jail_Toilet")));


                String[] args2 = {rowID};
                String selectSQL = "select Photo From MajorUtilEntry where Id=? ORDER BY Id  DESC";
                Cursor cursor1 = sqLiteDatabase.rawQuery(selectSQL, args2);
                Log.e("USERID", Userid);
                while (cursor1.moveToNext()) {
                    majorUtilInfo.setPhoto((cursor1.getString(cursor1.getColumnIndex("Photo"))));

                }

                //basicdetail.add(basicInfo);

               // majorUtil_basic_details.add(majorUtilInfo);
                //cursor1.close();
            }
            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();


        } catch (Exception e) {
            e.printStackTrace();
            //majorUtil_basic_details = null;
            // TODO: handle exception

        }
        return majorUtilInfo;
    }

    public ArrayList<OfficeUnderPsEntity> getAllOfficeEntryDetail(String Userid) {
        ArrayList<OfficeUnderPsEntity> basicdetail = new ArrayList<OfficeUnderPsEntity>();

        try {

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid};

            // Cursor cursor = sqLiteDatabase.rawQuery("Select * from InsertOfficesUnderPs where Entry_By=? ORDER BY Id  DESC", args);
            Cursor cursor = sqLiteDatabase.rawQuery("Select id, OfficeType_Code , OfficeType_Name , Office_Code , Office_Name , PoliceOwnBuild_Code , PoliceOwnBuild_Name , Khata_Num , Khesra_Num , Total_Area_Land , Other_Offices , Other_Office_Name , Address , Remarks , Houseing_Faci , LsQuarter , UsQuarter , Male_Barrack , Female_Barrack , Armoury_Magazine , Ongoing_CivilWork , Office_In_Charge , Designation , Mobile_No , Landline_No , Establish_Year , Email_id , TrainingCourseName , TrainingCourseCapacity , Sanction_Strength , Working_Strength , Division_Fun , Major_Devices_Equi , Latitude , Longitude , stateOfficeName , prosecutionOfficelevel , courtCategId , courtTypeId , courtSubTypeId , HGOfficeLevel_ID , HGStateOffice , HGDistOffice , HG_regular_Male , HG_regular_Female , HG_regular_Others , HG_volunatry_Male , HG_volunatry_Female , HG_volunatry_Others , Entry_By , Entry_Date  from InsertOfficesUnderPs where Entry_By=? ORDER BY Id  DESC", args);
            int x = cursor.getCount();

            while (cursor.moveToNext()) {
                OfficeUnderPsEntity basicInfo = new OfficeUnderPsEntity();
                String rowID = cursor.getString(cursor.getColumnIndex("id"));
                basicInfo.setId((cursor.getString(cursor.getColumnIndex("id"))));
                basicInfo.setOfficeType_Code((cursor.getString(cursor.getColumnIndex("OfficeType_Code"))));
                basicInfo.setOfficeType_Name((cursor.getString(cursor.getColumnIndex("OfficeType_Name"))));
                basicInfo.setOffice_Code((cursor.getString(cursor.getColumnIndex("Office_Code"))));

                basicInfo.setOffice_Name((cursor.getString(cursor.getColumnIndex("Office_Name"))));
                basicInfo.setPoliceOwnBuild_Code((cursor.getString(cursor.getColumnIndex("PoliceOwnBuild_Code"))));
                basicInfo.setPoliceOwnBuild_Name((cursor.getString(cursor.getColumnIndex("PoliceOwnBuild_Name"))));
                basicInfo.setKhata_Num((cursor.getString(cursor.getColumnIndex("Khata_Num"))));
                basicInfo.setKhesra_Num((cursor.getString(cursor.getColumnIndex("Khesra_Num"))));
                basicInfo.setTotal_Area_Land((cursor.getString(cursor.getColumnIndex("Total_Area_Land"))));
                basicInfo.setOther_Offices((cursor.getString(cursor.getColumnIndex("Other_Offices"))));
                basicInfo.setOther_Office_Name((cursor.getString(cursor.getColumnIndex("Other_Office_Name"))));
                basicInfo.setAddress((cursor.getString(cursor.getColumnIndex("Address"))));
                basicInfo.setRemarks((cursor.getString(cursor.getColumnIndex("Remarks"))));
                basicInfo.setHouseing_Faci((cursor.getString(cursor.getColumnIndex("Houseing_Faci"))));
                basicInfo.setLsQuarter((cursor.getString(cursor.getColumnIndex("LsQuarter"))));
                basicInfo.setUsQuarter((cursor.getString(cursor.getColumnIndex("UsQuarter"))));
                basicInfo.setMale_Barrack((cursor.getString(cursor.getColumnIndex("Male_Barrack"))));
                basicInfo.setFemale_Barrack(cursor.getString((cursor.getColumnIndex("Female_Barrack"))));
                basicInfo.setArmoury_Magazine((cursor.getString(cursor.getColumnIndex("Armoury_Magazine"))));
                basicInfo.setOngoing_CivilWork((cursor.getString(cursor.getColumnIndex("Ongoing_CivilWork"))));
                basicInfo.setOffice_In_Charge((cursor.getString(cursor.getColumnIndex("Office_In_Charge"))));
                basicInfo.setDesignation((cursor.getString(cursor.getColumnIndex("Designation"))));
                basicInfo.setMobile_No((cursor.getString(cursor.getColumnIndex("Mobile_No"))));
                basicInfo.setLandline_No((cursor.getString(cursor.getColumnIndex("Landline_No"))));
                basicInfo.setEstablish_Year((cursor.getString(cursor.getColumnIndex("Establish_Year"))));
                basicInfo.setEmail_id((cursor.getString(cursor.getColumnIndex("Email_id"))));
                basicInfo.setTrainingCourseName((cursor.getString(cursor.getColumnIndex("TrainingCourseName"))));
                basicInfo.setTrainingCourseCapacity((cursor.getString(cursor.getColumnIndex("TrainingCourseCapacity"))));
                basicInfo.setSanction_Strength((cursor.getString(cursor.getColumnIndex("Sanction_Strength"))));
                basicInfo.setWorking_Strength((cursor.getString(cursor.getColumnIndex("Working_Strength"))));
                basicInfo.setDivision_Fun((cursor.getString(cursor.getColumnIndex("Division_Fun"))));
                basicInfo.setMajor_Devices_Equi((cursor.getString(cursor.getColumnIndex("Major_Devices_Equi"))));

                basicInfo.setLatitude((cursor.getString(cursor.getColumnIndex("Latitude"))));
                basicInfo.setLongitude((cursor.getString(cursor.getColumnIndex("Longitude"))));
                basicInfo.setStateOfficeName((cursor.getString(cursor.getColumnIndex("stateOfficeName"))));
                basicInfo.setProsecutionOfficelevel((cursor.getString(cursor.getColumnIndex("prosecutionOfficelevel"))));
                basicInfo.setCourtCategId((cursor.getString(cursor.getColumnIndex("courtCategId"))));
                basicInfo.setCourtTypeId((cursor.getString(cursor.getColumnIndex("courtTypeId"))));
                basicInfo.setCourtSubTypeId((cursor.getString(cursor.getColumnIndex("courtSubTypeId"))));
                basicInfo.setHGOfficeLevel_ID((cursor.getString(cursor.getColumnIndex("HGOfficeLevel_ID"))));
                basicInfo.setHGStateOffice((cursor.getString(cursor.getColumnIndex("HGStateOffice"))));

                basicInfo.setHGDistOffice((cursor.getString(cursor.getColumnIndex("HGDistOffice"))));
                basicInfo.setHG_regular_Male((cursor.getString(cursor.getColumnIndex("HG_regular_Male"))));
                basicInfo.setHG_regular_Female((cursor.getString(cursor.getColumnIndex("HG_regular_Female"))));
                basicInfo.setHG_regular_Others((cursor.getString(cursor.getColumnIndex("HG_regular_Others"))));
                basicInfo.setHG_volunatry_Male((cursor.getString(cursor.getColumnIndex("HG_volunatry_Male"))));
                basicInfo.setHG_volunatry_Female((cursor.getString(cursor.getColumnIndex("HG_volunatry_Female"))));

                basicInfo.setHG_volunatry_Others((cursor.getString(cursor.getColumnIndex("HG_volunatry_Others"))));
//                basicInfo.sete((cursor.getString(cursor.getColumnIndex("Entry_By"))));
//                basicInfo.setBundle1((cursor.getString(cursor.getColumnIndex("Entry_Date"))));


                String[] args2 = {rowID};
                String selectSQL = "select Photo From InsertOfficesUnderPs where Id=? ORDER BY Id  DESC";
                Cursor cursor1 = sqLiteDatabase.rawQuery(selectSQL, args2);
                Log.e("USERID", Userid);
                while (cursor1.moveToNext()) {
                    basicInfo.setPhoto((cursor1.getString(cursor1.getColumnIndex("Photo"))));

                }

                //basicdetail.add(basicInfo);

                basicdetail.add(basicInfo);
                cursor1.close();
            }
            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();


        } catch (Exception e) {
            e.printStackTrace();
            basicdetail = null;
            // TODO: handle exception

        }
        return basicdetail;
    }
    public long majordeleteEditRec(String id, String EntryBy) {
        long c = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String[] DeleteWhere = {String.valueOf(EntryBy), String.valueOf(id)};
            c = db.delete("MajorUtilEntry", "User_Id=? and id=?", DeleteWhere);

            this.getWritableDatabase().close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return c;
        }
        return c;

    }

    public long majordeleteOfficeLatLong(String id, String EntryBy) {
        long c = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String[] DeleteWhere = {String.valueOf(EntryBy), String.valueOf(id)};
            c = db.delete("MajorUtilEntry", "EntryBy=? and Insp_Id=?", DeleteWhere);

            this.getWritableDatabase().close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return c;
        }
        return c;

    }


    public long deleteEditRec(String id, String EntryBy) {
        long c = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String[] DeleteWhere = {String.valueOf(EntryBy), String.valueOf(id)};
            c = db.delete("InsertOfficesUnderPs", "Entry_By=? and id=?", DeleteWhere);

            this.getWritableDatabase().close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return c;
        }
        return c;

    }

    public long deleteOfficeLatLong(String id, String EntryBy) {
        long c = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String[] DeleteWhere = {String.valueOf(EntryBy), String.valueOf(id)};
            c = db.delete("InsertOfficeGps_List", "EntryBy=? and Insp_Id=?", DeleteWhere);

            this.getWritableDatabase().close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return c;
        }
        return c;

    }

    public OfficeUnderPsEntity getAllEntryDetailsingle(String Userid, String rowid) {
        //   ArrayList<OfficeUnderPsEntity> basicdetail = new ArrayList<OfficeUnderPsEntity>();
        OfficeUnderPsEntity basicInfo = new OfficeUnderPsEntity();
        try {

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid, rowid};
            // Cursor cursor=sqLiteDatabase.rawQuery("select * From BasicDetails where EntryBy=? AND Id=?",args);
            Cursor cursor = sqLiteDatabase.rawQuery("Select id, OfficeType_Code , OfficeType_Name , Office_Code , Office_Name , PoliceOwnBuild_Code , PoliceOwnBuild_Name , Khata_Num , Khesra_Num , Total_Area_Land , Other_Offices , Other_Office_Name , Address , Remarks , Houseing_Faci , LsQuarter , UsQuarter , Male_Barrack , Female_Barrack , Armoury_Magazine , Ongoing_CivilWork , Office_In_Charge , Designation , Mobile_No , Landline_No , Establish_Year , Email_id , TrainingCourseName , TrainingCourseCapacity , Sanction_Strength , Working_Strength , Division_Fun , Major_Devices_Equi , Latitude , Longitude , stateOfficeName , prosecutionOfficelevel , courtCategId , courtTypeId , courtSubTypeId , HGOfficeLevel_ID , HGStateOffice , HGDistOffice , HG_regular_Male , HG_regular_Female , HG_regular_Others , HG_volunatry_Male , HG_volunatry_Female , HG_volunatry_Others , Entry_By , Entry_Date  from InsertOfficesUnderPs where Entry_By=? And id=? ORDER BY Id  DESC", args);

            int x = cursor.getCount();


            while (cursor.moveToNext()) {

                String rowID = cursor.getString(cursor.getColumnIndex("id"));
                basicInfo.setId((cursor.getString(cursor.getColumnIndex("id"))));
                basicInfo.setOfficeType_Code((cursor.getString(cursor.getColumnIndex("OfficeType_Code"))));
                basicInfo.setOfficeType_Name((cursor.getString(cursor.getColumnIndex("OfficeType_Name"))));
                basicInfo.setOffice_Code((cursor.getString(cursor.getColumnIndex("Office_Code"))));

                basicInfo.setOffice_Name((cursor.getString(cursor.getColumnIndex("Office_Name"))));
                basicInfo.setPoliceOwnBuild_Code((cursor.getString(cursor.getColumnIndex("PoliceOwnBuild_Code"))));
                basicInfo.setPoliceOwnBuild_Name((cursor.getString(cursor.getColumnIndex("PoliceOwnBuild_Name"))));
                basicInfo.setKhata_Num((cursor.getString(cursor.getColumnIndex("Khata_Num"))));
                basicInfo.setKhesra_Num((cursor.getString(cursor.getColumnIndex("Khesra_Num"))));
                basicInfo.setTotal_Area_Land((cursor.getString(cursor.getColumnIndex("Total_Area_Land"))));
                basicInfo.setOther_Offices((cursor.getString(cursor.getColumnIndex("Other_Offices"))));
                basicInfo.setOther_Office_Name((cursor.getString(cursor.getColumnIndex("Other_Office_Name"))));
                basicInfo.setAddress((cursor.getString(cursor.getColumnIndex("Address"))));
                basicInfo.setRemarks((cursor.getString(cursor.getColumnIndex("Remarks"))));
                basicInfo.setHouseing_Faci((cursor.getString(cursor.getColumnIndex("Houseing_Faci"))));
                basicInfo.setLsQuarter((cursor.getString(cursor.getColumnIndex("LsQuarter"))));
                basicInfo.setUsQuarter((cursor.getString(cursor.getColumnIndex("UsQuarter"))));
                basicInfo.setMale_Barrack((cursor.getString(cursor.getColumnIndex("Male_Barrack"))));
                basicInfo.setFemale_Barrack(cursor.getString((cursor.getColumnIndex("Female_Barrack"))));
                basicInfo.setArmoury_Magazine((cursor.getString(cursor.getColumnIndex("Armoury_Magazine"))));
                basicInfo.setOngoing_CivilWork((cursor.getString(cursor.getColumnIndex("Ongoing_CivilWork"))));
                basicInfo.setOffice_In_Charge((cursor.getString(cursor.getColumnIndex("Office_In_Charge"))));
                basicInfo.setDesignation((cursor.getString(cursor.getColumnIndex("Designation"))));
                basicInfo.setMobile_No((cursor.getString(cursor.getColumnIndex("Mobile_No"))));
                basicInfo.setLandline_No((cursor.getString(cursor.getColumnIndex("Landline_No"))));
                basicInfo.setEstablish_Year((cursor.getString(cursor.getColumnIndex("Establish_Year"))));
                basicInfo.setEmail_id((cursor.getString(cursor.getColumnIndex("Email_id"))));
                basicInfo.setTrainingCourseName((cursor.getString(cursor.getColumnIndex("TrainingCourseName"))));
                basicInfo.setTrainingCourseCapacity((cursor.getString(cursor.getColumnIndex("TrainingCourseCapacity"))));
                basicInfo.setSanction_Strength((cursor.getString(cursor.getColumnIndex("Sanction_Strength"))));
                basicInfo.setWorking_Strength((cursor.getString(cursor.getColumnIndex("Working_Strength"))));
                basicInfo.setDivision_Fun((cursor.getString(cursor.getColumnIndex("Division_Fun"))));
                basicInfo.setMajor_Devices_Equi((cursor.getString(cursor.getColumnIndex("Major_Devices_Equi"))));

                basicInfo.setLatitude((cursor.getString(cursor.getColumnIndex("Latitude"))));
                basicInfo.setLongitude((cursor.getString(cursor.getColumnIndex("Longitude"))));
                basicInfo.setStateOfficeName((cursor.getString(cursor.getColumnIndex("stateOfficeName"))));
                basicInfo.setProsecutionOfficelevel((cursor.getString(cursor.getColumnIndex("prosecutionOfficelevel"))));
                basicInfo.setCourtCategId((cursor.getString(cursor.getColumnIndex("courtCategId"))));
                basicInfo.setCourtTypeId((cursor.getString(cursor.getColumnIndex("courtTypeId"))));
                basicInfo.setCourtSubTypeId((cursor.getString(cursor.getColumnIndex("courtSubTypeId"))));
                basicInfo.setHGOfficeLevel_ID((cursor.getString(cursor.getColumnIndex("HGOfficeLevel_ID"))));
                basicInfo.setHGStateOffice((cursor.getString(cursor.getColumnIndex("HGStateOffice"))));
                basicInfo.setHGDistOffice((cursor.getString(cursor.getColumnIndex("HGDistOffice"))));

                basicInfo.setHG_regular_Male((cursor.getString(cursor.getColumnIndex("HG_regular_Male"))));
                basicInfo.setHG_regular_Female((cursor.getString(cursor.getColumnIndex("HG_regular_Female"))));
                basicInfo.setHG_regular_Others((cursor.getString(cursor.getColumnIndex("HG_regular_Others"))));

                basicInfo.setHG_volunatry_Male((cursor.getString(cursor.getColumnIndex("HG_volunatry_Male"))));
                basicInfo.setHG_volunatry_Female((cursor.getString(cursor.getColumnIndex("HG_volunatry_Female"))));
                basicInfo.setHG_volunatry_Others((cursor.getString(cursor.getColumnIndex("HG_volunatry_Others"))));

                String[] args2 = {rowID};
                String selectSQL = "select Photo From InsertOfficesUnderPs where id=? ORDER BY Id  DESC";
                Cursor cursor1 = sqLiteDatabase.rawQuery(selectSQL, args2);
                Log.e("USERID", Userid);
                while (cursor1.moveToNext()) {
                    basicInfo.setPhoto((cursor1.getString(cursor1.getColumnIndex("Photo"))));

                }

                //basicdetail.add(basicInfo);

                // basicdetail.add(basicInfo);
                cursor1.close();
            }

            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return basicInfo;
    }


    //MajorUtilities
    public ArrayList<InspectionDetailsModel> getMajorUtilsGpsList(String Userid, String rowid) {
        ArrayList<InspectionDetailsModel> basicdetail = new ArrayList<InspectionDetailsModel>();

        try {

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid, rowid};
            // Cursor cursor=sqLiteDatabase.rawQuery("select * From BasicDetails where EntryBy=? AND Id=?",args);
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from MajorUtil_GpsList where EntryBy=? And Insp_Id=? ORDER BY Insp_Id  DESC", args);

            int x = cursor.getCount();

            InspectionDetailsModel basicInfo = new InspectionDetailsModel();
            while (cursor.moveToNext()) {


                basicInfo.setLatitude((cursor.getString(cursor.getColumnIndex("Latitude"))));
                basicInfo.setLongitude((cursor.getString(cursor.getColumnIndex("Longitude"))));
                basicInfo.setInsp_Id((cursor.getString(cursor.getColumnIndex("Insp_Id"))));
                basicInfo.setUserid((cursor.getString(cursor.getColumnIndex("EntryBy"))));


                basicdetail.add(basicInfo);

            }

            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return basicdetail;
    }

    public ArrayList<OtherFacility> getMajorUtilsOtherList(String Userid, String rowid) {
        ArrayList<OtherFacility> basicdetail = new ArrayList<OtherFacility>();

        try {

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid, rowid};
            // Cursor cursor=sqLiteDatabase.rawQuery("select * From BasicDetails where EntryBy=? AND Id=?",args);
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from Other_Facility where User_Id=? And Insp_Id=? ORDER BY Insp_Id  DESC", args);

            int x = cursor.getCount();

            OtherFacility basicInfo = new OtherFacility();
            while (cursor.moveToNext()) {


                basicInfo.setText_facility((cursor.getString(cursor.getColumnIndex("Text_Facility"))));
                basicInfo.setUser_Id((cursor.getString(cursor.getColumnIndex("User_Id"))));
                basicInfo.setInsp_Id((cursor.getString(cursor.getColumnIndex("Insp_Id"))));
                //basicInfo.setLongitude((cursor.getString(cursor.getColumnIndex("Longitude"))));


                basicdetail.add(basicInfo);

            }

            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return basicdetail;
    }

    //MajorUtilities
    public long InsertMajorUtilitiesLatLongs(ArrayList<InspectionDetailsModel> list, String userid, String isnpection_id) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<InspectionDetailsModel> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("MajorUtil_GpsList", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Latitude", info.get(i).getLatitude());
                    values.put("Longitude", info.get(i).getLongitude());
                    values.put("Insp_Id", isnpection_id);
                    values.put("EntryBy", userid);

                    c = db.insert("MajorUtil_GpsList", null, values);


                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }

    public long InsertMajorUtilitiesOthers(ArrayList<OtherFacility> list, String userid, String isnpection_id) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<OtherFacility> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("Other_Facility", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Text_Facility", info.get(i).getText_facility());
                    values.put("Insp_Id", isnpection_id);
                    values.put("User_Id", userid);

                    c = db.insert("Other_Facility", null, values);


                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }

    //AddOfficeUnderPolice
    public ArrayList<InspectionDetailsModel> getOfficeGpsList(String Userid, String rowid) {
        ArrayList<InspectionDetailsModel> basicdetail = new ArrayList<InspectionDetailsModel>();

        try {

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid, rowid};
            // Cursor cursor=sqLiteDatabase.rawQuery("select * From BasicDetails where EntryBy=? AND Id=?",args);
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from InsertOfficeGps_List where EntryBy=? And Insp_Id=? ORDER BY Insp_Id  DESC", args);

            int x = cursor.getCount();

            InspectionDetailsModel basicInfo = new InspectionDetailsModel();
            while (cursor.moveToNext()) {


                basicInfo.setLatitude((cursor.getString(cursor.getColumnIndex("Latitude"))));
                basicInfo.setLongitude((cursor.getString(cursor.getColumnIndex("Longitude"))));


                basicdetail.add(basicInfo);

            }

            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return basicdetail;
    }

    //AddOfficeUnderPolice
    public long InsertOfficeLatLongs(ArrayList<InspectionDetailsModel> list, String userid, String isnpection_id) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<InspectionDetailsModel> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("InsertOfficeGps_List", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Latitude", info.get(i).getLatitude());
                    values.put("Longitude", info.get(i).getLongitude());
                    values.put("Insp_Id", isnpection_id);
                    values.put("EntryBy", userid);

                    c = db.insert("InsertOfficeGps_List", null, values);


                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }


    public long InsertContactDetails(ContactDetailsEntry result, String userid) {

        long c = 0;
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Cntct_Type_code", result.getContact_Code());
            values.put("Cntct_Type_Name", result.getContact_Name());
            values.put("Officer_Name", result.getOfficer_Name());
            values.put("email", result.getOfficer_Email());
            values.put("Photo", result.getPhoto1());
            values.put("Lat", result.getLatitude());
            values.put("Long", result.getLongitude());
            values.put("Block_code", result.getBlock_code());
          values.put("Block_name", result.getBlock_name());
            values.put("Dist_Code", result.getDist_code());
            values.put("Dist_name", result.getDist_name());
            values.put("Thana_code", result.getThana_code());
            values.put("Thana_name", result.getThana_name());
            values.put("Range_Name", result.getRange_name());
            values.put("Range_Code", result.getRange_name());
            values.put("Sub_div_code", result.getSub_div_code());
            values.put("Sub_div_name", result.getSub_div_name());
            values.put("PostOffice_Name", result.getPostOffice_Name());
            values.put("PostOffice_Address", result.getPostOffice_Add());
            values.put("Hospital_type_code", result.getHosp_Code());
            values.put("Hospital_name", result.getHosp_Name());
            values.put("Beds_capacity", result.getCapacity_Bed());
            values.put("Hospital_address", result.getHosp_Add());
            values.put("BusStand_type", result.getBusStand_Code());
            values.put("BusStand_name", result.getBusStand_Name());
            values.put("BusStand_Address", result.getBusStand_Add());
            values.put("Entry_By", userid);
            values.put("Officer_Contact",  result.getOfficer_Contact());
            values.put("HospitalContact",  result.getHosp_Contact());
            values.put("PostOffice_Contact",  result.getPostOffice_Number());
            values.put("School_contact",  result.getSchool_Contact());
            values.put("School_Address",  result.getSchool_Add());
            values.put("School_Name",  result.getSchool_Name());
            values.put("School_Type_code",  result.getSchool_Code());



            c = db.insert("Insert_Imp_Contacts", null, values);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return c;
        }
        return c;

    }

    public ArrayList<ContactDetailsFromServer> getContactTypeLocal() {
        ArrayList<ContactDetailsFromServer> bdetail = new ArrayList<ContactDetailsFromServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from mst_Contact_Type order by Contact_Type_Code", null);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                ContactDetailsFromServer financial_year = new ContactDetailsFromServer();
                financial_year.setContact_Id(cur.getString(cur.getColumnIndex("Contact_Type_Code")));
                financial_year.setContact_Name((cur.getString(cur.getColumnIndex("Contact_Type_Name"))));
                bdetail.add(financial_year);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }

    public ArrayList<BlockList> getBlockListLocal(String Distcode) {
        ArrayList<BlockList> bdetail = new ArrayList<BlockList>();
        try {
            String[] args = {Distcode};
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from BlockList where Dist_Code=? order by Block_Code", args);
            int x = cur.getCount();
            while (cur.moveToNext()) {
                BlockList financial_year = new BlockList();
                financial_year.setBlock_Code(cur.getString(cur.getColumnIndex("Block_Code")));
                financial_year.setBlock_Name((cur.getString(cur.getColumnIndex("Block_Name"))));
                bdetail.add(financial_year);
            }
            cur.close();
            db.close();
        } catch (Exception e) {
        }
        return bdetail;
    }


    public long SetContactTypeLocal(ArrayList<ContactDetailsFromServer> list) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<ContactDetailsFromServer> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("mst_Contact_Type", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Contact_Type_Code", info.get(i).getContact_Id());
                    values.put("Contact_Type_Name", info.get(i).getContact_Name());


                    String[] whereArgs = new String[]{info.get(i).getContact_Id()};

                    c = db.update("mst_Contact_Type", values, "Contact_Type_Code=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("mst_Contact_Type", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }

    public long setBlockListLocal(ArrayList<BlockList> list,String distcode) {


        long c = -1;


        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<BlockList> info = list;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("BlockList", null, null);
        if (info != null) {
            try {
                for (int i = 0; i < info.size(); i++) {

                    values.put("Block_Code", info.get(i).getBlock_Code());
                    values.put("Block_Name", info.get(i).getBlock_Name());
                    values.put("Dist_Code", distcode);


                    String[] whereArgs = new String[]{info.get(i).getBlock_Code()};

                    c = db.update("BlockList", values, "Block_Code=?", whereArgs);
                    if (!(c > 0)) {
                        c = db.insert("BlockList", null, values);
                    }

                }
                db.close();


            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;

    }



    public ArrayList<ContactDetailsEntry> getAllContactsEntryDetail(String Userid) {
        ArrayList<ContactDetailsEntry> basicdetail = new ArrayList<ContactDetailsEntry>();

        try {

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid};

            // Cursor cursor = sqLiteDatabase.rawQuery("Select * from InsertOfficesUnderPs where Entry_By=? ORDER BY Id  DESC", args);
            Cursor cursor = sqLiteDatabase.rawQuery("Select Id , Cntct_Type_code , Cntct_Type_Name , Officer_Name , email , Lat , Long , Block_code , Block_name , Dist_Code , Dist_name , Thana_code , Thana_name , Range_Name , Range_Code , Sub_div_code , Sub_div_name , PostOffice_Name , PostOffice_Address , Hospital_type_code , Hospital_name , Beds_capacity , Hospital_address , BusStand_type , BusStand_name , BusStand_Address , Entry_By , Officer_Contact , HospitalContact , PostOffice_Contact , School_contact,School_Address,School_Name,School_Type_code  from Insert_Imp_Contacts where Entry_By=? ORDER BY Id  DESC", args);
            int x = cursor.getCount();

            while (cursor.moveToNext()) {
                ContactDetailsEntry basicInfo = new ContactDetailsEntry();
                String rowID = cursor.getString(cursor.getColumnIndex("Id"));
                basicInfo.setId((cursor.getString(cursor.getColumnIndex("Id"))));
                basicInfo.setContact_Code((cursor.getString(cursor.getColumnIndex("Cntct_Type_code"))));
                basicInfo.setContact_Name((cursor.getString(cursor.getColumnIndex("Cntct_Type_Name"))));
                basicInfo.setOfficer_Name((cursor.getString(cursor.getColumnIndex("Officer_Name"))));

                basicInfo.setOfficer_Email((cursor.getString(cursor.getColumnIndex("email"))));

                basicInfo.setLatitude((cursor.getString(cursor.getColumnIndex("Lat"))));
                basicInfo.setLongitude((cursor.getString(cursor.getColumnIndex("Long"))));
                basicInfo.setBlock_code((cursor.getString(cursor.getColumnIndex("Block_code"))));
                basicInfo.setBlock_name((cursor.getString(cursor.getColumnIndex("Block_name"))));
                basicInfo.setDist_code((cursor.getString(cursor.getColumnIndex("Dist_Code"))));
                basicInfo.setDist_name((cursor.getString(cursor.getColumnIndex("Dist_name"))));
                basicInfo.setThana_code((cursor.getString(cursor.getColumnIndex("Thana_code"))));
                basicInfo.setThana_name((cursor.getString(cursor.getColumnIndex("Thana_name"))));
                basicInfo.setRange_name((cursor.getString(cursor.getColumnIndex("Range_Name"))));
                basicInfo.setRange_code((cursor.getString(cursor.getColumnIndex("Range_Code"))));
                basicInfo.setSub_div_code((cursor.getString(cursor.getColumnIndex("Sub_div_code"))));
                basicInfo.setSub_div_name((cursor.getString(cursor.getColumnIndex("Sub_div_name"))));
                basicInfo.setPostOffice_Name(cursor.getString((cursor.getColumnIndex("PostOffice_Name"))));
                basicInfo.setPostOffice_Add((cursor.getString(cursor.getColumnIndex("PostOffice_Address"))));
                basicInfo.setHosp_Code((cursor.getString(cursor.getColumnIndex("Hospital_type_code"))));
                basicInfo.setHosp_Name((cursor.getString(cursor.getColumnIndex("Hospital_name"))));
                basicInfo.setCapacity_Bed((cursor.getString(cursor.getColumnIndex("Beds_capacity"))));
                basicInfo.setHosp_Add((cursor.getString(cursor.getColumnIndex("Hospital_address"))));
                basicInfo.setBusStand_Code((cursor.getString(cursor.getColumnIndex("BusStand_type"))));
                basicInfo.setBusStand_Name((cursor.getString(cursor.getColumnIndex("BusStand_name"))));
                basicInfo.setBusStand_Add((cursor.getString(cursor.getColumnIndex("BusStand_Address"))));
//                basicInfo.sete((cursor.getString(cursor.getColumnIndex("Entry_By"))));
                basicInfo.setOfficer_Contact((cursor.getString(cursor.getColumnIndex("Officer_Contact"))));
                basicInfo.setHosp_Contact((cursor.getString(cursor.getColumnIndex("HospitalContact"))));
                basicInfo.setPostOffice_Number((cursor.getString(cursor.getColumnIndex("PostOffice_Contact"))));
                basicInfo.setSchool_Contact((cursor.getString(cursor.getColumnIndex("School_contact"))));
                basicInfo.setSchool_Add((cursor.getString(cursor.getColumnIndex("School_Address"))));
                basicInfo.setSchool_Name((cursor.getString(cursor.getColumnIndex("School_Name"))));
                basicInfo.setSchool_Code((cursor.getString(cursor.getColumnIndex("School_Type_code"))));


                String[] args2 = {rowID};
                String selectSQL = "select Photo From Insert_Imp_Contacts where Id=? ORDER BY Id  DESC";
                Cursor cursor1 = sqLiteDatabase.rawQuery(selectSQL, args2);
                Log.e("USERID", Userid);
                while (cursor1.moveToNext()) {
                    basicInfo.setPhoto1((cursor1.getString(cursor1.getColumnIndex("Photo"))));

                }

                //basicdetail.add(basicInfo);

                basicdetail.add(basicInfo);
                cursor1.close();
            }
            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();


        }
        catch (Exception e) {
            e.printStackTrace();
            basicdetail = null;
            // TODO: handle exception

        }
        return basicdetail;
    }

    public ContactDetailsEntry getContactDetailsingle(String Userid, String rowid) {
        //   ArrayList<OfficeUnderPsEntity> basicdetail = new ArrayList<OfficeUnderPsEntity>();
        ContactDetailsEntry basicInfo = new ContactDetailsEntry();
        try {

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String[] args = {Userid, rowid};
            // Cursor cursor=sqLiteDatabase.rawQuery("select * From BasicDetails where EntryBy=? AND Id=?",args);
        //    Cursor cursor = sqLiteDatabase.rawQuery("Select id, OfficeType_Code , OfficeType_Name , Office_Code , Office_Name , PoliceOwnBuild_Code , PoliceOwnBuild_Name , Khata_Num , Khesra_Num , Total_Area_Land , Other_Offices , Other_Office_Name , Address , Remarks , Houseing_Faci , LsQuarter , UsQuarter , Male_Barrack , Female_Barrack , Armoury_Magazine , Ongoing_CivilWork , Office_In_Charge , Designation , Mobile_No , Landline_No , Establish_Year , Email_id , TrainingCourseName , TrainingCourseCapacity , Sanction_Strength , Working_Strength , Division_Fun , Major_Devices_Equi , Latitude , Longitude , stateOfficeName , prosecutionOfficelevel , courtCategId , courtTypeId , courtSubTypeId , HGOfficeLevel_ID , HGStateOffice , HGDistOffice , HG_regular_Male , HG_regular_Female , HG_regular_Others , HG_volunatry_Male , HG_volunatry_Female , HG_volunatry_Others , Entry_By , Entry_Date  from Insert_Imp_Contacts where Entry_By=? And Id=? ORDER BY Id  DESC", args);
            Cursor cursor = sqLiteDatabase.rawQuery("Select Id , Cntct_Type_code , Cntct_Type_Name , Officer_Name , email , Lat , Long , Block_code , Block_name , Dist_Code , Dist_name , Thana_code , Thana_name , Range_Name , Range_Code , Sub_div_code , Sub_div_name , PostOffice_Name , PostOffice_Address , Hospital_type_code , Hospital_name , Beds_capacity , Hospital_address , BusStand_type , BusStand_name , BusStand_Address , Entry_By , Officer_Contact , HospitalContact , PostOffice_Contact , School_contact,School_Address,School_Name,School_Type_code  from Insert_Imp_Contacts where Entry_By=? And Id=? ORDER BY Id  DESC", args);

            int x = cursor.getCount();


            while (cursor.moveToNext()) {

               // ContactDetailsEntry basicInfo = new ContactDetailsEntry();
                String rowID = cursor.getString(cursor.getColumnIndex("Id"));
                basicInfo.setId((cursor.getString(cursor.getColumnIndex("Id"))));
                basicInfo.setContact_Code((cursor.getString(cursor.getColumnIndex("Cntct_Type_code"))));
                basicInfo.setContact_Name((cursor.getString(cursor.getColumnIndex("Cntct_Type_Name"))));
                basicInfo.setOfficer_Name((cursor.getString(cursor.getColumnIndex("Officer_Name"))));

                basicInfo.setOfficer_Email((cursor.getString(cursor.getColumnIndex("email"))));

                basicInfo.setLatitude((cursor.getString(cursor.getColumnIndex("Lat"))));
                basicInfo.setLongitude((cursor.getString(cursor.getColumnIndex("Long"))));
                basicInfo.setBlock_code((cursor.getString(cursor.getColumnIndex("Block_code"))));
                basicInfo.setBlock_name((cursor.getString(cursor.getColumnIndex("Block_name"))));
                basicInfo.setDist_code((cursor.getString(cursor.getColumnIndex("Dist_Code"))));
                basicInfo.setDist_name((cursor.getString(cursor.getColumnIndex("Dist_name"))));
                basicInfo.setThana_code((cursor.getString(cursor.getColumnIndex("Thana_code"))));
                basicInfo.setThana_name((cursor.getString(cursor.getColumnIndex("Thana_name"))));
                basicInfo.setRange_name((cursor.getString(cursor.getColumnIndex("Range_Name"))));
                basicInfo.setRange_code((cursor.getString(cursor.getColumnIndex("Range_Code"))));
                basicInfo.setSub_div_code((cursor.getString(cursor.getColumnIndex("Sub_div_code"))));
                basicInfo.setSub_div_name((cursor.getString(cursor.getColumnIndex("Sub_div_name"))));
                basicInfo.setPostOffice_Name(cursor.getString((cursor.getColumnIndex("PostOffice_Name"))));
                basicInfo.setPostOffice_Add((cursor.getString(cursor.getColumnIndex("PostOffice_Address"))));
                basicInfo.setHosp_Code((cursor.getString(cursor.getColumnIndex("Hospital_type_code"))));
                basicInfo.setHosp_Name((cursor.getString(cursor.getColumnIndex("Hospital_name"))));
                basicInfo.setCapacity_Bed((cursor.getString(cursor.getColumnIndex("Beds_capacity"))));
                basicInfo.setHosp_Add((cursor.getString(cursor.getColumnIndex("Hospital_address"))));
                basicInfo.setBusStand_Code((cursor.getString(cursor.getColumnIndex("BusStand_type"))));
                basicInfo.setBusStand_Name((cursor.getString(cursor.getColumnIndex("BusStand_name"))));
                basicInfo.setBusStand_Add((cursor.getString(cursor.getColumnIndex("BusStand_Address"))));
//                basicInfo.sete((cursor.getString(cursor.getColumnIndex("Entry_By"))));
                basicInfo.setOfficer_Contact((cursor.getString(cursor.getColumnIndex("Officer_Contact"))));
                basicInfo.setHosp_Contact((cursor.getString(cursor.getColumnIndex("HospitalContact"))));
                basicInfo.setPostOffice_Number((cursor.getString(cursor.getColumnIndex("PostOffice_Contact"))));
                basicInfo.setSchool_Contact((cursor.getString(cursor.getColumnIndex("School_contact"))));
                basicInfo.setSchool_Add((cursor.getString(cursor.getColumnIndex("School_Address"))));
                basicInfo.setSchool_Name((cursor.getString(cursor.getColumnIndex("School_Name"))));
                basicInfo.setSchool_Code((cursor.getString(cursor.getColumnIndex("School_Type_code"))));


                String[] args2 = {rowID};
                String selectSQL = "select Photo From Insert_Imp_Contacts where Id=? ORDER BY Id  DESC";
                Cursor cursor1 = sqLiteDatabase.rawQuery(selectSQL, args2);
                Log.e("USERID", Userid);
                while (cursor1.moveToNext()) {
                    basicInfo.setPhoto1((cursor1.getString(cursor1.getColumnIndex("Photo"))));

                }

                //basicdetail.add(basicInfo);

               // basicdetail.add(basicInfo);
                cursor1.close();
            }

            cursor.close();
            this.getReadableDatabase().close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return basicInfo;
    }

    public long deleteContactRec(String id, String EntryBy) {
        long c = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String[] DeleteWhere = {String.valueOf(EntryBy), String.valueOf(id)};
            c = db.delete("Insert_Imp_Contacts", "Entry_By=? and Id=?", DeleteWhere);

            this.getWritableDatabase().close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return c;
        }
        return c;

    }
}