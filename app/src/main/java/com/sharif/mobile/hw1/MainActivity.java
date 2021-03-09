package com.sharif.mobile.hw1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.sharif.mobile.hw1.Controller.CryptoViewHandler;
import com.sharif.mobile.hw1.Controller.Loader;
import com.sharif.mobile.hw1.Views.CryptoViewAdapter;

public class MainActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    public CryptoViewAdapter cryptoViewAdapter;
    public SwipeRefreshLayout swipeContainer;

    private CryptoViewHandler handler;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("MAIN", "Starting MainActivity");
        Loader loader = Loader.getInstance();
        loader.loadCoins(1);
        Log.v("MAIN", "After Load");

        cryptoViewAdapter = new CryptoViewAdapter();  // todo pass list to constructor
        recyclerView = (RecyclerView)findViewById(R.id.crypto_list);
        recyclerView.setAdapter(cryptoViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        handler = new CryptoViewHandler(this);
//
//        // setup recyclerView
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        cryptoViewAdapter = new CryptoViewAdapter();
//        recyclerView.setAdapter(cryptoViewAdapter);
//        recyclerView.setHasFixedSize(false);
//
//        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                handler.sendEmptyMessage(CryptoViewHandler.LOAD_COINS);
//            }
//        });
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                int visibleThreshold = 1;
//                if (totalItemCount <= lastVisibleItem + visibleThreshold) {
//                    handler.sendEmptyMessage(CryptoViewHandler.LOAD_COINS);
//                }
//            }
//        });
//
//        handler.sendEmptyMessage(CryptoViewHandler.LOAD_COINS);

    }
}
