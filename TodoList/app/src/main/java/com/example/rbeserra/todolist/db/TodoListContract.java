package com.example.rbeserra.todolist.db;

import android.provider.BaseColumns;

/**
 * The contract of the application database.
 * Created by renato on 9/4/15.
 */
public class TodoListContract {

    public TodoListContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TASK implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_CREATED_TIME = "created_time";
    }
}
