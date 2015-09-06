package com.example.rbeserra.todolist.content;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rbeserra.todolist.db.TodoListContract;
import com.example.rbeserra.todolist.db.TodoListDbHelper;

/**
 * Extension of {@link CursorLoader} that retrieves all tasks.
 * The extension is needed because this application doesn't define a {@link android.content.ContentProvider}
 * An improvement would be to define it as abstract, adding table, projection and selection in the constructor, so
 * that any internal database could use it.
 * Created by renato on 9/5/15.
 */
public class TaskCursorLoader extends CursorLoader {

    final ForceLoadContentObserver mObserver;

    public TaskCursorLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mDbHelper = new TodoListDbHelper(context);
    }

    private TodoListDbHelper mDbHelper;

    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
    }

    /* Runs on a worker thread */
    @Override
    public Cursor loadInBackground() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TodoListContract.TASK._ID,
                TodoListContract.TASK.COLUMN_NAME_CONTENT,
                TodoListContract.TASK.COLUMN_NAME_COLOR,
                TodoListContract.TASK.COLUMN_NAME_CREATED_TIME,
        };

        //defines the order of the results.
        String sortOrder =
                TodoListContract.TASK.COLUMN_NAME_CREATED_TIME + " DESC";


        Cursor cursor = db.query(
                TodoListContract.TASK.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if (cursor != null) {
            try {
                // Ensure the cursor window is filled.
                cursor.getCount();
                cursor.registerContentObserver(mObserver);
            } catch (RuntimeException ex) {
                cursor.close();
                throw ex;
            }
        }

        return cursor;
    }
}
