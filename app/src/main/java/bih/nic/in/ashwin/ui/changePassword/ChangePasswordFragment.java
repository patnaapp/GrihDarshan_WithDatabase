package bih.nic.in.ashwin.ui.changePassword;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.ParseException;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.ui.activity.UserHomeActivity;
import bih.nic.in.ashwin.ui.home.HomeFragment;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class ChangePasswordFragment extends Fragment {

    TextView tv_username;
    EditText et_old_pass,et_new_pass,et_confirm_pass;
    Button btn_proceed;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);

        initializeViews(root);

        setUserDetail();

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataValidated()){
                    Toast.makeText(getContext(), "Validated", Toast.LENGTH_SHORT).show();
                    if(Utiilties.isOnline(getContext())) {
                        new ChangePassword().execute();
                    }else{
                        Utiilties.showInternetAlert(getContext());
                    }
                }
            }
        });
        return root;
    }

    void initializeViews(View root) {
        tv_username = root.findViewById(R.id.tv_username);

        et_old_pass = root.findViewById(R.id.et_old_pass);
        et_new_pass = root.findViewById(R.id.et_new_pass);
        et_confirm_pass = root.findViewById(R.id.et_confirm_pass);

        btn_proceed = root.findViewById(R.id.btn_proceed);
    }

    public void setUserDetail(){
        UserDetails userInfo = CommonPref.getUserDetails(getContext());

        tv_username.setText(userInfo.getUserName());
    }

    public Boolean isDataValidated(){
        View focusView = null;
        boolean validate = true;

        if (et_old_pass.getText().toString().equals("")) {
            et_old_pass.setError("कृप्या पुराना पासवर्ड डालें");
            focusView = et_old_pass;
            validate = false;
        }

        if (et_new_pass.getText().toString().equals("")) {
            et_new_pass.setError("कृप्या नया पासवर्ड डालें");
            focusView = et_new_pass;
            validate = false;
        }

        if (et_confirm_pass.getText().toString().equals("")) {
            et_confirm_pass.setError("कृप्या नया पासवर्ड पुष्टि करें");
            focusView = et_confirm_pass;
            validate = false;
        }else if(!et_confirm_pass.getText().toString().equals(et_new_pass.getText().toString())){
            et_confirm_pass.setError("पुष्टि पासवर्ड नए पासवर्ड से मेल नहीं खाता");
            focusView = et_confirm_pass;
            validate = false;
        }

        return validate;
    }


    private class ChangePassword extends AsyncTask<String, Void, String> {
        String data;
        private final ProgressDialog dialog = new ProgressDialog(getContext());

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext()).create();

//        ChangePassword(String data) {
//            this.data = data;
//
//        }


        @Override
        protected void onPreExecute()
        {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param)
        {

            String res = WebServiceHelper.ChangePassword(CommonPref.getUserDetails(getContext()).getUserrole(),CommonPref.getUserDetails(getContext()).getUserID(),et_old_pass.getText().toString(),et_confirm_pass.getText().toString());
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("Responseval", "" + result);
            if (result != null) {
                if (result.equals("1")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    // Ask the final question
                    builder.setMessage("The Password has been changed successfully!");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when user clicked the Yes button
                            // Set the TextView visibility GONE
                            Intent iUserHome = new Intent(getContext(), UserHomeActivity.class);
                            iUserHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(iUserHome);

                           // finish();
//                            Intent intent = new Intent(getContext(), HomeFragment.class);
//                            startActivity(intent);


                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
               /* Toast.makeText(getApplicationContext(), "The Password has been sent to your registerd mobile number", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Custom_forgot_password.this, Home.class);
                startActivity(intent);*/

                } else {
                    Toast.makeText(getContext(), "Failed to change password, try later", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
