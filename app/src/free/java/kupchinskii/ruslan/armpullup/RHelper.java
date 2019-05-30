package kupchinskii.ruslan.armpullup;

import android.content.Context;


import java.util.Random;

public class RHelper extends RHelperBase {

    static String locale;


    public static boolean UseStatisticsR() {
        return !locale.equals("UA");
    }

    static public void init(Context context) {
        locale = context.getResources().getConfiguration().locale.getCountry();
    }

    public static int UseEndR() {
        Random rnd = new Random();

        //if (!locale.equals("UA")) {

            if (!PreferencesHelper.GetNoShowPropO() && rnd.nextInt(SHOW_O) == 7)
                return R_O;

        //}

        if (!PreferencesHelper.GetNoShowPropP() && rnd.nextInt(SHOW_P) == 7)
            return R_PAY_FULL;

        return R_EMPTY_R;
    }


}
