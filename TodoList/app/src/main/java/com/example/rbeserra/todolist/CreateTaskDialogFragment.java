package com.example.rbeserra.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.rbeserra.todolist.model.Colors;

import java.util.HashMap;

/**
 * Uses a {@link DialogFragment} to provide the UI to insert new tasks.
 * Defines {@link CreateTaskDialogListener} that must be implemented by the calling activity
 * to receive the "submit task event".
 * Created by renato on 9/5/15.
 */
public class CreateTaskDialogFragment  extends DialogFragment {
    private static final String TAG = "CreateTaskDialog";
    private static final String KEY_COLOR = "key_color";
    private static final String KEY_HAS_TEXT = "key_has_text";
    private Colors mColor;
    private EditText mAddTaskText;
    private View mDialogView;
    private Button mSubmitButton;
    private boolean mHasText;
    private TextWatcher mTextChangedListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mDialogView = inflater.inflate(R.layout.dialog_add_task, null);
        builder.setView(mDialogView);


        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        mAddTaskText = (EditText) mDialogView.findViewById(R.id.addTaskText);
        mTextChangedListener = getTextWatcher();
        mAddTaskText.addTextChangedListener(mTextChangedListener);

        mSubmitButton = (Button) mDialogView.findViewById(R.id.buttonSubmitTask);

        if (savedInstanceState != null) {
            Colors color = Colors.valueOf(savedInstanceState.getString(KEY_COLOR, Colors.DEFAULT.name()));
            setColor(color);
            mHasText = savedInstanceState.getBoolean(KEY_HAS_TEXT,false);
        } else {
            setColor(Colors.DEFAULT);
            mHasText = false;
        }
        applySubmitButtonState();

        // Create the AlertDialog object and return it
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;

        return dialog;
    }

    private void setColor(Colors color) {
        this.mColor = color;
        mAddTaskText.setBackgroundResource(mColor.getResId());
        mAddTaskText.setTextColor(getResources().getColor(mColor.getTextResId()));
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_COLOR, mColor.name());
        super.onSaveInstanceState(outState);
    }

    public void onClickColor(View view) {
        Log.i(TAG, "Clicked id: " + view.getId());
        Colors color = getColor(view.getId());
        if (color != mColor) {
            setColor(color);
        } else {
            setColor(Colors.DEFAULT);
        }
        applySubmitButtonState();
    }

    private void applySubmitButtonState() {
        if (mHasText && mColor != Colors.DEFAULT) {
            mSubmitButton.setEnabled(true);
        } else {
            mSubmitButton.setEnabled(false);
        }

    }

    private Colors getColor(int viewId) {
        switch (viewId) {
            case R.id.red_button:
                return Colors.RED;
            case R.id.blue_button:
                return Colors.BLUE;
            case R.id.green_button:
                return Colors.GREEN;
            case R.id.orange_button:
                return Colors.ORANGE;
            case R.id.purple_button:
                return Colors.PURPLE;
            default:
                throw new IllegalStateException("Invalid getColor call");
        }
    }

    /**
     * Interface to deliver the submit task event to the calling activity.
     */
    public interface CreateTaskDialogListener {
        public void onSubmit(String text, Colors color);
    }

    // Use this instance of the interface to deliver action events
    CreateTaskDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CreateTaskDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CreateTaskDialogListener");
        }
    }

    public void clickSubmitButton(View v) {
        String text = mAddTaskText.getText().toString();
        Log.i(TAG, "task text: " +  text);
        mListener.onSubmit(text, mColor);
        dismiss();
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //pass
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mHasText = true;
                    applySubmitButtonState();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //pass
            }
        };
    }
}

