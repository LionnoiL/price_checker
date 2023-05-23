package ua.gaponov.pricecheker.main;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Helpers {
    public static final int IDM_OPTIONS = 101;

    public static Context context;
    public static SharedPreferences mSharedPref;
    public static String host;
    public static String c1UserName;
    public static String c1UserPassword;
    public static Boolean useVideo;
    public static int videoPauseTime;

    public static void getOptions() {
        mSharedPref = getDefaultSharedPreferences(context);
        host = mSharedPref.getString("HOSTNAME", "");
        c1UserName = mSharedPref.getString("USERNAME", "");
        c1UserPassword = mSharedPref.getString("USERPASS", "");
        useVideo = mSharedPref.getBoolean("USEVIDEO", false);
        videoPauseTime = Integer.parseInt(mSharedPref.getString("VIDEOPAUSETIME", "60"));
    }
}
