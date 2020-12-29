package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;


import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.Profile_entity;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView tv_clickpic,tv_asha_name,tv_user_idme,tv_dist,tv_block,tv_hsc,tv_mobile,tv_father_name,tv_name_as_per_aadhar,tv_aadhar_number,tv_ifsc,tv_account,tv_dob,tv_date_of_joining;
    CircleImageView img_ash_selfie;
    private final static int CAMERA_PIC = 99;
    Intent imageData1;
    String str_img1="",Reg_No="";
    DataBaseHelper dataBaseHelper;


    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    String encoded_base64_img="";
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);

        dataBaseHelper = new DataBaseHelper(this);

        inititlize();
        new GetAasha_AashaFaci_Details().execute();
        //setUserDetail();
    }

    public void inititlize()
    {
        tv_asha_name=findViewById(R.id.tv_asha_name);
        tv_user_idme=findViewById(R.id.tv_user_idme);
        tv_dist=findViewById(R.id.tv_dist);
        tv_block=findViewById(R.id.tv_block);
        tv_hsc=findViewById(R.id.tv_hsc);
        tv_mobile=findViewById(R.id.tv_mobile);
        img_ash_selfie=findViewById(R.id.img_ash_selfie);
        tv_name_as_per_aadhar=findViewById(R.id.tv_name_as_per_aadhar);
        tv_aadhar_number=findViewById(R.id.tv_aadhar_number);
        tv_ifsc=findViewById(R.id.tv_ifsc);
        tv_account=findViewById(R.id.tv_account);
        tv_father_name=findViewById(R.id.tv_father_name);
        tv_dob=findViewById(R.id.tv_dob);
        tv_date_of_joining=findViewById(R.id.tv_date_of_joining);
        tv_clickpic=findViewById(R.id.tv_clickpic);

        img_ash_selfie.setOnClickListener(ProfileActivity.this);
        img_ash_selfie.setVisibility(View.GONE);
        tv_clickpic.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED)
            {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
               AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 1);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS");
        builder.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS..")
//		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Turn on GPS", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 0:
                {
                try {
                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    byte[] byteArray = bytes.toByteArray();
                    encoded_base64_img = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    Log.e("Activity", "Pick from Camera::>>> ");

                    String timeStamp = Utiilties.getCurrentDateWithTime();
                    destination = new File(Environment.getExternalStorageDirectory() + "/" +getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imgPath = destination.getAbsolutePath();
                    img_ash_selfie.setImageBitmap(bitmap);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            break;
            case 1:
                {
                Uri selectedImage = data.getData();
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                    byte[] byteArray = bytes.toByteArray();
                    encoded_base64_img = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.e("Activity", "Pick from Gallery::>>> ");

                    imgPath = getRealPathFromURI(selectedImage);
                    destination = new File(imgPath.toString());
                    img_ash_selfie.setImageBitmap(bitmap);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            break;

        }

    }

    public void setUserDetail()
    {
        UserDetails userInfo = CommonPref.getUserDetails(ProfileActivity.this);
        tv_asha_name.setText(userInfo.getUserName().toUpperCase());
        tv_user_idme.setText(userInfo.getUserID().toUpperCase());
        tv_dist.setText(userInfo.getDistName());
        tv_block.setText(userInfo.getBlockName());
        tv_hsc.setText(userInfo.getHSCName());
        tv_mobile.setText(userInfo.getMobileNo());


//        String imagesr=dataBaseHelper.getAshaImg(userInfo.getUserID());
//
//        if (imagesr!=null)
//        {
//            // if (benDetails.getVchPhoto() == null) {
//            byte[] imgData = Base64.decode(imagesr, Base64.DEFAULT);
//            Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
//            //  img_studphoto.setScaleType(ImageView.ScaleType.FIT_XY);
//            img_ash_selfie.setImageBitmap(bmp);
//            //}
//        }
//        else
//        {
            Log.d("asha_img_url",userInfo.getAsha_Img());
            if(!userInfo.getAsha_Img().equalsIgnoreCase("NA")|| !userInfo.getAsha_Img().equalsIgnoreCase("anyType{}"))
            {
                String url = "http://ashwin.bih.nic.in" + userInfo.getAsha_Img().replace("~", "");
                Log.e("imgUrl", url);
                Picasso.with(this).load(url).into(img_ash_selfie);
                //Picasso.with(this).load("http://10.133.20.159/"+benDetails.getVchPhoto()).error(R.drawable.profile).into(img_studphoto);
                // Picasso.with(this).load(benDetails.getVchPhoto()).into(img_studphoto);
            }
     //   }

    }

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void saveImgaetoLocal()
    {
        DataBaseHelper placeData = new DataBaseHelper(ProfileActivity.this);
        SQLiteDatabase db = placeData.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] whereArgs;
        values.put("AshaProfile_Img", str_img1);
//        values.put("Lat1",String.valueOf(imageData1.getStringExtra("Lat")));
//        values.put("Long1",String.valueOf(imageData1.getStringExtra("Lng")));
        whereArgs = new String[]{String.valueOf(Reg_No)};
        long c=db.update("UserDetails", values, "UserID=?", whereArgs);
        if(c>0)
        {
            Toast.makeText(ProfileActivity.this, "Asha Image saved successfully", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(ProfileActivity.this, "Asha Image not saved ", Toast.LENGTH_LONG).show();
        }
    }


    private class GetAasha_AashaFaci_Details extends AsyncTask<String, Void, Profile_entity> {

        GetAasha_AashaFaci_Details()
        {

        }
        @Override
        protected void onPreExecute() {
            try {
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("लोड हो रहा है...");
                dialog.show();
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Profile_entity doInBackground(String... param)
        {
            return WebServiceHelper.getAshaWorkerAndAshaFcList(CommonPref.getUserDetails(ProfileActivity.this).getSVRID(),CommonPref.getUserDetails(ProfileActivity.this).getUserrole());
        }

        @Override
        protected void onPostExecute(Profile_entity result)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            if (result != null)

                if (result != null)
                {
                    tv_dist.setText(result.getDistrictName().toUpperCase());
                    tv_block.setText(result.getBlockName().toUpperCase());
                    tv_hsc.setText(result.getHSCName().toUpperCase());
                    tv_user_idme.setText(result.getASHAID().toUpperCase());
                    //tv_user_idme.setText(result.getASHAFacilitatorID().toUpperCase());
                    tv_asha_name.setText(result.getName().toUpperCase());
                    tv_mobile.setText(result.getMobileNo());
                    tv_name_as_per_aadhar.setText(result.getName_AsPerAadhaar());
                    tv_aadhar_number.setText(result.getAadhaarNo());
                    tv_ifsc.setText(result.getIFSCCode());
                    tv_account.setText(result.getBenAccountNo());
                    tv_father_name.setText(result.getFHName());
                    tv_dob.setText(result.getDateofBirth());
                    tv_date_of_joining.setText(result.getDateofJoining());
                }
                else
                {
                    Utiilties.showErrorAlet(ProfileActivity.this, "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
                }
            else
            {
                if (dialog.isShowing())
                {
                    dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Response NULL.", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
