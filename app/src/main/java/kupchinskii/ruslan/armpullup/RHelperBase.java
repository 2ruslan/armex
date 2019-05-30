package kupchinskii.ruslan.armpullup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public abstract class RHelperBase {
    // шансы показать 1 из ...
    public static final int SHOW_R = 17; // реклама
    public static final int SHOW_O = 37; // оценка
    public static final int SHOW_P = 57; // полная версия

    public static final int R_PAY_FULL = 0; // купить полную версию
    public static final int R_O = 2; // просьба оценить
    public static final int R_EMPTY_R = 3; // ничего не делаем


    public static void BuyFull(Context context) {
        String appPackageName = context.getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName.replace("free", "full")
                + "&utm_source=free_version&utm_medium=banner%20&utm_campaign=free_version"));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(marketIntent);
    }

    public static void Estimate(Context context) {
        String appPackageName = context.getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName
                        + "&utm_source=app_estim&utm_medium=banner%20&utm_campaign=app_estim"
        ));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(marketIntent);
        PreferencesHelper.SetNoShowPropO(true);
    }
}
