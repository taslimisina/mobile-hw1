package com.sharif.mobile.hw1.Views;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.sharif.mobile.hw1.Models.Crypto;
import com.sharif.mobile.hw1.R;


public class CryptoViewAdapter extends RecyclerView.Adapter<CryptoViewHolder> {

    private ArrayList<Crypto> coins;
    private final DecimalFormat fmt = new DecimalFormat("+#.##;-#");

    public CryptoViewAdapter(ArrayList<Crypto> coins) {
        this.coins = coins;
    }

    // TODO: remove! only for initial testing
    public CryptoViewAdapter() {
        DecimalFormat fmt = new DecimalFormat("+#,##0.00;-#");
        System.out.println(fmt.format(98787654.897));
        System.out.println(fmt.format(-98787654.897));
        coins = new ArrayList<>();
        coins.add(new Crypto("0", "Bitcoin", "BTC", 123.456,3.5, -2, -10.445));
        coins.add(new Crypto("1", "Coin1", "CN1", 112,1.1, 1, -1));
        coins.add(new Crypto("2", "Coin2", "CN2", 223,-2.2, -2, 2));
        coins.add(new Crypto("3", "Coin3", "CN3", 334,3.3, 0, -3));
        coins.add(new Crypto("4", "Coin4", "CN4", 334,-4.4, -2, 4));
        coins.add(new Crypto("5", "Coin5", "CN5", 555,5.5, 0, -5));
        coins.add(new Crypto("6", "Coin6", "CN6", 666.6,-6.6, 1, 6));
        coins.add(new Crypto("7", "Coin7", "CN7", 777.8,7.7, -2, -7));
        coins.add(new Crypto("8", "Coin8", "CN8", 88,-8.8, 0, 8));
        coins.add(new Crypto("9", "Coin9", "CN9", 99.999,9.9, -2, -9));
    }

    @NonNull
    @Override
    public CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_layout, parent,false);
        // TODO: setOnClickListener -> opens candle-chart-view
        return new CryptoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        Crypto crypto = coins.get(position);
        holder.symbol.setText(crypto.getSymbol());
        holder.name.setText(crypto.getName());
        holder.price.setText(String.format(Locale.getDefault(),"%.02f$", crypto.getPrice()));

        holder.hChange.setText(fmt.format(crypto.getHChange()).concat("$"));
        holder.hChange.setTextColor(crypto.getHChange() > 0 ? Color.GREEN : Color.RED);
        holder.hChange.setText(fmt.format(crypto.getHChange()).concat("%"));
        holder.dChange.setTextColor(crypto.getDChange() > 0 ? Color.GREEN : Color.RED);
        holder.hChange.setText(fmt.format(crypto.getHChange()).concat("%"));
        holder.wChange.setTextColor(crypto.getWChange() > 0 ? Color.GREEN : Color.RED);

        // TODO: load image
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    public void addAll(ArrayList<Crypto> list) {
        for (Crypto crypto : list)
            if (!coins.contains(crypto))
                coins.add(crypto);
        notifyDataSetChanged();
    }

    public void clear() {
        coins.clear();
        notifyDataSetChanged();
    }

}
