package kupchinskii.ruslan.armpullup;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;


public class timer extends Activity {

    public static final String PRM_SECOND = "prmSecond";
    public static final String PRM_NEXT_INFO_GRIP = "prmNExtInfoGrip";
    public static final String PRM_NEXT_INFO_CNT = "prmNExtInfoCNT";
    public static final String PRM_NEXT_INFO_PREV = "prmNExtInfoPREV";

    static int soundIdEnd;
    TextView tvTmr;
    TextView tCnt;
    ImageView ctrlImage;
    CountDownTimer tmr;
    SoundPool sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Bundle b = getIntent().getExtras();

        tvTmr = (TextView) findViewById(R.id.tvTimerVal);
        tCnt= (TextView) findViewById(R.id.tCnt);
        ctrlImage = (ImageView) findViewById(R.id.imCnt);

        int seconds = b.getInt(PRM_SECOND, 0);
        int cnt = b.getInt(PRM_NEXT_INFO_CNT, 0);
        int prevVal = b.getInt(PRM_NEXT_INFO_PREV, 0);
        ArmPrg.en_grip grip = (ArmPrg.en_grip)b.get(PRM_NEXT_INFO_GRIP);


        /**/
        String hint;
        if (cnt == ArmPrg.NEXT_MAX) {
            hint = "max";
            if (prevVal > 0)
                hint += "\n(" + String.valueOf(prevVal) + ")";

        } else {
            hint = String.valueOf(cnt);
        }
        tCnt.setHint(hint);


        if (grip == ArmPrg.en_grip.reverse_narrow)
            ctrlImage.setImageResource(R.drawable.reverse_narrow);
        else if (grip == ArmPrg.en_grip.direct_wide)
            ctrlImage.setImageResource(R.drawable.direct_wide);
        else if (grip == ArmPrg.en_grip.direct_norm)
            ctrlImage.setImageResource(R.drawable.direct_norm);
        /**/


        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundIdEnd = sp.load(this, R.raw.tmr_end, 1);




        showTimer(seconds);
    }

    private void showTimer(int seconds) {
        if(tmr != null) { tmr.cancel(); }
        tmr = new CountDownTimer(seconds * 1000,  1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTmr.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                tmr.cancel();
                playEnd();
                close();
            }
        }.start();
    }

    private void playEnd()
    {
        sp.play(soundIdEnd, 1, 1, 0, 0, 1);
    }

    public void close() {
        setResult(RESULT_OK);
        finish();
    }

}
