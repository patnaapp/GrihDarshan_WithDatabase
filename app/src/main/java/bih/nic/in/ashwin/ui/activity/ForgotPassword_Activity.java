package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ForgotPasswordEntitiy;
import bih.nic.in.ashwin.entity.OtpEntitiy;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.utility.AppConstant;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class ForgotPassword_Activity extends AppCompatActivity {

    Spinner spinner_role,spinner_filter;
    EditText et_userid,et_mob,et_aadhar;
    Button btn_get_password;
    DataBaseHelper localDBHelper;
    ArrayList<UserRole> userRoleList = new ArrayList<UserRole>();
    ArrayAdapter<String> roleAdapter;
    String _filter_type_list[] = {"-select-","यूजर आई० डी०","मोबाइल नंबर"};
    String userRole = "",filter_type="",filter_type_id="";
    TextView tv_role,tv_filter_type;
    LinearLayout ll_userid,ll_mob,ll_aadhar,ll_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_);

        initialise();
        loadUserRoleSpinnerdata();

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, _filter_type_list);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_filter.setAdapter(adaptor);

        spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 > 0) {

                    UserRole role = userRoleList.get(arg2-1);
                    userRole = role.getRole();

                    if (userRole.equals("DEPT"))
                    {
                        ll_aadhar.setVisibility(View.GONE);
                        ll_filter.setVisibility(View.GONE);
                        ll_mob.setVisibility(View.VISIBLE);
                        ll_userid.setVisibility(View.VISIBLE);

                    }
                    else {
                        ll_aadhar.setVisibility(View.VISIBLE);
                        ll_filter.setVisibility(View.VISIBLE);
                    }

                } else {
                  //  userRole ="";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 > 0) {



                    filter_type = _filter_type_list[arg2];
                    if (filter_type.equals("यूजर आई० डी०")) {
                        filter_type_id = "1";
                        ll_aadhar.setVisibility(View.VISIBLE);
                        ll_userid.setVisibility(View.VISIBLE);
                        ll_mob.setVisibility(View.GONE);

                    } else if (filter_type.equals("मोबाइल नंबर")) {
                        filter_type_id = "2";
                        ll_aadhar.setVisibility(View.VISIBLE);
                        ll_userid.setVisibility(View.GONE);
                        ll_mob.setVisibility(View.VISIBLE);


                    }
                    else {
                        filter_type_id="";
                    }

                } else {
                   // userRole ="";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btn_get_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isDataValidated()) {
                    ForgotPasswordEntitiy data = new ForgotPasswordEntitiy();
                    data.setUserRole(userRole);
                    data.setFilterType(filter_type_id);
                    data.setAadharNo(et_aadhar.getText().toString().trim());
                    data.setUserId(et_userid.getText().toString().trim());
                    data.setMobNo(et_mob.getText().toString().trim());

                    new ForgotPassword(data).execute();

                }
            }
        });
    }

    public void initialise()
    {
        spinner_role=findViewById(R.id.spinner_role);
        spinner_filter=findViewById(R.id.spinner_filter);
        et_mob=findViewById(R.id.et_mob);
        et_userid=findViewById(R.id.et_userid);
        et_aadhar=findViewById(R.id.et_aadhar);
        btn_get_password=findViewById(R.id.btn_get_password);
        tv_filter_type=findViewById(R.id.tv_filter_type);
        tv_role=findViewById(R.id.tv_role);
        ll_userid=findViewById(R.id.ll_userid);
        ll_mob=findViewById(R.id.ll_mob);
        ll_aadhar=findViewById(R.id.ll_aadhar);
        ll_filter=findViewById(R.id.ll_filter);
    }

    public void  loadUserRoleSpinnerdata() {
        localDBHelper = new DataBaseHelper(getApplicationContext());
        userRoleList = localDBHelper.getUserRoleList();
        String[] typeNameArray = new String[userRoleList.size() + 1];
        typeNameArray[0] = "- चयन करें -";
        int i = 1;
        for (UserRole type : userRoleList)
        {
            typeNameArray[i] = type.getRoleDescHN();
            i++;
        }
        roleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, typeNameArray);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_role.setAdapter(roleAdapter);
    }


    private class ForgotPassword extends AsyncTask<String, Void, String> {
        ForgotPasswordEntitiy data;

        private final ProgressDialog dialog = new ProgressDialog(ForgotPassword_Activity.this);

        public ForgotPassword(ForgotPasswordEntitiy data) {
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
            return WebServiceHelper.ForgotPassword(data);
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

                if(result.equals("1"))
                {
                    showErrorAlet(ForgotPassword_Activity.this, "सफलतापूर्वक भेजा गया", "पासवर्ड पंजीकृत मोबाइल नंबर पर भेज दिया गया है");

                }
                else
                {
                    showErrorAlet(ForgotPassword_Activity.this, "पासवर्ड भेजने में विफल, कृपया पुनः प्रयास करें", result);
                }
            }
            else
            {
                Toast.makeText(ForgotPassword_Activity.this, "Null Record", Toast.LENGTH_SHORT).show();
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


    public Boolean isDataValidated()
    {
        View focusView = null;
        boolean validate = true;

        if (!userRole.equals("DEPT")) {
            if (filter_type_id.equals("")) {
                tv_filter_type.setError("कृप्या फ़िल्टर का चयन करें ");
                focusView = tv_filter_type;
                validate = false;
            }

        }
        if(userRole.equals(""))
        {
            tv_role.setError("कृप्या यूजर का प्रकार का चयन करे");
            focusView = tv_role;
            validate = false;
        }

        if (userRole.equals("DEPT") || filter_type_id.equals("1"))
        {
            if (et_userid.getText().toString().equals(""))
            {
                et_userid.setError("कृप्या यूजर आई० डी० दर्ज करे");
                focusView = et_userid;
                validate = false;
            }
        }

        if (userRole.equals("DEPT")|| filter_type_id.equals("2")) {
            if (et_mob.getText().toString().equals("") || et_mob.getText().length() < 10) {
                et_mob.setError("कृप्या मोबाइल नंबर दर्ज करे");
                focusView = et_mob;
                validate = false;
            }
        }

        if (!userRole.equals("DEPT")) {
            if (et_aadhar.getText().toString().equals("") || et_aadhar.getText().length() < 4) {
                et_aadhar.setError("कृप्या आधार का अंतिम 4 अंक दर्ज करे");
                focusView = et_aadhar;
                validate = false;
            }
        }

        return validate;
    }


}