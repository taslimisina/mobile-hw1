package com.sharif.mobile.hw1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sharif.mobile.hw1.Activities.CandleChartActivity;
import com.sharif.mobile.hw1.Controller.CryptoViewHandler;
import com.sharif.mobile.hw1.Controller.Loader;
import com.sharif.mobile.hw1.Controller.ThreadController;
import com.sharif.mobile.hw1.Models.Crypto;
import com.sharif.mobile.hw1.Views.CryptoViewAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main";
    public ProgressBar progressBar;
    public CryptoViewAdapter cryptoViewAdapter;
    public SwipeRefreshLayout swipeContainer;

    private RecyclerView recyclerView;
    private Loader loader;

    public void showCandleChart(View view) {
        Intent intent = new Intent(this, CandleChartActivity.class);
        intent.putExtra("coinName", ((TextView)view.findViewById(R.id.crypto_symbol)).getText());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cryptoViewAdapter = new CryptoViewAdapter();
        recyclerView = (RecyclerView)findViewById(R.id.crypto_list);
        recyclerView.setAdapter(cryptoViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        loader = Loader.getInstance();
        loader.setContext(this);
        loader.setHandler(new CryptoViewHandler(this));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (loader.isBusy())
                        return;
                    loader.setBusy();
                    Log.v(MainActivity.TAG, "Load More Coins");
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    progressBar.setProgress(progressBar.getMax() * 3/10);
                    ThreadController.getInstance().submitTask(() -> loader.loadMoreCoins(cryptoViewAdapter.getItemCount() + 1));
                }
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            if (loader.isBusy()) {
                Log.d(MainActivity.TAG, "loader is busy");
                swipeContainer.setRefreshing(false);
                return;
            }
            loader.setBusy();
            Log.v(MainActivity.TAG, "Refresh");
            progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBar.setProgress(progressBar.getMax() * 3/10);
            ThreadController.getInstance().submitTask(() -> loader.refreshCoins());
        });

        if (loader.isBusy())
            Log.e(MainActivity.TAG, "BUSY!!!!!!");
        loader.setBusy();
        progressBar.setVisibility(ProgressBar.VISIBLE);
        progressBar.setProgress(progressBar.getMax() * 3/10);
        ThreadController.getInstance().submitTask(() -> loader.loadMoreCoins(1));
    }
}
