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

    public long InsertNewEntry(AddMajorUtilitiesActivity newEntryActivity, MajorUtilEntry result) {
        long c = -1;

        try {
            DataBaseHelper placeData = new DataBaseHelper(newEntryActivity);
            SQLiteDatabase db = placeData.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("major_UtilCode", result.getMajor_UtilCode());
            values.put("user_id", result.getUser_Id());
            values.put("password", result.getPassword());
            values.put("fair_Festival_Name", result.getFair_Festival_Name());
            values.put("court_Address", result.getCourt_Address());
            values.put("fair_Festival_Address", result.getFair_Festival_Address());

            values.put("Flag", "I");
            c = db.insert("majorUtils", null, values);
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
    public long setPrisonMasterLocal(ArrayList<GetPrisionMasterServer> list,String distcode,String jailtype) {


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
                    values.put("Distcode",distcode);
                    values.put("JailType_Code",jailtype);

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


    public ArrayList<GetPrisionMasterServer> getPrisonMasterLocal(String distcode,String jailtype_code) {
        ArrayList<GetPrisionMasterServer> bdetail = new ArrayList<GetPrisionMasterServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] whereArgs = new String[]{distcode,jailtype_code};
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


    public long setCourtLocal(ArrayList<CourtType_Entity> list,String courtcateg) {


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
                    values.put("CourtCateg_ID",courtcateg);

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
                    values.put("Dist_Code",info.get(i).getDistrict_Code());
                    values.put("OfficeType_Code",info.get(i).getOffice_Type_Code());
                    values.put("Range_Code",info.get(i).getRange_Code());
                    values.put("SubDiv_Code",info.get(i).getSubdivision_Code());

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

    public ArrayList<Office_Name_List_Modal> getOfficeNameLocal(String officeType,String rangecode) {
        ArrayList<Office_Name_List_Modal> bdetail = new ArrayList<Office_Name_List_Modal>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] whereArgs = new String[]{officeType,rangecode};
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



    public long InsertOfficeDetails(OfficeUnderPsEntity result,String userid) {

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
            Log.e("HG_regular_Male", result.getHG_regular_Male());
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


        }
        catch (Exception e) {
            e.printStackTrace();
            basicdetail = null;
            // TODO: handle exception

        }
        return basicdetail;
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
                String selectSQL = "select Photo From InsertOfficesUnderPs where Id=? ORDER BY Id  DESC";
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



    public long InsertOfficeLatLongs(ArrayList<InspectionDetailsModel> list,String userid,String isnpection_id) {


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
                    values.put("EntryBy",userid);

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

}