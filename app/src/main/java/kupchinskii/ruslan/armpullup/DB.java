package kupchinskii.ruslan.armpullup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "training.db";
    //region create T1 - 5max
    public static final String T_T1 = "t1";
    public static final String C_T1_ID = "_id";
    public static final String C_T1_W = "w";
    public static final String C_T1_V1 = "v1";
    public static final String C_T1_V2 = "v2";
    public static final String C_T1_V3 = "v3";
    public static final String C_T1_V4 = "v4";
    public static final String C_T1_V5 = "v5";
    public static final String C_T1_DATE = "dt";
    //endregion
    //region create T2 - piramid
    public static final String T_T2 = "t2";
    public static final String C_T2_ID = "_id";
    public static final String C_T2_W = "w";
    public static final String C_T2_V = "v";
    public static final String C_T2_DATE = "dt";
    //endregion
    //region create T3 - 3x3 grip
    public static final String T_T3 = "t3";
    public static final String C_T3_ID = "_id";
    public static final String C_T3_W = "w";
    public static final String C_T3_V1 = "v1";
    //endregion
    public static final String C_T3_V2 = "v2";
    public static final String C_T3_V3 = "v3";
    public static final String C_T3_V4 = "v4";
    public static final String C_T3_V5 = "v5";
    public static final String C_T3_V6 = "v6";
    public static final String C_T3_V7 = "v7";
    public static final String C_T3_V8 = "v8";
    public static final String C_T3_V9 = "v9";
    public static final String C_T3_DATE = "dt";
    //region create T4 - maxSet
    public static final String T_T4 = "t4";
    public static final String C_T4_ID = "_id";
    public static final String C_T4_W = "W";
    public static final String C_T4_V = "v";
    //region create TW - week
    public static final String T_TW = "w";
    //endregion
    public static final String C_TW_ID = "_id";
    public static final String C_TW_Y = "y";
    public static final String C_TW_N = "n";
    public static final String C_TW_RS = "rs";
    public static final String C_TW_D1 = "d1";
    //endregion
    public static final String C_TW_D2 = "d2";
    public static final String C_TW_D3 = "d3";
    public static final String C_TW_D4 = "d4";
    public static final String C_TW_D5 = "d5";
    public static final String C_TW_D6 = "d6";
    public static final String C_TW_D7 = "d7";
    public static final String C_T4_DATE = "dt";

    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_T1 = "CREATE TABLE " + T_T1 + " ("
            + C_T1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_T1_W + " INTEGER, "
            + C_T1_V1 + " INTEGER, "
            + C_T1_V2 + " INTEGER, "
            + C_T1_V3 + " INTEGER, "
            + C_T1_V4 + " INTEGER, "
            + C_T1_V5 + " INTEGER, "
            + C_T1_DATE + " DATE "

            + ");";
    private static final String SQL_CREATE_T2 = "CREATE TABLE " + T_T2 + " ("
            + C_T2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_T2_W + " INTEGER, "
            + C_T2_V + " INTEGER, "
            + C_T2_DATE + " DATE "
            + ");";
    private static final String SQL_CREATE_T3 = "CREATE TABLE " + T_T3 + " ("
            + C_T3_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_T3_W + " INTEGER, "
            + C_T3_V1 + " INTEGER, "
            + C_T3_V2 + " INTEGER, "
            + C_T3_V3 + " INTEGER, "
            + C_T3_V4 + " INTEGER, "
            + C_T3_V5 + " INTEGER, "
            + C_T3_V6 + " INTEGER, "
            + C_T3_V7 + " INTEGER, "
            + C_T3_V8 + " INTEGER, "
            + C_T3_V9 + " INTEGER, "
            + C_T3_DATE + " DATE "
            + ");";
    private static final String SQL_CREATE_T4 = "CREATE TABLE " + T_T4 + " ("
            + C_T4_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_T4_W + " INTEGER, "
            + C_T4_V + " INTEGER, "
            + C_T4_DATE + " DATE "
            + ");";
    private static final String SQL_CREATE_TW = "CREATE TABLE " + T_TW + " ("
            + C_TW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_TW_Y  + " INTEGER, "
            + C_TW_N  + " INTEGER, "
            + C_TW_RS + " INTEGER, " // расчетное число повторений не неделю
            + C_TW_D1 + " INTEGER, " /*D1 - D7 training type on day */
            + C_TW_D2 + " INTEGER, "
            + C_TW_D3 + " INTEGER, "
            + C_TW_D4 + " INTEGER, "
            + C_TW_D5 + " INTEGER, "
            + C_TW_D6 + " INTEGER, "
            + C_TW_D7 + " INTEGER  "
            + ");";
    //region utils
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //endregion
    private static DB _db = null;

    //region main
    private DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static void initDB(Context context) {
        if (_db == null)
            _db = new DB(context);
    }

    public static SQLiteDatabase getDBRead() {
        return _db.getReadableDatabase();
    }

    public static SQLiteDatabase getDBWrite() {
        return _db.getWritableDatabase();
    }

    public static String getDataStr(Date dt) {
        return sdf.format(dt);
    }
    //endregion main

    public static Date getData(String dt) {
        try {
            return sdf.parse(dt);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getColName(String table, String column, String alias){
        return table + "." + column + (alias.equals("") ? "" : " as " + alias);
    }

    public static String getColName(String table, String column) {
        return getColName(table, column, "");
    }

    public static String getColNameNvl(String table, String column, String alias) {
        return "ifnull(" + getColName(table, column, "") + ", 0)" + (alias.equals("") ? "" : " as " + alias);
    }

    public static String getColNameNvl(String table, String column) {
        return getColNameNvl(table, column, "");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_T1);
        db.execSQL(SQL_CREATE_T2);
        db.execSQL(SQL_CREATE_T3);
        db.execSQL(SQL_CREATE_T4);
        db.execSQL(SQL_CREATE_TW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1 && oldVersion < 2)
        {
            db.execSQL("ALTER TABLE " + T_T1 + " ADD COLUMN " + C_T1_DATE + " DATE ");
            db.execSQL("ALTER TABLE " + T_T2 + " ADD COLUMN " + C_T2_DATE + " DATE ");
            db.execSQL("ALTER TABLE " + T_T3 + " ADD COLUMN " + C_T3_DATE + " DATE ");
            db.execSQL("ALTER TABLE " + T_T4 + " ADD COLUMN " + C_T4_DATE + " DATE ");
        }
    }

    //endregion
}