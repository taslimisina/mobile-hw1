package com.sharif.mobile.hw1.Controller;

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

import java.io.IOException;
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
    private static final String API_KEY = "63B57F90-9427-4080-8E86-A9CCC08CB8FB";

    private final OkHttpClient restClient;

    private CandleLoader() {
        restClient = new OkHttpClient();
    }

    public static CandleLoader getInstance() {
        return INSTANCE;
    }

    public void updateChart(final String symbol, Range range, final CandleStickChart chart) {
        String miniUrl;
        final String description;
        String dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .format(new Date(System.currentTimeMillis()));
        switch (range) {
            case weekly:
                miniUrl = "period_id=1DAY".
                        concat("&time_end=".concat(dateTime)
                                .concat("&limit=7"));
                description = "Daily candles from now";
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY"
                        .concat("&time_end=".concat(dateTime)
                                .concat("&limit=30"));
                description = "Daily candles from now";
                break;

            default:
                miniUrl = "";
                description = "";

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
                    List<CandleEntry> candleEntries =
                            extractCandlesFromResponse(response.body().string());
                    CandleDataSet set = new CandleDataSet(candleEntries, symbol);
                    set.setColor(Color.rgb(80, 80, 80));
                    set.setShadowColor(R.color.green);
                    set.setShadowWidth(0.8f);
                    set.setDecreasingColor(R.color.red);
                    set.setDecreasingPaintStyle(Paint.Style.FILL);
                    set.setIncreasingColor(R.color.colorAccent);
                    set.setIncreasingPaintStyle(Paint.Style.FILL);
                    set.setNeutralColor(Color.LTGRAY);
                    set.setDrawValues(true);
                    chart.setData(new CandleData(set));
                }
            }
        });
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

    public static enum Range {
        weekly,
        oneMonth,
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