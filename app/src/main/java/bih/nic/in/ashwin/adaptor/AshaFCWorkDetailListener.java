package bih.nic.in.ashwin.adaptor;

import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;

public interface AshaFCWorkDetailListener {
    public void onEditFCWork(AshaFascilitatorWorkEntity info);
    void onDeleteFCWork(int position);
    void onEditAshaWork(AshaWorkEntity info);
}
