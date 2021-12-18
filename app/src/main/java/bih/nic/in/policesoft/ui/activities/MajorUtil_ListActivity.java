package bih.nic.in.policesoft.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.MajorUtilAdapter;
import bih.nic.in.policesoft.adaptor.SerBList1;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;

public class MajorUtil_ListActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    DataBaseHelper dataBaseHelper;
    ListView listView;
    String username;
    //Adapter_view_report adaptor_view_report;
    ArrayList<MajorUtilEntry> majorUtilEntries_list = new ArrayList<MajorUtilEntry>();
    RecyclerView recyclerVieweditentry;
    RecyclerView.Adapter recyclerViewAdapterEdit;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    TextView tv_Notrecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_util_list);


        dataBaseHelper=new DataBaseHelper(MajorUtil_ListActivity.this);

        recyclerVieweditentry = (RecyclerView) findViewById(R.id.rv_majorUtils);
        tv_Notrecord = (TextView) findViewById(R.id.tv_Not_record);

        majorUtilEntries_list = dataBaseHelper.getAllMajorUtilEntryDetail(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", ""));

        recyclerVieweditentry = (RecyclerView) findViewById(R.id.rv_majorUtils);
        tv_Notrecord = (TextView) findViewById(R.id.tv_Not_record);


        if (majorUtilEntries_list!= null &&majorUtilEntries_list.size() > 0) {
            tv_Notrecord.setVisibility(View.GONE);
            recyclerVieweditentry.setVisibility(View.VISIBLE);

        } else {
            recyclerVieweditentry.setVisibility(View.GONE);
            tv_Notrecord.setVisibility(View.VISIBLE);
        }

        recylerViewLayoutManager = new LinearLayoutManager(MajorUtil_ListActivity.this);

        recyclerVieweditentry.setLayoutManager(recylerViewLayoutManager);
        recyclerViewAdapterEdit = new MajorUtilAdapter(MajorUtil_ListActivity.this, majorUtilEntries_list,PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", ""),PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Token", ""));

        recyclerViewAdapterEdit.notifyDataSetChanged();
        recyclerVieweditentry.setAdapter(recyclerViewAdapterEdit);
    }
}