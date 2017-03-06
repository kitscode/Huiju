package kevinz.huiju.data;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseData<T> {
    protected Handler mhandler;
    String murl;
    List<T> list = new ArrayList<>();

    public BaseData(Handler handler) {
        loadFromNet();
        mhandler=handler;
    }
    public BaseData(Handler handler,String url) {
        mhandler=handler;
        murl=url;
        loadFromNet();
    }
    abstract void loadFromNet();

    public List<T> getList() {
        return list;
    }

    public boolean hasData() {
        return !list.isEmpty();
    }
}
