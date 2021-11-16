package bih.nic.in.policesoft.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.entity.PoliceStationSignup;
import bih.nic.in.policesoft.ui.activity.LoginActivity;
import bih.nic.in.policesoft.utility.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PSDetailsFullScreenDilog extends DialogFragment {
    public static final String TAG = "FullScreenDialog";
    @BindView(R.id.tv_range)
    TextView tv_range;
    @BindView(R.id.tv_disttrict)
    TextView tv_disttrict;
    @BindView(R.id.tv_sub)
    TextView tv_sub;
    @BindView(R.id.tv_psname)
    TextView tv_psname;
    @BindView(R.id.tv_shoname)
    TextView tv_shoname;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.userid)
    TextView userid;
    @BindView(R.id.password)
    TextView password;
    @BindView(R.id.tv_email)
    TextView tv_email;
    private PoliceStationSignup policeStationSignup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            policeStationSignup = bundle.getParcelable(Constants.PS_PARAM);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        View view = getActivity().getLayoutInflater().inflate(R.layout.psdetails_dilog, parent, false);
        ButterKnife.bind(this, view);
        tv_range.setText(": "+policeStationSignup.getRangeName());
        tv_disttrict.setText(": "+policeStationSignup.getDistName());
        tv_sub.setText(": "+policeStationSignup.getSubDivName());
        tv_psname.setText(": "+policeStationSignup.getPsName());
        tv_shoname.setText(": "+policeStationSignup.getSHOName());
        tv_mobile.setText(": "+policeStationSignup.getSHOMobile());
        tv_email.setText(": "+policeStationSignup.getSHOEmail());
        password.setText(": "+policeStationSignup.getPassword());
        userid.setText(": "+policeStationSignup.getUID());
        return view;
    }

    @OnClick(R.id.btn_ok)
    protected void Close(){
        dismiss();
        Intent i=new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();

    }

}
