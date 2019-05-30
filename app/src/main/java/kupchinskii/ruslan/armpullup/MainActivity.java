package kupchinskii.ruslan.armpullup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private TextView mStatInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB.initDB(this);
        PreferencesHelper.init(getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE));
        RHelper.init(this);

        initLayout();

        if (PreferencesHelper.GetFirstRun())
            OnClickSetWorkDays();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu_work_days) {
            OnClickSetWorkDays();
            return true;
        } else if (id == R.id.action_menu_backup) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_menu_backup);
            builder
                    .setMessage(R.string.title_msg_quest)
                    .setCancelable(false)
                    .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (DBHelper.BackupData(getApplicationContext()))
                                ShowToast(R.string.title_backup_ok);
                            else
                                ShowToast(R.string.title_backup_error);
                        }
                    })
                    .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();


            return true;
        } else if (id == R.id.action_menu_restore) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_menu_restore);
            builder
                    .setMessage(R.string.title_msg_quest)
                    .setCancelable(false)
                    .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (DBHelper.RestoreData(getApplicationContext()))
                                ShowToast(R.string.title_restore_ok);
                            else
                                ShowToast(R.string.title_restore_error);
                        }
                    })
                    .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();

            return true;
        } else if (id == R.id.action_menu_restore_free) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_menu_restore_free);
            builder
                    .setMessage(R.string.title_msg_quest)
                    .setCancelable(false)
                    .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (DBHelper.RestoreDataFree(getApplicationContext()))
                                ShowToast(R.string.title_restore_ok);
                            else
                                ShowToast(R.string.title_restore_error);
                        }
                    })
                    .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();

            return true;
        } else if (id == R.id.action_menu_buy) {
            RHelper.BuyFull(this);
            return true;
        } else if (id == R.id.action_menu_estim) {
            RHelper.Estimate(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowToast(int msg) {
        Toast t = Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }


    @Override
    public void onBackPressed() {
        showEndExt(-1);
    }

    private void initLayout() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int infoH = 26;
        int pagMargin = Math.round(metrics.widthPixels * 0.02f);
        int bms = Math.round(metrics.widthPixels * 0.4f - pagMargin / 2);
        int bmst = Math.round(metrics.widthPixels * 0.4f - pagMargin / 2);
        int bmc = Math.round(metrics.widthPixels * 0.3f - pagMargin / 2);
        int tdh = Math.round(metrics.heightPixels - 2 * bms - 2 * pagMargin);
        int tdw = metrics.widthPixels - 2 * pagMargin;

        int sdk = android.os.Build.VERSION.SDK_INT;

        RelativeLayout relativelayout = new RelativeLayout(this);
        relativelayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        relativelayout.setPadding(pagMargin, pagMargin, pagMargin, pagMargin);

        Button but_start = new Button(this);
        //but_start.setId(R.id.but_start);
        RelativeLayout.LayoutParams params_but_start = new RelativeLayout.LayoutParams(bms, bms);
        params_but_start.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_but_start.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_green));
        } else {
            but_start.setBackground(getResources().getDrawable(R.drawable.background_ring_green));
        }

        but_start.setText(R.string.title_start);
        but_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickStart();
            }
        });
        relativelayout.addView(but_start, params_but_start);

        Button but_close = new Button(this);
        //but_close.setId(R.id.but_close);
        RelativeLayout.LayoutParams params_but_close = new RelativeLayout.LayoutParams(bms, bms);
        params_but_close.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params_but_close.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but_close.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_red));
        } else {
            but_close.setBackground(getResources().getDrawable(R.drawable.background_ring_red));
        }

        but_close.setText(R.string.title_close);
        but_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickClose();
            }
        });
        relativelayout.addView(but_close, params_but_close);

        Button but_info = new Button(this);
        RelativeLayout.LayoutParams params_but_info = new RelativeLayout.LayoutParams(bms, bms);
        params_but_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_but_info.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but_info.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_white));
        } else {
            but_info.setBackground(getResources().getDrawable(R.drawable.background_ring_white));
        }



        but_info.setText(R.string.title_info);
        but_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickInfo();
            }
        });
        relativelayout.addView(but_info, params_but_info);

        Button but_statistics = new Button(this);
        RelativeLayout.LayoutParams params_but_exit = new RelativeLayout.LayoutParams(bmst, bmst);
        params_but_exit.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params_but_exit.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            but_statistics.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_ring_blue));
        } else {
            but_statistics.setBackground(getResources().getDrawable(R.drawable.background_ring_blue));
        }
        but_statistics.setText(R.string.title_statistics);
        but_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickStatistics();
            }
        });
        relativelayout.addView(but_statistics, params_but_exit);

        TextView ctrCnt = new TextView(this);
        RelativeLayout.LayoutParams params_ctrCnt = new RelativeLayout.LayoutParams(tdw, tdh);
        params_ctrCnt.addRule(RelativeLayout.BELOW, but_start.getId());
        params_ctrCnt.addRule(RelativeLayout.ABOVE, but_close.getId());
        ctrCnt.setGravity(Gravity.CENTER);
        ctrCnt.setTextSize(16);
        ctrCnt.setText(ArmPrg.getMainInfo(this));
        relativelayout.addView(ctrCnt, params_ctrCnt);

        relativelayout.setKeepScreenOn(true);

        setContentView(relativelayout);

    }

    private void showEndExt(int mode) {

        if (mode == -1)
            mode = RHelper.UseEndR();

        Intent intent = null;
        switch (mode) {
            case RHelper.R_PAY_FULL:
                intent = new Intent(MainActivity.this, endPrg.class);
                intent.putExtra(endPrg.MODE, endPrg.MODE_P);
                break;
            case RHelper.R_O:
                intent = new Intent(MainActivity.this, endPrg.class);
                intent.putExtra(endPrg.MODE, endPrg.MODE_O);
                break;
        }

        if (intent != null)
            startActivity(intent);

        finish();
    }

    //region handlers
    public void OnClickSetWorkDays(){
        View frg = View.inflate(this, R.layout.fragment_set_work_days, null);
        final RadioButton rbDayOffset0 = (RadioButton) frg.findViewById(R.id.rb_d0);
        final RadioButton rbDayOffset1 = (RadioButton) frg.findViewById(R.id.rb_d1);
        final RadioButton rbDayOffset2 = (RadioButton) frg.findViewById(R.id.rb_d2);
        final RadioButton rbDayOffset3 = (RadioButton) frg.findViewById(R.id.rb_d3);
        final RadioButton rbDayOffset4 = (RadioButton) frg.findViewById(R.id.rb_d4);
        final RadioButton rbDayOffset5 = (RadioButton) frg.findViewById(R.id.rb_d5);
        final RadioButton rbDayOffset6 = (RadioButton) frg.findViewById(R.id.rb_d6);

        int dayOffset = PreferencesHelper.GetDayOffset();

        if (dayOffset == 0) rbDayOffset0.setChecked(true);
        if (dayOffset == 1) rbDayOffset1.setChecked(true);
        if (dayOffset == 2) rbDayOffset2.setChecked(true);
        if (dayOffset == 3) rbDayOffset3.setChecked(true);
        if (dayOffset == 4) rbDayOffset4.setChecked(true);
        if (dayOffset == 5) rbDayOffset5.setChecked(true);
        if (dayOffset == 6) rbDayOffset6.setChecked(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle(Helpers.getEditDlgTitle(addmode));
        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArmPrg.en_type todayType;
                        int dayof = 0;
                        if (rbDayOffset0.isChecked()) dayof = 0;
                        if (rbDayOffset1.isChecked()) dayof = 1;
                        if (rbDayOffset2.isChecked()) dayof = 2;
                        if (rbDayOffset3.isChecked()) dayof = 3;
                        if (rbDayOffset4.isChecked()) dayof = 4;
                        if (rbDayOffset5.isChecked()) dayof = 5;
                        if (rbDayOffset6.isChecked()) dayof = 6;

                        PreferencesHelper.SetDayOffset(dayof);
                    }
                })
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //     finish();
                    }
                }).show();
    }

    public void OnClickStart() {
        Intent intent = new Intent(MainActivity.this, work.class);
        startActivity(intent);
    }

    public void OnClickClose() {
        showEndExt(-1);
    }

    public void OnClickInfo() {
        Intent intent = new Intent(MainActivity.this, info.class);
        startActivity(intent);
    }

    public void OnClickStatistics() {
        Intent intent = new Intent(MainActivity.this, statistics.class);
        startActivity(intent);
    }
    //endregion handlers
}
