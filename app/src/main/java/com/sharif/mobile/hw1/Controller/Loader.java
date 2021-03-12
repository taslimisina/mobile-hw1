package com.sharif.mobile.hw1.Controller;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.sharif.mobile.hw1.Models.Crypto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Loader {

    private static Loader instance = null;
    private static final String COIN_MARKET_CAP_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private static final int MAX_COIN_LOAD_COUNT = 10;
    private static final String CACHE_FORMAT = "price-cache-%s.json";
    private final ThreadController threadController;
    private final AtomicBoolean busy;
    private WeakReference<Context> context;
    private CryptoViewHandler handler;

    private Loader() {
        this.threadController = ThreadController.getInstance();
        this.busy = new AtomicBoolean(false);
    }

    public static Loader getInstance() {
        if (instance == null)
            instance = new Loader();
        return instance;
    }

    public void loadFromNetwork(int start) {
        Log.v("LoadCoins", "build request");
        OkHttpClient okHttpClient = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(COIN_MARKET_CAP_URL).newBuilder();
        urlBuilder.addQueryParameter("start", String.valueOf(start));
        urlBuilder.addQueryParameter("limit", String.valueOf(MAX_COIN_LOAD_COUNT));
        urlBuilder.addQueryParameter("convert", "USD");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url)
                .addHeader("X-CMC_PRO_API_KEY", "98193a8b-6323-46c1-87d9-dcd8bb98c485")
                .addHeader("Accept" ,"application/json").build();

        Message message = new Message();
        message.what = CryptoViewHandler.SET_PROGRESS;
        message.obj = 0.5f;
        handler.sendMessage(message);

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.v("LOAD-COINS", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                Message message = new Message();
                message.what = CryptoViewHandler.SET_PROGRESS;
                message.obj = 0.8f;
                handler.sendMessage(message);

                if (! response.isSuccessful())
                    throw new IOException("Unsuccessful " + response);
                Log.v("LOAD-COINS", "Successful " + response);

                try {
                    String body = response.body().string();
                    ArrayList<Crypto> coins = extractCoins(body);

                    message = new Message();
                    message.what = start == 1 ? CryptoViewHandler.REFRESH_DONE : CryptoViewHandler.LOAD_DONE;
                    message.obj = coins;
                    handler.sendMessage(message);

                    if (!threadController.isPoolFull()) {
                        threadController.submitTask(() -> updateCache(body, start));
                    }
                } catch (Exception e) {
                    Log.v("LOAD-COINS", e.getMessage());
                }
            }
        });
    }

    private void loadFromCache(int start) {
        if (context == null)
            return;

        final File cacheFile = new File(String.format(CACHE_FORMAT, start));
        try {
            FileInputStream inputStream = context.get().openFileInput(cacheFile.getPath());
            String body = readFile(inputStream);

            Message message = new Message();
            message.what = CryptoViewHandler.SET_PROGRESS;
            message.obj = 0.7f;
            handler.sendMessage(message);

            ArrayList<Crypto> coins = extractCoins(body);

            message = new Message();
            message.what = CryptoViewHandler.LOAD_DONE;
            message.obj = coins;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.i("LOAD-CACHE", e.getMessage());
            e.printStackTrace();
            this.setFree();
        }
    }

    private void updateCache(String body, int start) {
        if (context == null)
            return;

        File cacheFile = new File(String.format(CACHE_FORMAT, start));
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.get().openFileOutput(cacheFile.getPath(), MODE_PRIVATE);
            fileOutputStream.write(body.getBytes());
        } catch (Exception e) {
            Log.v("UPDATE-CACHE", e.getMessage());
        }
    }

    public void loadMoreCoins(int start) {
        if (NetworkUtil.isConnected(context.get())) {
            loadFromNetwork(start);
        } else {
            loadFromCache(start);
        }
    }

    public void refreshCoins() {
        if (NetworkUtil.isConnected(context.get())) {
            loadFromNetwork(1);
        } else {
            Log.v("REFRESH", "No Network");
            // todo: show network warning
            this.setFree();
        }
    }

    private ArrayList<Crypto> extractCoins(String body) throws JSONException {
        ArrayList<Crypto> coins = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(body).getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            coins.add(new Crypto(json));
        }
        return coins;
    }

    private String readFile(InputStream cacheFile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        while((i = cacheFile.read())!= -1)
            stringBuilder.append((char) i);
        return stringBuilder.toString();
    }

    public boolean isBusy() {
        return this.busy.get();
    }

    public void setFree() {
        this.busy.set(false);
    }

    public void setBusy() {
        this.busy.set(true);
    }

    public void setHandler(CryptoViewHandler cryptoViewHandler) {
        this.handler = cryptoViewHandler;
    }

    public void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

}
