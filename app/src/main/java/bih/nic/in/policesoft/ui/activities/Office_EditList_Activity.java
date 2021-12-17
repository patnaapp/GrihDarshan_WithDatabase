package bih.nic.in.policesoft.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.SerBList1;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;

public class Office_EditList_Activity extends AppCompatActivity {

    private ProgressDialog dialog;
    DataBaseHelper dataBaseHelper;
    ListView listView;
    String username;
    //Adapter_view_report adaptor_view_report;
    ArrayList<OfficeUnderPsEntity> listPhase2 = new ArrayList<OfficeUnderPsEntity>();
    RecyclerView recyclerVieweditentry;
    RecyclerView.Adapter recyclerViewAdapterEdit;
    RecyclerView.LayoutManager recylerViewLayoutManager1;

    TextView tv_Norecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office__edit_list_);

        dataBaseHelper=new DataBaseHelper(Office_EditList_Activity.this);

        recyclerVieweditentry = (RecyclerView) findViewById(R.id.my_recycler_view1);
        tv_Norecord = (TextView) findViewById(R.id.tv_Norecord1);

        listPhase2 = dataBaseHelper.getAllOfficeEntryDetail(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", ""));

        recyclerVieweditentry = (RecyclerView) findViewById(R.id.my_recycler_view1);
        tv_Norecord = (TextView) findViewById(R.id.tv_Norecord1);


        if (listPhase2!= null &&listPhase2.size() > 0) {
            tv_Norecord.setVisibility(View.GONE);
            recyclerVieweditentry.setVisibility(View.VISIBLE);

        } else {
            recyclerVieweditentry.setVisibility(View.GONE);
            tv_Norecord.setVisibility(View.VISIBLE);
        }





        recylerViewLayoutManager1 = new LinearLayoutManager(Office_EditList_Activity.this);

        recyclerVieweditentry.setLayoutManager(recylerViewLayoutManager1);
        recyclerViewAdapterEdit = new SerBList1(Office_EditList_Activity.this, listPhase2,PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", ""),PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Token", ""));

        recyclerViewAdapterEdit.notifyDataSetChanged();
        recyclerVieweditentry.setAdapter(recyclerViewAdapterEdit);
    }
}