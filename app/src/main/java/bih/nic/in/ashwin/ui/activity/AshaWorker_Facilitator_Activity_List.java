package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.ui.home.HomeFragment;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaWorker_Facilitator_Activity_List extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="";
    TextView tv_name,tv_year,tv_month,tv_role;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data;
    Button btn_finalize;
    ArrayList<AshaWorkEntity> ashawork;
    String version="";
    Spinner sp_worker;
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    DataBaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker__facilitator___list);

        dbhelper=new DataBaseHelper(this);
        initialise();

        user_role=getIntent().getStringExtra("role");
        //   svrid=getIntent().getStringExtra("svr");
        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");

//        if (user_role.equals("ASHA"))
//        {
//            asha_worker_id=getIntent().getStringExtra("ashaid");
//            asha_worker_nm=getIntent().getStringExtra("ashanm");
//            tv_name.setText(asha_worker_nm);
        tv_role.setText("आशा वर्कर");
//        }
//        else if (user_role.equals("ASHAFC"))
//        {
//            faciltator_id=getIntent().getStringExtra("_faciltator_id");
//            facilitator_nm=getIntent().getStringExtra("_faciltator_nm");
//            tv_name.setText(facilitator_nm);
//            tv_role.setText("आशा फैसिलिटेटर");
//        }

        loadWorkerFascilatorData();

        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());
//
//        if (user_role.equals("ASHA")) {
//            new SynchronizeAshaActivityList().execute();
//        }
//        else {
//
//        }

        btn_finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPendingforfinalie(ashawork)){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this);
                    builder1.setMessage("कार्य सूचि विचाराधीन है, कृपया अंतिम रूप देने से पहले सभी कार्य को स्वीकृत या अस्वीकृत करे |");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ओके",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else if (isPendingandnotfincalisedbyasha(ashawork)){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this);
                    builder1.setMessage("कार्य सूचि विचाराधीन है और आशा द्वारा अंतिम रूप नहीं दिया गया , कृपया अंतिम रूप देने से पहले सभी कार्य को स्वीकृत या अस्वीकृत करे |");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ओके",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else if (isPendingByAsha(ashawork)){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this);
                    builder1.setMessage("आशा द्वारा अंतिम रूप नहीं दिया गया |");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ओके",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else if (isPendingByAnm(ashawork)){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this);
                    builder1.setMessage("पहले से ही अंतिम रूप दिया गया |");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ओके",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this);
                    builder1.setMessage("क्या आप निश्चित हैं कि आप अंतिम रूप देना चाहते हैं");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "हाँ",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    for (AshaWorkEntity cn : ashawork) {
                                        new FinalizeAshaActivityByANM(cn).execute();
                                    }
                                }
                            });
                    builder1.setNegativeButton(
                            "नहीं",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
    }

    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
        rv_data = findViewById(R.id.recyclerview_data);
        btn_finalize = findViewById(R.id.btn_finalize);
        sp_worker = findViewById(R.id.sp_worker);
        // btn_finalize.setVisibility(View.GONE);
        sp_worker.setOnItemSelectedListener(this);

    }

    public void setupRecuyclerView(ArrayList<AshaWorkEntity> data){
//        if (!isPendingByAnm(data))
//        {
//
//            btn_finalize.setVisibility(View.VISIBLE);
//        }
//        else if(isPendingByAnm(data)) {
//
//        }
        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AshaActivityAccpRjctAdapter adapter = new AshaActivityAccpRjctAdapter(AshaWorker_Facilitator_Activity_List.this, data, fyear, fmonth);
        rv_data.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.sp_worker:
                if (position > 0) {


                    AshaWoker_Entity role = ashaworkerList.get(position - 1);
                    asha_worker_nm = role.get_Asha_Name_Hn();
                    asha_worker_id = role.get_ASHAID();
                    svrid = role.get_svr_id();
                    tv_name.setText(asha_worker_nm);
                    new SynchronizeAshaActivityList().execute();

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class SynchronizeAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {

        private final ProgressDialog dialog = new ProgressDialog(AshaWorker_Facilitator_Activity_List.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                ashawork=result;
                setupRecuyclerView(result);

            }
        }
    }


    public static boolean isPendingforfinalie(ArrayList<AshaWorkEntity> arraylist) {
        for (AshaWorkEntity info : arraylist) {
            if (info.getVerificationStatus().contains("P") && info.get_IsANMFinalize().equals("N")&& info.getIsFinalize().equals("Y")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPendingandnotfincalisedbyasha(ArrayList<AshaWorkEntity> arraylist) {
        for (AshaWorkEntity info : arraylist) {
            if (info.getVerificationStatus().contains("P") && info.get_IsANMFinalize().equals("N")&& info.getIsFinalize().equals("N")) {
                return true;
            }
        }
        return false;
    }


    public static boolean isPendingByAsha(ArrayList<AshaWorkEntity> arraylist) {
        for (AshaWorkEntity info : arraylist) {
            if ((info.getVerificationStatus().contains("A")||info.getVerificationStatus().contains("R")) && info.get_IsANMFinalize().equals("N")&& info.getIsFinalize().equals("N")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPendingByAnm(ArrayList<AshaWorkEntity> arraylist) {
        for (AshaWorkEntity info : arraylist) {
            if ((info.getVerificationStatus().contains("A")||info.getVerificationStatus().contains("R")) && info.get_IsANMFinalize().equals("Y")&& info.getIsFinalize().equals("Y")) {
                return true;
            }
        }
        return false;
    }


    private class FinalizeAshaActivityByANM extends AsyncTask<String, Void, String> {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(AshaWorker_Facilitator_Activity_List.this);
        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this).create();


        FinalizeAshaActivityByANM(AshaWorkEntity data) {
            this.data = data;
            //   this.position = position;
            //_uid = data.getId();
            //rowid = data.get_phase1_id();

        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("पुष्टि किया जा रहा हैं...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            String devicename=getDeviceName();
            String app_version=getAppVersion();
            result = WebServiceHelper.FinalizeAshaActivityANM(data,CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserID(),app_version,devicename);
            return result;

        }

        @Override
        protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue", "" + result);
            if (result != null) {
                if(result.equals("1")){


                    new android.app.AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड को अंतिम रूप दिया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this);
                    builder.setIcon(R.drawable.ashwin_logo);
                    builder.setTitle("Failed");
                    // Ask the final question
                    builder.setMessage("Failed");
                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

            }
            else
            {
                Toast.makeText(AshaWorker_Facilitator_Activity_List.this, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {

            return model.toUpperCase();
        } else {

            return manufacturer.toUpperCase() + " " + model;
        }
    }
    public String getAppVersion(){
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }

    public void loadWorkerFascilatorData(){
        if (user_role.equals("ASHA")){
            ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getHSCCode());

            ArrayList array = new ArrayList<String>();
            array.add("-Select-");

            for (AshaWoker_Entity info: ashaworkerList){
                array.add(info.get_Asha_Name_Hn());
            }

            ArrayAdapter adaptor = new ArrayAdapter(AshaWorker_Facilitator_Activity_List.this, android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_worker.setAdapter(adaptor);

        }


    }


}
