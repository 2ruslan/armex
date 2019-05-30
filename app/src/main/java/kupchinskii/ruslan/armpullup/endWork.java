package kupchinskii.ruslan.armpullup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class endWork extends Activity {

    public static final String MODE = "mode";
    public static final int MODE_DAY_FINISH = 0;
    public static final int MODE_DAY_DAY_OFF = 10;

    int activityMode;

    LinearLayout ctrl_dayFinish;
    LinearLayout ctrl_dayOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_work);

        Bundle ex = getIntent().getExtras();
        activityMode = ex.getInt(MODE);

        ctrl_dayFinish = (LinearLayout) findViewById(R.id.day_finish);
        ctrl_dayOff = (LinearLayout) findViewById(R.id.day_dayoff);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (activityMode == MODE_DAY_FINISH) {
            ctrl_dayOff.setVisibility(View.GONE);
        } else if (activityMode == MODE_DAY_DAY_OFF) {
            ctrl_dayFinish.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void OnClickClose(View view) {
        Intent intent = new Intent(endWork.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void OnClickStatistics(View view) {
        Intent intent = new Intent(endWork.this, statistics.class);
        startActivity(intent);
    }

    public void OnClickSend(View view) {

        DBHelper.SendResInfo res = DBHelper.getSendReskInfo();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.msgback, options);

        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        Paint paintPict = new Paint();

        Typeface plain = Typeface.createFromAsset(getAssets(), "fonts/msgfont.ttf");

        Paint paintFont = new Paint();
        paintFont.setColor(Color.WHITE);
        paintFont.setTextSize(56);
        paintFont.setTextAlign(Paint.Align.CENTER);
        paintFont.setTypeface(plain);
        Paint paintFontRes = new Paint();
        paintFontRes.setColor(Color.WHITE);
        paintFontRes.setTextSize(76);
        paintFontRes.setTypeface(plain);


        canvas.drawText(getResources().getString(R.string.post_msg1), canvas.getWidth() / 2, 56, paintFont);
        canvas.drawText(getResources().getString(R.string.post_msg2), canvas.getWidth() / 2, 112, paintFont);

        if (res.tp == ArmPrg.en_type.grip) {
            Bitmap bitmapK = BitmapFactory.decodeResource(getResources(), R.drawable.direct_norm_50);
            canvas.drawBitmap(bitmapK, 140, 140, paintPict);
            String exRes = getVal3(res.res_1, res.res_2, res.res_3);
            if (exRes.length() > 0) canvas.drawText(exRes, 320, 220, paintFontRes);

            bitmapK = BitmapFactory.decodeResource(getResources(), R.drawable.reverse_narrow_50);
            canvas.drawBitmap(bitmapK, 140, 260, paintPict);
            exRes = getVal3(res.res_4, res.res_5, res.res_6);
            if (exRes.length() > 0) canvas.drawText(exRes, 320, 340, paintFontRes);

            bitmapK = BitmapFactory.decodeResource(getResources(), R.drawable.direct_wide_50);
            canvas.drawBitmap(bitmapK, 140, 380, paintPict);
            exRes = getVal3(res.res_7, res.res_8, res.res_9);
            if (exRes.length() > 0) canvas.drawText(exRes, 320, 460, paintFontRes);
        } else {
            String exName;
            String exRes = "";
            if (res.tp == ArmPrg.en_type.max5) {
                exName = getResources().getString(R.string.title_day_type_max_count);
                if (res.res_1 > 0) exRes = String.valueOf(res.res_1) + ", ";
                if (res.res_2 > 0) exRes += String.valueOf(res.res_2) + ", ";
                if (res.res_3 > 0) exRes += String.valueOf(res.res_3) + ", ";
                if (res.res_4 > 0) exRes += String.valueOf(res.res_4) + ", ";
                if (res.res_5 > 0) exRes += String.valueOf(res.res_5) + ", ";
                if (exRes.length() > 2) exRes = exRes.substring(0, exRes.length() - 2);
            } else if (res.tp == ArmPrg.en_type.maxSet) {
                exName = getResources().getString(R.string.title_day_type_max_sets);
                if (res.res_1 > 0 && res.res_2 > 0)
                    exRes = String.valueOf(res.res_2) + " x " + String.valueOf(res.res_1);
            } else if (res.tp == ArmPrg.en_type.pyramid) {
                exName = getResources().getString(R.string.title_day_type_pyramid);
                if (res.res_1 == 1)
                    exRes = "1";
                else if (res.res_1 == 2)
                    exRes = "1, 2";
                else if (res.res_1 == 3)
                    exRes = "1, 2, 3";
                else if (res.res_1 > 0)
                    exRes = "1, 2, ... " + String.valueOf(res.res_1);
            } else {
                exName = "";
                exRes = "";
            }

            canvas.drawText(exName, canvas.getWidth() / 2, 220, paintFont);
            canvas.drawText(exRes, 320, 360, paintFontRes);

            Bitmap bitmapK = BitmapFactory.decodeResource(getResources(), R.drawable.direct_norm_50);
            canvas.drawBitmap(bitmapK, 140, 280, paintPict);
        }

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), mutableBitmap, "res", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        try {
            startActivity(Intent.createChooser(intent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
        }
    }

    private String getVal3(int res_1, int res_2, int res_3) {
        String exRes = "";
        if (res_1 == res_2 && res_2 == res_3 && res_1 > 0) {
            exRes = "3 x " + String.valueOf(res_1);
        } else {
            if (res_1 > 0) exRes = String.valueOf(res_1) + ", ";
            if (res_2 > 0) exRes += String.valueOf(res_2) + ", ";
            if (res_3 > 0) exRes += String.valueOf(res_3) + ", ";
            if (exRes.length() > 2) exRes = exRes.substring(0, exRes.length() - 2);
        }

        return exRes;
    }

    public void onBackPressed() {
        OnClickClose(null);
    }

}
