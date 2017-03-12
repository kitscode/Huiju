package kevinz.huiju.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Create cache and collection tables.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlUtil.CREATE_COLLECTION_TABLE);
        db.execSQL(SqlUtil.CREATE_GUOKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
