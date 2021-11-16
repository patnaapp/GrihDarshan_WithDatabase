package bih.nic.in.policesoft.ui.activity.entryform;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import bih.nic.in.policesoft.R;

public class HealthSubCentreEntryActivity extends AppCompatActivity {

    TextView tv_username_hsc,tv_aanganwadi_hsc,tv_hscname_hsc,tv_district_hsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_sub_centre_entry);
    }
}
