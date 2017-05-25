package com.swetabh.contentproviderandsyncadapterdemo.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.swetabh.contentproviderandsyncadapterdemo.R;
import com.swetabh.contentproviderandsyncadapterdemo.contentprovider.MyTodoContentProvider;
import com.swetabh.contentproviderandsyncadapterdemo.database.TodoTable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MainFragment extends Fragment
        implements MainContract.FragmentMainViewContract,
        AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private MainContract.FragmentMainPresenterContract mPresenter;
    private MainContract.ActivityCommunicator mCommunicator;
    private static final int FRAGMENT_CREATE = 0;
    private static final int FRAGMENT_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private SimpleCursorAdapter adapter;
    private ListView mListView;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.todo_list, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setDividerHeight(2);
        fillData();
        registerForContextMenu(mListView);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/"
                        + info.id);
                getContext().getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{TodoTable.COLUMN_SUMMARY};
        // Fields on the UI to which we map
        int[] to = new int[]{R.id.label};
        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(getContext(), R.layout.todo_row, null, from, to, 0);
        mListView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void setPresenter(MainContract.FragmentMainPresenterContract presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setCommunicator(MainContract.ActivityCommunicator communicator) {
        mCommunicator = communicator;
    }

    @Override
    public void updateActionBar() {
        mCommunicator.updateTitle("New Title");
        mCommunicator.menuItemsToEnable(R.id.insert);
    }

    @Override
    public void createTodoItem() {
        mCommunicator.openDetailFragment();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TodoTable.COLUMN_ID, TodoTable.COLUMN_SUMMARY};
        CursorLoader cursorLoader = new CursorLoader(getContext(),
                MyTodoContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
