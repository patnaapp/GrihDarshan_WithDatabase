package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import bih.nic.in.ashwin.R;

public class AshaWorkerEntryForm_Activity extends AppCompatActivity {

    Spinner sp_work_categ,sp_work;
    EditText edt_work_complt_date,edt_amount,edt_volume,edt_pageno,edt_slno,edt_reg_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker_entry_form_);
    }
}
