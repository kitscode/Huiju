package kevinz.huiju.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;


public class Utils {
    private static boolean DEBUG = true;

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


}
