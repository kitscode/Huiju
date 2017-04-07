package kevinz.huiju.utils;

import android.content.Context;
import android.content.SharedPreferences;

import kevinz.huiju.HuijuApplication;


public class Settings {
    public static  boolean needRecreate = false;
    public static boolean isNightMode = false;
    public static boolean noPicMode = false;
    public static boolean autoRefresh = false;

    public static final String XML_NAME = "kevinz.huiju_preferences";
    public static final String LANGUAGE = "language";
    public static final String NIGHT_MODE = "night_mode";
    public static final String AUTO_REFRESH = "auto_refresh";
    public static final String NO_PICS = "no_pics";

    private static SharedPreferences mPrefs;

    public Settings() {
        mPrefs = HuijuApplication.AppContext.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
    }
    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public Settings putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
        return this;
    }
    public Settings putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
        return this;
    }

    public static boolean getBoolean(String key, boolean def) {
        return mPrefs.getBoolean(key, def);
    }
}
