package bih.nic.in.policesoft.ui.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.entity.PoliceStationSignup;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PreviewBottonSheet extends BottomSheetDialogFragment {

    BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.et_range_name)
    EditText et_range_name;
    @BindView(R.id.et_district)
    EditText et_district;
    @BindView(R.id.et_subdiv)
    EditText et_subdiv;
    @BindView(R.id.et_policestation)
    EditText et_policestation;
    @BindView(R.id.et_sho_name)
    EditText et_sho_name;
    @BindView(R.id.et_mobile_no)
    EditText et_mobile_no;
    @BindView(R.id.et_landline)
    EditText et_landline;
    @BindView(R.id.et_notification)
    EditText et_notification;
    @BindView(R.id.et_notification_num)
    EditText et_notification_num;
    @BindView(R.id.txt_notification_date)
    TextView txt_notification_date;

    @BindView(R.id.et_land_avail)
    EditText et_land_avail;
    @BindView(R.id.et_khata_num)
    EditText et_khata_num;
    @BindView(R.id.et_khesra_num)
    TextView et_khesra_num;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confirm_password)
    EditText et_confirm_password;
    OnDoneButtonInterface mListener;
    private PoliceStationSignup policeStationSignup;
    public static PreviewBottonSheet getInstance() {
        return new PreviewBottonSheet();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            policeStationSignup = bundle.getParcelable(Constants.PS_PARAM);
        }
        if (context instanceof OnDoneButtonInterface) {
            mListener = (OnDoneButtonInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDoneButtonInterface");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.preview_bottom_layout, container, false);
        ButterKnife.bind(this, view);
        et_range_name.setText(policeStationSignup.getRangeName());
        et_district.setText(policeStationSignup.getDistName());
        et_subdiv.setText(policeStationSignup.getSubDivName());
        et_policestation.setText(policeStationSignup.getPsName());
        et_sho_name.setText(policeStationSignup.getSHOName());
        et_mobile_no.setText(policeStationSignup.getSHOMobile());
        et_landline.setText(policeStationSignup.getLandline());
        if(!policeStationSignup.getThanaNotification().equals("")) {
            if (policeStationSignup.getThanaNotification().equals("Y")) {
                et_notification.setText(getResources().getString(R.string.yes));
                et_notification_num.setText(policeStationSignup.getThanaNotification_Code());
                txt_notification_date.setText(policeStationSignup.getThanaNotification_Date());
            } else if (policeStationSignup.getThanaNotification().equals("N")) {
                et_notification.setText(getResources().getString(R.string.no));
            }
        }
        if(!policeStationSignup.getLandAvail().equals("")) {
            if (policeStationSignup.getLandAvail().equals("Y")) {
                et_land_avail.setText(getResources().getString(R.string.yes));
                et_khata_num.setText(policeStationSignup.getKhataNum());
                et_khesra_num.setText(policeStationSignup.getKhesraNum());
            } else if (policeStationSignup.getLandAvail().equals("N")) {
                et_land_avail.setText(getResources().getString(R.string.no));
            }
        }
        et_password.setText("**********");
        et_confirm_password.setText(policeStationSignup.getConfirmPassword());
        return view;
    }
    @OnClick(R.id.done_btn)
    protected void OnDoneButtonCLick(){
        if (mListener != null) {
            mListener.OnDoneClick();
            dismiss();
        }
    }
    @OnClick(R.id.edit_btn)
    protected void OnEditButtonCLick(){
        dismiss();
    }
}

