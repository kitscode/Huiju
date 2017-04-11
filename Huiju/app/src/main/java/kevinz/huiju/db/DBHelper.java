package kevinz.huiju.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static kevinz.huiju.db.SqlUtil.*;


public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Create cache and collection tables.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlUtil.CREATE_COLLECTION_TABLE);
        db.execSQL(SqlUtil.CREATE_GUOKE_TABLE);
        db.execSQL(SqlUtil.CREATE_TABS);
        for(int i=0;i<channel_tag.length;i++){
            db.execSQL("insert into tabs values("+i+",'"+channel_tag[i]+"','"+channel_title[i]+"')");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
