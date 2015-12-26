package huge.aldajo.com.currencyconverter.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.util.ArrayList;

import huge.aldajo.com.currencyconverter.R;
import huge.aldajo.com.currencyconverter.apiConection.Rates;

/**
 * Fragment representing the convert mount information.
 */
public class CardBackFragment extends Fragment {

    /**
     * global view reference
     */
    private View myFragmentView;

    /**
     * Object that manage the rates values.
     */
    private Rates rates;

    /**
     * View for each currency.
     */
    private TextView tv_gbp, tv_eur, tv_jpy, tv_brl;

    /**
     * Input Money
     */
    private double USD = 0;

    /**
     * Expected currencies: Include here, and inside in setData().
     */
    private double GBP = 0;
    private double EUR = 0;
    private double JPY = 0;
    private double BRL = 0;

    /**
     * BarChart used to graph currency converter
     */
    protected BarChart mChart;

    /**
     * Constructor
     */
    public CardBackFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.fragment_card_back, container, false);

        /**
         * Get TextView for show the values for each Currency.
         */
        tv_gbp = (TextView) myFragmentView.findViewById(R.id.tv_gbp);
        tv_eur = (TextView) myFragmentView.findViewById(R.id.tv_eur);
        tv_jpy = (TextView) myFragmentView.findViewById(R.id.tv_jpy);
        tv_brl = (TextView) myFragmentView.findViewById(R.id.tv_brl);

        /**
         * Do the conversion if rates is different to null
         */
        if(rates != null){
            GBP = rates.getGBP()*USD;
            EUR = rates.getEUR()*USD;
            JPY = rates.getJPY()*USD;
            BRL = rates.getBRL()*USD;
        }

        /**
         * get two decimals.
         */
        tv_gbp.setText(String.format("%.4f",GBP));
        tv_eur.setText(String.format("%.4f",EUR));
        tv_jpy.setText(String.format("%.4f",JPY));
        tv_brl.setText(String.format("%.4f",BRL));

        /**
         * Customize the BarChart
         */
        mChart = (BarChart) myFragmentView.findViewById(R.id.chart1);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");

        /**
         * Customize the BarChart X Axis
         */
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        /**
         * Customize the BarChart Y left Axis
         */
        YAxisValueFormatter custom = new MyYAxisValueFormatter();
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(4, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        /**
         * Customize the BarChart Y right Axis
         */
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(4, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);

        /**
         * Customize the Legend
         */
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        /**
         * Update the Data for each Bar
         */
        setData();

        /**
         * Disable some CharBar characteristics.
         */
        mChart.setScaleEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setHighlightPerDragEnabled(false);

        /**
         * Animate after update data.
         */
        mChart.animateY(2000);

        return myFragmentView;
    }

    /**
     * Update the CharBar
     */
    private void setData() {

        ArrayList<String> xVals = new ArrayList<>();

        /**
         * Include here the values for show in CharBar
         */
        xVals.add("GBP");
        xVals.add("EUR");
        xVals.add("JPY");
        xVals.add("BRL");

        /**
         * set the values for each Bar
         */
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        yVals1.add(new BarEntry((float)GBP,0));
        yVals1.add(new BarEntry((float) EUR, 1));
        yVals1.add(new BarEntry((float) JPY, 2));
        yVals1.add(new BarEntry((float) BRL, 3));

        BarDataSet set1 = new BarDataSet(yVals1, "Conversion");
        set1.setBarSpacePercent(35f);
        set1.setColor(R.color.theme_default_primary_dark);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        mChart.setData(data);
    }

    /**
     * @param r: Rates for this fragment
     */
    public void putRates(Rates r){
        rates = r;
    }

    /**
     * @param usd : Value for do the conversion
     */
    public void setUSD(double usd) {
        this.USD = usd;
    }
}