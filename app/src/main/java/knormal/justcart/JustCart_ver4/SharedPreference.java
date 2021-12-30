package knormal.justcart.JustCart_ver4;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference {
    static final String PREF_USER_ID = "userID";
    static final String PREF_USER_PW = "userPass";
    static final String PREF_USER_NAME = "userName";
    static final String PREF_USER_EMAIL = "userEmail";
    static final String PREF_ORDER_ID = "order_id";
    static final String PREF_TOTAL_PRICE = "total_price";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserID(Context ctx, String userID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.commit();
    }
    public static void setUserPass(Context ctx, String userPass) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PW, userPass);
        editor.commit();
    }
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }
    public static void setUserEmail(Context ctx, String userEmail) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, userEmail);
        editor.commit();
    }

    public static void setOrderID(Context ctx, String order_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ORDER_ID, order_id);
        editor.commit();
    }

    public static void setTotalPrice(Context ctx, String total_price) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOTAL_PRICE, total_price);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static String getUserEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }
    public static String getOrderID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_ORDER_ID, "");
    }
    public static String getTotalPrice(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_TOTAL_PRICE, "");
    }

    // 로그아웃
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

}