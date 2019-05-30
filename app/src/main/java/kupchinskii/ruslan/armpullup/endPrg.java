package kupchinskii.ruslan.armpullup;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class endPrg extends Activity {

    public static final String MODE = "mode";
    public static final int MODE_O = 1; // оценка
    public static final int MODE_P = 2; // купить
    private static final int WAIT_SEC = 5;
    CountDownTimer tmr;
    int activityMode;
    LinearLayout ctrl_r;
    LinearLayout ctrl_o;
    LinearLayout ctrl_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_prg);

        Bundle ex = getIntent().getExtras();
        activityMode = ex.getInt(MODE);

        ctrl_r = (LinearLayout) findViewById(R.id.prg_r);
        ctrl_o = (LinearLayout) findViewById(R.id.prg_o);
        ctrl_p = (LinearLayout) findViewById(R.id.prg_p);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (activityMode == MODE_O) {
            showEstim();
        } else if (activityMode == MODE_P) {
            showPai();
        }
    }

    private void waitAdMob(int seconds) {
        if (tmr != null) {
            tmr.cancel();
        }
        tmr = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                tmr.cancel();
                Button bt_close = (Button) findViewById(R.id.bt_close);
                if (bt_close.getVisibility() == View.GONE)
                    finish();
            }
        }.start();
    }

    private void showEstim() {
        ctrl_p.setVisibility(View.GONE);
        ctrl_r.setVisibility(View.GONE);
    }

    private void showPai() {
        ctrl_r.setVisibility(View.GONE);
        ctrl_o.setVisibility(View.GONE);
    }

    public void OnClickClose(View view) {
        finish();
    }

    public void OnClickPay(View view) {
        RHelper.BuyFull(this);
        finish();
    }

    public void OnClickNoShowBuy(View view) {
        PreferencesHelper.SetNoShowPropP(true);
        finish();
    }

    public void OnClickEstim(View view) {
        RHelper.Estimate(this);
        finish();
    }

    public void OnClickNoShowEstim(View view) {
        PreferencesHelper.SetNoShowPropO(true);
        finish();
    }


    public void onBackPressed() {
        finish();
    }

}
