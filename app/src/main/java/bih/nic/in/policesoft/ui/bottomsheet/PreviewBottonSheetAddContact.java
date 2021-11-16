package bih.nic.in.policesoft.ui.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.OutPostEntry;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PreviewBottonSheetAddContact extends BottomSheetDialogFragment {

    BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.et_contact_type)
    EditText et_contact_type;
    @BindView(R.id.et_officer_name)
    EditText et_officer_name;
    @BindView(R.id.et_officer_contact_num)
    EditText et_officer_contact_num;
    @BindView(R.id.et_officer_email)
    EditText et_officer_email;
    @BindView(R.id.et_postoffice_name)
    EditText et_postoffice_name;
    @BindView(R.id.et_postoffice_add)
    EditText et_postoffice_add;
    @BindView(R.id.et_postoffice_num)
    TextView et_postoffice_num;
    @BindView(R.id.et_type_hospital)
    EditText et_type_hospital;
    @BindView(R.id.et_hosp_name)
    EditText et_hosp_name;
    @BindView(R.id.et_capacity_of_beds)
    EditText et_capacity_of_beds;
    @BindView(R.id.et_hosp_contct_num)
    EditText et_hosp_contct_num;
    @BindView(R.id.et_hosp_address)
    EditText et_hosp_address;
    @BindView(R.id.et_type_of_school)
    EditText et_type_of_school;
    @BindView(R.id.et_school_name)
    EditText et_school_name;
    @BindView(R.id.et_school_address)
    EditText et_school_address;
    @BindView(R.id.et_school_contct_num)
    EditText et_school_contct_num;
    @BindView(R.id.et_type_busstand)
    EditText et_type_busstand;
    @BindView(R.id.et_busstand_name)
    EditText et_busstand_name;
    @BindView(R.id.et_busstand_address)
    EditText et_busstand_address;
    @BindView(R.id.ll_officer)
    LinearLayout ll_officer;
    @BindView(R.id.ll_post_office)
    LinearLayout ll_post_office;
    @BindView(R.id.ll_hospital)
    LinearLayout ll_hospital;
    @BindView(R.id.ll_school)
    LinearLayout ll_school;
    @BindView(R.id.ll_busstand)
    LinearLayout ll_busstand;


    OnDoneButtonInterface mListener;
    private ContactDetailsEntry contactDetailsEntry;

    public static PreviewBottonSheetAddContact getInstance() {
        return new PreviewBottonSheetAddContact();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            contactDetailsEntry = bundle.getParcelable(Constants.PS_PARAM);
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
        final View view = inflater.inflate(R.layout.preview_addcontact_bottom_layout, container, false);
        ButterKnife.bind(this, view);
        et_contact_type.setText(contactDetailsEntry.getContact_Name());
        if ((contactDetailsEntry.getContact_Code().equals("1")) || (contactDetailsEntry.getContact_Code().equals("2")) || (contactDetailsEntry.getContact_Code().equals("3")) || (contactDetailsEntry.getContact_Code().equals("4")) || (contactDetailsEntry.getContact_Code().equals("5")) || (contactDetailsEntry.getContact_Code().equals("6")) || (contactDetailsEntry.getContact_Code().equals("7"))) {
            ll_officer.setVisibility(View.VISIBLE);
            ll_post_office.setVisibility(View.GONE);
            ll_hospital.setVisibility(View.GONE);
            ll_school.setVisibility(View.GONE);
            ll_busstand.setVisibility(View.GONE);
            et_officer_name.setText(contactDetailsEntry.getOfficer_Name());
            et_officer_contact_num.setText(contactDetailsEntry.getOfficer_Contact());
            et_officer_email.setText(contactDetailsEntry.getOfficer_Email());
        } else if (contactDetailsEntry.getContact_Code().equals("8")) {
            ll_officer.setVisibility(View.GONE);
            ll_post_office.setVisibility(View.VISIBLE);
            ll_hospital.setVisibility(View.GONE);
            ll_school.setVisibility(View.GONE);
            ll_busstand.setVisibility(View.GONE);

            et_postoffice_name.setText(contactDetailsEntry.getPostOffice_Name());
            et_postoffice_add.setText(contactDetailsEntry.getPostOffice_Add());
            et_postoffice_num.setText(contactDetailsEntry.getPostOffice_Number());
        } else if (contactDetailsEntry.getContact_Code().equals("9")) {
            ll_officer.setVisibility(View.GONE);
            ll_post_office.setVisibility(View.GONE);
            ll_hospital.setVisibility(View.VISIBLE);
            ll_school.setVisibility(View.GONE);
            ll_busstand.setVisibility(View.GONE);
            if (contactDetailsEntry.getHosp_Code().equals("G")) {
                et_type_hospital.setText(getResources().getString(R.string.govt));
            } else if (contactDetailsEntry.getHosp_Code().equals("P")) {
                et_type_hospital.setText(getResources().getString(R.string.priv));
            }
            et_hosp_name.setText(contactDetailsEntry.getHosp_Name());
            et_capacity_of_beds.setText(contactDetailsEntry.getCapacity_Bed());
            et_hosp_contct_num.setText(contactDetailsEntry.getHosp_Contact());
            et_hosp_address.setText(contactDetailsEntry.getHosp_Add());
        } else if (contactDetailsEntry.getContact_Code().equals("10")) {
            ll_officer.setVisibility(View.GONE);
            ll_post_office.setVisibility(View.GONE);
            ll_hospital.setVisibility(View.GONE);
            ll_school.setVisibility(View.VISIBLE);
            ll_busstand.setVisibility(View.GONE);
            if (contactDetailsEntry.getSchool_Code().equals("G")) {
                et_type_of_school.setText(getResources().getString(R.string.govt));
            } else if (contactDetailsEntry.getSchool_Code().equals("P")) {
                et_type_of_school.setText(getResources().getString(R.string.priv));
            }
            et_school_name.setText(contactDetailsEntry.getSchool_Name());
            et_school_address.setText(contactDetailsEntry.getSchool_Add());
            et_school_contct_num.setText(contactDetailsEntry.getSchool_Contact());
        } else if (contactDetailsEntry.getContact_Code().equals("11")) {
            ll_officer.setVisibility(View.GONE);
            ll_post_office.setVisibility(View.GONE);
            ll_hospital.setVisibility(View.GONE);
            ll_school.setVisibility(View.GONE);
            ll_busstand.setVisibility(View.VISIBLE);


            if (contactDetailsEntry.getBusStand_Code().equals("G")) {
                et_type_busstand.setText(getResources().getString(R.string.govt));
            } else if (contactDetailsEntry.getBusStand_Code().equals("P")) {
                et_type_busstand.setText(getResources().getString(R.string.priv));
            }
            et_busstand_name.setText(contactDetailsEntry.getBusStand_Name());
            et_busstand_address.setText(contactDetailsEntry.getBusStand_Add());
        }


//        et_landline.setText(contactDetailsEntry.getLandline());
//        if (!contactDetailsEntry.getThanaNotification().equals("")) {
//            if (contactDetailsEntry.getThanaNotification().equals("Y")) {
//                et_notification.setText(getResources().getString(R.string.yes));
//                et_notification_num.setText(contactDetailsEntry.getThanaNotification_Code());
//                txt_notification_date.setText(contactDetailsEntry.getThanaNotification_Date());
//            } else if (contactDetailsEntry.getThanaNotification().equals("N")) {
//                et_notification.setText(getResources().getString(R.string.no));
//            }
//        }
//        if (!contactDetailsEntry.getLandAvail().equals("")) {
//            if (contactDetailsEntry.getLandAvail().equals("Y")) {
//                et_land_avail.setText(getResources().getString(R.string.yes));
//                et_khata_num.setText(contactDetailsEntry.getKhataNum());
//                et_khesra_num.setText(contactDetailsEntry.getKhesraNum());
//            } else if (contactDetailsEntry.getLandAvail().equals("N")) {
//                et_land_avail.setText(getResources().getString(R.string.no));
//            }
//        }
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

