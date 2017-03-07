package kevinz.huiju.ui.base;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yalantis.phoenix.PullToRefreshView;

import kevinz.huiju.HuijuApplication;
import kevinz.huiju.R;
import kevinz.huiju.support.CONSTANT;


public abstract class BaseDataFragment extends Fragment {

    protected RecyclerView.Adapter adapter;
    protected RecyclerView recyclerView;
    PullToRefreshView refreshView;
    protected FloatingActionButton floatingActionButton;
    protected abstract int getContentId();
    protected abstract void getData();
    protected abstract void bindAdapter();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentId(),container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        getData();
        bindAdapter();

        refreshView = (PullToRefreshView) view.findViewById(R.id.refreshView);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                Toast.makeText(getContext(),"已是最新数据",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.ID_SUCCESS:
                    adapter.notifyDataSetChanged();
                    refreshView.setRefreshing(false);
                    break;
                case CONSTANT.ID_FAILURE:
                    Toast.makeText(HuijuApplication.AppContext,"数据获取失败,请刷新重试",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });
}
