package kevinz.huiju.ui.guoke;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.facebook.drawee.view.SimpleDraweeView;

import kevinz.huiju.R;
import kevinz.huiju.bean.guoke.ArticleBean;
import kevinz.huiju.database.DataBaseHelper;
import kevinz.huiju.support.DisplayUtil;
import kevinz.huiju.ui.base.BaseDetailsActivity;

/**
 * Created by Administrator on 2016/10/28.
 */

public class GuokeDetailsActivity extends BaseDetailsActivity {
    private ArticleBean articleBean;
    protected SimpleDraweeView topImage;
    ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleBean = (ArticleBean) getIntent().getSerializableExtra("article");
        topImage = (SimpleDraweeView)findViewById(R.id.topImage);
        scrollView = (ScrollView)findViewById(R.id.scroll);
        initView();
        topImage.setImageURI(Uri.parse(articleBean.getImage_info().getUrl()));
    }

    @Override
    protected void initView() {
        super.initView();
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                topImage.setTranslationY(Math.max(-scrollY / 2, -DisplayUtil.dip2px(getBaseContext(), 170)));
            }
        });
        contentView.loadUrl(articleBean.getUrl());
    }


    @Override
    protected String getShareInfo() {
        return "["+articleBean.getTitle()+"]:"+articleBean.getUrl()+"(分享自汇聚)";
    }

    @Override
    protected void addToCollection() {
        DataBaseHelper helper = new DataBaseHelper(this,"huiju",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title",articleBean.getTitle());
        values.put("image",articleBean.getImage_info().getUrl());
        values.put("description",articleBean.getSummary());
        values.put("url",articleBean.getUrl());
        values.put("ifcollected",1);
        db.insert("collections",null,values);

    }
}
