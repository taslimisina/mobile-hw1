package com.sharif.mobile.hw1.Activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.sharif.mobile.hw1.Models.CryptoCandleChartData;
import com.sharif.mobile.hw1.R;

import java.util.ArrayList;

public class CandleChartActivity extends AppCompatActivity {

    private CryptoCandleChartData data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_chart_layout);

        setTitle("test" + " Candle chart");
        CandleStickChart chart = findViewById(R.id.chart);
        chart.setBackgroundColor(Color.BLACK);
        chart.setDrawGridBackground(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        chart.getLegend().setEnabled(false);


        // TODO: load data
        // example data
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        candleEntries.add(new CandleEntry(10.5f, 10, 1, 5, 2));
        candleEntries.add(new CandleEntry(10.7f, 7, 5, 5, 2));
        candleEntries.add(new CandleEntry(10.6f, 9, 5, 5, 2));
        ArrayList<CandleEntry> yValsCandleStick= new ArrayList<CandleEntry>();
        yValsCandleStick.add(new CandleEntry(0, 225.0f, 219.84f, 224.94f, 221.07f));
        yValsCandleStick.add(new CandleEntry(1, 228.35f, 222.57f, 223.52f, 226.41f));
        yValsCandleStick.add(new CandleEntry(2, 226.84f,  222.52f, 225.75f, 223.84f));
        yValsCandleStick.add(new CandleEntry(3, 222.95f, 217.27f, 222.15f, 217.88f));
        CandleDataSet set1 = new CandleDataSet(yValsCandleStick, "DataSet 1");
        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(getResources().getColor(R.color.green));
        set1.setShadowWidth(0.8f);
        set1.setDecreasingColor(getResources().getColor(R.color.red));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(getResources().getColor(R.color.colorAccent));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.LTGRAY);
        set1.setDrawValues(false);
        chart.setData(new CandleData(set1));
    }
}
