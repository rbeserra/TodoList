package com.example.rbeserra.todolist.content;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rbeserra.todolist.R;
import com.example.rbeserra.todolist.db.TodoListContract;
import com.example.rbeserra.todolist.model.Colors;

/**
 * Custom Recycler to interact with {@link TaskCursorLoader};
 * Created by renato on 9/4/15.
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {
    private final OnTaskRemovedListener mTaskRemovedListener;
    private final OnAddCalendarEventListener mAddCalendarEventListener;
    private Cursor mCursor;
    private static final String TAG = "TaskRecyclerAdapter";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTaskText;
        public ImageButton mCloseButton;
        public ImageButton mCalendarButton;
        public Long mId;

        public ViewHolder(View view) {
            super(view);
            Log.i(TAG, "view holder id: " + getItemId());
            Log.i(TAG, "view holder adapter position: " + getAdapterPosition());
            mTaskText = (TextView) view.findViewById(R.id.taskItemText);
            mCloseButton = (ImageButton) view.findViewById(R.id.close_item_button);
            mCalendarButton = (ImageButton) view.findViewById(R.id.addCalendarEvent);

            mCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTaskRemovedListener.onTaskRemoved(mId);
                }
            });

            mCalendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddCalendarEventListener.onAddCalendarEvent(mTaskText.getText());
                }
            });
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskRecyclerAdapter(Cursor cursor, OnTaskRemovedListener onTaskRemovedListener, OnAddCalendarEventListener calendarEventListener) {
        super();
        this.mCursor = cursor;
        this.mTaskRemovedListener = onTaskRemovedListener;
        this.mAddCalendarEventListener = calendarEventListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        mCursor.moveToPosition(position);
        Colors color = Colors.valueOf(mCursor.getString(mCursor.getColumnIndex(TodoListContract.TASK.COLUMN_NAME_COLOR)));
        holder.mId = mCursor.getLong(mCursor.getColumnIndex(TodoListContract.TASK._ID));
        holder.mTaskText.setText(mCursor.getString(mCursor.getColumnIndex(TodoListContract.TASK.COLUMN_NAME_CONTENT)));
        holder.mTaskText.setBackgroundResource(color.getResId());

        holder.mTaskText.setTextColor(Color.parseColor(color.getTextRgb()));
        holder.mTaskText.setVerticalScrollBarEnabled(true);


    }

    @Override
    public long getItemId(int position) {
        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mCursor.getColumnIndex(TodoListContract.TASK._ID));
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        else
            return mCursor.getCount();
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     *
     * @param cursor The new cursor to be used.
     */
    public void swapCursor(Cursor cursor) {
        if (cursor != this.mCursor) {
            this.mCursor = cursor;
            if (cursor != null) {
                // notify the observers about the new cursor
                notifyDataSetChanged();
            } else {
                // notify the observers about the lack of a data set
                notifyItemRangeRemoved(0, getItemCount());
            }
        }

    }
}

