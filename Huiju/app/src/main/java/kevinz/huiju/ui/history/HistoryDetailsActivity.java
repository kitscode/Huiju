package kevinz.huiju.ui.history;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import kevinz.huiju.HuijuApplication;
import kevinz.huiju.R;
import kevinz.huiju.bean.history.ArticleBean;
import kevinz.huiju.bean.history.ArticleDetails;
import kevinz.huiju.db.DBHelper;
import kevinz.huiju.ui.base.BaseDetailsActivity;
import kevinz.huiju.utils.CONSTANT;
import kevinz.huiju.utils.DisplayUtil;


public class HistoryDetailsActivity extends BaseDetailsActivity {
    protected SimpleDraweeView topImage;
    ScrollView scrollView;
    String mUrl;
    ArticleDetails[] articleDetailses;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getStringExtra("url");
        topImage = (SimpleDraweeView)findViewById(R.id.topImage);
        scrollView = (ScrollView)findViewById(R.id.scroll);
        findViewById(R.id.cover).setVisibility(View.GONE);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        onDataRefresh();
    }

    protected void onDataRefresh() {
        Request request = new Request.Builder()
                .url(mUrl)
                .build();
        OkHttpClient client = new OkHttpClient();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }
                String res = response.body().string();
                Gson gson = new Gson();
                ArticleBean articleBean = gson.fromJson(res, ArticleBean.class);
                articleDetailses = articleBean.getResult();
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_SUCCESS);

            }
        });

    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case CONSTANT.LOAD_DATA_SUCCESS:
                    scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            topImage.setTranslationY(Math.max(-scrollY / 2, -DisplayUtil.dip2px(getBaseContext(), 170)));
                        }
                    });

                    contentView.loadDataWithBaseURL(null,articleDetailses[0].getContent(), "text/html", "utf-8", null);

                    if(articleDetailses[0].getPic()!="") {
                       topImage.setImageURI(Uri.parse(articleDetailses[0].getPic()));
                   }else {
                      topImage.setImageResource(R.drawable.history_default);
                      topImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                  }
                    break;
            }
            return false;
        }
    });

    @Override
    protected String getShareInfo() {
        return "["+articleDetailses[0].getTitle()+"]:"+articleDetailses[0].getContent()+"(分享自汇聚)";
    }

    @Override
    protected void addToCollection() {
        DBHelper helper = new DBHelper(HuijuApplication.AppContext,"huiju",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",articleDetailses[0].getTitle());
        values.put("image",articleDetailses[0].getPic());
        values.put("description",articleDetailses[0].getContent());
        values.put("ifcollected",1);
        db.insert("collection",null,values);
    }
}
