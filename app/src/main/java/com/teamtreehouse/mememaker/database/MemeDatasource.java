package com.teamtreehouse.mememaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.teamtreehouse.mememaker.models.Meme;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeDatasource {

    private Context mContext;
    private MemeSQLiteHelper mMemeSqlLiteHelper;

    public MemeDatasource(Context context) {
        mContext = context;
        mMemeSqlLiteHelper = new MemeSQLiteHelper(context);
        SQLiteDatabase database = mMemeSqlLiteHelper.getReadableDatabase();//Enable the database to set up the schema
        database.close();
    }

    /**
     * Open the database connection.
     *
     * @return SQLiteDatabase
     */
    private SQLiteDatabase open()
    {
        return mMemeSqlLiteHelper.getWritableDatabase();
    }

    private void close (SQLiteDatabase database)
    {
        database.close();
    }

    public void create (Meme meme)
    {
        SQLiteDatabase database = open();
        database.beginTransaction();
        //Implementation logic


        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
