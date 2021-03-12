package com.sharif.mobile.hw1.Controller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import com.sharif.mobile.hw1.MainActivity;
import com.sharif.mobile.hw1.Models.Crypto;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CryptoViewHandler extends Handler {

    public static final int LOAD_DONE = 0;
    public static final int SET_PROGRESS = 1;
    public static final int REFRESH_DONE = 2;

    private WeakReference<MainActivity> cryptoActivityWeakReference;
    private Loader loader;

    public CryptoViewHandler(MainActivity cryptoActivity) {
        this.cryptoActivityWeakReference = new WeakReference<>(cryptoActivity);
        this.loader = Loader.getInstance();
    }

    @Override
    public void handleMessage(Message msg) {
        final MainActivity cryptoActivity = cryptoActivityWeakReference.get();
        if (cryptoActivity == null || cryptoActivity.isFinishing())
            return;

        ProgressBar progressBar = cryptoActivity.progressBar;
        if (msg.what == LOAD_DONE) {
            cryptoActivity.cryptoViewAdapter.addAll((ArrayList<Crypto>) msg.obj);
            progressBar.setVisibility(ProgressBar.GONE);
            loader.setFree();
        }
        else if (msg.what == SET_PROGRESS) {
            progressBar.setProgress((int)((float)msg.obj * progressBar.getMax()));
        } else if (msg.what == REFRESH_DONE) {
            progressBar.setProgress(progressBar.getMax());
            cryptoActivity.cryptoViewAdapter.clear();
            cryptoActivity.swipeContainer.setRefreshing(false);
            cryptoActivity.cryptoViewAdapter.addAll((ArrayList<Crypto>) msg.obj);
            progressBar.setVisibility(ProgressBar.GONE);
            loader.setFree();
        } else {
            Log.v("HANDLER", "Unknown Message");
        }
    }
}
