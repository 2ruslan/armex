package kupchinskii.ruslan.armpullup;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import java.util.Calendar;

class ArmPrg {
    public static final int NEXT_END = -1;
    public static final int NEXT_MAX = -2;
    private static int weekCnt = 0;
    private static boolean freeBase;
    private en_type type;
    private int pos = 0;
    private long tId;
    private DBHelper.PrevWeekInfo prevWeekInfo;

    public ArmPrg(en_type pType) {
        type = pType;
        tId = DBHelper.GetCurrentTNId(pType);
        prevWeekInfo = DBHelper.getPrevWeekInfo();
    }

    public static en_type GetTodayType(int ofset)
    {
        en_type res = en_type.none;
        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
        int dw = calendar.get(Calendar.DAY_OF_WEEK) - ofset;
        if (dw < 0)
            dw = 7 + dw;

        if (dw == Calendar.MONDAY)
            res = en_type.max5;
        else if (dw == Calendar.TUESDAY)
            res = en_type.pyramid;
        else if (dw == Calendar.WEDNESDAY)
            res = en_type.grip;
        else if (dw == Calendar.THURSDAY)
            res = en_type.maxSet;
        else if (dw == Calendar.FRIDAY)
            res = en_type.free;

        // инициализация первой недели
        if (res == en_type.grip || res == en_type.maxSet || res == en_type.free) {

            DBHelper.prevWeekCntResult inf = DBHelper.getPrevWeekCnt();
            if (res == en_type.free && (inf.pyramidSum == 0 || inf.maxSum == 0)) {
                res = en_type.max5;
            }
            if (res == en_type.maxSet && (inf.pyramidSum == 0 || inf.maxSum == 0)) {
                res = en_type.pyramid;
            }
            if (res == en_type.grip && (inf.pyramidSum == 0 || inf.maxSum == 0)) {
                res = en_type.max5;
            }
            if (res == en_type.free && (inf.first9 == 0)) {
                res = en_type.grip;
            }
            if (res == en_type.free && (inf.maxSetsSum == 0)) {
                res = en_type.maxSet;
            }
        }
        freeBase = res == en_type.free;

        if (freeBase) {
            DBHelper.chkCntResult ChkCnt = DBHelper.getChkCnt(true);
            if (ChkCnt.maxSetsCnt > 0)
                res = en_type.maxSet;
            else if (ChkCnt.gripCnt > 0)
                res = en_type.grip;
            else if (ChkCnt.pyramidCnt > 0)
                res = en_type.pyramid;
            else if (ChkCnt.maxCnt > 0)
                res = en_type.max5;
        }
        return res;
    }

    // получаем число недели
    public static int getWeekCnt(boolean last) {

        weekCnt = DBHelper.getWeekCnt(last);

        if (weekCnt == 0) {
            DBHelper.prevWeekCntResult inf = DBHelper.getPrevWeekCnt();
            int cMaxCnt = (inf.maxSum / 9) + 1;
            if (inf.maxSum % 9 > 8)
                cMaxCnt++;
            int cPyramid = (inf.pyramidSum / 9) + 1;
            if (inf.pyramidSum % 9 > 8)
                cPyramid++;
            int cMaxSets = (inf.maxSetsSum / 9);
            if (inf.maxSetsSum % 9 > 8)
                cMaxSets++;

            weekCnt = Math.max(3, cMaxCnt);
            weekCnt = Math.max(weekCnt, cPyramid);
            weekCnt = Math.max(weekCnt, cMaxSets);
        }

        return weekCnt;
    }

    // сохраняем число недели
    public static void setWeekCnt(int cnt) {
        DBHelper.saveWeekCnt(cnt);
        weekCnt = cnt;
    }

    public static Spanned getMainInfo(Context context) {

        DBHelper.mainStatResult inf = DBHelper.getMianStatistics();
        String txt = context.getString(R.string.title_main_msg_stat);
        String ftxt;
        if (inf.weeks > 0 && inf.today > inf.start)
            ftxt = String.format(txt, inf.weeks, inf.today - inf.start, inf.today, inf.start);
        else
            ftxt = context.getString(R.string.title_main_msg_stat_first);

        return Html.fromHtml(ftxt);
    }

