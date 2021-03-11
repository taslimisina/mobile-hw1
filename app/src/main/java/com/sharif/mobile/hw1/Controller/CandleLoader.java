package com.sharif.mobile.hw1.Controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.sharif.mobile.hw1.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CandleLoader {
    private static final CandleLoader INSTANCE = new CandleLoader();
    private static final String API_KEY = "9BDA1C16-C842-45CC-A533-816FD802D4A8";
    private static final String CACHE_FORMAT = "%s-%s.json"; // coin name, range

    private final OkHttpClient restClient;
    private WeakReference<Context> context;
    private ThreadController threadController;
    private volatile boolean updateInProgress;

    private CandleLoader() {
        threadController = ThreadController.getInstance();
        restClient = new OkHttpClient();
        updateInProgress = false;
    }

    public void updateChart(final String symbol, RequestRange range, final CandleStickChart chart) {
        final File cacheFile = new File(String.format(CACHE_FORMAT, symbol, range.toString()));
        try (FileInputStream inputStream = context.get().openFileInput(cacheFile.getPath())) {
            String data = getContentOfFile(inputStream);
            if (!data.isEmpty()) {
                updateChartData(data, symbol, chart);
            }
        } catch (IOException e) {
            Log.i("INFO", "cache file doesn't exists.");
            e.printStackTrace();
        }
        if (!threadController.isPoolFull()) {
            threadController.submitTask(() -> updateCandleData(symbol, range, chart, cacheFile));
        }
        // TODO: show a message when thread pool is full.
    }

    private synchronized void updateCandleData(final String symbol, RequestRange range, final CandleStickChart chart, final File cacheFile) {
        updateInProgress = true;
        String dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .format(new Date(System.currentTimeMillis()));
        final String miniUrl;
        switch (range) {
            case weekly:
                miniUrl = "period_id=1DAY".
                        concat("&time_end=".concat(dateTime)
                                .concat("&limit=7"));
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY"
                        .concat("&time_end=".concat(dateTime)
                                .concat("&limit=30"));
                break;

            default:
                miniUrl = "";
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://rest.coinapi.io/v1/ohlcv/"
                .concat(symbol).concat("/USD/history?".concat(miniUrl)))
                .newBuilder();

        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder().url(url)
                .addHeader("X-CoinAPI-Key", API_KEY)
                .build();

        restClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String body = response.body().string();
                    FileOutputStream fileOutputStream = context.get()
                            .openFileOutput(cacheFile.getPath(), MODE_PRIVATE);
                    fileOutputStream.write(body.getBytes());
                    CandleLoader.this.updateChartData(body, symbol, chart);
                }
                updateInProgress = false;
            }
        });
    }

    private String getContentOfFile(InputStream cacheFile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int i=0;
        while((i=cacheFile.read())!=-1){
            stringBuilder.append((char) i);
        }
        return stringBuilder.toString();
    }

    private void updateChartData(String data, String symbol, CandleStickChart chart) {
        List<CandleEntry> candleEntries =
                extractCandlesFromResponse(data);
        if (candleEntries.size() == 0) {
            return;
        }
        chart.clear();
        CandleDataSet set = new CandleDataSet(candleEntries, symbol);

        set.setColor(Color.rgb(80, 80, 80));
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(1f);
        set.setDecreasingColor(context.get().getResources().getColor(R.color.green));
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(context.get().getResources().getColor(R.color.red));
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.LTGRAY);
        set.setDrawValues(true);
        chart.setData(new CandleData(set));
        chart.invalidate();
    }

    /**
     * Parse and return candle entries from json-data.
     */
    private List<CandleEntry> extractCandlesFromResponse(String data) {
        List<HistoryData> historyData;
        try {
            historyData = new ObjectMapper().readValue(data,
                    new TypeReference<List<HistoryData>>() {});
        } catch (JsonProcessingException e) {
            Log.e("PARSE", e.getMessage(), e);
            return null;
        }
        List<CandleEntry> candleEntries = new ArrayList<>();
        for (int i = 0; i < historyData.size(); i++) {
            HistoryData entry = historyData.get(i);
            CandleEntry candleEntry = new CandleEntry(i, entry.priceHigh, entry.priceLow,
                    entry.priceOpen, entry.priceClose);
            candleEntries.add(candleEntry);
        }
        return candleEntries;
    }

    public void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    public static CandleLoader getInstance() {
        return INSTANCE;
    }

    public boolean isUpdateInProgress() {
        return updateInProgress;
    }

    private static class HistoryData {
        @JsonProperty("time_period_start")
        private String timePeriodStart;

        @JsonProperty("time_period_end")
        private String timePeriodEnd;

        @JsonProperty("time_open")
        private String timeOpen;

        @JsonProperty("time_close")
        private String timeClose;

        @JsonProperty("price_open")
        private float priceOpen;

        @JsonProperty("price_high")
        private float priceHigh;

        @JsonProperty("price_low")
        private float priceLow;

        @JsonProperty("price_close")
        private float priceClose;

        @JsonProperty("volume_traded")
        private float volumeTraded;

        @JsonProperty("trades_count")
        private float tradesCount;

        // Getter Methods

        public String getTimePeriodStart() {
            return timePeriodStart;
        }

        public String getTimePeriodEnd() {
            return timePeriodEnd;
        }

        public String getTimeOpen() {
            return timeOpen;
        }

        public String getTimeClose() {
            return timeClose;
        }

        public float getPriceOpen() {
            return priceOpen;
        }

        public float getPriceHigh() {
            return priceHigh;
        }

        public float getPriceLow() {
            return priceLow;
        }

        public float getPriceClose() {
            return priceClose;
        }

        public float getVolumeTraded() {
            return volumeTraded;
        }

        public float getTradesCount() {
            return tradesCount;
        }

        // Setter Methods

        public void setTimePeriodStart(String timePeriodStart) {
            this.timePeriodStart = timePeriodStart;
        }

        public void setTimePeriodEnd(String timePeriodEnd) {
            this.timePeriodEnd = timePeriodEnd;
        }

        public void setTimeOpen(String timeOpen) {
            this.timeOpen = timeOpen;
        }

        public void setTimeClose(String timeClose) {
            this.timeClose = timeClose;
        }

        public void setPriceOpen(float priceOpen) {
            this.priceOpen = priceOpen;
        }

        public void setPriceHigh(float priceHigh) {
            this.priceHigh = priceHigh;
        }

        public void setPriceLow(float priceLow) {
            this.priceLow = priceLow;
        }

        public void setPriceClose(float priceClose) {
            this.priceClose = priceClose;
        }

        public void setVolumeTraded(float volumeTraded) {
            this.volumeTraded = volumeTraded;
        }

        public void setTradesCount(float tradesCount) {
            this.tradesCount = tradesCount;
        }
    }
}
