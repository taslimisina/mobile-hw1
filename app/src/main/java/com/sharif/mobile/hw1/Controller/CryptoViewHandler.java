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
    public static final int INIT_COINS = 1;
    public static final int LOAD_MORE_COINS = 2;

    private WeakReference<MainActivity> cryptoActivityWeakReference;
    private Loader loader;

    public CryptoViewHandler(MainActivity cryptoActivity) {
        this.cryptoActivityWeakReference = new WeakReference<>(cryptoActivity);
        this.loader = Loader.getInstance();
        this.loader.setHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {

        final MainActivity cryptoActivity = cryptoActivityWeakReference.get();
        if (cryptoActivity == null)
            return;

        if (msg.what == LOAD_DONE) {
            if (cryptoActivity.progressBar.getVisibility() == ProgressBar.VISIBLE)
                cryptoActivity.progressBar.setVisibility(ProgressBar.GONE);
            cryptoActivity.cryptoViewAdapter.addAll((ArrayList<Crypto>) msg.obj);
            cryptoActivity.swipeContainer.setRefreshing(false);
            loader.setNetworkFree();
        } else if (msg.what == INIT_COINS) {
            if (loader.isNetworkBusy())
                return;
            loader.setNetworkBusy();
            cryptoActivity.cryptoViewAdapter.clear();
            loader.getExecutor().execute(new Runnable() {
                @Override
                public void run() { loader.loadCoins(1); }
            });
        } else if (msg.what == LOAD_MORE_COINS) {
            if (loader.isNetworkBusy())
                return;
            loader.setNetworkBusy();
            loader.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    loader.loadCoins(cryptoActivity.cryptoViewAdapter.getItemCount() + 1);
                }
            });
        } else {
            Log.v("HANDLER", "Unknown Message");
        }
    }
}

