package com.teamtreehouse.mememaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.teamtreehouse.mememaker.models.Meme;
import com.teamtreehouse.mememaker.models.MemeAnnotation;

import java.util.ArrayList;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeDatasource {

    private Context mContext;
    private MemeSQLiteHelper mMemeSqlLiteHelper;

    public MemeDatasource(Context context) {
        mContext = context;
        mMemeSqlLiteHelper = new MemeSQLiteHelper(context);
//        SQLiteDatabase database = mMemeSqlLiteHelper.getReadableDatabase();//Enable the database to set up the schema
//        database.close();
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
        //use content values to set data into the database.
        ContentValues memeValues = new ContentValues();
        memeValues.put(MemeSQLiteHelper.COLUMN_NAME, meme.getName());
        memeValues.put(MemeSQLiteHelper.COLUMN_ASSET, meme.getAssetLocation());

        long memeId = database.insert(MemeSQLiteHelper.MEMES_TABLE, null, memeValues);

        for (MemeAnnotation annotation : meme.getAnnotations())
        {
            ContentValues annotationValues = new ContentValues();
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_TITLE, annotation.getTitle());
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_COLOR, annotation.getColor());
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_X, annotation.getLocationX());
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_Y, annotation.getLocationY());
            annotationValues.put(MemeSQLiteHelper.COLUMN_FOREIGN_KEY_MEME, memeId);

            database.insert(MemeSQLiteHelper.ANNOTATIONS_TABLE, null, annotationValues);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public ArrayList<Meme> read()
    {
        ArrayList<Meme> memes = readMemes();
        addMemeAnnotation(memes);
        return memes;//Get the memes with the annotations.
    }

    public ArrayList<Meme> readMemes()
    {
        SQLiteDatabase database = open();
        //Database queries return cursors.
        Cursor cursor = database.query(MemeSQLiteHelper.MEMES_TABLE,
                new String[] {MemeSQLiteHelper.COLUMN_NAME,MemeSQLiteHelper.COLUMN_ASSET, BaseColumns._ID},
                null,
                null,
                null,
                null,
                null);

        ArrayList<Meme> memes = new ArrayList<>();

        if (cursor.moveToFirst())//Move the cursor to the first row.
        {
            do {
                Meme meme = new Meme(getIntFromColumnName(cursor,BaseColumns._ID),
                        getStringFromColumnName(cursor,MemeSQLiteHelper.COLUMN_ASSET),
                        getStringFromColumnName(cursor, MemeSQLiteHelper.COLUMN_NAME),
                        null);

                memes.add(meme);
            }while (cursor.moveToNext());//Move the cursor to the next row
        }

        cursor.close();
        database.close();
        return memes;
    }

    public void addMemeAnnotation (ArrayList<Meme> memes)
    {
        SQLiteDatabase database = open();

        for (Meme meme : memes)
        {
            ArrayList<MemeAnnotation> annotations = new ArrayList<>();
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + MemeSQLiteHelper.ANNOTATIONS_TABLE +
                            " WHERE MEME_ID = " + meme.getId(),null);

           if (cursor.moveToFirst())
           {
               do {
                   MemeAnnotation annotation = new MemeAnnotation(getIntFromColumnName(cursor, BaseColumns._ID),
                           getStringFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_COLOR),
                           getStringFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_TITLE),
                           getIntFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_X),
                           getIntFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_Y));

                   annotations.add(annotation);
                   meme.setAnnotations(annotations);

               }while (cursor.moveToNext());
           }
            cursor.close();
        }
        database.close();
    }

    private int getIntFromColumnName(Cursor cursor, String columnName)
    {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);//Value of the column as an index
    }

    private String getStringFromColumnName (Cursor cursor, String columnName)
    {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex); //Returns the value of the column index as a string
    }

    public void update(Meme meme)
    {
        SQLiteDatabase database = open();
        database.beginTransaction();
        //Update the meme
        ContentValues updateMeme = new ContentValues();
        updateMeme.put(MemeSQLiteHelper.COLUMN_NAME, meme.getName());
        database.update(MemeSQLiteHelper.MEMES_TABLE,
                updateMeme,
                String.format("%s=%d", BaseColumns._ID,meme.getId()),
                null);

        //Update the annotations
        for (MemeAnnotation annotation : meme.getAnnotations())
        {
            ContentValues updateAnnotationValues = new ContentValues();
            updateAnnotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_TITLE, annotation.getTitle());
            updateAnnotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_COLOR, annotation.getColor());
            updateAnnotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_X, annotation.getLocationX());
            updateAnnotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_Y, annotation.getLocationY());

            if (annotation.hasBeenSaved())
            {
                database.update(MemeSQLiteHelper.ANNOTATIONS_TABLE,
                        updateAnnotationValues,
                        String.format("%s=%d", BaseColumns._ID, annotation.getId()),
                        null);
            }else {
                database.insert(MemeSQLiteHelper.ANNOTATIONS_TABLE,
                        null,
                        updateAnnotationValues);
            }

        }
        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
