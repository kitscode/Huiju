package kevinz.huiju.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import kevinz.huiju.R;


public abstract class BaseDetailsActivity extends AppCompatActivity{

    protected Toolbar toolbar;
    protected WebView contentView;
    protected NestedScrollView scrollView;
    protected FrameLayout mainContent;
    protected int isCollected = 0;
    protected View cover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
    }

    protected int getLayoutID(){
        return R.layout.details_activity;
    }


    protected void initView() {

        mainContent = (FrameLayout) findViewById(R.id.main_content);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        contentView = (WebView) findViewById(R.id.content_view);
        cover = findViewById(R.id.cover);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu,menu);
        updateCollectionMenu(menu.findItem(R.id.collect));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getShareInfo());
            startActivity(Intent.createChooser(sharingIntent,getString(R.string.share_to)));
        }else if(item.getItemId() == R.id.collect){
            if(isCollected == 1){
                removeFromCollection();
                isCollected = 0;
                updateCollectionMenu(item);
                Toast.makeText(getBaseContext(),"已从收藏删除",Toast.LENGTH_SHORT).show();
            }else {
                 addToCollection();
                isCollected = 1;
                updateCollectionMenu(item);
                Toast.makeText(getBaseContext(),"添加到收藏",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void updateCollectionMenu(MenuItem item){
        if(isCollected == 1){
            item.setIcon(R.mipmap.star_black);
        }else {
            item.setIcon(R.mipmap.star_white);
        }
    }

    protected abstract String getShareInfo();
    protected abstract void addToCollection();
    protected void removeFromCollection(){};
}
