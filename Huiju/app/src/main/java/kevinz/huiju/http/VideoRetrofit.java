package kevinz.huiju.http;

import kevinz.huiju.bean.Videobean;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/11/13.
 */

public interface VideoRetrofit {
    @GET("10-10.html")
    Call<Videobean> getVideoInfo();
}
