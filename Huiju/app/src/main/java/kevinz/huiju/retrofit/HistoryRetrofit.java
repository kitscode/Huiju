package kevinz.huiju.retrofit;


import kevinz.huiju.bean.history.HistoryBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HistoryRetrofit {
    @GET("toh?key=3cd552456d8000b294ddb55a2a3af09e&v=1.0")
    Call<HistoryBean> getHistoryInfo(@Query("month") String month, @Query("day") String day);
}
