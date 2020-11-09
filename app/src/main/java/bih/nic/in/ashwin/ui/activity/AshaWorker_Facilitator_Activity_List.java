package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.ui.home.HomeFragment;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaWorker_Facilitator_Activity_List extends AppCompatActivity {

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="";
    TextView tv_name,tv_year,tv_month,tv_role;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data;
    Button btn_finalize;
    ArrayList<AshaWorkEntity> ashawork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker__facilitator___list);

        initialise();

        user_role=getIntent().getStringExtra("role");
        svrid=getIntent().getStringExtra("svr");
        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");


        if (user_role.equals("ASHA")){
            asha_worker_id=getIntent().getStringExtra("ashaid");
            asha_worker_nm=getIntent().getStringExtra("ashanm");
            tv_name.setText(asha_worker_nm);
            tv_role.setText("आशा वर्कर");
        }
        else if (user_role.equals("ASHAFC")) {
            faciltator_id=getIntent().getStringExtra("_faciltator_id");
            facilitator_nm=getIntent().getStringExtra("_faciltator_nm");
            tv_name.setText(facilitator_nm);
            tv_role.setText("आशा फैसिलिटेटर");
        }

        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());

        if (user_role.equals("ASHA")) {
            new SynchronizeAshaActivityList().execute();
        }
        else {

        }

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
                            "ओके",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
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
       // btn_finalize.setVisibility(View.GONE);

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
}
