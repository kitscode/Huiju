package kevinz.huiju;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import kevinz.huiju.ui.guoke.NaviFragment;
import kevinz.huiju.ui.history.HistoryFragment;
import kevinz.huiju.ui.home.SettingsFragment;
import kevinz.huiju.ui.video.VideoFragment;
import kevinz.huiju.utils.Settings;
import kevinz.huiju.utils.Utils;
import kevinz.huiju.utils.rearrange_tabs.DraggableGridViewSampleActivity;


public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private int lang;
    private boolean needQuit = false;
    private FrameLayout fl;

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
        initBottomMenu();
        fragment = new NaviFragment();
        changeFragment();
        setRearrangeBtn();
    }

    //解决home页面的标题距离问题


    private void setRearrangeBtn() {
        findViewById(R.id.rearrange_tabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DraggableGridViewSampleActivity.class));
            }
        });
    }

    private void changeFragment() {
        getFragmentManager().beginTransaction().replace(R.id.main_list, fragment).commit();
    }

    private void initBottomMenu() {
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
                        fragment = new NaviFragment();
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
                        e.printStackTrace();
                    }
                    needQuit = false;
                }

            }.start();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}


