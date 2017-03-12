package kevinz.huiju.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yalantis.phoenix.PullToRefreshView;

import kevinz.huiju.R;
import kevinz.huiju.adapter.CollectAdapter;
import kevinz.huiju.db.DBHelper;

/**
 * Created by Administrator on 2016/11/6.
 */

public class CollectionActivity extends AppCompatActivity{
    Cursor cursor;
    Toolbar toolbar;
    PullToRefreshView refreshView;
    CollectAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(R.string.collection);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshView = (PullToRefreshView)findViewById(R.id.refreshView);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView.setRefreshing(false);
            }
        });

        RecyclerView view = (RecyclerView) findViewById(R.id.recyclerView);
        cursor = getCollection();
        adapter = new CollectAdapter(cursor,this);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);
    }

    private Cursor getCollection(){
        SQLiteDatabase db = new DBHelper(this,"huiju",null,1).getWritableDatabase();
        return db.rawQuery("select * from collection",null);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
}
