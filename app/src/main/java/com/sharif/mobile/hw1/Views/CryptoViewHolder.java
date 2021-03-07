package com.sharif.mobile.hw1.Views;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharif.mobile.hw1.R;


public class CryptoViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView symbol, name, price, hChange, dChange, wChange;

    public CryptoViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.crypto_logo);
        symbol = itemView.findViewById(R.id.crypto_symbol);
        name = itemView.findViewById(R.id.crypto_name);
        price = itemView.findViewById(R.id.crypto_price);
        hChange = itemView.findViewById(R.id.hourly_change);
        dChange = itemView.findViewById(R.id.daily_change);
        wChange = itemView.findViewById(R.id.weekly_change);
    }
}
