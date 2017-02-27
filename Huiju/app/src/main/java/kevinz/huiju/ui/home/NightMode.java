package kevinz.huiju.ui.home;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;


/**
 * Created by Administrator on 2016/8/14.
 */
public class NightMode extends Service {
    private LinearLayout layout;
    private MyBinder mbind = new MyBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager windowManager = (WindowManager)getApplication().getSystemService(Context.WINDOW_SERVICE);
        LayoutParams params = new LayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        params.type = LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.flags = LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.CENTER;
        layout = new LinearLayout(getApplicationContext());
        layout.setBackgroundColor(0x99000000);
        windowManager.addView(layout,params);

    }
    public IBinder onBind(Intent intent) {
        return mbind;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        WindowManager localWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        localWindowManager.removeView(layout);
    }

    public class MyBinder extends Binder{
        public NightMode getService(){
            return NightMode.this;
        }
    }
}
