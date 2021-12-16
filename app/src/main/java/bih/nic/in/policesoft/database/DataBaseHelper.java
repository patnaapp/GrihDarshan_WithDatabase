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

import bih.nic.in.policesoft.entity.GetPrisionMasterServer;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
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
            values.put("major_UtilCode",result.getMajor_UtilCode());
            values.put("user_id",result.getUser_Id());
            values.put("password",result.getPassword());
            values.put("fair_Festival_Name",result.getFair_Festival_Name());
            values.put("court_Address",result.getCourt_Address());
            values.put("fair_Festival_Address",result.getFair_Festival_Address());

            values.put("Flag", "I");
            c = db.insert("majorUtils", null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            return c;
        }

        return c;
    }



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

    public long setPrisonMasterLocal(ArrayList<GetPrisionMasterServer> list) {


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


    public ArrayList<GetPrisionMasterServer> getPrisonMasterLocal() {
        ArrayList<GetPrisionMasterServer> bdetail = new ArrayList<GetPrisionMasterServer>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from PrisonMaster_List order by PrisonCode", null);
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

}