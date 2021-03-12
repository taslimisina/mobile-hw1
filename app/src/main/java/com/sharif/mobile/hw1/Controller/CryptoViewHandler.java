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
            progressBar.setProgress(progressBar.getMax());

            if (cryptoActivity.swipeContainer.isRefreshing()) // todo: check!!
                cryptoActivity.cryptoViewAdapter.clear();

            cryptoActivity.cryptoViewAdapter.addAll((ArrayList<Crypto>) msg.obj);
            cryptoActivity.swipeContainer.setRefreshing(false);
            loader.setFree();

            progressBar.setVisibility(ProgressBar.GONE);
        }
        else if (msg.what == SET_PROGRESS) {
            progressBar.setProgress((int)((float)msg.obj * progressBar.getMax()));
        }
        else {
            Log.v("HANDLER", "Unknown Message");
        }
    }
}
