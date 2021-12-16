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
import bih.nic.in.policesoft.entity.GetPrisionypeServer;
import bih.nic.in.policesoft.entity.GetTypeOfHydrantServer;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
import bih.nic.in.policesoft.entity.OfficeListFromServer;
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


}