package kevinz.huiju.db;

/**
 * Created by Administrator on 2017/3/12.
 */

public class SqlUtil {
    public final static String CREATE_COLLECTION_TABLE = "create table collection " +
            "(title text primary key,image text,description text,url text,ifcollected int)";

    public final static String CREATE_GUOKE_TABLE="create table guoke "+
            "(title text primary key,image text,url text,tag text)";
}
