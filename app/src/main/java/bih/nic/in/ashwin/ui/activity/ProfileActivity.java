package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.utility.Utiilties;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView tv_asha_name,tv_fh_name,tv_aadhar,tv_mobile,tv_address,tv_acc_no,tv_ifsc;
    CircleImageView img_ash_selfie;
    private final static int CAMERA_PIC = 99;
    Intent imageData1;
    String str_img1="",Reg_No="";
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataBaseHelper = new DataBaseHelper(this);

        inititlize();
    }

    public void inititlize()
    {
        tv_asha_name=findViewById(R.id.tv_asha_name);
        tv_fh_name=findViewById(R.id.tv_fh_name);
        tv_aadhar=findViewById(R.id.tv_aadhar);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_address=findViewById(R.id.tv_address);
        tv_acc_no=findViewById(R.id.tv_acc_no);
        tv_ifsc=findViewById(R.id.tv_ifsc);
        img_ash_selfie=findViewById(R.id.img_ash_selfie);

        img_ash_selfie.setOnClickListener(ProfileActivity.this);

    }

    @Override
    public void onClick(View view) {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true)
        {
            Intent iCamera = new Intent(getApplicationContext(), CameraActivity.class);
            if (view.equals(img_ash_selfie))
                iCamera.putExtra("KEY_PIC", "1");
            startActivityForResult(iCamera, CAMERA_PIC);
        }
        else
        {

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                buildAlertMessageNoGps();
            }
        }
    }

    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS");
        builder.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS..")
//		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Turn on GPS", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
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
            case CAMERA_PIC:
                if (resultCode == RESULT_OK)
                {
                    byte[] imgData = data.getByteArrayExtra("CapturedImage");

                    str_img1= org.kobjects.base64.Base64.encode(imgData);
                    imageData1=data;
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgData,0,imgData.length);
                    //img_studphoto.setScaleType(ImageView.ScaleType.FIT_XY);
                    img_ash_selfie.setImageBitmap(Utiilties.GenerateThumbnail(bmp,500, 500));

                    break;
                }
        }

    }
}
