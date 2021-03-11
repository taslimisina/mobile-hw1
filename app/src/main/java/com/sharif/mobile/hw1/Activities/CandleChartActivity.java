package com.sharif.mobile.hw1.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.sharif.mobile.hw1.Controller.CandleLoader;
import com.sharif.mobile.hw1.MainActivity;
import com.sharif.mobile.hw1.R;

import java.util.ArrayList;

public class CandleChartActivity extends AppCompatActivity {

    private CandleLoader.Range range;
    private String coinName;
    private CandleStickChart chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_chart_layout);

        Context applicationContext = getApplicationContext();
        CandleLoader.getInstance().setContext(applicationContext);
        // TODO: handle this during merge
        coinName = "BTC";
//        coinName = getIntent().getStringExtra("coinName");
        range = CandleLoader.Range.weekly;
        setTitle(coinName + " Candle chart");

        chart = findViewById(R.id.chart);
        chart.setBackgroundColor(Color.BLACK);
        chart.setDrawGridBackground(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        chart.getLegend().setEnabled(false);

        CandleLoader.getInstance().updateChart(coinName, range, chart);

        final Button refreshButton = findViewById(R.id.refreshButton);
        Button returnButton = findViewById(R.id.returnButton);
        Button weeklyButton = findViewById(R.id.weekly);
        Button monthlyButton = findViewById(R.id.monthly);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CandleLoader.getInstance().updateChart(coinName, range, chart);
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = CandleLoader.Range.weekly;
                CandleLoader.getInstance().updateChart(coinName, range, chart);
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = CandleLoader.Range.oneMonth;
                CandleLoader.getInstance().updateChart(coinName, range, chart);
            }
        });

        // TODO: set onclick for return function
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
