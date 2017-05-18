package kevinz.huiju.ui.base;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.phoenix.PullToRefreshView;

import kevinz.huiju.HuijuApplication;
import kevinz.huiju.R;
import kevinz.huiju.utils.CONSTANT;
import kevinz.huiju.utils.Settings;


public abstract class BaseDataFragment extends Fragment {

    protected RecyclerView.Adapter adapter;
    protected RecyclerView recyclerView;
    PullToRefreshView refreshView;
    protected Button search;
    protected abstract int getContentId();
    protected abstract void loadFromInternet();
    protected abstract void bindAdapter();
    protected abstract boolean needCache();
    protected void loadFromCache(){}
    protected void cache(){}

    private View thisView;
    private TextView pullToRefresh;
    private boolean autoRefresh=Settings.getBoolean(Settings.AUTO_REFRESH, false);
    private boolean notify_load_success;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(getContentId(),container,false);
        pullToRefresh=(TextView)thisView.findViewById(R.id.pull_to_refresh);
        if (autoRefresh){
            loadFromInternet();
        }else {
            loadFromCache();
        }
        setPullToRefresh();
        bindAdapter();
        return thisView;
    }

    private void setPullToRefresh(){
        recyclerView = (RecyclerView) thisView.findViewById(R.id.recyclerView);
        refreshView = (PullToRefreshView) thisView.findViewById(R.id.refreshView);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFromInternet();
                notify_load_success=true;
            }
        });

    }

    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.LOAD_DATA_SUCCESS:
                    if(notify_load_success){
                        Toast.makeText(getContext(),R.string.refresh_success,Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                    refreshView.setRefreshing(false);
                    pullToRefresh.setVisibility(View.GONE);
                    if(needCache()){
                        cache();
                    }
                    break;
                case CONSTANT.LOAD_DATA_FAILURE:
                    Toast.makeText(HuijuApplication.AppContext,R.string.refresh_fail,Toast.LENGTH_SHORT).show();
                    refreshView.setRefreshing(false);
                    break;
            }
            return false;
        }
    });


}
