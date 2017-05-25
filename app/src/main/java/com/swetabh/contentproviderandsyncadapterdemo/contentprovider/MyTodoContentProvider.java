package com.swetabh.contentproviderandsyncadapterdemo.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;

import com.swetabh.contentproviderandsyncadapterdemo.database.TodoDatabaseHelper;
import com.swetabh.contentproviderandsyncadapterdemo.database.TodoTable;

import java.util.Arrays;
import java.util.HashSet;

public class MyTodoContentProvider extends ContentProvider {

    private TodoDatabaseHelper databaseHelper;

    // used for the UriMacher
    private static final int TODOS = 10;
    private static final int TODO_ID = 20;

    private static final String AUTHORITY = "com.suman.contentprovider";
    private static final String BASE_PATH = "todos";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/todos";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/todo";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
    }

    public MyTodoContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case TODOS:
                rowsDeleted = db.delete(TodoTable.TABLE_TODO, selection, selectionArgs);
                break;
            case TODO_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(TodoTable.TABLE_TODO,
                            TodoTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = db.delete(TodoTable.TABLE_TODO,
                            TodoTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case TODOS:
                id = db.insert(TodoTable.TABLE_TODO, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        databaseHelper = new TodoDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(TodoTable.TABLE_TODO);
        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case TODOS:
                break;
            case TODO_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(TodoTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case TODOS:
                rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TODO_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO,
                            values,
                            TodoTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO,
                            values,
                            TodoTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {TodoTable.COLUMN_ID,
                TodoTable.COLUMN_SUMMARY,
                TodoTable.COLUMN_CATEGORY,
                TodoTable.COLUMN_DESCRIPTION};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}
