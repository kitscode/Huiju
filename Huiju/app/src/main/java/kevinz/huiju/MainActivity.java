package kevinz.huiju;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import kevinz.huiju.database.DataBaseHelper;
import kevinz.huiju.support.Settings;
import kevinz.huiju.support.Utils;
import kevinz.huiju.ui.guoke.GuokeNaviFragment;
import kevinz.huiju.ui.history.HistoryFragment;
import kevinz.huiju.ui.home.SettingsFragment;
import kevinz.huiju.ui.video.VideoFragment;


/**
 * Created by Administrator on 2016/10/19.
 */

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Fragment fragment;

    static String content;
    TextView textView;
    int lang;
    TabLayout tabLayout;
    private boolean needQuit = false;

    private Settings mSettings = new Settings();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Utils.getCurrentLanguage();
        Settings.isNightMode = mSettings.getBoolean(Settings.NIGHT_MODE, false);
        if(lang > -1){
            Utils.changeLanguage(this,lang);
        }
        setContentView(R.layout.main_layout);

     /*   if(Settings.isNightMode){
            this.setTheme(R.style.NightTheme);
            findViewById(R.id.tab_layout).setBackgroundResource(R.color.night_primary);
        }else{
            this.setTheme(R.style.DayTheme);
        }*/

        initBottomMenu();
        fragment = new GuokeNaviFragment();
        changeFragment();
        DataBaseHelper helper = new DataBaseHelper(this,"huiju",null,1);
        helper.getWritableDatabase();
    }

    void changeFragment() {
        getFragmentManager().beginTransaction().replace(R.id.main_list, fragment).commit();
    }

    void initBottomMenu() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_news, R.string.navi_news))
                .addItem(new BottomNavigationItem(R.drawable.ic_video, R.string.navi_video))
                .addItem(new BottomNavigationItem(R.drawable.ic_history, R.string.navi_history))
                .addItem(new BottomNavigationItem(R.drawable.ic_home, R.string.navi_home))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        fragment = new GuokeNaviFragment();
                        changeFragment();
                        break;
                    case 1:
                        fragment = new VideoFragment();
                        changeFragment();
                        break;
                    case 2:
                        fragment = new HistoryFragment();
                        changeFragment();
                        break;
                    case 3:
                        fragment = new SettingsFragment();
                        changeFragment();
                        break;
                }

            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(Settings.needRecreate) {
            Settings.needRecreate = false;
            this.recreate();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(needQuit){
            super.onBackPressed();
        }else{
            needQuit = true;
            Snackbar.make(getCurrentFocus(),R.string.press_again,Snackbar.LENGTH_SHORT).show();
            new Thread(){
                public void run() {

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    needQuit = false;
                }

            }.start();
        }

    }
}


