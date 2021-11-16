package bih.nic.in.policesoft.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import bih.nic.in.policesoft.R;


public class CustomAlertDialog {
    AlertDialog alertDialog=null;

    public CustomAlertDialog(Context ctx) {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.custom_alert_dialog);
            LayoutInflater inflater = LayoutInflater.from(ctx);
            View v = inflater.inflate(R.layout.alertdialog, null);
            builder.setView(v);
            ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.alertprogress);
            Sprite doublebouncy = new Circle();
            progressBar.setIndeterminateDrawable(doublebouncy);
            progressBar.setClickable(false);
            alertDialog = builder.create();
        }

    }

    public void showDialog() {
        alertDialog.show();
    }

    public void dismisDialog() {
        alertDialog.dismiss();
    }
}
