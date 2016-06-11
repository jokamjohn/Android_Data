package com.teamtreehouse.mememaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeSQLiteHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "meme.db";
    private static final String MEMES_TABLE = "MEMES";
    private static final String ASSET_COLUMN = "ASSET_COLUMN";
    private static final String NAME_COLUMN = "NAME_COLUMN";
    private static final String CREATE_MEME = "CREATE TABLE " + MEMES_TABLE + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ASSET_COLUMN + " TEXT," +
            NAME_COLUMN + " TEXT)";

    //Meme Table Annotations functionality
    public static final String ANNOTATIONS_TABLE = "ANNOTATIONS";
    public static final String COLUMN_ANNOTATION_COLOR = "COLOR";
    public static final String COLUMN_ANNOTATION_X = "X";
    public static final String COLUMN_ANNOTATION_Y = "Y";
    public static final String COLUMN_ANNOTATION_TITLE = "TITLE";
    public static final String COLUMN_FOREIGN_KEY_MEME = "MEME_ID";

    private static final String CREATE_ANNOTATIONS = "CREATE TABLE " + ANNOTATIONS_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ANNOTATION_X + " INTEGER, " +
            COLUMN_ANNOTATION_Y + " INTEGER, " +
            COLUMN_ANNOTATION_TITLE + " TEXT, " +
            COLUMN_ANNOTATION_COLOR + " TEXT, " +
            COLUMN_FOREIGN_KEY_MEME + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_FOREIGN_KEY_MEME + ") REFERENCES MEMES(_ID))";


    public MemeSQLiteHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEME);
        db.execSQL(CREATE_ANNOTATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
