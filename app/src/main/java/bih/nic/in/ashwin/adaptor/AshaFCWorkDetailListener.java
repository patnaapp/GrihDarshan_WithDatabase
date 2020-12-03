package bih.nic.in.ashwin.adaptor;

import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;

public interface AshaFCWorkDetailListener {
    public void onEditFCWork(AshaFascilitatorWorkEntity info);
    void onDeleteFCWork(int position);
}
