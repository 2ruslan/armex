package kupchinskii.ruslan.armpullup;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.NumberFormat;

public class ChartHelper {


    public static GraphicalView buildMax5(Context context) {
        Cursor crr =  DBHelper.GetReportMax5();
        return getChartBase(context, crr, Color.GREEN, R.string.title_chart_max);
    }

    public static GraphicalView buildWeekTotal(Context context) {
        Cursor crr = DBHelper.GetReportWeekTotal();
        return getChartBase(context, crr, Color.CYAN, R.string.title_chart_week_total);
    }

    public static GraphicalView buildMaxCntDay(Context context) {
        Cursor crr = DBHelper.GetReportMaxCntDay();
        return getChart5(context, crr, Color.YELLOW, R.string.title_chart_max_cnt);
    }

    public static GraphicalView buildPyramidDay(Context context) {
        Cursor crr = DBHelper.GetReportPyramidDay();
        return getChartBase(context, crr, Color.YELLOW, R.string.title_chart_pyramid);
    }

    public static GraphicalView buildGripDay(Context context) {
        Cursor crr = DBHelper.GetReportGripDay();
        return getChartBase(context, crr, Color.CYAN, R.string.title_chart_grip);
    }

    public static GraphicalView buildMaxSetDay(Context context) {
        Cursor crr = DBHelper.GetReportMaxSetDay();
        return getChartBase(context, crr, Color.YELLOW, R.string.title_chart_max_sets);
    }

    private static XYSeriesRenderer getDefRender(int color) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);

        XYSeriesRenderer sRenderer = new XYSeriesRenderer();
        sRenderer.setDisplayChartValues(true);
        sRenderer.setChartValuesFormat(format);
        sRenderer.setLineWidth((float) .2);
        sRenderer.setChartValuesTextSize(20);
        sRenderer.setChartValuesTextAlign(Paint.Align.RIGHT);


        XYSeriesRenderer.FillOutsideLine fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        fill.setColor(Color.LTGRAY);
        sRenderer.addFillOutsideLine(fill);
        sRenderer.setColor(color);
        return sRenderer;
    }

    private static XYMultipleSeriesRenderer getMultiRender(String chartName) {
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setXLabels(0);
        multiRenderer.setYLabelsAngle(-90);

        multiRenderer.setBarSpacing(2);

        multiRenderer.setShowGrid(false);
        multiRenderer.setChartTitle(chartName);
        multiRenderer.setChartTitleTextSize(40);

        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setLabelsTextSize(40);

        multiRenderer.setShowLegend(false);
        multiRenderer.setClickEnabled(false);
        multiRenderer.setInScroll(true);
        multiRenderer.setPanEnabled(true, false);

        return multiRenderer;
    }


    private static GraphicalView getChart5(Context context, Cursor crr
            , int color, int chartname) {


        XYSeries viewsSeries1 = new XYSeries("Views1");
        XYSeries viewsSeries2 = new XYSeries("Views2");
        XYSeries viewsSeries3 = new XYSeries("Views3");
        XYSeries viewsSeries4 = new XYSeries("Views4");
        XYSeries viewsSeries5 = new XYSeries("Views5");

        XYSeriesRenderer sRenderer1 = getDefRender(Color.GREEN);
        XYSeriesRenderer sRenderer2 = getDefRender(Color.RED);
        XYSeriesRenderer sRenderer3 = getDefRender(Color.YELLOW);
        XYSeriesRenderer sRenderer4 = getDefRender(Color.MAGENTA);
        XYSeriesRenderer sRenderer5 = getDefRender(Color.CYAN);

        XYMultipleSeriesRenderer multiRenderer = getMultiRender(context.getString(chartname));
        multiRenderer.setBarWidth(28);

        long xmax = 0;
        float ymin = 0, ymax = 0;
        crr.moveToFirst();
        for (int i = 0; i < crr.getCount(); i++) {
            float yval = crr.getFloat(2);
            int xval = i + 1;

            if (yval < ymin) ymin = yval;
            if (yval > ymax) ymax = yval;
            xmax++;

            viewsSeries1.add(i, yval);

            yval = crr.getFloat(3);
            viewsSeries2.add(i, yval);

            yval = crr.getFloat(4);
            viewsSeries3.add(i, yval);

            yval = crr.getFloat(5);
            viewsSeries4.add(i, yval);

            yval = crr.getFloat(6);
            viewsSeries5.add(i, yval);

            multiRenderer.addXTextLabel(i, String.valueOf(xval));
            crr.moveToNext();
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(viewsSeries1);
        dataset.addSeries(viewsSeries2);
        dataset.addSeries(viewsSeries3);
        dataset.addSeries(viewsSeries4);
        dataset.addSeries(viewsSeries5);

        multiRenderer.setYAxisMin(ymin);
        multiRenderer.setYAxisMax(ymax + (ymax * 0.1));

        multiRenderer.setXAxisMax(xmax);
        multiRenderer.setXAxisMin(xmax > 3 ? xmax - 3 : 0);

        multiRenderer.addSeriesRenderer(sRenderer1);
        multiRenderer.addSeriesRenderer(sRenderer2);
        multiRenderer.addSeriesRenderer(sRenderer3);
        multiRenderer.addSeriesRenderer(sRenderer4);
        multiRenderer.addSeriesRenderer(sRenderer5);

        return ChartFactory.getBarChartView(context, dataset, multiRenderer, BarChart.Type.DEFAULT);
    }

    private static GraphicalView getChartBase(Context context, Cursor crr
            , int color, int chartname) {
        crr.moveToFirst();

        XYSeries viewsSeries = new XYSeries("Views");
        XYSeriesRenderer sRenderer = getDefRender(color);
        XYMultipleSeriesRenderer multiRenderer = getMultiRender(context.getString(chartname));


        long xmax = 0;
        float ymin = 0, ymax = 0;

        for (int i = 0; i < crr.getCount(); i++) {
            float yval = crr.getFloat(crr.getColumnIndex(DBHelper.COLUMN_VAL));
            int xval = i + 1;

            if (yval < ymin) ymin = yval;
            if (yval > ymax) ymax = yval;
            xmax++;

            viewsSeries.add(i, yval);
            multiRenderer.addXTextLabel(i, String.valueOf(xval));
            crr.moveToNext();
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(viewsSeries);

        multiRenderer.setYAxisMin(ymin);
        multiRenderer.setYAxisMax(ymax + (ymax * 0.1));

        multiRenderer.setXAxisMax(xmax);
        multiRenderer.setXAxisMin(xmax > 7 ? xmax - 6 : 0);

        multiRenderer.setBarWidth(46);

        multiRenderer.addSeriesRenderer(sRenderer);

        return ChartFactory.getBarChartView(context, dataset, multiRenderer, BarChart.Type.DEFAULT);
    }

}
