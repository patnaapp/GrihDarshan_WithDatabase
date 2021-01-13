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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.adaptor.AshaActivityMonthlyAdapter;
import bih.nic.in.ashwin.adaptor.AshaSalaryByBhm_Adapter;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.Activity_Type_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.BlockListDCM;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.ui.home.HomeFragment;
import bih.nic.in.ashwin.utility.AppConstant;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaWorker_Facilitator_Activity_List extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextView tv_name,tv_year,tv_month,tv_role,tv_daily,tv_monthly;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data,rv_data_monthly;
    Button btn_finalize;
    ArrayList<AshaWorkEntity> ashawork;
    Spinner sp_worker,sp_hsc_list,sp_block,sp_workCat,sp_work,sp_status;
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    ArrayList<Activity_Type_entity> AshaActvityCategoryList = new ArrayList<Activity_Type_entity>();
    ArrayList<Activity_entity> Activity_entityList = new ArrayList<Activity_entity>();
    ArrayList<HscList_Entity> hscList = new ArrayList<HscList_Entity>();
    ArrayList<BlockListDCM> blockList = new ArrayList<BlockListDCM>();
    DataBaseHelper dbhelper;
    LinearLayout ll_monthly,ll_daily,ll_dmf_tab,ll_hsc,ll_block;
    String tabType = "D";
    String hscname="",AcitivtyCategoryId="0",AcitivtyCategoryDesc="",work_ActivityId="0",work_ActivityDesc="",status="0";
    private ProgressDialog dialog;
    String Dist_Code="",Blk_Code="",Role="",HSCCode="",entered_Aasha="",user_Type="";

    BlockListDCM block;
    TextView tv_view_details;
    ArrayList<AshaWorkEntity> ashaWorkData;
    ArrayList<Activity_entity> monthlyData;
    String arr_status[] = {"-सभी-","विचाराधीन","अनुशंषित","अस्वीकृत"};

    String faciltator_id="",facilitator_nm="";
    String asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker__facilitator___list);
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dbhelper=new DataBaseHelper(this);
        Dist_Code=CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getDistrictCode();
        Blk_Code=CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getBlockCode();
        Role=CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole();
        HSCCode=CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getHSCCode();

        initialise();

        user_role=getIntent().getStringExtra("role");

        entered_Aasha=getIntent().getStringExtra(AppConstant.USERTYPE);
        user_Type=getIntent().getStringExtra(AppConstant.USER);
        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");

        tv_role.setText(Role);

        if (Role.equals("BLKBCM")){
            ll_hsc.setVisibility(View.VISIBLE);
            new GetHScListActivityEntryWise().execute();

        }

        else if (Role.equals("HSC") || Role.equals("ANM"))
        {
            ll_hsc.setVisibility(View.VISIBLE);
            new GetHScListAnmWise().execute();

        }else if (Role.equals("DSTADM")){
            ll_hsc.setVisibility(View.VISIBLE);
            ll_block.setVisibility(View.VISIBLE);
            new SyncBlockListForDCM().execute();
        }

        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());

        tv_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabType = "D";
                handleTabView();
            }
        });

        tv_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabType = "M";
                handleTabView();
            }
        });


        btn_finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPendingforfinalie(ashawork))
                {
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
                else if (isPendingandnotfincalisedbyasha(ashawork))
                {
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
                else if (isPendingByAsha(ashawork))
                {
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
                                        //  new FinalizeAshaActivityByANM(cn).execute();
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

        tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SyncAshaActivityList(svrid).execute();
            }
        });
    }

    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
        tv_daily = findViewById(R.id.tv_daily);
        tv_monthly = findViewById(R.id.tv_monthly);
        tv_view_details = findViewById(R.id.tv_view_details);
        tv_view_details.setVisibility(View.GONE);

        rv_data = findViewById(R.id.recyclerview_data);
        rv_data_monthly = findViewById(R.id.recyclerview_data_monthly);

        btn_finalize = findViewById(R.id.btn_finalize);

        sp_worker = findViewById(R.id.sp_worker);
        sp_hsc_list = findViewById(R.id.sp_hsc_list);
        sp_block = findViewById(R.id.sp_block);
        sp_workCat = findViewById(R.id.sp_workCat);
        sp_work = findViewById(R.id.sp_work);
        sp_status = findViewById(R.id.sp_status);

        ll_monthly = findViewById(R.id.ll_monthly);
        ll_daily = findViewById(R.id.ll_daily);
        ll_dmf_tab = findViewById(R.id.ll_dmf_tab);
        ll_block = findViewById(R.id.ll_block);

        ll_hsc = findViewById(R.id.ll_hsc);
        ll_daily.setVisibility(View.GONE);
        ll_monthly.setVisibility(View.GONE);
        ll_hsc.setVisibility(View.GONE);
        ll_block.setVisibility(View.GONE);

        // btn_finalize.setVisibility(View.GONE);
        sp_worker.setOnItemSelectedListener(this);
        sp_hsc_list.setOnItemSelectedListener(this);
        sp_block.setOnItemSelectedListener(this);
        sp_workCat.setOnItemSelectedListener(this);
        sp_work.setOnItemSelectedListener(this);
        sp_status.setOnItemSelectedListener(this);

    }

    public void loadStatusSpinner(){
        ArrayAdapter adaptor = new ArrayAdapter(AshaWorker_Facilitator_Activity_List.this,android.R.layout.simple_spinner_item, arr_status);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_status.setAdapter(adaptor);
    }

    public void setupRecuyclerView(ArrayList<AshaWorkEntity> data)
    {
        ll_daily.setVisibility(View.VISIBLE);
        rv_data.setVisibility(View.VISIBLE);
        ll_monthly.setVisibility(View.GONE);
        rv_data_monthly.setVisibility(View.GONE);
        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AshaActivityAccpRjctAdapter adapter = new AshaActivityAccpRjctAdapter(AshaWorker_Facilitator_Activity_List.this, data, fyear, fmonth);
        rv_data.setAdapter(adapter);
    }

    public void setupMonthlyRecuyclerView(ArrayList<AshaWorkEntity> data)
    {
        if (data.size()>0)
        {
            ll_daily.setVisibility(View.GONE);
            rv_data.setVisibility(View.GONE);
            rv_data_monthly.setVisibility(View.VISIBLE);
            ll_monthly.setVisibility(View.VISIBLE);
            rv_data_monthly.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            AshaActivityMonthlyAdapter adapter = new AshaActivityMonthlyAdapter(AshaWorker_Facilitator_Activity_List.this, data, fyear, fmonth);
            rv_data_monthly.setAdapter(adapter);
        }
        else {
            rv_data_monthly.setVisibility(View.GONE);
            ll_monthly.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.sp_worker:
                if (position > 0)
                {
                    AshaWoker_Entity role = ashaworkerList.get(position - 1);
                    asha_worker_nm = role.get_Asha_Name_Hn();
                    asha_worker_id = role.get_svr_id();
                    svrid = role.get_svr_id();
                    tv_name.setText(asha_worker_nm);
                    ll_dmf_tab.setVisibility(View.VISIBLE);

                    tv_view_details.setVisibility(View.VISIBLE);

                    new getAshaActvityCategoryListEntryWise().execute();

                    loadFilterActivtyList();
                }else if(position == 0){
                    svrid = "0";
                    loadFilterActivtyList();
                }else{
                    tv_view_details.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_hsc_list:
                if (position > 0)
                {
                    HscList_Entity role = hscList.get(position - 1);
                    hscname = role.get_HSCName_Hn();
                    HSCCode = role.get_HSCCode();
                    //new GetAshaWorkersList().execute();
                    new getAshaListActivityEntryWise().execute();

                }else{
                    HSCCode = "0";
                    //loadFilterActivtyList();
                }
                break;
            case R.id.sp_block:
                if (position > 0)
                {
                    block = blockList.get(position - 1);
                    new SyncHSCListForDCM().execute();
                }
                break;
            case R.id.sp_workCat:
                if (position > 0)
                {
                    Activity_Type_entity activity_type_entity = AshaActvityCategoryList.get(position-1);
                    AcitivtyCategoryId=activity_type_entity.get_ActTypeId();
                    AcitivtyCategoryDesc=activity_type_entity.get_Actname();
                    new AshaActvityListEntryWise().execute();

                    loadFilterActivtyList();
                }else if(position == 0){
                    AcitivtyCategoryId = "0";
                    loadFilterActivtyList();
                }
                break;
            case R.id.sp_work:
                if (position > 0)
                {
                    Activity_entity activity_type_entity = Activity_entityList.get(position-1);
                    work_ActivityId=activity_type_entity.get_ActivityId();
                    work_ActivityDesc=activity_type_entity.get_ActivityDesc();

                    loadFilterActivtyList();
                }else if(position == 0){
                    work_ActivityId = "0";
                    loadFilterActivtyList();
                }
                break;
            case R.id.sp_status:
                if (position > 0)
                {
                    status = arr_status[position].toString();
                    if(status.equalsIgnoreCase("विचाराधीन")){
                        status="P";
                    }else if(status.equalsIgnoreCase("अनुशंषित")){
                        status="A";
                    }
                    else if(status.equalsIgnoreCase("अस्वीकृत")){
                        status="R";
                    }
                    loadFilterActivtyList();
                }else if(position == 0){
                    status = "0";
                    loadFilterActivtyList();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    public void loadFilterActivtyList(){
        new AshaAcitivtyCategoryMonthWise(svrid,AcitivtyCategoryId,work_ActivityId,status).execute();
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
        protected ArrayList<AshaWorkEntity> doInBackground(String... param)
        {            // return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole());
            return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole());
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
                tabType = "D";
                tv_daily.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorGreyDark));
                rv_data.setVisibility(View.VISIBLE);
                rv_data_monthly.setVisibility(View.GONE);
                //handleTabView();
                //new getAshaActvityCategoryListEntryWise().execute();

                //  new SynchronizeMonthlyAshaActivityList().execute();

            }
        }
    }

    private class SynchronizeMonthlyAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {

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

            // return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole());
            return WebServiceHelper.getAshaWorkMonthlyActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole());
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
                setupMonthlyRecuyclerView(result);



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
        String version="";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }

    public void loadWorkerFascilatorData()
    {
        //   if (user_role.equals("ASHA")){

//        if (!hsccode.equals(""))
//        {
//            ashaworkerList = dbhelper.getAshaWorkerList(hsccode,CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getBlockCode());
//
//        }
//        else
//        {
//            ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getHSCCode(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getBlockCode());
//
//        }

        ArrayList array = new ArrayList<String>();
        array.add("-All-");

        for (AshaWoker_Entity info: ashaworkerList){
            array.add(info.get_ASHAID()+":-"+info.get_Asha_Name_Hn());
        }

        ArrayAdapter adaptor = new ArrayAdapter(AshaWorker_Facilitator_Activity_List.this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_worker.setAdapter(adaptor);

        // }


    }
    public void loadAashaListCategoryWiseData()
    {
        //   if (user_role.equals("ASHA")){

//        if (!hsccode.equals(""))
//        {
//            ashaworkerList = dbhelper.getAshaWorkerList(hsccode,CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getBlockCode());
//
//        }
//        else
//        {
//            ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getHSCCode(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getBlockCode());
//
//        }

        ArrayList array = new ArrayList<String>();
        array.add("-All-");

        for (Activity_Type_entity info: AshaActvityCategoryList){
            //array.add(info.get_ActnameHN()+":-"+info.get_Asha_Name_Hn());
            array.add(info.get_Actname());
        }

        ArrayAdapter adaptor = new ArrayAdapter(AshaWorker_Facilitator_Activity_List.this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_workCat.setAdapter(adaptor);

        // }


    }
    public void loadActvityListEntryWise()
    {
        //   if (user_role.equals("ASHA")){

//        if (!hsccode.equals(""))
//        {
//            ashaworkerList = dbhelper.getAshaWorkerList(hsccode,CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getBlockCode());
//
//        }
//        else
//        {
//            ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getHSCCode(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getBlockCode());
//
//        }

        ArrayList array = new ArrayList<String>();
        array.add("-सभी-");

        for (Activity_entity info: Activity_entityList){
            //array.add(info.get_ActnameHN()+":-"+info.get_Asha_Name_Hn());
            array.add(info.get_ActivityDesc());
        }

        ArrayAdapter adaptor = new ArrayAdapter(AshaWorker_Facilitator_Activity_List.this,R.layout.dropdownlist, array);
        adaptor.setDropDownViewResource(R.layout.dropdownlist);
        sp_work.setAdapter(adaptor);

        // }


    }

    public void loadBlockList(){
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        for (BlockListDCM info: blockList)
        {
            array.add(info.getBlockName());
        }

        ArrayAdapter adaptor = new ArrayAdapter(AshaWorker_Facilitator_Activity_List.this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_block.setAdapter(adaptor);

    }

    public void loadHscList()
    {
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (HscList_Entity info: hscList)
        {
            array.add(info.get_HSCName_Hn());
        }

        ArrayAdapter adaptor = new ArrayAdapter(AshaWorker_Facilitator_Activity_List.this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_hsc_list.setAdapter(adaptor);
    }



    public void handleTabView()
    {
        switch (tabType)
        {
            case "D":
                tv_daily.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorGreyDark));
                rv_data.setVisibility(View.VISIBLE);
                rv_data_monthly.setVisibility(View.GONE);
//                if (CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole().equals("HSC"))
//                {
                //new SynchronizeAshaActivityList().execute();
                loadFilterActivtyList();
                // }

                break;

            case "M":
                tv_daily.setTextColor(getResources().getColor(R.color.colorGreyDark));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorPrimary));
                rv_data_monthly.setVisibility(View.VISIBLE);
                rv_data.setVisibility(View.GONE);
                new SynchronizeMonthlyAshaActivityList().execute();
                break;
        }
    }


    private class GetAshaWorkersList extends AsyncTask<String, Void, ArrayList<AshaWoker_Entity>>
    {
        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Asha details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWoker_Entity> doInBackground(String... param) {
            String hsc_code = "";
//            if (CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("BLKBCM"))
//            {
            hsc_code = HSCCode;

//            }
//            else {
//                hsc_code=CommonPref.getUserDetails(getApplicationContext()).getHSCCode();
//            }

            if (CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole().equals("DSTADM")) {
                return WebServiceHelper.getAshaWorkerListDCM(CommonPref.getUserDetails(getApplicationContext()).getDistrictCode(), block.getOtherBlock(), hsc_code, fyear.getYear_Id(), fmonth.get_MonthId());
            } else {
                return WebServiceHelper.getAshaWorkerList(CommonPref.getUserDetails(getApplicationContext()).getDistrictCode(), CommonPref.getUserDetails(getApplicationContext()).getBlockCode(), hsc_code);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWoker_Entity> result) {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null && result.size()>0)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                ashaworkerList = result;
                loadWorkerFascilatorData();
                long i = helper.setAshaWorkerList_Local(result,CommonPref.getUserDetails(getApplicationContext()).getHSCCode(),CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
                if (i > 0) {

                    loadWorkerFascilatorData();
                    Toast.makeText(getApplicationContext(), "Asha worker list loaded", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
            else {

                Toast.makeText(getApplicationContext(), "No Asha Worker List In This Hsc", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class GetHScListAnmWise extends AsyncTask<String, Void, ArrayList<HscList_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Hsc details...");
            dialog.show();
        }

        @Override
        protected ArrayList<HscList_Entity> doInBackground(String... param)
        {
            Log.d("fyjbchb",CommonPref.getUserDetails(getApplicationContext()).getSVRID());
            return WebServiceHelper.getHscListANMWISe(CommonPref.getUserDetails(getApplicationContext()).getSVRID());
        }

        @Override
        protected void onPostExecute(ArrayList<HscList_Entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                hscList = result;
                loadHscList();
                Log.d("Resultgfg", "" + result);

            }
            else {

            }
        }
    }

    private class SyncBlockListForDCM extends AsyncTask<String, Void, ArrayList<BlockListDCM>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Blocks...");
            dialog.show();
        }

        @Override
        protected ArrayList<BlockListDCM> doInBackground(String... param)
        {
            return WebServiceHelper.getBlockListDCM(fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getDistrictCode());
        }

        @Override
        protected void onPostExecute(ArrayList<BlockListDCM> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                blockList = result;
                loadBlockList();
                Log.d("Resultgfg", "" + result);

            }
            else {

            }
        }
    }

    private class SyncHSCListForDCM extends AsyncTask<String, Void, ArrayList<HscList_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Blocks...");
            dialog.show();
        }

        @Override
        protected ArrayList<HscList_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getHSCListDCM(fmonth.get_MonthId(),fyear.getYear_Id(),block.getOtherBlock());
        }

        @Override
        protected void onPostExecute(ArrayList<HscList_Entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                hscList = result;
                loadHscList();
                Log.d("Resultgfg", "" + result);

            }
            else {

            }
        }
    }

   /* private class GetHScListBlockWise extends AsyncTask<String, Void, ArrayList<HscList_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Hsc details...");
            dialog.show();
        }

        @Override
        protected ArrayList<HscList_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getHscList(CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
        }

        @Override
        protected void onPostExecute(ArrayList<HscList_Entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                long i = helper.setHscList_Local(result,CommonPref.getUserDetails(getApplicationContext()).getUserID());
                if (i > 0)
                {
                    Toast.makeText(getApplicationContext(), "Hsc list loaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
                hscList = result;
                loadHscList();
            }
        }
    }*/
    private class GetHScListActivityEntryWise extends AsyncTask<String, Void, ArrayList<HscList_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Hsc details...");
            dialog.show();
        }

        @Override
        protected ArrayList<HscList_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.GetHScListActivityWise(Dist_Code,Blk_Code,fmonth.get_MonthId(),fyear.getYear_Id(),Role);
        }

        @Override
        protected void onPostExecute(ArrayList<HscList_Entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                hscList = result;
                loadHscList();
            }
        }
    }

    private class getAshaListActivityEntryWise extends AsyncTask<String, Void, ArrayList<AshaWoker_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Asha List Activity details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWoker_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getAshaListActivityEntryWise(fyear.getYear_Id(),fmonth.get_MonthId(),HSCCode,Role);
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWoker_Entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                ashaworkerList = result;
                loadWorkerFascilatorData();
                loadStatusSpinner();
            }
        }
    }
    private class getAshaActvityCategoryListEntryWise extends AsyncTask<String, Void, ArrayList<Activity_Type_entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Asha Actvity Category 0List EntryWise details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_Type_entity> doInBackground(String... param)
        {
            return WebServiceHelper.getAshaActvityCategoryListEntryWise(fyear.getYear_Id(),fmonth.get_MonthId(),svrid,Role);
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_Type_entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                AshaActvityCategoryList = result;
                loadAashaListCategoryWiseData();
            }
        }
    }

    private class AshaAcitivtyCategoryMonthWise extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {


        private final ProgressDialog dialog = new ProgressDialog(AshaWorker_Facilitator_Activity_List.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this).create();

        String acitivtyCategoryId,acitivtyId,status_id,AshaWorkerId;

        public AshaAcitivtyCategoryMonthWise( String ashaWorkerId, String acitivtyCategoryId, String acitivtyId, String status_id) {
            this.acitivtyCategoryId = acitivtyCategoryId;
            this.acitivtyId = acitivtyId;
            this.status_id = status_id;
            AshaWorkerId = ashaWorkerId;
        }

        //        public AshaAcitivtyCategoryMonthWise(String AshaWorkerId,String acitivtyCategoryId,String AcitivtyId,String Status) {
//            svrid=AshaWorkerId;
//            AcitivtyCategoryId=acitivtyCategoryId;
//            work_ActivityId=AcitivtyId;
//            status_code=Status;
//        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param)
        {

            // return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole());
            return WebServiceHelper.getAshaAcitivtyCategoryMonthWise(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),AcitivtyCategoryId,work_ActivityId,Role,HSCCode,Blk_Code,Dist_Code,status_id);
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
                //  new SynchronizeMonthlyAshaActivityList().execute();
            }else{
                ashawork = new ArrayList<>();
            }

            setupRecuyclerView(ashawork);
        }
    }

    private class AshaActvityListEntryWise extends AsyncTask<String, Void, ArrayList<Activity_entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Asha Actvity List EntryWise details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param)
        {
            return WebServiceHelper.getAshaActvityListEntryWise(fyear.getYear_Id(),fmonth.get_MonthId(),svrid,AcitivtyCategoryId,Role);
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                Activity_entityList = result;
                loadActvityListEntryWise();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //handleTabView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i=new Intent(AshaWorker_Facilitator_Activity_List.this,UserHomeActivity.class);
//        startActivity(i);
//        finish();
    }
    private class SyncAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {
        String _svr_id="";
        public SyncAshaActivityList(String svrid) {
            _svr_id=svrid;
        }

        @Override
        protected void onPreExecute() {

            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("दैनिक कार्य सूची लोड हो रहा है...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkActivityList(_svr_id,fmonth.get_MonthId(),fyear.getYear_Id(),"ASHA");
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {

            if (result != null)
            {
                ashaWorkData = result;
                new SyncSelectedMonthlyActivityList(_svr_id).execute();

            }else{
                Utiilties.showErrorAlet(AshaWorker_Facilitator_Activity_List.this, "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    private class SyncSelectedMonthlyActivityList extends AsyncTask<String, Void, ArrayList<Activity_entity>> {
        String _srvr_id="";
        public SyncSelectedMonthlyActivityList(String srvr_id) {
            _srvr_id=srvr_id;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkMonthlyActivityListBHM(_srvr_id,fmonth.get_MonthId(),fyear.getYear_Id(),"ASHA");
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result) {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }

            if (result != null)
            {
                monthlyData=result;

                Intent i = new Intent(AshaWorker_Facilitator_Activity_List.this, FinalizeAshaWorkActivity.class);
                i.putExtra("fyear", fyear);
                i.putExtra("fmonth", fmonth);
                i.putExtra("workArray", ashaWorkData);
                i.putExtra("monthly", monthlyData);

                startActivity(i);

            }
            else
            {
                Utiilties.showErrorAlet(AshaWorker_Facilitator_Activity_List.this, "सर्वर कनेक्शन त्रुटि", "मासिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }
}
