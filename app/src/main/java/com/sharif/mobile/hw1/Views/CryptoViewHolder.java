package com.sharif.mobile.hw1.Views;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharif.mobile.hw1.R;


public class CryptoViewHolder extends RecyclerView.ViewHolder {

    public final ImageView image;
    public final TextView symbol;
    public final TextView name;
    public final TextView price;
    public final TextView hChange;
    public final TextView dChange;
    public final TextView wChange;

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
