package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
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
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView tv_asha_name,tv_user_idme,tv_dist,tv_block,tv_hsc,tv_mobile,tv_email;
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataBaseHelper = new DataBaseHelper(this);

        inititlize();
        setUserDetail();
    }

    public void inititlize()
    {
        tv_asha_name=findViewById(R.id.tv_asha_name);
        tv_user_idme=findViewById(R.id.tv_user_idme);
        tv_dist=findViewById(R.id.tv_dist);
        tv_block=findViewById(R.id.tv_block);
        tv_hsc=findViewById(R.id.tv_hsc);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_email=findViewById(R.id.tv_email);
        img_ash_selfie=findViewById(R.id.img_ash_selfie);

        img_ash_selfie.setOnClickListener(ProfileActivity.this);
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

        tv_email.setText(userInfo.getEmail());
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
