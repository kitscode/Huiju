package kevinz.huiju.ui.history;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.adapter.HistoryAdapter;
import kevinz.huiju.bean.history.HistoryBean;
import kevinz.huiju.bean.history.HistoryDetails;
import kevinz.huiju.net.HistoryRetrofit;
import kevinz.huiju.utils.CONSTANT;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HistoryFragment extends Fragment {

    private ExpandableListView expandableListView;
    private HistoryAdapter adapter;
    private List<HistoryDetails> list=new ArrayList<>();
    private TextView date_title;
    private String month;
    private String day;
    private int num;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);

        getDate();
        loadFromInternet();
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        initView();
        return view;
    }


    private void initView() {
        adapter = new HistoryAdapter(getContext(),list);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null); // 去掉默认带的箭头

    }


    protected void loadFromInternet() {
        String baseUrl = "http://api.juheapi.com/japi/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HistoryRetrofit historyRetrofit = retrofit.create(HistoryRetrofit.class);
        Call<HistoryBean> call = historyRetrofit.getHistoryInfo(month,day);
        call.enqueue(new Callback<HistoryBean>() {
            @Override
            public void onResponse(Call<HistoryBean> call, Response<HistoryBean> response) {
                HistoryDetails[] details=response.body().getResult();
                for (HistoryDetails data:details){
                    list.add(data);
                }
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_SUCCESS);
            }

            @Override
            public void onFailure(Call<HistoryBean> call, Throwable t) {
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_FAILURE);
            }
        });
    }
    protected void getDate() {
        SimpleDateFormat monthF = new SimpleDateFormat ("M");
        SimpleDateFormat dayF = new SimpleDateFormat ("d");
        Date currentDate = new Date();
        month = monthF.format(currentDate);
        day = dayF.format(currentDate);
    }
    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.LOAD_DATA_SUCCESS:
                    break;
            }
            adapter.notifyDataSetChanged();
            num = expandableListView.getCount();
            for (int i = 0; i < num; i++) {
                expandableListView.expandGroup(i);
            }
            date_title.setText("历史上"+month+"月"+day+"日有"+num+"件重要的事");
          /*  new AlertDialog.Builder(getContext())
                    .setMessage("今天是"+str_month+"月"+str_day+"日\n"
                            +"历史上的今天共发生过\n"+num+"件\n值得纪念的事")
                    .create()
                    .show();*/
            return false;
        }
    });

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        date_title=(TextView)getActivity().findViewById(R.id.date_title);
        date_title.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        date_title.setVisibility(View.GONE);
    }
}
