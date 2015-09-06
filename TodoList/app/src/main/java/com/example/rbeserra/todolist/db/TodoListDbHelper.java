package com.example.rbeserra.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rbeserra.todolist.db.TodoListContract.TASK;

/**
 * The heler to access the database
 * Created by renato on 9/4/15.
 */
public class TodoListDbHelper extends SQLiteOpenHelper {

    private static final String TAG = TodoListDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TodoList.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMAP_SEP = ",";
    private static final String SQL_CREATE_TASK =  "CREATE TABLE " + TASK.TABLE_NAME + " (" +
            TASK._ID + " INTEGER PRIMARY KEY," +
            TASK.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMAP_SEP +
            TASK.COLUMN_NAME_COLOR + TEXT_TYPE + COMMAP_SEP +
            TASK.COLUMN_NAME_CREATED_TIME + " INTEGER" +
            " )";
    private static final String SQL_DROP_TASKS = "DROP TABLE IF EXISTS " + TASK.TABLE_NAME;


    public TodoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate TodoList.db");
        db.execSQL(SQL_CREATE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade TodoList.db");
        db.execSQL(SQL_DROP_TASKS);
        onCreate(db);
    }
}
