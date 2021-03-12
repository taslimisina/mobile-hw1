package com.sharif.mobile.hw1.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.sharif.mobile.hw1.Controller.CandleChartToastHandler;
import com.sharif.mobile.hw1.Controller.CandleLoader;
import com.sharif.mobile.hw1.Controller.RequestRange;
import com.sharif.mobile.hw1.MainActivity;
import com.sharif.mobile.hw1.R;

public class CandleChartActivity extends AppCompatActivity {

    private RequestRange range;
    private String coinName;
    private CandleStickChart chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_chart_layout);

        Context applicationContext = getApplicationContext();
        CandleLoader.getInstance().setContext(applicationContext);
        CandleLoader.getInstance().setHandler(new CandleChartToastHandler(this));
        coinName = getIntent().getStringExtra("coinName");
        range = RequestRange.weekly;
        setTitle(coinName + " Candle chart");

        chart = findViewById(R.id.chart);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(true);
        chart.setHighlightPerDragEnabled(true);
        chart.setDrawBorders(true);
        chart.setBorderColor(getResources().getColor(R.color.colorAccent));

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        chart.getLegend().setEnabled(true);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        chart.requestDisallowInterceptTouchEvent(true);
        xAxis.setDrawLabels(true);
        rightAxis.setTextColor(Color.BLACK);
        yAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setEnabled(true);
        chart.setClipValuesToContent(true);
        xAxis.setAvoidFirstLastClipping(true);

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
                range = RequestRange.weekly;
                CandleLoader.getInstance().updateChart(coinName, range, chart);
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = RequestRange.oneMonth;
                CandleLoader.getInstance().updateChart(coinName, range, chart);
            }
        });
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
