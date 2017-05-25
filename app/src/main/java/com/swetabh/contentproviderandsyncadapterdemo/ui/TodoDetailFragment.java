package com.swetabh.contentproviderandsyncadapterdemo.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.swetabh.contentproviderandsyncadapterdemo.R;
import com.swetabh.contentproviderandsyncadapterdemo.contentprovider.MyTodoContentProvider;
import com.swetabh.contentproviderandsyncadapterdemo.database.TodoTable;

/**
 * Created by swets on 15-05-2017.
 */

public class TodoDetailFragment extends Fragment implements MainContract.FragmentDetailViewContract {
    private MainContract.FragmentDetailPresenterContract mPresenter;
    private MainContract.ActivityCommunicator mCommunicator;
    private Spinner mCategory;
    private EditText mTitleText;
    private EditText mBodyText;
    private Uri todoUri;

    public TodoDetailFragment() {
    }

    public static TodoDetailFragment newInstance() {
        return new TodoDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // check from the saved Instance
            todoUri = (getArguments().getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE) == null) ?
                    null : (Uri) getArguments().getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);
            fillData(todoUri);
        }
    }

    private void fillData(Uri uri) {
        String[] projection = {TodoTable.COLUMN_SUMMARY,
                TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));

            for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String) mCategory.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }

            mTitleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_SUMMARY)));
            mBodyText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));

            // always close the cursor
            cursor.close();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.todo_edit, container, false);
        mCategory = (Spinner) view.findViewById(R.id.category);
        mTitleText = (EditText) view.findViewById(R.id.todo_edit_summary);
        mBodyText = (EditText) view.findViewById(R.id.todo_edit_description);
        Button confirmButton = (Button) view.findViewById(R.id.todo_edit_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    makeToast();
                } else {
                    mCommunicator.onBackPressed(false);
                }
            }

        });
        return view;
    }

    private void makeToast() {
        Toast.makeText(getContext(), "Please maintain a summary",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void setPresenter(MainContract.FragmentDetailPresenterContract presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setCommunicator(MainContract.ActivityCommunicator communicator) {
        this.mCommunicator = communicator;
    }

    @Override
    public void updateActionBar() {
        if (mCommunicator != null) {
            mCommunicator.updateTitle("Detail Screen");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String category = (String) mCategory.getSelectedItem();
        String summary = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();

        // only save if either summary or description
        // is available

        if (description.length() == 0 && summary.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TodoTable.COLUMN_CATEGORY, category);
        values.put(TodoTable.COLUMN_SUMMARY, summary);
        values.put(TodoTable.COLUMN_DESCRIPTION, description);

        if (todoUri == null) {
            // New todo
            todoUri = getContext().getContentResolver().insert(
                    MyTodoContentProvider.CONTENT_URI, values);
        } else {
            // Update todo
            getContext().getContentResolver().update(todoUri, values, null, null);
        }
    }
}