    private void ChkTodayContinue(en_type tp) {
        DBHelper.chkCntResult res = DBHelper.getChkCnt(freeBase);
        if (tp == en_type.max5)
            pos = res.maxCnt;
        else if (tp == en_type.pyramid)
            pos = res.pyramidCnt;
        else if (tp == en_type.grip)
            pos = res.gripCnt;
        else if (tp == en_type.maxSet)
            pos = res.maxSetsCnt;
    }

    public NextCntInfo GetNextCount()
    {
        if (pos == 0)
            ChkTodayContinue(type);

        pos ++;

        NextCntInfo res = new NextCntInfo();

        res.currentSets = pos;
        res.totalSets = 0;
        res.totalSetsPrev = 0;

        if (type == en_type.max5) {
            res.cnt = pos < 6 ? NEXT_MAX : NEXT_END;
            res.editEnable = true;
            res.grip = en_grip.direct_norm;
            res.totalSets = 5;
            if (pos == 1) res.prevVal = prevWeekInfo.cnt1;
            else if (pos == 2) res.prevVal = prevWeekInfo.cnt2;
            else if (pos == 3) res.prevVal = prevWeekInfo.cnt3;
            else if (pos == 4) res.prevVal = prevWeekInfo.cnt4;
            else if (pos == 5) res.prevVal = prevWeekInfo.cnt5;
        }
        else if (type == en_type.pyramid) {
            res.cnt = pos;
            res.editEnable = false;
            res.grip = en_grip.direct_norm;
            res.totalSetsPrev = prevWeekInfo.totalSetsPyramid;
        }
        else if (type == en_type.grip) {
            if (weekCnt == 0)
                weekCnt = getWeekCnt(true);
            if (weekCnt == 0)
                weekCnt = 6;

            res.cnt = pos < 10 ? weekCnt : NEXT_END;
            res.editEnable = true;
            res.totalSets = 9;
            if (pos < 4)
                res.grip = en_grip.direct_norm;
            else if (pos < 7)
                res.grip = en_grip.reverse_narrow;
            else
                res.grip = en_grip.direct_wide;
        }
        else if (type == en_type.maxSet) {
            if (weekCnt == 0)
                weekCnt = getWeekCnt(true);
            if (weekCnt == 0)
                weekCnt = 6;

            res.cnt = weekCnt;
            res.editEnable = false;
            res.grip = en_grip.direct_norm;
            res.totalSetsPrev = prevWeekInfo.totalSetsMax;
        }
        else {
            res.cnt = NEXT_END;
            res.editEnable = false;
        }

        return res;
    }

    // сколько секунд отдыхать
    public TimerCntInfo getTimerSec(int cnt)
    {
        TimerCntInfo res = new TimerCntInfo();
        res.timerSec = 0;


        if (type == en_type.max5) {
            res.timerSec = pos >= 5 ? 0 : 90; // всего 5 подходов , если дальше то 0 и на выход
        }
        else if (type == en_type.pyramid) {
            res.timerSec = cnt * 10;
        } else if (type == en_type.grip) {
            res.timerSec = pos >= 9 ? 0 : 60; // всего 9 подходов  , если дальше то 0 и на выход
        }
        else if (type == en_type.maxSet) {
            res.timerSec = 60;
        }

        return res;
    }

    // записываем результат и возвращаем секунды отдыха
    public void setRealCount(int cnt)
    {
        if (type == en_type.max5) {
            DBHelper.saveMax5(tId, pos, cnt);
        }
        else if (type == en_type.pyramid) {
            DBHelper.savePyramid(tId, cnt);
        }
        else if (type == en_type.grip) {
            DBHelper.saveGrip(tId, pos, cnt);
        }
        else if (type == en_type.maxSet) {
            DBHelper.saveMaxSet(tId, pos);
        }
    }

    public enum en_type {max5, pyramid, grip, maxSet, free, none}

    public enum en_grip {direct_norm, direct_wide, reverse_narrow}

    public class NextCntInfo{
        public int cnt;
        public boolean editEnable;
        public en_grip grip;
        public int prevVal;

        public int currentSets;
        public int totalSets;
        public int totalSetsPrev;
    }

    public class TimerCntInfo {
        public int timerSec;
    }

}
