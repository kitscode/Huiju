package kevinz.huiju.retrofit;

import kevinz.huiju.bean.video.Videobean;
import retrofit2.Call;
import retrofit2.http.GET;

public interface VideoRetrofit {
    @GET("10-10.html")
    Call<Videobean> getVideoInfo();
}
