package com.ys.mynearby;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

/**
 * Created by ss on 007-07-01-2017.
 */

public class GPSEnableDialog extends AlertDialog implements Dialog.OnClickListener{
    Context context;
    public GPSEnableDialog(Context context) {
        super(context);
        this.context = context;
        setTitle("MNB");
        setMessage("Enable location services!");
        setButton(AlertDialog.BUTTON_POSITIVE,"Enable",this);
        setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setCancelable(false);



    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d("MNB","i : "+i);
        if (i == AlertDialog.BUTTON_POSITIVE){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
            dismiss();
        }else if(i == AlertDialog.BUTTON_NEGATIVE){
            dismiss();
        }
    }
}
