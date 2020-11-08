package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;

public class AshaWorker_Facilitator_Activity_List extends AppCompatActivity {

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="";
    TextView tv_name,tv_year,tv_month,tv_role;
    Financial_Year fyear;
    Financial_Month fmonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker__facilitator___list);

        initialise();

        user_role=getIntent().getStringExtra("role");
        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");


        if (user_role.equals("ASHA")){
            asha_worker_id=getIntent().getStringExtra("ashaid");
            asha_worker_nm=getIntent().getStringExtra("ashanm");
            tv_name.setText(asha_worker_nm);
            tv_role.setText("आशा वर्कर");
        }
        else if (user_role.equals("ASHAFC")) {
            faciltator_id=getIntent().getStringExtra("_faciltator_id");
            facilitator_nm=getIntent().getStringExtra("_faciltator_nm");
            tv_name.setText(facilitator_nm);
            tv_role.setText("आशा फैसिलिटेटर");
        }

        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());
    }

    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
    }
}
