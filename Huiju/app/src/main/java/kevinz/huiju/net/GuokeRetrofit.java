package kevinz.huiju.net;


import kevinz.huiju.bean.guoke.GuokeBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GuokeRetrofit {
    @GET("article.json?retrieve_type=by_channel")
    Call<GuokeBean> getGuokeInfo(@Query("channel_key") String channel_key);
}
