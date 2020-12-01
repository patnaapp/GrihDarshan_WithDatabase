package bih.nic.in.ashwin.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import bih.nic.in.ashwin.R;


public class Utiilties {

    public Utiilties() {
        // TODO Auto-generated constructor stub
    }

    public static long getDateDifferenceFromCurrentDate(String fdate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date fromDate = null, todayWithZeroTime=null;
        long daysDiff = 0;

        try {
            fromDate = dateFormat.parse(fdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(fromDate != null){
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(fromDate);

            long msDiff = Calendar.getInstance().getTimeInMillis() - cCal.getTimeInMillis();
            daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        }

        return (daysDiff);
    }

    public static void ShowMessage(Context context, String Title, String Message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(Title);
        alertDialog.setMessage(Message);
        alertDialog.show();
    }


    public static void showAlet(final Context context){

        if (Utiilties.isOnline(context) == false) {
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setCancelable(false);
            ab.setTitle("Internet Connnection Error!!!");
            ab.setMessage("Please turn on your mobile data or wifi connection");
            ab.setPositiveButton("Turn On",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            GlobalVariables.isOffline = false;
                            Intent I = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            context.startActivity(I);
                        }
                    });
            ab.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            GlobalVariables.isOffline = true;

                        }
                    });

            ab.create().getWindow().getAttributes().windowAnimations = R.style.AppTheme;

            ab.show();
        }else {

            GlobalVariables.isOffline = false;
            // new CheckUpdate().execute();
        }
    }

    public static void showErrorAlet(final Context context, String title, String message){

            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setCancelable(false);
            ab.setTitle(title);
            ab.setMessage(message);
            ab.setPositiveButton("ओके",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });


            ab.create().getWindow().getAttributes().windowAnimations = R.style.AppTheme;

            ab.show();
    }


    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() == true);
    }

    public static Bitmap GenerateThumbnail(Bitmap imageBitmap,
                                           int THUMBNAIL_HEIGHT, int THUMBNAIL_WIDTH) {

        Float width = new Float(imageBitmap.getWidth());
        Float height = new Float(imageBitmap.getHeight());
        Float ratio = width / height;
        Bitmap CompressedBitmap = Bitmap.createScaledBitmap(imageBitmap,
                (int) (THUMBNAIL_HEIGHT * ratio), THUMBNAIL_WIDTH, false);

        return CompressedBitmap;
    }



    public static Bitmap DrawText(Activity activity, Bitmap mBitmap, String displaytext1, String displaytext2, String displaytext3, String displaytext4) {
        Bitmap bmOverlay = Bitmap.createBitmap(mBitmap.getWidth(),
                mBitmap.getHeight(), Bitmap.Config.ARGB_4444);
        // create a canvas on which to draw
        Canvas canvas = new Canvas(bmOverlay);

        int margin = 5;
        Paint paint = new Paint();
        Paint.FontMetrics fm = new Paint.FontMetrics();
        paint.setColor(activity.getResources().getColor(R.color.color_red));
        paint.setTextSize(17);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setFakeBoldText(false);
        paint.setShadowLayer(0, 0, 0, Color.BLACK);

        // if the background image is defined in main.xml, omit this line
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        canvas.drawText(displaytext1, 0, mBitmap.getHeight() - 50, paint);
        canvas.drawText(displaytext2, 0, mBitmap.getHeight() - 32, paint);

        //canvas.drawText(displaytext3, 0, mBitmap.getHeight() - 22, paint);

        paint.setTextSize(15);
        canvas.drawText(displaytext4, 0, mBitmap.getHeight() - 15, paint);
        // set the bitmap into the ImageView
        return bmOverlay;
    }


    public static Object deserialize(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getDateString() {
        SimpleDateFormat postFormater = new SimpleDateFormat(
                "MMMM dd, yyyy hh:mm a");

        String newDateStr = postFormater.format(Calendar.getInstance()
                .getTime());
        return newDateStr;




    }

    public static String getDateString(String Formats) {
        SimpleDateFormat postFormater = new SimpleDateFormat(Formats);

        String newDateStr = postFormater.format(Calendar.getInstance().getTime());
        return newDateStr;
    }

    public static String convertDateStringFormet(String presnetFormat, String requiredFormat, String dateStr){
        try{
            SimpleDateFormat format1 = new SimpleDateFormat(presnetFormat);
            SimpleDateFormat format2 = new SimpleDateFormat(requiredFormat);
            Date date = format1.parse(dateStr);
            return format2.format(date);
        }catch (Exception e){
            return dateStr;
        }
    }



    /*public static void setActionBarBackground(Activity activity)
    {
        ActionBar actionBar;
        actionBar=activity.getActionBar();
        Resources res = activity.getResources();
        Drawable drawable = res.getDrawable(R.drawable.digitallogo2);
        actionBar.setBackgroundDrawable(drawable);
    }*/


    public static void setStatusBarColor(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = activity.getWindow();


            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(Color.parseColor("#1565a9"));
        }
    }




    public static String getCurrentDate()
    {
        Calendar cal= Calendar.getInstance();
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);
        month=month+1;

        int h=cal.get(Calendar.HOUR);
        int m=cal.get(Calendar.MINUTE);
        int s=cal.get(Calendar.SECOND);

        String date=month+"/"+day+"/"+year;
        return date;

    }




    public static String getCurrentDateWithTime() throws ParseException {

        SimpleDateFormat f = new SimpleDateFormat("MMM d,yyyy HH:mm");
        Date date=null;
        date=f.parse(getDateString());
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d,yyyy HH:mm a");
        String dateString = formatter.format(date);
        return dateString;
    }



    public static String getDateTime() {

        String date=getDateString();
        String a="";
        StringTokenizer st=new StringTokenizer(date," ");
        while (st.hasMoreTokens()){
            a=st.nextToken();
        }

        if(a.equals("a.m."))
        {

            date=date.replace(a,"AM");
        }
        if(a.equals("p.m."))
        {
            date=date.replace(a,"PM");


        }


        return date;
    }



    public static String getshowCurrentDate()
    {
        Calendar cal= Calendar.getInstance();
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);
        month =month+1;

        int h=cal.get(Calendar.HOUR);
        int m=cal.get(Calendar.MINUTE);
        int s=cal.get(Calendar.SECOND);

        String date=day+"/"+month+"/"+year;
        return date;

    }

    public static String parseDate(String date)
    {
        StringTokenizer st=new StringTokenizer(date,"/");
        String month="",day="",year="";
        try {
            month = st.nextToken();
            day = st.nextToken();
            year = st.nextToken();
        }catch (Exception e){e.printStackTrace();}

        return day+"/"+month+"/"+year;
    }

    public static void displayPromptForEnablingGPS(final Activity activity)
    {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                                activity.finish();
                            }
                        });
        builder.create().show();
    }
    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public static boolean isfrontCameraAvalable(){
        int numCameras= Camera.getNumberOfCameras();
        for(int i=0;i<numCameras;i++){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if(Camera.CameraInfo.CAMERA_FACING_FRONT == info.facing){
                return true;
            }
        }
        return false;
    }
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    //TODO check android.support.v and correct this.
//    public static boolean checkPermission(final Context context)
//    {
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if(currentAPIVersion>= Build.VERSION_CODES.M)
//        {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
//    }

    public static byte[] StringToBitMap(String encodedString) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            byte[] byte_arr = out.toByteArray();
            return byte_arr;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static byte[] bitmaptoByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static String BitArrayToString(byte[] b1) {
        byte[] b = b1;
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static String getOptionFilterName(int type){
        switch (type){
            case 1:
                return "Monitoring_Designation";
            case 2:
                return "surface_Inspection_Work_Status";
            case 3:
                return "Surface_Work_Persentage";
            case 4:
                return "Surface_work_done_as_previous";
            case 5:
                return "Surface_observation_Category";
            case 6:
                return "Scheme_Name";
            case 7:
                return "Surface_Scheme_Finantial_Year";
            case 8:
                return "Surface_Scheme_Fund_Type";
            case 9:
                return "Surface_scheme_Type";
            default:
                return "NA";
        }
    }

    public static byte[] convertBitmapToByteArray(Bitmap bmp){
        //Bitmap bmp = intent.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 45, stream);
        byte[] byteArray = stream.toByteArray();
        //bmp.recycle();
        return byteArray;
    }


    public static String getAppVersion(Context context){
        try {
            String version = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "NA";
        }
    }


    public static String getDeviceIMEI(Context context) {
        String imei;
        MarshmallowPermission permission = new MarshmallowPermission(context, Manifest.permission.READ_PHONE_STATE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            imei = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        }
        else
        {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null)
            {
                imei = mTelephony.getDeviceId();
            }
            else
            {
                imei = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
            }
        }

        return imei;
    }

    public static String getAshaWorkActivityStatus(String code)
    {
        try
        {
            switch (code)
            {
                case "P":
                    return "विचाराधीन";
                case "NA":
                    return "विचाराधीन";
                case "A":
                    return "अनुशंषित";
                case "R":
                    return "अस्वीकृत";
                default:
                    return "";
            }
        }
        catch (Exception e)
        {
            return "";
        }

    }

    public static String getAshaWorkActivityStatusBHM(String code)
    {
        try
        {
            switch (code)
            {
                case "P":
                    return "विचाराधीन";
                case "NA":
                    return "विचाराधीन";
                case "Y":
                    return "अनुशंषित";
                case "R":
                    return "अस्वीकृत";
                default:
                    return "";
            }
        }
        catch (Exception e)
        {
            return "";
        }

    }

    public static String getActivityTypeStatus(String code)
    {
        try
        {
            switch (code)
            {
                case "D":
                    return "दैनिक";
                case "M":
                    return "मासिक";
                case "Q":
                    return "त्रैमासिक";
                case "H":
                    return "अर्धवार्षिक";
                case "Y":
                    return "वार्षिक";
                default:
                    return "";
            }
        }
        catch (Exception e)
        {
            return "";
        }

    }
}
