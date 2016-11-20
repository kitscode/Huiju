package kevinz.huiju.ui.guoke;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.adapter.GuokeAdapter;
import kevinz.huiju.bean.guoke.ArticleBean;
import kevinz.huiju.bean.guoke.GuokeBean;
import kevinz.huiju.http.GuokeRetrofit;
import kevinz.huiju.support.CONSTANT;
import kevinz.huiju.ui.base.BaseDataFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/10/19.
 */

public class GuokeFragment extends BaseDataFragment{

    RecyclerView.LayoutManager layoutManager;
    List<ArticleBean> articleBeen = new ArrayList<>();


    @Override
    public int getContentId(){
        return R.layout.recycler;
    }

    @Override
    public void getData(){
        String channel = getArguments().getString("channel");
        String baseUrl = "http://www.guokr.com/apis/minisite/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GuokeRetrofit guokeRetrofit = retrofit.create(GuokeRetrofit.class);
        Call<GuokeBean> call = guokeRetrofit.getGuokeInfo(channel);
        call.enqueue(new Callback<GuokeBean>() {
            @Override
            public void onResponse(Call<GuokeBean> call, Response<GuokeBean> response) {
                if (!response.isSuccessful()) {
                    handler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }
               ArticleBean[] bean = response.body().getResult();
                for(ArticleBean data : bean){
                    articleBeen.add(data);
                }
                handler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }

            @Override
            public void onFailure(Call<GuokeBean> call, Throwable t) {
                handler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }
        });
    }

    @Override
    public void bindAdapter() {
        adapter = new GuokeAdapter(getContext(),articleBeen);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.floatButton);
//        floatingActionButton.setVisibility(View.VISIBLE);
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        floatingActionButton.setVisibility(View.GONE);
//    }
}
