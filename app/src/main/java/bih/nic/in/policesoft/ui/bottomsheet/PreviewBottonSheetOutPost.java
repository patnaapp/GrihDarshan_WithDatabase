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
import bih.nic.in.policesoft.entity.OutPostEntry;
import bih.nic.in.policesoft.entity.PoliceStationSignup;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PreviewBottonSheetOutPost extends BottomSheetDialogFragment {

    BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.et_oupost_name)
    EditText et_oupost_name;
    @BindView(R.id.et_outpost_inch)
    EditText et_outpost_inch;
    @BindView(R.id.et_outpost_inch_Mobi)
    EditText et_outpost_inch_Mobi;
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

    OnDoneButtonInterface mListener;
    private OutPostEntry outPostEntry;

    public static PreviewBottonSheetOutPost getInstance() {
        return new PreviewBottonSheetOutPost();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            outPostEntry = bundle.getParcelable(Constants.PS_PARAM);
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
        final View view = inflater.inflate(R.layout.preview_outpost_bottom_layout, container, false);
        ButterKnife.bind(this, view);
        et_oupost_name.setText(outPostEntry.getOutPostName());
        et_outpost_inch.setText(outPostEntry.getOutPost_Inch_Name());
        et_outpost_inch_Mobi.setText(outPostEntry.getSHOMobile());
        et_landline.setText(outPostEntry.getLandline());
        if (!outPostEntry.getThanaNotification().equals("")) {
            if (outPostEntry.getThanaNotification().equals("Y")) {
                et_notification.setText(getResources().getString(R.string.yes));
                et_notification_num.setText(outPostEntry.getThanaNotification_Code());
                txt_notification_date.setText(outPostEntry.getThanaNotification_Date());
            } else if (outPostEntry.getThanaNotification().equals("N")) {
                et_notification.setText(getResources().getString(R.string.no));
            }
        }
        if (!outPostEntry.getLandAvail().equals("")) {
            if (outPostEntry.getLandAvail().equals("Y")) {
                et_land_avail.setText(getResources().getString(R.string.yes));
                et_khata_num.setText(outPostEntry.getKhataNum());
                et_khesra_num.setText(outPostEntry.getKhesraNum());
            } else if (outPostEntry.getLandAvail().equals("N")) {
                et_land_avail.setText(getResources().getString(R.string.no));
            }
        }
        return view;
    }

    @OnClick(R.id.done_btn)
    protected void OnDoneButtonCLick() {
        if (mListener != null) {
            mListener.OnDoneClick();
            dismiss();
        }
    }

    @OnClick(R.id.edit_btn)
    protected void OnEditButtonCLick() {
        dismiss();
    }
}

