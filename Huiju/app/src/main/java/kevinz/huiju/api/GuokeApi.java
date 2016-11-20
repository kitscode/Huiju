package kevinz.huiju.api;

/**
 * Created by Administrator on 2016/10/25.
 */

public class GuokeApi {
    public static String[] channel_tag = {"hot", "frontier", "review", "interview", "visual", "brief", "fact", "techb"};
    public static String[] channel_title = {"热点", "前沿", "评论", "专访", "视觉", "速读", "谣言粉碎机", "商业科技"};
    public static String guoke_channel_url = "http://www.guokr.com/apis/minisite/article.json?retrieve_type=by_channel&channel_key=";
    public static String guoke_channel_url2 = "http://www.guokr.com/apis/minisite/article.json?retrieve_type=by_subject&subject_key=astronomy";
}