package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.utility.CommonPref;

public class AshaFacilitatorNoOfDays_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="";
    TextView tv_name,tv_year,tv_month,tv_role;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data;
    Button btn_finalize;
    ArrayList<AshaWorkEntity> ashawork;
    String version="";
    Spinner sp_worker;
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    DataBaseHelper dbhelper;
    ArrayList<AshaFacilitator_Entity> facilitatorList = new ArrayList<AshaFacilitator_Entity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_facilitator_no_of_days_);

        dbhelper=new DataBaseHelper(this);
        initialise();

        user_role=getIntent().getStringExtra("role");
        //   svrid=getIntent().getStringExtra("svr");
        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");

        tv_role.setText("आशा फैसिलिटेटर");
        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());
        loadWorkerFascilatorData();

    }

    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
        rv_data = findViewById(R.id.recyclerview_data);
        btn_finalize = findViewById(R.id.btn_finalize);
        sp_worker = findViewById(R.id.sp_worker);
        // btn_finalize.setVisibility(View.GONE);
        sp_worker.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void loadWorkerFascilatorData(){
   if (user_role.equals("ASHAFC")){
            facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(AshaFacilitatorNoOfDays_Activity.this).getHSCCode());

            ArrayList array = new ArrayList<String>();
            array.add("-Select-");
            array.add("ALL");

            for (AshaFacilitator_Entity info: facilitatorList)
            {
                // if(!info.getFinancial_year().equals("anyType{}")){
                array.add(info.get_Facilitator_Name_Hn());
                // }
            }

            ArrayAdapter adaptor = new ArrayAdapter(AshaFacilitatorNoOfDays_Activity.this, android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_worker.setAdapter(adaptor);
            sp_worker.setSelection(1);

        }
        sp_worker.setOnItemSelectedListener(this);
    }
}
