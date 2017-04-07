package kevinz.huiju.ui.guoke;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.adapter.GuokeAdapter;
import kevinz.huiju.bean.guoke.ArticleBean;
import kevinz.huiju.bean.guoke.GuokeBean;
import kevinz.huiju.db.DBHelper;
import kevinz.huiju.retrofit.GuokeRetrofit;
import kevinz.huiju.utils.CONSTANT;
import kevinz.huiju.ui.base.BaseDataFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GuokeFragment extends BaseDataFragment{

    RecyclerView.LayoutManager layoutManager;
    List<ArticleBean> articleBean = new ArrayList<>();

    @Override
    protected boolean needCache() {
        return true;
    }

    @Override
    public int getContentId(){
        return R.layout.recycler;
    }

    protected void loadFromCache(){
        SQLiteDatabase db = new DBHelper(getContext(),"huiju",null,1).getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from guoke where tag=?",
                new String[]{getArguments().getString("channel")});
        articleBean.clear();
        while(cursor.moveToNext()){
            ArticleBean article=new ArticleBean();
            article.setTitle(cursor.getString(0));
            ArticleBean.Image_info image_info=article.new Image_info();
            image_info.setUrl(cursor.getString(1));
            article.setImage_info(image_info);
            article.setUrl(cursor.getString(2));
            articleBean.add(article);
        }
    }

    @Override
    public void loadFromInternet(){
        articleBean.clear();
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
                    handler.sendEmptyMessage(CONSTANT.LOAD_DATA_FAILURE);
                    return;
                }
               ArticleBean[] bean = response.body().getResult();
                for(ArticleBean data : bean){
                    articleBean.add(data);
                }
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_SUCCESS);
            }

            @Override
            public void onFailure(Call<GuokeBean> call, Throwable t) {
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_FAILURE);
            }
        });
    }

    @Override
    public void bindAdapter() {
        adapter = new GuokeAdapter(getContext(),articleBean);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void cache() {
        SQLiteDatabase db = new DBHelper(getContext(),"huiju",null,1).getWritableDatabase();
        ContentValues values=new ContentValues();
        for(ArticleBean article:articleBean){
            values.put("title",article.getTitle());
            values.put("image",article.getImage_info().getUrl());
            values.put("url",article.getUrl());
            values.put("tag",getArguments().getString("channel"));
            db.insert("guoke",null,values);
        }
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
