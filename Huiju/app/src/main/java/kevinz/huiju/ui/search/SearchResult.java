package kevinz.huiju.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kevinz.huiju.R;
import kevinz.huiju.utils.CONSTANT;


/**
 * Created by Administrator on 2017/4/17.
 */

public class SearchResult extends AppCompatActivity {
    String res;
    List<String> titles=new ArrayList<>();
    List<String> urls=new ArrayList<>();
    ListView result_list;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Button search=(Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord=((EditText)findViewById(R.id.key_word)).getText().toString();
                searchArticle(keyWord);
            }
        });
        result_list=(ListView)findViewById(R.id.result_list);
        adapter=new ArrayAdapter(this,R.layout.search_result_text,titles);
        result_list.setAdapter(adapter);
        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(SearchResult.this,ResultArticle.class);
                    intent.putExtra("url",urls.get(position));
                    startActivity(intent);
            }
        });
    }


    private void searchArticle(String key) {
        Request request = new Request.Builder()
                .url("http://www.guokr.com/search/all/?wd="+key)
                .build();
        OkHttpClient client = new OkHttpClient();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                res = response.body().string();
                handler.sendEmptyMessage(CONSTANT.LOAD_DATA_SUCCESS);
            }
        });

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.LOAD_DATA_SUCCESS:
                    parseHtml(res);
                    adapter.notifyDataSetChanged();
                    break;
                case CONSTANT.LOAD_DATA_FAILURE:
                    Log.d("??","fail");
                    break;
            }
            return false;
        }
    });

    private void parseHtml(String html){
        titles.clear();
        urls.clear();
        Pattern pattern= Pattern.compile("<h2>.*</h2>");
        Pattern pattern2= Pattern.compile("href.*");
        Pattern pattern3= Pattern.compile("[\\u4e00-\\u9fa5]");
        Pattern pattern4= Pattern.compile("\\/[^\\/]*\\/\\d*\\/");
        List<String> h2List=new ArrayList<>();
        Matcher matcher = pattern.matcher(html);
        while(matcher.find())
            h2List.add(matcher.group());

        //存放标题
        for(int i=0;i<h2List.size();i++){
            Matcher matcher2 = pattern2.matcher(h2List.get(i));
            Matcher matcher4=null;
            //href="/post/117843/"><strong>单</strong><strong>车</strong>旅行</a></h2>
            while(matcher2.find()){
                List<String> pre_title=new ArrayList<>();
                pre_title.add(matcher2.group());
                for(int j=0;j<pre_title.size();j++){
                    StringBuffer title=new StringBuffer();
                    Matcher matcher3 = pattern3.matcher(pre_title.get(j));
                    while(matcher3.find()){
                        title.append(matcher3.group());
                    }
                    titles.add(title.toString());
                }
               matcher4=pattern4.matcher(matcher2.group());
            }
            while(matcher4.find()){
                urls.add(matcher4.group());
            }
        }
       for(String t:titles){
           Log.d("???",t);
       }
        for(String t:urls){
            Log.d("???",t);
        }
    }
}
