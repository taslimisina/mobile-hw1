package com.sharif.mobile.hw1.Controller;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.sharif.mobile.hw1.Activities.CandleChartActivity;

import java.lang.ref.WeakReference;

public class CandleChartToastHandler extends Handler {

    public static final int TOAST = 0;
    private WeakReference<CandleChartActivity> candleActivityReference;

    public CandleChartToastHandler(CandleChartActivity activity) {
        this.candleActivityReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        CandleChartActivity candleChartActivity = this.candleActivityReference.get();
        if (candleChartActivity == null || candleChartActivity.isFinishing())
            return;

        if (msg.what == TOAST) {
            String text = (String) msg.obj;
            Toast.makeText(candleChartActivity.getApplicationContext(), text, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
