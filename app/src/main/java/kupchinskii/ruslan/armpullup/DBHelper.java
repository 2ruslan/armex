package kupchinskii.ruslan.armpullup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;

public class DBHelper {

    public static final String COLUMN_YEAR = "colYear";
    public static final String COLUMN_WEEK_NUM = "colWeekNum";
    public static final String COLUMN_VAL = "colVal";

    // статистика отчет по максимуму
    public static final String SQL_REP_MAX5 =
            " SELECT " +
                    DB.getColName(DB.T_TW, DB.C_TW_Y, COLUMN_YEAR) + ", " +
                    DB.getColName(DB.T_TW, DB.C_TW_N, COLUMN_WEEK_NUM) + ", " +
                    " MAX( " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + " , " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + " , " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + " , " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + " , " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) +
                    " ) AS " + COLUMN_VAL +
                    " FROM " + DB.T_T1 + " INNER JOIN " + DB.T_TW + " ON " + DB.getColName(DB.T_T1, DB.C_T1_W) + " = " + DB.getColName(DB.T_TW, DB.C_TW_ID) +
                    " ORDER BY 1, 2 ";

    public static final String SQL_REP_WEEK_TOTAL =
            " SELECT " +
                    DB.getColName(DB.T_TW, DB.C_TW_Y, COLUMN_YEAR) + ", " +
                    DB.getColName(DB.T_TW, DB.C_TW_N, COLUMN_WEEK_NUM) + ", " +
                    " SUM(TS.SM) AS " + COLUMN_VAL +
                    " FROM " + DB.T_TW + " INNER JOIN " +
                    " (SELECT " +
                    DB.getColName(DB.T_T1, DB.C_T1_W) + ", " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + " + " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + " + " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + " + " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + " + " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) + " AS SM " +
                    " FROM " + DB.T_T1 +
                    " UNION ALL SELECT " +
                    DB.getColName(DB.T_T2, DB.C_T2_W) + ", " +
                    "((" + DB.getColNameNvl(DB.T_T2, DB.C_T2_V) + " + 1) / 2) *  " + DB.getColNameNvl(DB.T_T2, DB.C_T2_V) + " AS SM " +
                    " FROM " + DB.T_T2 +
                    " UNION ALL SELECT " +
                    DB.getColName(DB.T_T3, DB.C_T3_W) + ", " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T1_V1) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V2) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V3) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V4) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V5) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V6) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V7) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V8) + " + " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V9) + " AS SM " +
                    " FROM " + DB.T_T3 +
                    " UNION ALL SELECT " +
                    DB.getColNameNvl(DB.T_T4, DB.C_T4_W) + ", " +
                    DB.getColNameNvl(DB.T_TW, DB.C_TW_RS) + " * " + DB.getColNameNvl(DB.T_T4, DB.C_T4_V) + " AS SM " +
                    " FROM " + DB.T_T4 + " INNER JOIN " + DB.T_TW + " ON " + DB.getColName(DB.T_T4, DB.C_T4_W) + " = " + DB.getColName(DB.T_TW, DB.C_TW_ID) +
                    " ) TS ON " + DB.getColName(DB.T_TW, DB.C_TW_ID) + " = TS.W " +
                    " GROUP BY " + DB.getColName(DB.T_TW, DB.C_TW_Y) + ", " + DB.getColName(DB.T_TW, DB.C_TW_N) +
                    " ORDER BY 1, 2 ";

    public static final String SQL_REP_PYRAMID_DAY =
            " SELECT " +
                    DB.getColName(DB.T_TW, DB.C_TW_Y, COLUMN_YEAR) + ", " +
                    DB.getColName(DB.T_TW, DB.C_TW_N, COLUMN_WEEK_NUM) + ", " +
                    DB.getColName(DB.T_T2, DB.C_T2_V) + " AS " + COLUMN_VAL +
                    " FROM " + DB.T_T2 + " INNER JOIN " + DB.T_TW + " ON " + DB.getColName(DB.T_T2, DB.C_T2_W) + " = " + DB.getColName(DB.T_TW, DB.C_TW_ID) +
                    " WHERE " + DB.getColName(DB.T_T2, DB.C_T2_V) + " IS NOT NULL " +
                    " ORDER BY 1, 2 ";

    public static final String SQL_REP_MAX_SETS_DAY =
            " SELECT " +
                    DB.getColName(DB.T_TW, DB.C_TW_Y, COLUMN_YEAR) + ", " +
                    DB.getColName(DB.T_TW, DB.C_TW_N, COLUMN_WEEK_NUM) + ", " +
                    DB.getColName(DB.T_T4, DB.C_T4_V) + " * " + DB.getColName(DB.T_TW, DB.C_TW_RS) + " AS " + COLUMN_VAL +
                    " FROM " + DB.T_T4 + " INNER JOIN " + DB.T_TW + " ON " + DB.getColName(DB.T_T4, DB.C_T4_W) + " = " + DB.getColName(DB.T_TW, DB.C_TW_ID) +
                    " WHERE " + DB.getColName(DB.T_T4, DB.C_T4_V) + " IS NOT NULL " +
                    " ORDER BY 1, 2 ";

    public static final String SQL_REP_MAX_CNT_DAY =
            " SELECT " +
                    DB.getColName(DB.T_TW, DB.C_TW_Y, COLUMN_YEAR) + ", " +
                    DB.getColName(DB.T_TW, DB.C_TW_N, COLUMN_WEEK_NUM) + ", " +
                    DB.getColName(DB.T_T1, DB.C_T1_V1) + ", " +
					DB.getColName(DB.T_T1, DB.C_T1_V2) + ", " +
					DB.getColName(DB.T_T1, DB.C_T1_V3) + ", " +
					DB.getColName(DB.T_T1, DB.C_T1_V4) + ", " +
					DB.getColName(DB.T_T1, DB.C_T1_V5) + 
                    " FROM " + DB.T_T1 + " INNER JOIN " + DB.T_TW + " ON " + DB.getColName(DB.T_T1, DB.C_T1_W) + " = " + DB.getColName(DB.T_TW, DB.C_TW_ID) +
                    " WHERE " + DB.getColName(DB.T_T1, DB.C_T1_V1) + " IS NOT NULL " +
                    " ORDER BY 1, 2 ";

    public static final String SQL_REP_GRIP_DAY =
            " SELECT " +
                    DB.getColName(DB.T_TW, DB.C_TW_Y, COLUMN_YEAR) + ", " +
                    DB.getColName(DB.T_TW, DB.C_TW_N, COLUMN_WEEK_NUM) + ", " +
                    DB.getColName(DB.T_T3, DB.C_T3_V1) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V2) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V3) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V4) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V5) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V6) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V7) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V8) + " + " +
                    DB.getColName(DB.T_T3, DB.C_T3_V9) + " AS " + COLUMN_VAL +
                    " FROM " + DB.T_T3 + " INNER JOIN " + DB.T_TW + " ON " + DB.getColName(DB.T_T3, DB.C_T3_W) + " = " + DB.getColName(DB.T_TW, DB.C_TW_ID) +
                    " WHERE " + DB.getColName(DB.T_T3, DB.C_T3_V1) + " IS NOT NULL " +
                    " ORDER BY 1, 2 ";

    private static final String main_stat_sql = 
			" SELECT " +
            "   (SELECT COUNT(*) FROM " + DB.T_TW + " ) as weeks, " +
            "   (SELECT MAX(MAX(" +
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + ", " + 	
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) + 
					")) FROM " + DB.T_T1 + ") , " +
            "   (SELECT MAX(" + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + ", " + 	
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) + 
				" ) FROM " + DB.T_T1 + 
				" WHERE " + DB.getColName(DB.T_T1, DB.C_T1_ID) + " = ( SELECT MIN(" + DB.getColName(DB.T_T1, DB.C_T1_ID) + ") FROM " + DB.T_T1 + " ) ) , " +
			 "   (SELECT MAX(" + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + ", " + 	
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + ", " + 
					DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) + 
				" ) FROM " + DB.T_T1 + 
				" WHERE " + DB.getColName(DB.T_T1, DB.C_T1_ID) + " = ( SELECT MAX(" + DB.getColName(DB.T_T1, DB.C_T1_ID) + ") FROM " + DB.T_T1 + " ) ) "	
			;

    private static final String week_cnt_sql = 
		" SELECT " +
            "   ( SELECT " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + " + " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + " + " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + " + " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + " + " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) +
            "   FROM " + DB.T_T1 + 
			"	WHERE " + DB.getColName(DB.T_T1, DB.C_T1_ID) + " = (  SELECT MAX(" + DB.getColName(DB.T_T1, DB.C_T1_ID) + ")" +
																	" FROM " + DB.T_T1 + 
																	" WHERE " + DB.getColName(DB.T_T1, DB.C_T1_V1) + " IS NOT NULL ) "+ 
				" ) , " +
                "   ( SELECT ((" + DB.getColName(DB.T_T2, DB.C_T2_V) + " + 1) / 2) * " + DB.getColName(DB.T_T2, DB.C_T2_V) +
			" 	FROM " + DB.T_T2 + 
			" 	WHERE " + DB.getColName(DB.T_T2, DB.C_T2_ID) + " = ( SELECT MAX(" + DB.getColName(DB.T_T2, DB.C_T2_ID) + ")"+
																	" FROM " + DB.T_T2 +
                " WHERE " + DB.getColName(DB.T_T2, DB.C_T2_V) + " IS NOT NULL )" +
                " ) , " +
            "   ifNull(( SELECT " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V1) +
						" FROM " + DB.T_T3 + 
						" WHERE " + DB.getColName(DB.T_T3, DB.C_T3_ID)  + " = ( SELECT MAX(" + DB.getColName(DB.T_T3, DB.C_T3_ID) + ")"+
																			" FROM " + DB.T_T3 + 
																			" where " + DB.getColName(DB.T_T3, DB.C_T3_V1) + " IS NOT NULL ) )" +
				", 0) , " +
            "   ( SELECT " + DB.getColName(DB.T_T4, DB.C_T4_V) + " * " + DB.getColName(DB.T_TW, DB.C_TW_RS) +
                "	FROM " + DB.T_T4 + " INNER JOIN " + DB.T_TW + " ON " + DB.getColName(DB.T_T4, DB.C_T4_W) + " = " + DB.getColName(DB.T_TW, DB.C_TW_ID) +
                "	WHERE " + DB.getColName(DB.T_T4, DB.C_T4_ID) + " = ( SELECT MAX(" + DB.getColName(DB.T_T4, DB.C_T4_ID) + ") " +
                " FROM " + DB.T_T4 +
																	" WHERE " + DB.getColName(DB.T_T4, DB.C_T4_V) + " IS NOT NULL )" +
			") ";


    private static final String prev_week_info = 
		"SELECT " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + ", " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + ", " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + ", " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + ", " +
                DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) + ", " +
                " ( SELECT MAX(" + DB.getColName(DB.T_T2, DB.C_T2_V) + ") FROM " + DB.T_T2 + " ) , " +
                " ( SELECT MAX(" + DB.getColName(DB.T_T4, DB.C_T4_V) + ") FROM " + DB.T_T4 + " )  " +
        " FROM " + DB.T_T1 +
                " WHERE " + DB.getColName(DB.T_T1, DB.C_T1_V1) + " = ( SELECT MAX(" + DB.getColName(DB.T_T1, DB.C_T1_V1) + ") FROM " + DB.T_T1 + " )";

    private static final String current_week_chk_cnt =
            "SELECT " +
                    " ( SELECT " +
                    " CASE " +
                    " WHEN " + DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) + " > 0 THEN  5 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + " > 0 THEN  4 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + " > 0 THEN  3 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + " > 0 THEN  2 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + " > 0 THEN  1 " +
                    " ELSE 0 " +
                    " END " +
                    " FROM " + DB.T_T1 +
                    " WHERE " + DB.getColName(DB.T_T1, DB.C_T1_ID) + " = ( SELECT MAX(" + DB.getColName(DB.T_T1, DB.C_T1_ID) + ") FROM " + DB.T_T1 +
                                        " WHERE " + DB.getColName(DB.T_T1, DB.C_T1_W) + " = ? " +
                                        "    AND " + DB.getColName(DB.T_T1, DB.C_T1_DATE) + " = ? ) ) AS E1, " +

                    " ( SELECT " + DB.getColNameNvl(DB.T_T2, DB.C_T2_V) +
                    " FROM " + DB.T_T2 +
                    " WHERE " + DB.getColName(DB.T_T2, DB.C_T2_ID) + " = ( SELECT MAX(" + DB.getColName(DB.T_T2, DB.C_T2_ID) + ") FROM " + DB.T_T2
                    + " WHERE  " + DB.getColName(DB.T_T2, DB.C_T2_W) + " = ? " +
                    "                AND " + DB.getColName(DB.T_T2, DB.C_T2_DATE) + " = ?  ) ) AS E2, " +

                    " ( SELECT " +
                    " CASE " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V9) + " > 0 THEN  9 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V8) + " > 0 THEN  8 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V7) + " > 0 THEN  7 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V6) + " > 0 THEN  6 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V5) + " > 0 THEN  5 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V4) + " > 0 THEN  4 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V3) + " > 0 THEN  3 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V2) + " > 0 THEN  2 " +
                    " WHEN " + DB.getColNameNvl(DB.T_T3, DB.C_T3_V1) + " > 0 THEN  1 " +
                    " ELSE 0 " +
                    " END " +
                    " FROM " + DB.T_T3 +
                    " WHERE " + DB.getColName(DB.T_T3, DB.C_T3_ID) + " = ( SELECT MAX(" + DB.getColName(DB.T_T3, DB.C_T3_ID) + ") FROM " + DB.T_T3
                    + " WHERE " + DB.getColName(DB.T_T3, DB.C_T3_W) + " = ?" +
                    " AND " + DB.getColName(DB.T_T3, DB.C_T3_DATE) + " = ? ) ) AS E3, " +


                    " ( SELECT " + DB.getColNameNvl(DB.T_T4, DB.C_T4_V) +
                    " FROM " + DB.T_T4 +
                    " WHERE " + DB.getColName(DB.T_T4, DB.C_T4_ID) + " = ( SELECT MAX(" + DB.getColName(DB.T_T4, DB.C_T4_ID) + ") FROM " + DB.T_T4
                    + " WHERE " + DB.getColName(DB.T_T4, DB.C_T4_W) + " = ? " +
                    " AND  " + DB.getColName(DB.T_T4, DB.C_T4_DATE) + " = ?) ) AS E4, " +

                    " ( SELECT COUNT(*) FROM " + DB.T_T1 + " WHERE " + DB.getColName(DB.T_T1, DB.C_T1_W) + " = ? ) AS C1, " +
                    " ( SELECT COUNT(*) FROM " + DB.T_T2 + " WHERE " + DB.getColName(DB.T_T2, DB.C_T2_W) + " = ? ) AS C2, " +
                    " ( SELECT COUNT(*) FROM " + DB.T_T3 + " WHERE " + DB.getColName(DB.T_T3, DB.C_T3_W) + " = ? ) AS C3, " +
                    " ( SELECT COUNT(*) FROM " + DB.T_T4 + " WHERE " + DB.getColName(DB.T_T4, DB.C_T4_W) + " = ? ) AS C4 ";

    private static final String current_day_info =
            "SELECT " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V1) + " AS V1, " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V2) + " AS V2, " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V3) + " AS V3, " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V4) + " AS V4, " +
                    DB.getColNameNvl(DB.T_T1, DB.C_T1_V5) + " AS V5, " +
                    "0  AS V6, " +
                    "0  AS V7, " +
                    "0  AS V8, " +
                    "0  AS V9, " +
                    "1 as TP " +
                    " FROM " + DB.T_T1 +
                    " WHERE " + DB.getColName(DB.T_T1, DB.C_T1_DATE) + " = ?" +
                    " UNION ALL " +
                    "SELECT " +
                    DB.getColNameNvl(DB.T_T2, DB.C_T2_V) + " AS V1, " +
                    "0  AS V2, " +
                    "0  AS V3, " +
                    "0  AS V4, " +
                    "0  AS V5, " +
                    "0  AS V6, " +
                    "0  AS V7, " +
                    "0  AS V8, " +
                    "0  AS V9, " +
                    "2 as TP " +
                    " FROM " + DB.T_T2 +
                    " WHERE " + DB.getColName(DB.T_T2, DB.C_T2_DATE) + " = ?" +
                    " UNION ALL " +
                    "SELECT " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V1) + " AS V1, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V2) + " AS V2, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V3) + " AS V3, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V4) + " AS V4, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V5) + " AS V5, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V6) + " AS V6, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V7) + " AS V7, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V8) + " AS V8, " +
                    DB.getColNameNvl(DB.T_T3, DB.C_T3_V9) + " AS V9, " +
                    "3 as TP " +
                    " FROM " + DB.T_T3 +
                    " WHERE " + DB.getColName(DB.T_T3, DB.C_T3_DATE) + " = ?" +
                    " UNION ALL " +
                    "SELECT " +
                    "( SELECT " + DB.getColNameNvl(DB.T_TW, DB.C_TW_RS) +
                    " FROM " + DB.T_TW +
                    " WHERE  " + DB.getColName(DB.T_TW, DB.C_TW_ID) + " =  " + DB.getColName(DB.T_T4, DB.C_T4_W) +
                    "  )" + " AS V1, " +
                    DB.getColName(DB.T_T4, DB.C_T4_V) + " AS V2, " +
                    "0  AS V3, " +
                    "0  AS V4, " +
                    "0  AS V5, " +
                    "0  AS V6, " +
                    "0  AS V7, " +
                    "0  AS V8, " +
                    "0  AS V9, " +
                    "4 as TP " +
                    " FROM " + DB.T_T4 +
                    " WHERE " + DB.getColName(DB.T_T4, DB.C_T4_DATE) + " = ?";

    // region backup
    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    private static File getExtPath(Context context) {
        File extFile = null;
        final String BackupFileName = "backup.dat";
        File[] paths = ContextCompat.getExternalCacheDirs(context);
        if (paths.length > 0)
            extFile = new File(paths[0].getAbsolutePath(), BackupFileName);

        return extFile;
    }

    private static File getExtPathFree(Context context) {
        File extFile = null;
        final String BackupFileName = "backup.dat";
        File[] paths = ContextCompat.getExternalCacheDirs(context);
        if (paths.length > 0)
            extFile = new File(paths[0].getAbsolutePath().replace("full", "free"), BackupFileName);

        return extFile;
    }


    public static boolean BackupData(Context context) {
        try {
            copyFile(context.getDatabasePath(DB.DATABASE_NAME), getExtPath(context));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean RestoreData(Context context) {
        try {
            copyFile(getExtPath(context), context.getDatabasePath(DB.DATABASE_NAME));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean RestoreDataFree(Context context) {
        try {
            copyFile(getExtPathFree(context), getExtPath(context));
            RestoreData(context);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    // endregion backup

    public static long GetCurrentWeekId() {
        Cursor cursor = null;
        long res = -1;

        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int nw = calendar.get(Calendar.WEEK_OF_YEAR);

        SQLiteDatabase db = DB.getDBRead();
        try {
            cursor = db.query(DB.T_TW, new String[]{DB.C_TW_ID}
                    , DB.C_TW_Y + " = ? AND " + DB.C_TW_N + " = ? "
                    , new String[]{String.valueOf(year), String.valueOf(nw)}, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res = cursor.getLong(cursor.getColumnIndex(DB.C_TW_ID));
            }

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        if (res == -1) {
            try {
                db = DB.getDBWrite();
                ContentValues values = new ContentValues();
                values.put(DB.C_TW_Y, year);
                values.put(DB.C_TW_N, nw);

                res = db.insert(DB.T_TW, null, values);
            } finally {
                db.close();
            }
        }
        return res;
    }

    public static long GetCurrentTNId(ArmPrg.en_type prgType) {
        String colId = "";
        String colW = "";
        String tabName = "";
        String colDt = "";
        if (prgType == ArmPrg.en_type.max5) {
            tabName = DB.T_T1;
            colId = DB.C_T1_ID;
            colW = DB.C_T1_W;
            colDt = DB.C_T1_DATE;
        } else if (prgType == ArmPrg.en_type.pyramid) {
            tabName = DB.T_T2;
            colId = DB.C_T2_ID;
            colW = DB.C_T2_W;
            colDt = DB.C_T2_DATE;
        } else if (prgType == ArmPrg.en_type.grip) {
            tabName = DB.T_T3;
            colId = DB.C_T3_ID;
            colW = DB.C_T3_W;
            colDt = DB.C_T3_DATE;
        } else if (prgType == ArmPrg.en_type.maxSet) {
            tabName = DB.T_T4;
            colId = DB.C_T4_ID;
            colW = DB.C_T4_W;
            colDt = DB.C_T4_DATE;
        }

        Cursor cursor = null;
        long res = -1;

        long weekId = DBHelper.GetCurrentWeekId();

        SQLiteDatabase db = DB.getDBRead();

        try {
                db = DB.getDBRead();
                cursor = db.query(tabName, new String[]{colId}
                        , colW + " = ? "
                        , new String[]{String.valueOf(weekId)}, null, null, null);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    res = cursor.getLong(cursor.getColumnIndex(colId));
                }

        } finally {
                if (cursor != null)
                    cursor.close();
                db.close();
         }

        if (res == -1) {
            try {
                db = DB.getDBWrite();
                ContentValues values = new ContentValues();
                values.put(colW, weekId);
                values.put(colDt, DB.getDataStr(Calendar.getInstance().getTime()));
                res = db.insert(tabName, null, values);
            } finally {
                db.close();
            }
        }
        return res;
    }

    public static void saveMax5(long tId, int pos, int count) {
        SQLiteDatabase db = DB.getDBRead();
        try {
            ContentValues values = new ContentValues();
            if (pos == 1)
                values.put(DB.C_T1_V1, count);
            else if (pos == 2)
                values.put(DB.C_T1_V2, count);
            else if (pos == 3)
                values.put(DB.C_T1_V3, count);
            else if (pos == 4)
                values.put(DB.C_T1_V4, count);
            else if (pos == 5)
                values.put(DB.C_T1_V5, count);

            db.update(DB.T_T1, values, DB.C_T1_ID + " = ? ", new String[]{String.valueOf(tId)});

        } finally {
            db.close();
        }
    }

    public static void savePyramid(long tId, int count) {
        SQLiteDatabase db = DB.getDBRead();
        try {
            ContentValues values = new ContentValues();
            values.put(DB.C_T2_V, count);

            db.update(DB.T_T2, values, DB.C_T2_ID + " = ? ", new String[]{String.valueOf(tId)});

        } finally {
            db.close();
        }
    }

    public static void saveGrip(long tId, int pos, int count) {
        SQLiteDatabase db = DB.getDBRead();
        try {
            ContentValues values = new ContentValues();
            if (pos == 1)
                values.put(DB.C_T3_V1, count);
            else if (pos == 2)
                values.put(DB.C_T3_V2, count);
            else if (pos == 3)
                values.put(DB.C_T3_V3, count);
            else if (pos == 4)
                values.put(DB.C_T3_V4, count);
            else if (pos == 5)
                values.put(DB.C_T3_V5, count);
            else if (pos == 6)
                values.put(DB.C_T3_V6, count);
            else if (pos == 7)
                values.put(DB.C_T3_V7, count);
            else if (pos == 8)
                values.put(DB.C_T3_V8, count);
            else if (pos == 9)
                values.put(DB.C_T3_V9, count);

            db.update(DB.T_T3, values, DB.C_T3_ID + " = ? ", new String[]{String.valueOf(tId)});

        } finally {
            db.close();
        }
    }

    public static void saveMaxSet(long tId, int count) {
        SQLiteDatabase db = DB.getDBRead();
        try {
            ContentValues values = new ContentValues();
            values.put(DB.C_T4_V, count);

            db.update(DB.T_T4, values, DB.C_T4_ID + " = ? ", new String[]{String.valueOf(tId)});

        } finally {
            db.close();
        }
    }

    public static void saveWeekCnt(int count) {
        long weekId = DBHelper.GetCurrentWeekId();

        SQLiteDatabase db = DB.getDBRead();
        try {
            ContentValues values = new ContentValues();
            values.put(DB.C_TW_RS, count);

            db.update(DB.T_TW, values, DB.C_TW_ID + " = ? ", new String[]{String.valueOf(weekId)});

        } finally {
            db.close();
        }
    }

    public static int getWeekCnt(boolean last) {
        Cursor cursor = null;
        int res = 0;
        long weekId = DBHelper.GetCurrentWeekId();

        SQLiteDatabase db = DB.getDBRead();
        try {
            if (last) {
                cursor = db.query(DB.T_TW, new String[]{DB.C_TW_RS}
                        , DB.C_TW_ID + " = ( SELECT MAX(" + DB.C_TW_ID + ") FROM " + DB.T_TW + " WHERE " + DB.C_TW_RS + " IS NOT NULL ) "
                        , new String[]{}, null, null, null);
            } else {
                cursor = db.query(DB.T_TW, new String[]{DB.C_TW_RS}
                        , DB.C_TW_ID + " = ? "
                        , new String[]{String.valueOf(weekId)}, null, null, null);
            }


            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res = cursor.getInt(cursor.getColumnIndex(DB.C_TW_RS));
            }

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return res;
    }

    public static chkCntResult getChkCnt(boolean freeBase) {
        Cursor cursor = null;
        chkCntResult res = new chkCntResult();
        String weekId = String.valueOf(DBHelper.GetCurrentWeekId());

        SQLiteDatabase db = DB.getDBRead();
        try {
            String onDt =   DB.getDataStr(Calendar.getInstance().getTime()) ;

            cursor = db.rawQuery(current_week_chk_cnt,
                    new String[]{weekId, onDt,
                                weekId,  onDt,
                                weekId,  onDt,
                                weekId,  onDt,
                                weekId, weekId, weekId, weekId}
            );


            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res.maxCnt = cursor.getInt(0);
                res.pyramidCnt = cursor.getInt(1);
                res.gripCnt = cursor.getInt(2);
                res.maxSetsCnt = cursor.getInt(3);
                if (freeBase) {
                    if (cursor.getInt(4) == 1) res.maxCnt = 0;
                    if (cursor.getInt(5) == 1) res.pyramidCnt = 0;
                    if (cursor.getInt(6) == 1) res.gripCnt = 0;
                    if (cursor.getInt(7) == 1) res.maxSetsCnt = 0;
                }
            }

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return res;
    }



    static public Cursor GetReportMax5() {
        SQLiteDatabase db = DB.getDBRead();
        return db.rawQuery(SQL_REP_MAX5, new String[]{});
    }

    static public Cursor GetReportWeekTotal() {
        SQLiteDatabase db = DB.getDBRead();
        return db.rawQuery(SQL_REP_WEEK_TOTAL, new String[]{});
    }

    static public Cursor GetReportMaxCntDay() {
        SQLiteDatabase db = DB.getDBRead();
        return db.rawQuery(SQL_REP_MAX_CNT_DAY, new String[]{});
    }

    static public Cursor GetReportPyramidDay() {
        SQLiteDatabase db = DB.getDBRead();
        return db.rawQuery(SQL_REP_PYRAMID_DAY, new String[]{});
    }

    static public Cursor GetReportGripDay() {
        SQLiteDatabase db = DB.getDBRead();
        return db.rawQuery(SQL_REP_GRIP_DAY, new String[]{});
    }

    static public Cursor GetReportMaxSetDay() {
        SQLiteDatabase db = DB.getDBRead();
        return db.rawQuery(SQL_REP_MAX_SETS_DAY, new String[]{});
    }

    public static mainStatResult getMianStatistics() {
        mainStatResult res = new mainStatResult();
        SQLiteDatabase db = DB.getDBRead();
        try {
            Cursor cursor = db.rawQuery(main_stat_sql, new String[]{});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res.weeks = cursor.getInt(0);
                res.max = cursor.getInt(1);
                res.start = cursor.getInt(2);
                res.today = cursor.getInt(3);
            }

        } finally {
            db.close();
        }

        return res;

    }

    public static prevWeekCntResult getPrevWeekCnt() {
        prevWeekCntResult res = new prevWeekCntResult();
        SQLiteDatabase db = DB.getDBRead();
        try {
            Cursor cursor = db.rawQuery(week_cnt_sql, new String[]{});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res.maxSum = cursor.getInt(0);
                res.pyramidSum = cursor.getInt(1);
                res.first9 = cursor.getInt(2);
                res.maxSetsSum = cursor.getInt(3);
            }

        } finally {
            db.close();
        }

        return res;

    }

    public static PrevWeekInfo getPrevWeekInfo() {
        PrevWeekInfo res = new PrevWeekInfo();
        SQLiteDatabase db = DB.getDBRead();
        try {
            Cursor cursor = db.rawQuery(prev_week_info, new String[]{});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res.cnt1 = cursor.getInt(0);
                res.cnt2 = cursor.getInt(1);
                res.cnt3 = cursor.getInt(2);
                res.cnt4 = cursor.getInt(3);
                res.cnt5 = cursor.getInt(4);
                res.totalSetsPyramid = cursor.getInt(5);
                res.totalSetsMax = cursor.getInt(6);
            }

        } finally {
            db.close();
        }

        return res;
    }

    public static SendResInfo getSendReskInfo() {
        SendResInfo res = new SendResInfo();
        SQLiteDatabase db = DB.getDBRead();
        try {
            String onDt = DB.getDataStr(Calendar.getInstance().getTime());
            Cursor cursor = db.rawQuery(current_day_info, new String[]{onDt, onDt, onDt, onDt});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res.res_1 = cursor.getInt(0);
                res.res_2 = cursor.getInt(1);
                res.res_3 = cursor.getInt(2);
                res.res_4 = cursor.getInt(3);
                res.res_5 = cursor.getInt(4);
                res.res_6 = cursor.getInt(5);
                res.res_7 = cursor.getInt(6);
                res.res_8 = cursor.getInt(7);
                res.res_9 = cursor.getInt(8);
                if (cursor.getInt(9) == 1)
                    res.tp = ArmPrg.en_type.max5;
                else if (cursor.getInt(9) == 2)
                    res.tp = ArmPrg.en_type.pyramid;
                else if (cursor.getInt(9) == 3)
                    res.tp = ArmPrg.en_type.grip;
                else if (cursor.getInt(9) == 4)
                    res.tp = ArmPrg.en_type.maxSet;
            }

        } finally {
            db.close();
        }

        //test
/*
        res.tp = ArmPrg.en_type.grip;
        res.res_1 = 10;
        res.res_2 = 10;
        res.res_3 = 10;
        res.res_4 = 8;
        res.res_5 = 8;
        res.res_6 = 5;
        res.res_7 = 8;
        res.res_8 = 8;
        res.res_9 = 8;
*/
        return res;
    }


    public static class chkCntResult {
        int maxCnt;
        int pyramidCnt;
        int gripCnt;
        int maxSetsCnt;

    }

    public static class prevWeekCntResult {
        int maxSum;
        int pyramidSum;
        int first9;
        int maxSetsSum;

    }

    public static class mainStatResult {
        int weeks;
        int max;
        int start;
        int today;
    }

    public static class PrevWeekInfo {
        int totalSetsPyramid;
        int totalSetsMax;
        int cnt1;
        int cnt2;
        int cnt3;
        int cnt4;
        int cnt5;
    }

    public static class SendResInfo {
        public ArmPrg.en_type tp;

        public int res_1;
        public int res_2;
        public int res_3;

        public int res_4;
        public int res_5;
        public int res_6;

        public int res_7;
        public int res_8;
        public int res_9;
    }
}

