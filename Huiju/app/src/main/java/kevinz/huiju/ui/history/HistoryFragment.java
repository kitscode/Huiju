package kevinz.huiju.ui.history;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.adapter.HistoryAdapter;
import kevinz.huiju.api.HistoryApi;
import kevinz.huiju.bean.history.HistoryDetails;
import kevinz.huiju.data.HistoryData;
import kevinz.huiju.support.CONSTANT;


public class HistoryFragment extends Fragment {

    private ExpandableListView expandableListView;
    private HistoryAdapter adapter;
    private HistoryData historyData;
    private List<HistoryDetails> list;
    private String mUrl;
    String str_month;
    String str_day;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);

        getUrl();

        historyData = new HistoryData(handler,mUrl);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        initView();

        return view;
    }


    private void initView() {
        list = initData();
        adapter = new HistoryAdapter(getContext(), list);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null); // 去掉默认带的箭头
    }

    List<HistoryDetails> initData() {
        list = historyData.getList();
        return list;
    }
    protected void getUrl() {
        SimpleDateFormat month = new SimpleDateFormat ("MM");
        SimpleDateFormat day = new SimpleDateFormat ("d");
        Date currentDate = new Date();
        str_month = month.format(currentDate);
        str_day = day.format(currentDate);
        mUrl = HistoryApi.history_url+str_month+HistoryApi.day+str_day;

    }
    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.LOAD_DATA_SUCCESS:
                    break;
            }
            adapter.notifyDataSetChanged();
            int num = expandableListView.getCount();
            for (int i = 0; i < num; i++) {
                expandableListView.expandGroup(i);
            }
            new AlertDialog.Builder(getContext())
                    .setMessage("今天是"+str_month+"月"+str_day+"日\n"
                            +"历史上的今天共发生过\n"+num+"件\n值得纪念的事")
                    .create()
                    .show();
            return false;
        }
    });

}
