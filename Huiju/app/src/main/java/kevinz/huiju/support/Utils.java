package kevinz.huiju.support;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;

import java.util.Locale;

import kevinz.huiju.HuijuApplication;

/**
 * Created by Administrator on 2016/11/5.
 */

public class Utils {
    private static boolean DEBUG = true;
    private static Context mContext = HuijuApplication.AppContext;

    public static void DLog(String text){
        if(DEBUG) {
            Log.d("调试信息：", text);
        }
    }

    public static int getCurrentLanguage(){
        int lang = new Settings().getInt(Settings.LANGUAGE,-1);
        if (lang == -1) {
            String language = Locale.getDefault().getLanguage();

            if (language.equalsIgnoreCase("zh")) {
              lang = 1;
            } else {
                lang = 0;
            }
        }
        return lang;
    }
    // Must be called before setContentView()
    public static void changeLanguage(Context context, int lang) {
        String language = null;
        String country = null;

        switch (lang) {
            case 1:
                language = "zh";
                country = "CN";
                break;
            default:
                language = "en";
                country = "US";
                break;
        }

        Locale locale = new Locale(language,country);
        Configuration conf = context.getResources().getConfiguration();
        conf.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
    }

    public static final int MAX_BRIGHTNESS = 255;
    public static int getSysScreenBrightness() {
        int screenBrightness = MAX_BRIGHTNESS;
        try {
            screenBrightness = android.provider.Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            Utils.DLog("获得当前系统的亮度值失败：");
        }
        return screenBrightness;
    }

    /**
     * 设置当前系统的亮度值:0~255
     */
    public static void setSysScreenBrightness(int brightness) {
        try {
            ContentResolver resolver = mContext.getContentResolver();
            Uri uri = android.provider.Settings.System.getUriFor(android.provider.Settings.System.SCREEN_BRIGHTNESS);
            android.provider.Settings.System.putInt(resolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
            resolver.notifyChange(uri, null); // 实时通知改变
        } catch (Exception e) {
            Utils.DLog("设置当前系统的亮度值失败："+e);
        }
    }

}
