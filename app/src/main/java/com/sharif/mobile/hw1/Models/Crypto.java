package com.sharif.mobile.hw1.Models;


public class Crypto {
    private String id;
    private String name;
    private String symbol;
    private double price;
    private double hChange, dChange, wChange;
    // TODO: save image?

    public Crypto(String id, String name, String symbol, double price, double hChange, double dChange, double wChange) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.hChange = hChange;
        this.dChange = dChange;
        this.wChange = wChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crypto crypto = (Crypto) o;
        return id.equals(crypto.id);
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
