package kupchinskii.ruslan.armpullup;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import org.achartengine.GraphicalView;

public class statistics extends Activity {

    private GraphicalView mChartViewMax5;
    private GraphicalView mChartViewWeekTotal;
    private GraphicalView mChartViewMaxCntDay;
    private GraphicalView mChartViewPyramidDay;
    private GraphicalView mChartViewGripDay;
    private GraphicalView mChartViewMaxSetsdDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return  true;
    }

    private void init() {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int dh = Math.round(metrics.heightPixels * 0.625f);

            // max 5
            LinearLayout layoutMax5 = (LinearLayout) findViewById(R.id.chartMax5);
            ViewGroup.LayoutParams paramsMax5 = layoutMax5.getLayoutParams();
            paramsMax5.height = dh;
            mChartViewMax5 = ChartHelper.buildMax5(getBaseContext());
            layoutMax5.addView(mChartViewMax5, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // week total
            LinearLayout layoutWeekTotal = (LinearLayout) findViewById(R.id.chartWeekTotal);
            ViewGroup.LayoutParams paramsWeekTotal = layoutWeekTotal.getLayoutParams();
            paramsWeekTotal.height = dh;
            mChartViewWeekTotal = ChartHelper.buildWeekTotal(getBaseContext());
            layoutWeekTotal.addView(mChartViewWeekTotal, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // max cnt
            LinearLayout layoutMaxCntDay = (LinearLayout) findViewById(R.id.chartMaxCnt);
            ViewGroup.LayoutParams paramsMaxCntsDay = layoutMaxCntDay.getLayoutParams();
            paramsMaxCntsDay.height = dh;
            mChartViewMaxCntDay = ChartHelper.buildMaxCntDay(getBaseContext());
            layoutMaxCntDay.addView(mChartViewMaxCntDay, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // pyramid day
            LinearLayout layoutPyramidDay = (LinearLayout) findViewById(R.id.chartPyramidDay);
            ViewGroup.LayoutParams paramsPyramidDay = layoutPyramidDay.getLayoutParams();
            paramsPyramidDay.height = dh;
            mChartViewPyramidDay = ChartHelper.buildPyramidDay(getBaseContext());
            layoutPyramidDay.addView(mChartViewPyramidDay, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // grip
            LinearLayout layoutGripDay = (LinearLayout) findViewById(R.id.chartGrip);
            ViewGroup.LayoutParams paramsGripDay = layoutGripDay.getLayoutParams();
            paramsGripDay.height = dh;
            mChartViewGripDay = ChartHelper.buildGripDay(getBaseContext());
            layoutGripDay.addView(mChartViewGripDay, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // max cnt
            LinearLayout layoutMaxSetsDay = (LinearLayout) findViewById(R.id.chartMaxSets);
            ViewGroup.LayoutParams paramsMaxSetsDay = layoutMaxSetsDay.getLayoutParams();
            paramsMaxSetsDay.height = dh;
            mChartViewMaxSetsdDay = ChartHelper.buildMaxSetDay(getBaseContext());
            layoutMaxSetsDay.addView(mChartViewMaxSetsdDay, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void OnClickClose(View view) {
        finish();
    }

}
