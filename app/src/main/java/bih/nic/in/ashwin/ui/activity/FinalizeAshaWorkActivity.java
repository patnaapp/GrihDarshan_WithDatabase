package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailListener;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.MonthlyActivityAdapter;
import bih.nic.in.ashwin.adaptor.MonthlyActivityListener;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.AshaWorkFinalizeEntity;
import bih.nic.in.ashwin.entity.Centralamount_entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.OtpEntitiy;
import bih.nic.in.ashwin.entity.Stateamount_entity;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class FinalizeAshaWorkActivity extends AppCompatActivity implements MonthlyActivityListener, AshaFCWorkDetailListener {

    TextView tv_fn_yr,fn_mnth,tv_total_work,tv_total_central_amnt,tv_total_state_amnt;
    TextView tv_monthly_amnt,tv_total_amnt,tv_rr,tv_sc;
    TextView tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat;
    RecyclerView rv_data,rv_work,rv_data_sc;
    CheckBox ch_1,ch_2,ch_3;
    LinearLayout ll_btn_bottom,ll_declaration,ll_div_zone,ll_otp,ll_pan,ll_division,ll_monthly;
    Button btn_verify_otp;
    EditText edt_otp;

    DataBaseHelper dbhelper;
    Financial_Year fyear;
    Financial_Month fmonth;

    ActivityCategory_entity category;

    ArrayList<AshaWorkEntity> ashaWorkData = new ArrayList<>();
    ArrayList<AshaFascilitatorWorkEntity> ashaFCWorkData = new ArrayList<>();
    ArrayList<Activity_entity> activityArray = new ArrayList<>();
    ArrayList<Activity_entity> stateContArray = new ArrayList<>();

    ArrayList<Stateamount_entity> stateAmountArray;
    ArrayList<Centralamount_entity> centralAmountArray;

    Double totalWorkAmount,totalStateAmount;
    String userRole;
    UserDetails userInfo;

    private ProgressDialog dialog;
    Boolean getOtp = true;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_asha_work);

        initializeViews();
        extractDataFromIntent();
    }

    void initializeViews() {
        dbhelper = new DataBaseHelper(this);
        dialog = new ProgressDialog(this);

        tv_total_work = findViewById(R.id.tv_total_work);
        tv_total_central_amnt = findViewById(R.id.tv_total_central_amnt);
        tv_total_state_amnt = findViewById(R.id.tv_total_state_amnt);
        tv_monthly_amnt = findViewById(R.id.tv_monthly_amnt);
        tv_total_amnt = findViewById(R.id.tv_total_amnt);

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

        tv_aanganwadi = findViewById(R.id.tv_aanganwadi);
        tv_hscname = findViewById(R.id.tv_hscname);
        tv_district = findViewById(R.id.tv_district);
        tv_block = findViewById(R.id.tv_block);
        tv_panchayat = findViewById(R.id.tv_panchayat);

        tv_rr = findViewById(R.id.tv_rr);
        tv_sc = findViewById(R.id.tv_sc);

        rv_data = findViewById(R.id.rv_data);
        rv_work = findViewById(R.id.rv_work);
        rv_data_sc = findViewById(R.id.rv_data_sc);

        ch_1 = findViewById(R.id.ch_1);
        ch_2 = findViewById(R.id.ch_2);
        ch_3 = findViewById(R.id.ch_3);

        ll_btn_bottom = findViewById(R.id.ll_btn_bottom);
        ll_declaration = findViewById(R.id.ll_declaration);
        ll_div_zone = findViewById(R.id.ll_div_zone);
        ll_otp = findViewById(R.id.ll_otp);
        ll_pan = findViewById(R.id.ll_pan);
        ll_division = findViewById(R.id.ll_division);
        ll_monthly = findViewById(R.id.ll_monthly);

        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        edt_otp = findViewById(R.id.edt_otp);

        if (CommonPref.getUserDetails(FinalizeAshaWorkActivity.this).getUserrole().equals("BLKBHM") || CommonPref.getUserDetails(FinalizeAshaWorkActivity.this).getUserrole().equals("BLKMO"))
        {
            ll_div_zone.setVisibility(View.GONE);
        }
        else
        {
            ll_div_zone.setVisibility(View.VISIBLE  );
        }
        //category = getActivityCategory();
    }

    public void extractDataFromIntent()
    {
        userInfo = CommonPref.getUserDetails(this);

        fyear = (Financial_Year) getIntent().getSerializableExtra("fyear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("fmonth");

        tv_aanganwadi.setText(userInfo.getAwcName());
        tv_hscname.setText(userInfo.getHSCName());
        tv_district.setText(userInfo.getDistNameHN());
        tv_block.setText(userInfo.getBlockNameHN());
        tv_panchayat.setText(userInfo.getPanchayatNameHN());

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());
        //tv_total_work.setText(""+ashaWorkData.size());

        if(userInfo.getUserrole().equals("ASHA")){
            ashaWorkData =  (ArrayList<AshaWorkEntity>) getIntent().getSerializableExtra("workArray");
            setMonthlyActivity((ArrayList<Activity_entity>) getIntent().getSerializableExtra("monthly"));
            ll_monthly.setVisibility(View.VISIBLE);

        }else if(userInfo.getUserrole().equals("ASHAFC")){
            ashaFCWorkData =  (ArrayList<AshaFascilitatorWorkEntity>) getIntent().getSerializableExtra("workFCArray");
            ll_division.setVisibility(View.GONE);
            ll_pan.setVisibility(View.GONE);
        }else if(userInfo.getUserrole().equals("BLKBHM") || userInfo.getUserrole().equals("BLKMO")){
            ashaWorkData =  (ArrayList<AshaWorkEntity>) getIntent().getSerializableExtra("workArray");
            ArrayList<Activity_entity> monthly = (ArrayList<Activity_entity>) getIntent().getSerializableExtra("monthly");
            ashaFCWorkData =  (ArrayList<AshaFascilitatorWorkEntity>) getIntent().getSerializableExtra("workFCArray");

            if(ashaWorkData.size()> 0 || monthly.size() > 0){
                userInfo.setUserrole("ASHA");
            }else if(ashaFCWorkData.size()>0){
                userInfo.setUserrole("ASHAFC");
            }
        }

        new GetStateAmount().execute();
    }

    public void setMonthlyActivity(ArrayList<Activity_entity> list){
        //ArrayList<Activity_entity> monthly = new ArrayList<>();
        activityArray.clear();
        stateContArray.clear();
        for(Activity_entity item: list){
            if(item.getAbbr().contains("PC1") || item.getAbbr().contains("PI1")){
                activityArray.add(item);
            }else if (item.getAbbr().contains("PC2") || item.getAbbr().contains("PI2")){
                stateContArray.add(item);
            }
        }
    }

    public void loadAshaData(){
        setWorkRecycler();
        setActivityRecycler();

        totalWorkAmount = getTotalWorkAmount();
        totalStateAmount = getTotalStateAmount();

        tv_total_central_amnt.setText("\u20B9"+totalWorkAmount);
        tv_total_state_amnt.setText("\u20B9"+totalStateAmount);



        updateTotalAmount();

        if(!isDataFinalize() && isReadyForFinalize())
        {
            //ll_btn_bottom.setVisibility(View.VISIBLE);
            ll_otp.setVisibility(View.VISIBLE);
            ll_declaration.setVisibility(View.VISIBLE);
        }
    }

    public void loadAshaFCData(){
        //totalWorkAmount = getTotalWorkAmount();
        totalWorkAmount = getTotalCentralAmount();
        totalStateAmount = getTotalFCStateAmount();

        tv_total_central_amnt.setText("\u20B9"+totalWorkAmount);
        tv_total_state_amnt.setText("\u20B9"+totalStateAmount);

        tv_total_amnt.setText("\u20B9"+(totalWorkAmount+totalStateAmount));

        setFCActivityRecycler();

        if(!isDataFinalize() && isReadyForFinalize())
        {
            //ll_btn_bottom.setVisibility(View.VISIBLE);
            ll_otp.setVisibility(View.VISIBLE);
            ll_declaration.setVisibility(View.VISIBLE);
            ch_1.setText(" उपरोक्त सभी दावा BCM के द्वारा सत्यापित हैं| ");
        }
    }

    public Double getTotalCentralAmount(){
        Double amount = 0.0;
        Integer count = 0;

        try{
            for(Centralamount_entity amnt: centralAmountArray){
                if(amnt.get_DesigId().equals("2") && amnt.get_Active().equals("Y"))
                    amount += Double.parseDouble(amnt.get_CentralAmt());
            }

            for(AshaFascilitatorWorkEntity info: ashaFCWorkData){
                if(info.getVerificationStatus().equals("A")){
                    count += 1;
                }
            }
        }catch (Exception e){

        }

        return amount*count;
    }

    public Boolean isReadyForFinalize(){
        if(userInfo.getUserrole().equals("ASHA") || userInfo.getUserrole().equals("BLKBHM") || userInfo.getUserrole().equals("BLKMO")) {
            if (ashaWorkData.size() > 0) {
                for (AshaWorkEntity work : ashaWorkData) {
                    if (work.getVerificationStatus().equals("P") || work.getVerificationStatus().equals("NA")) {
                        return false;
                    }
                }
            } else {
                return false;
            }

            if (activityArray.size() > 0) {
                for (Activity_entity work : activityArray) {
                    if (work.getVerificationStatus().equals("P") || work.getVerificationStatus().equals("NA")) {
                        return false;
                    }
                }
            }

            if (stateContArray.size() > 0) {
                for (Activity_entity work : stateContArray) {
                    if (work.getVerificationStatus().equals("P") || work.getVerificationStatus().equals("NA")) {
                        return false;
                    }
                }
            }
//            else {
//                return false;
//            }

        }else if (userInfo.getUserrole().equals("ASHAFC")){
            if (ashaFCWorkData.size() > 0) {
                for (AshaFascilitatorWorkEntity work : ashaFCWorkData) {
                    if (work.getVerificationStatus().equals("P") || work.getVerificationStatus().equals("NA")) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public Boolean isDataFinalize(){
        if(userInfo.getUserrole().equals("ASHA") || userInfo.getUserrole().equals("BLKBHM") || userInfo.getUserrole().equals("BLKMO")){
            if(ashaWorkData.size()> 0)
            {
                for(AshaWorkEntity work: ashaWorkData)
                {
                    if(work.getIsFinalize().equals("Y"))
                    {
                        return true;
                    }
                }
            }
            else
            {
                return false;
            }

            if(activityArray.size()> 0){
                for(Activity_entity work: activityArray)
                {
                    if(work.getIsFinalize() != null && work.getIsFinalize().equals("Y"))
                    {
                        return true;
                    }
                }
            }

            if(stateContArray.size()> 0){
                for(Activity_entity work: stateContArray)
                {
                    if(work.getIsFinalize() != null && work.getIsFinalize().equals("Y"))
                    {
                        return true;
                    }
                }
            }
        }else if (userInfo.getUserrole().equals("ASHAFC")){
            if (ashaFCWorkData.size() > 0) {
                for (AshaFascilitatorWorkEntity work : ashaFCWorkData) {
                    if (work.get_IsFinalize().equals("Y")) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }

        return false;
    }

    public Double getTotalWorkAmount()
    {
        Double amount = 0.0;
        for(AshaWorkEntity info: ashaWorkData)
        {
            if(info.getVerificationStatus().equals("A"))
                amount += Double.parseDouble(info.getActivityAmt());
        }

        return amount;
    }

    public Double getTotalStateAmount()
    {
        //ArrayList<Stateamount_entity> list = dbhelper.getStateAmountList("ASHA");

        Double amount = 0.0;
        int scApprovedCount = 0;

        for(Activity_entity info: stateContArray){
            if(info.getVerificationStatus().equals("A"))
                scApprovedCount += 1;
        }

        if(scApprovedCount < 4){
            return amount;
        }

        for(Stateamount_entity info: stateAmountArray){
            if(info.get_Desig().equals("ASHA"))
                amount += Double.parseDouble(info.get_StateAmt());
        }

        return amount;
    }

    public Double getTotalFCStateAmount()
    {
        //ArrayList<Stateamount_entity> list = dbhelper.getStateAmountList("ASHA");

        Double amount = 0.0;

        for(Stateamount_entity info: stateAmountArray)
        {
            if(info.get_Desig().equals("ASHAFC"))
                amount += Double.parseDouble(info.get_StateAmt());
        }

        return amount;
    }

    public void setFCActivityRecycler()
    {
        rv_work.setLayoutManager(new LinearLayoutManager(this));
        AshaFCWorkDetailAdapter adapter = new AshaFCWorkDetailAdapter(this, ashaFCWorkData, this);
        rv_work.setAdapter(adapter);
    }

    public void setActivityRecycler()
    {
        if(activityArray.size()> 0){
            rv_data.setLayoutManager(new LinearLayoutManager(this));
            MonthlyActivityAdapter adapter = new MonthlyActivityAdapter(this, activityArray, this, true, true);
            rv_data.setAdapter(adapter);
        }else{
            tv_rr.setVisibility(View.GONE);
        }

        if(stateContArray.size()>0){
            rv_data_sc.setLayoutManager(new LinearLayoutManager(this));
            MonthlyActivityAdapter adapter1 = new MonthlyActivityAdapter(this, stateContArray, this, true, true);
            rv_data_sc.setAdapter(adapter1);
        }else{
            tv_sc.setVisibility(View.GONE);
        }
    }

    public void setWorkRecycler()
    {
        rv_work.setLayoutManager(new LinearLayoutManager(this));
        AshaWorkDetailAdapter adapter = new AshaWorkDetailAdapter(this, ashaWorkData, fyear, fmonth, this);
        rv_work.setAdapter(adapter);
    }

    public ActivityCategory_entity getActivityCategory()
    {
        ArrayList<ActivityCategory_entity>  categoryArray = dbhelper.getActictivityCategoryList("1","M");
        for(ActivityCategory_entity info: categoryArray)
        {
            if(info.get_AcitivtyCategoryDesc().equals("Monthly Category"))
            {
                return info;
            }
        }
        return null;
    }

    @Override
    public void onActivityCheckboxChanged(int position, Boolean isChecked, String type)
    {
        Activity_entity activity = activityArray.get(position);
        activity.setChecked(isChecked);
        activityArray.set(position, activity);

        updateTotalAmount();
    }

    public void updateTotalAmount()
    {
        Double monthly = getMonthlyAmount();
        //tv_monthly_amnt.setText("\u20B9"+monthly);
        tv_total_amnt.setText("\u20B9"+(totalWorkAmount+totalStateAmount+monthly));
    }

    public Double getMonthlyAmount()
    {
        Double amount = 0.0;
        for(Activity_entity info: activityArray)
        {
            if(info.getVerificationStatus().equals("A"))
                amount += Double.parseDouble(info.get_ActivityAmt());
        }
        return amount;
    }

    public void dismissActivity(View view)
    {
        finish();
    }

    public String getTotalActivitiesWorkCount()
    {
        Integer count = 0;
        for(Activity_entity info: activityArray)
        {
            if(info.getChecked())
                count += 1;
        }

        return String.valueOf(count+ashaWorkData.size());
    }
    public void finalizeActivity(View view)
    {
        if(edt_otp.getText().toString().equals(otp))
        {
            AshaWorkFinalizeEntity entity = new AshaWorkFinalizeEntity(CommonPref.getUserDetails(this).getUserID().toUpperCase(),CommonPref.getUserDetails(this).getSVRID(),fyear.getYear_Id(),fmonth.get_MonthId(),getTotalActivitiesWorkCount(),""+(totalWorkAmount+totalStateAmount),CommonPref.getUserDetails(this).getSVRID(), Utiilties.getDeviceIMEI(this), Utiilties.getAppVersion(this),activityArray);
            entity.setUserRole(CommonPref.getUserDetails(this).getUserrole());
            new UploadAshaFinalizeData(entity).execute();
        }else{
            Toast.makeText(this, "ओटीपी मैच नहीं हुआ, कृपया सही ओटीपी दर्ज करें", Toast.LENGTH_SHORT).show();
        }

//        if(isValidated())
//        {
//            AshaWorkFinalizeEntity entity = new AshaWorkFinalizeEntity(CommonPref.getUserDetails(this).getUserID().toUpperCase(),CommonPref.getUserDetails(this).getSVRID(),fyear.getYear_Id(),fmonth.get_MonthId(),getTotalActivitiesWorkCount(),""+(totalWorkAmount+totalStateAmount),CommonPref.getUserDetails(this).getSVRID(), Utiilties.getDeviceIMEI(this), Utiilties.getAppVersion(this),activityArray);
//            entity.setUserRole(CommonPref.getUserDetails(this).getUserrole());
//            new UploadAshaFinalizeData(entity).execute();
//        }
    }

    public Boolean isValidated()
    {
        Boolean validate = true;
        if(!ch_1.isChecked() || !ch_2.isChecked() || !ch_3.isChecked())
        {
            validate = false;
            Toast.makeText(this, "कृपया सभी घोषणा का चयन करें", Toast.LENGTH_SHORT).show();
        }
        return validate;
    }

    public Boolean isActivityChecked()
    {
        for(Activity_entity info: activityArray)
        {
            if(info.getChecked())
                return true;
        }
        return false;
    }

    public void onDataUploaded()
    {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setIcon(R.drawable.asha)
                .setMessage("Activity Finalized")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onEditFCWork(AshaFascilitatorWorkEntity info) {
    }

    @Override
    public void onDeleteFCWork(int position) {

    }

    public void onGetVerifyOtp(View view) {
        if(isValidated()){
            //if(getOtp){
                OtpEntitiy entity = new OtpEntitiy();
                entity.setId(userInfo.getSVRID());
                entity.setFYearID(fyear.getYear_Id());
                entity.setMonthId(fmonth.get_MonthId());
                entity.setUserid(userInfo.getUserID());
                entity.setUserolle(userInfo.getUserrole());
                new AsyncGetOtp(entity).execute();
//            }else{
//
//            }
        }

    }

    private class UploadAshaFinalizeData extends AsyncTask<String, Void, String>{
        AshaWorkFinalizeEntity data;

        private final ProgressDialog dialog = new ProgressDialog(FinalizeAshaWorkActivity.this);

        UploadAshaFinalizeData(AshaWorkFinalizeEntity data)
        {
            this.data = data;
        }

        @Override
        protected void onPreExecute()
        {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("अपलोड हो राहा है...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param)
        {
            return WebServiceHelper.uploadAshaFinalizeWorkDetail(data);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue",""+result);

            if (result != null)
            {
                if(result.contains(",")){
                    String[] res = result.split(",");
                    if(res.length == 2)
                    {
                        showErrorAlet(FinalizeAshaWorkActivity.this, "Message", res[1]);
                    }
                    else
                    {
                        showErrorAlet(FinalizeAshaWorkActivity.this, "Message", result);
                    }
                }else
                {
                    showErrorAlet(FinalizeAshaWorkActivity.this, "Message", result);
                }
            }
            else
            {
                Toast.makeText(FinalizeAshaWorkActivity.this, "null record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsyncGetOtp extends AsyncTask<String, Void, String>{
        OtpEntitiy data;

        private final ProgressDialog dialog = new ProgressDialog(FinalizeAshaWorkActivity.this);

        public AsyncGetOtp(OtpEntitiy data) {
            this.data = data;
        }

        @Override
        protected void onPreExecute()
        {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("अपलोड हो राहा है...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param)
        {
            return WebServiceHelper.getOtp(data);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue",""+result);

            if (result != null)
            {

                if(result.contains(",")){
                    String[] res = result.split(",");
                    if(res.length == 2)
                    {
                        otp = res[1];
                        edt_otp.setVisibility(View.VISIBLE);
                        ll_btn_bottom.setVisibility(View.VISIBLE);
                        Log.e("otp", otp);
                    }
                    else
                    {
                        Toast.makeText(FinalizeAshaWorkActivity.this, "Failed Try Again", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    showErrorAlet(FinalizeAshaWorkActivity.this, "Failed to send otp, Please try again", result);
                }
            }
            else
            {
                Toast.makeText(FinalizeAshaWorkActivity.this, "Null Record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showErrorAlet(final Context context, String title, String message)
    {

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
                        finish();
                    }
                });


        ab.create().getWindow().getAttributes().windowAnimations = R.style.AppTheme;

        ab.show();
    }

    private class GetStateAmount extends AsyncTask<String, Void, ArrayList<Stateamount_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading state amount details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Stateamount_entity> doInBackground(String... param) {

            return WebServiceHelper.getstateamount();
        }

        @Override
        protected void onPostExecute(ArrayList<Stateamount_entity> result) {

            if (result != null)
            {
                stateAmountArray = result;
                if (CommonPref.getUserDetails(getApplicationContext()).getUserrole().equals("ASHAFC")){
                    new GetCentreAmount().execute();
                }
                else{
                    loadAshaData();
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "state amount details loaded", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Null Record, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetCentreAmount extends AsyncTask<String, Void, ArrayList<Centralamount_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading central amount details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Centralamount_entity> doInBackground(String... param) {

            return WebServiceHelper.getcentralamount();
        }

        @Override
        protected void onPostExecute(ArrayList<Centralamount_entity> result) {

            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                centralAmountArray = result;
                loadAshaFCData();
                Toast.makeText(getApplicationContext(), "central amount details loaded", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Null Record, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
