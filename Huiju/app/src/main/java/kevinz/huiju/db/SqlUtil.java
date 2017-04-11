package kevinz.huiju.db;

/**
 * Created by Administrator on 2017/3/12.
 */

public class SqlUtil {

    public final static String CREATE_COLLECTION_TABLE = "create table collection " +
            "(title text primary key,image text,description text,url text,ifcollected int)";

    public final static String CREATE_GUOKE_TABLE="create table guoke "+
            "(title text primary key,image text,url text,tag text)";

    public final static String CREATE_TABS="create table tabs (id int primary key," +
            "tab text,title text)";

    public static String[] channel_tag = {"hot", "frontier", "review", "interview", "visual", "brief", "fact", "techb"};
    public static String[] channel_title = {"热点", "前沿", "评论", "专访", "视觉", "速读", "谣言粉碎机", "商业科技"};


}
