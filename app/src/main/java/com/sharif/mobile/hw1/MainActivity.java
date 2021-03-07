package com.sharif.mobile.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sharif.mobile.hw1.Views.CryptoViewAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
//        CryptoViewAdapter adapter = new CryptoViewAdapter();
//        rv.setAdapter(adapter);
    }
}
