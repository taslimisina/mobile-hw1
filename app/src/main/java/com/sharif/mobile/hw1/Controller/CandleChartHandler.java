package com.sharif.mobile.hw1.Controller;

import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.sharif.mobile.hw1.Activities.CandleChartActivity;

import java.lang.ref.WeakReference;

public class CandleChartHandler extends Handler {

    public static final int TOAST = 0;
    public static final int SET_PROGRESS = 1;
    public static final int PROGRESS_DONE = 2;
    public static final int INVALIDAT_CHART = 3;
    private WeakReference<CandleChartActivity> candleActivityReference;

    public CandleChartHandler(CandleChartActivity activity) {
        this.candleActivityReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        CandleChartActivity candleChartActivity = this.candleActivityReference.get();
        if (candleChartActivity == null || candleChartActivity.isFinishing())
            return;
        ProgressBar progressBar = candleChartActivity.getProgressBar();

        if (msg.what == TOAST) {
            String text = (String) msg.obj;
            Toast.makeText(candleChartActivity.getApplicationContext(), text, Toast.LENGTH_LONG)
                    .show();
        }
        else if (msg.what == SET_PROGRESS) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBar.setProgress((int)((float)msg.obj * progressBar.getMax()));
        }
        else if (msg.what == PROGRESS_DONE) {
            progressBar.setVisibility(ProgressBar.GONE);
        } else if (msg.what == INVALIDAT_CHART) {
            ((Chart) msg.obj).invalidate();
        }
    }
}
