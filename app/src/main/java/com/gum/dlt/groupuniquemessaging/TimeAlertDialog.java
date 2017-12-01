package com.gum.dlt.groupuniquemessaging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Daniel on 12/1/2017.
 */

public class TimeAlertDialog extends AlertDialog.Builder {
    private Context mContext;
    private AlertDialog mAlertDialog;

    public TimeAlertDialog(Context context) {
        super(context);
        mContext = context;
    }

//    @SuppressLint("InflateParams")
    @Override
    public AlertDialog show() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.time_alert_dialog, null);

        mAlertDialog = super.show();
        return mAlertDialog;
    }

}