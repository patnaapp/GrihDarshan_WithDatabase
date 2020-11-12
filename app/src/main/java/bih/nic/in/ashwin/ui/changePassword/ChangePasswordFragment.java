package bih.nic.in.ashwin.ui.changePassword;

import android.os.Bundle;
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
import bih.nic.in.ashwin.utility.CommonPref;


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
}
