package kevinz.huiju.ui.video;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.adapter.VideoAdapter;
import kevinz.huiju.bean.video.Videobean;
import kevinz.huiju.retrofit.VideoRetrofit;
import kevinz.huiju.utils.CONSTANT;
import kevinz.huiju.ui.base.BaseDataFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VideoFragment extends BaseDataFragment{

    List<Videobean.Details> res = new ArrayList<>();



    @Override
    protected int getContentId() {
        return R.layout.recycler;
    }

    @Override
    protected boolean needCache() {
        return false;
    }

    @Override
    protected void loadFromInternet() {
        res.clear();
        String baseUrl = "http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VideoRetrofit videoRetrofit = retrofit.create(VideoRetrofit.class);
        Call<Videobean> call = videoRetrofit.getVideoInfo();
        call.enqueue(new Callback<Videobean>() {
            @Override
            public void onResponse(Call<Videobean> call, Response<Videobean> response) {
                if (!response.isSuccessful()) {
                    handler.sendEmptyMessage(CONSTANT.LOAD_DATA_FAILURE);
                    return;
                }
                Videobean.Details[] detailses = response.body().getV9LG4B3A0();
                for( Videobean.Details data : detailses){
                    res.add(data);
                }
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_SUCCESS);
            }

            @Override
            public void onFailure(Call<Videobean> call, Throwable t) {

            }
        });
    }


    @Override
    public void bindAdapter() {
        adapter = new VideoAdapter(getContext(),res);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
    }




    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space=space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
            }
        }
    }
}
