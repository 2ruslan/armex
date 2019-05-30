package kupchinskii.ruslan.armpullup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class work extends Activity {
    public static final int FIRST_VAL = -3;
    ArmPrg armPrg;
    EditText ctrCnt;
    TextView  ctrlSetsInfo;

    ImageView ctrlImage;


    public int getCount() {
        try {
            return Integer.parseInt(ctrCnt.getText().toString());
        } catch (Exception ex) {
            Toast t = Toast.makeText(getApplicationContext(), R.string.toast_nput_cnt,
                    Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
            return 0;
        }
    }

    public void setCount(int count) {
        int cnt = count > -1 ? count : 0;
        ctrCnt.setText(cnt > 0 ? String.valueOf(cnt) : "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();

        initArmPrg();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    //region method's
    private void initLayout()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int sdk = android.os.Build.VERSION.SDK_INT;

        int pagMargin = Math.round(metrics.widthPixels * 0.02f);
        int bd = Math.round(metrics.widthPixels * 0.4f - pagMargin / 2);
        int td = Math.min(Math.round(metrics.heightPixels - 2 * bd), metrics.widthPixels);
        float fsCnt = 96;//TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, td / 4.5f, metrics);
        float fsSetsInfo = fsCnt / 3.7f;

        RelativeLayout relativelayout = new RelativeLayout(this);
        relativelayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        relativelayout.setPadding(pagMargin, pagMargin, pagMargin, pagMargin);

        Button but_done = new Button(this);
        RelativeLayout.LayoutParams params_but_done = new RelativeLayout.LayoutParams(bd,bd);
        params_but_done.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_but_done.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but_done.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_green));
        } else {
            but_done.setBackground(getResources().getDrawable(R.drawable.background_ring_green));
        }

        but_done.setText(R.string.title_done);
        but_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickSave();
            }
        });
        relativelayout.addView(but_done, params_but_done);

        Button but_exit_save = new Button(this);
        RelativeLayout.LayoutParams params_but_exit_save = new RelativeLayout.LayoutParams(bd,bd);
        params_but_exit_save.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_but_exit_save.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but_exit_save.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_blue));
        } else {
            but_exit_save.setBackground(getResources().getDrawable(R.drawable.background_ring_blue));
        }

        but_exit_save.setText(R.string.title_exit_save);
        but_exit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickExitSave();
            }
        });
        relativelayout.addView(but_exit_save, params_but_exit_save);

        Button but_exit = new Button(this);
        RelativeLayout.LayoutParams params_but_exit = new RelativeLayout.LayoutParams(bd,bd);
        params_but_exit.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params_but_exit.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but_exit.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_red));
        } else {
            but_exit.setBackground(getResources().getDrawable(R.drawable.background_ring_red));
        }

        but_exit.setText(R.string.title_exit_no_save);
        but_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickExitNoSave();
            }
        });
        relativelayout.addView(but_exit, params_but_exit);

        ctrlImage = new ImageView(this);
        RelativeLayout.LayoutParams params_ctrlImage = new RelativeLayout.LayoutParams(bd,bd);
        params_ctrlImage.addRule(RelativeLayout.ALIGN_LEFT);
        params_ctrlImage.addRule(RelativeLayout.ALIGN_TOP);
        relativelayout.addView(ctrlImage, params_ctrlImage);

        int delta = (int) (td * 0.3);
        ctrlSetsInfo = new TextView(this);
        RelativeLayout.LayoutParams params_ctrlSetsInfo = new RelativeLayout.LayoutParams(td, td - delta );
        params_ctrlSetsInfo.addRule(RelativeLayout.CENTER_IN_PARENT);
        ctrlSetsInfo.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        ctrlSetsInfo.setTextSize(fsSetsInfo);
        relativelayout.addView(ctrlSetsInfo, params_ctrlSetsInfo);

        ctrCnt = new EditText(this);
        RelativeLayout.LayoutParams params_ctrCnt = new RelativeLayout.LayoutParams(td,td);
        params_ctrCnt.addRule(RelativeLayout.CENTER_IN_PARENT);
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ctrCnt.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_white));
        } else {
            ctrCnt.setBackground(getResources().getDrawable(R.drawable.background_ring_white));
        }

        ctrCnt.setGravity(Gravity.CENTER);
        ctrCnt.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        ctrCnt.setInputType(InputType.TYPE_CLASS_NUMBER);
        ctrCnt.setTextSize(fsCnt);
        relativelayout.addView(ctrCnt, params_ctrCnt);
        relativelayout.setKeepScreenOn(true);
        setContentView(relativelayout);
    }

    private void initArmPrg() {
        int dayOffset = PreferencesHelper.GetDayOffset();
        ArmPrg.en_type todayType = ArmPrg.GetTodayType(dayOffset);
        //todayType = ArmPrg.en_type.grip; // test
        if (todayType == ArmPrg.en_type.none) {
            Intent intent = new Intent(work.this, endWork.class);
            intent.putExtra(endWork.MODE, endWork.MODE_DAY_DAY_OFF);
            startActivity(intent);
        }
        else {
            ShowPreInfo(todayType);
        }
    }

    private void getNextCnt(int crrCnt){
        new WorkAsync().execute(crrCnt);
 }

    //region handlers
    public void OnClickSave() {
        int cn = getCount();
        if (cn > 0) {
            getNextCnt(getCount());
        }
    }

    //endregion method's

    public void OnClickExitSave() {
        int cn = getCount();
        if (cn > 0) {
            armPrg.setRealCount(cn);
            dayEnd();
        }
    }

    public void OnClickExitNoSave() {
        dayEnd();
    }

    @SuppressLint("StringFormatInvalid")
    private void ShowPreInfo(ArmPrg.en_type type) {

        View frg = View.inflate(this, R.layout.fragment_pre_work_info, null);

        final LinearLayout ctrlHard = (LinearLayout) frg.findViewById(R.id.pre_hard);
        final LinearLayout ctrlMaxSet = (LinearLayout) frg.findViewById(R.id.pre_max_set);
        final LinearLayout ctrlMaxCnt = (LinearLayout) frg.findViewById(R.id.pre_max_cnt);
        final LinearLayout ctrlPyramid = (LinearLayout) frg.findViewById(R.id.pre_pyramid);
        final LinearLayout ctrlGrip = (LinearLayout) frg.findViewById(R.id.pre_grip);
        AlertDialog.Builder builder = null;

        switch (type) {
            case free:
                builder = showDialogHardDay(frg);
                ctrlMaxSet.setVisibility(View.GONE);
                ctrlMaxCnt.setVisibility(View.GONE);
                ctrlPyramid.setVisibility(View.GONE);
                ctrlGrip.setVisibility(View.GONE);
                break;
            case grip:
                builder = showDialogGripDay(frg);
                ctrlHard.setVisibility(View.GONE);
                ctrlMaxSet.setVisibility(View.GONE);
                ctrlMaxCnt.setVisibility(View.GONE);
                ctrlPyramid.setVisibility(View.GONE);
                break;
            case max5:
                builder = showDialogDay(frg, type);
                ctrlHard.setVisibility(View.GONE);
                ctrlMaxSet.setVisibility(View.GONE);
                ctrlPyramid.setVisibility(View.GONE);
                ctrlGrip.setVisibility(View.GONE);
                break;
            case maxSet:
                builder = showDialogDay(frg, type);

                TextView tv = (TextView) frg.findViewById(R.id.pre_max_set_info);
                String txt = getString(R.string.msg_sel_max_set);
                int ecnt = ArmPrg.getWeekCnt(true);
                tv.setText(String.format(txt, ecnt));
                ctrlHard.setVisibility(View.GONE);
                ctrlMaxCnt.setVisibility(View.GONE);
                ctrlPyramid.setVisibility(View.GONE);
                ctrlGrip.setVisibility(View.GONE);
                break;
            case pyramid:
                builder = showDialogDay(frg, type);
                ctrlHard.setVisibility(View.GONE);
                ctrlMaxSet.setVisibility(View.GONE);
                ctrlMaxCnt.setVisibility(View.GONE);
                ctrlGrip.setVisibility(View.GONE);
                break;
        }

        builder.show();
    }

    private AlertDialog.Builder showDialogDay(View frg, final ArmPrg.en_type type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startWork(type);
                    }
                })
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        return builder;
    }

    private AlertDialog.Builder showDialogGripDay(View frg) {

        final EditText edWeekCnt = (EditText) frg.findViewById(R.id.ed_week_cnt);
        int ecnt = ArmPrg.getWeekCnt(false);
        edWeekCnt.setText(String.valueOf(ecnt));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int cnt = Integer.parseInt(edWeekCnt.getText().toString());
                        ArmPrg.setWeekCnt(cnt);
                        startWork(ArmPrg.en_type.grip);
                    }
                })
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        return builder;
    }

    private AlertDialog.Builder showDialogHardDay(View frg) {

        final RadioButton rbMaxCnt = (RadioButton) frg.findViewById(R.id.rb_max_cnt);
        final RadioButton rbPyramid = (RadioButton) frg.findViewById(R.id.rb_pyramid);
        final RadioButton rbGrip = (RadioButton) frg.findViewById(R.id.rb_grip);
        final RadioButton rbMaxSets = (RadioButton) frg.findViewById(R.id.rb_max_sets);

        rbMaxCnt.setChecked(true);
        rbMaxCnt.setEnabled(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle(Helpers.getEditDlgTitle(addmode));
        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArmPrg.en_type todayType;

                        if (rbMaxCnt.isChecked())
                            todayType = ArmPrg.en_type.max5;
                        else if (rbPyramid.isChecked())
                            todayType = ArmPrg.en_type.pyramid;
                        else if (rbGrip.isChecked())
                            todayType = ArmPrg.en_type.grip;
                        else if (rbMaxSets.isChecked())
                            todayType = ArmPrg.en_type.maxSet;
                        else
                            todayType = ArmPrg.en_type.none;

                        armPrg = new ArmPrg(todayType);
                        getNextCnt(FIRST_VAL);
                    }
                })
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        return builder;
    }

    private void startWork(ArmPrg.en_type todayType) {
        armPrg = new ArmPrg(todayType);
        getNextCnt(FIRST_VAL);
    }

    //endregion handlers

    private void dayEnd()
    {
        Intent intent = new Intent(work.this, endWork.class);
        intent.putExtra(endWork.MODE, endWork.MODE_DAY_FINISH);
        startActivity(intent);
    }

    @Override // а шоб не выходили просто так
    public void onBackPressed() {}

    private class WorkAsync extends AsyncTask<Integer, Object, ArmPrg.NextCntInfo> {
        int crrCnt = 0;

        @Override
        protected ArmPrg.NextCntInfo doInBackground(Integer... params) {
            crrCnt = params[0];
            ArmPrg.TimerCntInfo tmrInfo = null;
            if (crrCnt != FIRST_VAL){
                tmrInfo = armPrg.getTimerSec(crrCnt);
                armPrg.setRealCount(crrCnt);
            }

            ArmPrg.NextCntInfo result = armPrg.GetNextCount();

            if (tmrInfo != null && tmrInfo.timerSec > 0) {
                Intent intent = new Intent(work.this, timer.class);
                intent.putExtra(timer.PRM_SECOND, tmrInfo.timerSec);
                intent.putExtra(timer.PRM_NEXT_INFO_GRIP, result.grip);
                intent.putExtra(timer.PRM_NEXT_INFO_CNT, result.cnt);
                intent.putExtra(timer.PRM_NEXT_INFO_PREV, result.prevVal);


                startActivityForResult(intent, 0);
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArmPrg.NextCntInfo result) {
            if (result.cnt == ArmPrg.NEXT_END) {
                dayEnd();
                return;
            } else if (result.cnt == ArmPrg.NEXT_MAX) {
                setCount(0);
                String hint = "max";
                if (result.prevVal > 0)
                    hint += "\n(" + String.valueOf(result.prevVal) + ")";
                ctrCnt.setHint(hint);
            } else {
                setCount(result.cnt);
                ctrCnt.setHint(String.valueOf(result.cnt));
            }

            String info = String.valueOf(result.currentSets) + "/" + (result.totalSets > 0 ? String.valueOf(result.totalSets) : "max");
            if (result.totalSets == 0 && result.totalSetsPrev != 0)
                info += "(" + String.valueOf(result.totalSetsPrev) + ")";
            do {
                info = " " + info + " ";
            }while (info.length() < 12);
            SpannableString spanString = new SpannableString(info);
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
            ctrlSetsInfo.setText(spanString);

            if (result.grip == ArmPrg.en_grip.reverse_narrow)
                ctrlImage.setImageResource(R.drawable.reverse_narrow);
            else if (result.grip == ArmPrg.en_grip.direct_wide)
                ctrlImage.setImageResource(R.drawable.direct_wide);
            else if (result.grip == ArmPrg.en_grip.direct_norm)
                ctrlImage.setImageResource(R.drawable.direct_norm);

            ctrCnt.setFocusable(result.editEnable);
        }
    }

}
