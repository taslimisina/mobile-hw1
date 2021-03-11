package com.sharif.mobile.hw1.Models;


import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;

public class Crypto {
    private final String id;
    private final String name;
    private final String symbol;
    private final double price;
    private final double hChange;
    private final double dChange;
    private final double wChange;

    public Crypto(String id, String name, String symbol, double price, double hChange, double dChange, double wChange) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.hChange = hChange;
        this.dChange = dChange;
        this.wChange = wChange;
    }

    public Crypto(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
        this.symbol = jsonObject.getString("symbol");
        JSONObject prices = jsonObject.getJSONObject("quote").getJSONObject("USD");
        this.price = prices.getDouble("price");
        this.hChange = prices.getDouble("percent_change_1h");
        this.dChange = prices.getDouble("percent_change_24h");
        this.wChange = prices.getDouble("percent_change_7d");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crypto crypto = (Crypto) o;
        return id.equals(crypto.id);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US,
                "Crypto: id=%s, symbol=%s, name=%s, price=%.2f$, hChange=%.2f%%, dChange=%.2f%%, wChange=%.2f%%,",
                this.id, this.symbol, this.name, this.price, this.hChange, this.dChange, this.wChange);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getHChange() {
        return hChange;
    }

    public double getDChange() {
        return dChange;
    }

    public double getWChange() {
        return wChange;
    }
}
