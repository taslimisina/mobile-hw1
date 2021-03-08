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
    public static final int LOAD_COINS = 1;

    private WeakReference<MainActivity> cryptoActivityWeakReference;
    private Loader loader;

    public CryptoViewHandler(MainActivity cryptoActivity) {
        this.cryptoActivityWeakReference = new WeakReference<>(cryptoActivity);
        this.loader = Loader.getInstance();
        this.loader.setHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {

        MainActivity cryptoActivity = cryptoActivityWeakReference.get();
        if (cryptoActivity == null)
            return;

        if (msg.what == LOAD_DONE) {
            if (cryptoActivity.progressBar.getVisibility() == ProgressBar.VISIBLE)
                cryptoActivity.progressBar.setVisibility(ProgressBar.GONE);
            cryptoActivity.cryptoViewAdapter.addAll((ArrayList<Crypto>) msg.obj);
            cryptoActivity.swipeContainer.setRefreshing(false);
            loader.setFree();
        } else if (msg.what == LOAD_COINS) {
            if (loader.isBusy())
                return;
            loader.setBusy();
            loader.loadCoins(cryptoActivity.cryptoViewAdapter.getItemCount() + 1);
        } else {
            Log.v("HANDLER", "Unknown Message");
        }
    }
}
