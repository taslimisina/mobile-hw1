package com.sharif.mobile.hw1.Controller;

import android.os.Message;
import android.util.Log;

import com.sharif.mobile.hw1.Models.Crypto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Loader {

    private static Loader instance = null;
    private static final String COIN_MARKET_CAP_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private static final int MAX_COIN_LOAD_COUNT = 10;
    private CryptoViewHandler handler;
    private AtomicBoolean busy;

    private Loader() {
        this.busy = new AtomicBoolean(false);
    }

    public static Loader getInstance() {
        if (instance == null)
            instance = new Loader();
        return instance;
    }

    public ArrayList<Crypto> loadCoins(int start) {
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

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("LOAD-COINS", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (! response.isSuccessful())
                    throw new IOException("Unsuccessful " + response);
                Log.v("LOAD-COINS", "Successful " + response);

                String body = response.body().string();
                ArrayList<Crypto> coins = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONObject(body).getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        Crypto crypto = new Crypto(json);
                        coins.add(crypto);
                        Log.v("COIN", crypto.toString());
                    }

                    // TODO: pass to handler and notify ui thread
                    Message message = new Message();
                    message.what = CryptoViewHandler.LOAD_DONE;
                    message.obj = coins;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    Log.v("LOAD-COINS", e.getMessage());
                }

            }
        });
        return new ArrayList<>();
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
}
