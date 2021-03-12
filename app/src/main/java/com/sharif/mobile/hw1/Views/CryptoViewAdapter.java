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

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.sharif.mobile.hw1.Controller.GlideApp;
import com.sharif.mobile.hw1.Models.Crypto;
import com.sharif.mobile.hw1.R;

public class CryptoViewAdapter extends RecyclerView.Adapter<CryptoViewHolder> {

    private ArrayList<Crypto> coins;
    private final DecimalFormat fmt = new DecimalFormat("+#.##;-#");

    public CryptoViewAdapter() {
        this.coins = new ArrayList<>();
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

        holder.hChange.setText(fmt.format(crypto.getHChange()).concat("%"));
        holder.hChange.setTextColor(crypto.getHChange() > 0 ? Color.GREEN : Color.RED);
        holder.dChange.setText(fmt.format(crypto.getDChange()).concat("%"));
        holder.dChange.setTextColor(crypto.getDChange() > 0 ? Color.GREEN : Color.RED);
        holder.wChange.setText(fmt.format(crypto.getWChange()).concat("%"));
        holder.wChange.setTextColor(crypto.getWChange() > 0 ? Color.GREEN : Color.RED);

        // TODO: test cache; by default it should also cache images on disc
        GlideApp.with(holder.image.getContext())
                .load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + crypto.getId() + ".png")
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

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
