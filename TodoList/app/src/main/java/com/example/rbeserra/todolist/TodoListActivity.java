package com.example.rbeserra.todolist;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.rbeserra.todolist.content.CalendarUtils;
import com.example.rbeserra.todolist.content.OnAddCalendarEventListener;
import com.example.rbeserra.todolist.content.OnTaskRemovedListener;
import com.example.rbeserra.todolist.content.TaskCursorLoader;
import com.example.rbeserra.todolist.content.TaskRecyclerAdapter;
import com.example.rbeserra.todolist.db.TodoListContract;
import com.example.rbeserra.todolist.db.TodoListDbHelper;
import com.example.rbeserra.todolist.model.Colors;

import java.util.Date;

/**
 * Main Activity of the application. Uses a {@link RecyclerView} to show a list of
 * tasks added by the user. Provides the ability to add tasks (via {@link CreateTaskDialogFragment} and to remove tasks.
 */
public class TodoListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CreateTaskDialogFragment.CreateTaskDialogListener, OnTaskRemovedListener, OnAddCalendarEventListener {
    private static final String TAG = "TodoListActivity";

    private RecyclerView mRecyclerView;
    private TaskRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String DIALOG_FRAGMENT_TAG = "CreateTaskDialogFragment";
    private SQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TaskRecyclerAdapter(null, this, this);
        mRecyclerView.setAdapter(mAdapter);

        mDbHelper = new TodoListDbHelper(this);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TaskCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void addTask(View view) {
// Create an instance of the dialog fragment and show it
        CreateTaskDialogFragment dialog = new CreateTaskDialogFragment();
        dialog.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    public void clickColor(View view) {
        CreateTaskDialogFragment dialog = (CreateTaskDialogFragment) getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
        dialog.onClickColor(view);
    }

    public void clickSubmitButton(View v) {
        CreateTaskDialogFragment dialog = (CreateTaskDialogFragment) getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
        dialog.clickSubmitButton(v);
    }


    @Override
    public void onSubmit(String text, Colors color) {
        new AddNewTextTask().execute(text, color.name());
    }


    @Override
    public void onTaskRemoved(Long taskId) {
        Log.i(TAG, "Removing task with ID: " + taskId);
        new RemoveTask().execute(taskId);

    }

    @Override
    public void onAddCalendarEvent(CharSequence taskText) {
        startActivity(CalendarUtils.getIntent(taskText));
    }

    class RemoveTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            Long taskId = params[0];
            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            int result;
            result = db.delete(
                    TodoListContract.TASK.TABLE_NAME,
                    "_ID = ?",
                    new String[]{Long.toString(taskId)});
            getLoaderManager().getLoader(0).onContentChanged();
            Log.i(TAG, "Removed task count: " + result);
            return null;
        }
    }

    class AddNewTextTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String text = params[0];
            Colors color = Colors.valueOf(params[1]);
            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TodoListContract.TASK.COLUMN_NAME_CONTENT, text);
            values.put(TodoListContract.TASK.COLUMN_NAME_COLOR, color.name());
            values.put(TodoListContract.TASK.COLUMN_NAME_CREATED_TIME, new Date().getTime());

// Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    TodoListContract.TASK.TABLE_NAME,
                    null,
                    values);
            Log.i(TAG, "newRowId: " + newRowId);
            getLoaderManager().getLoader(0).onContentChanged();
            return null;
        }
    }
}


