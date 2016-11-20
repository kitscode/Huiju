package kevinz.huiju.data;

import android.os.Handler;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import kevinz.huiju.support.CONSTANT;
import kevinz.huiju.bean.history.HistoryBean;
import kevinz.huiju.bean.history.HistoryDetails;

/**
 * Created by Administrator on 2016/10/25.
 */

public class HistoryData extends BaseData {

    public HistoryData(Handler handler,String url) {
        super(handler,url);
    }

    @Override
    void loadFromNet() {
        Request request = new Request.Builder()
                .url(murl)
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
                HistoryBean historyBean = gson.fromJson(res, HistoryBean.class);
                HistoryDetails[] historyDetails = historyBean.getResult();
                for (HistoryDetails details : historyDetails) {
                    list.add(details);
                }
                mhandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }


}
