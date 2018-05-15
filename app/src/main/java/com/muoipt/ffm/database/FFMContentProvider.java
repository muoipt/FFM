package com.muoipt.ffm.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.muoipt.ffm.utils.AppConfig;

/**
 * Created by XalenMy on 3/8/2018.
 */

public class FFMContentProvider extends ContentProvider {

    private DatabaseUtils databaseUtils;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AppConfig.URI, AppConfig.FFM_ITEM, AppConfig.ALLROWS);
        URI_MATCHER.addURI(AppConfig.URI, AppConfig.FFM_ITEM + "/#", AppConfig.SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        databaseUtils = new DatabaseUtils(getContext(), DatabaseUtils.DATABASE_NAME, null, DatabaseUtils.DATA_VERSION);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = databaseUtils.getWritableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseUtils.TABLE_REPORT_DETAIL);

        switch (URI_MATCHER.match(uri)){
            case AppConfig.SINGLE_ROW:
                String reportTitle = uri.getPathSegments().get(5);
                queryBuilder.appendWhere(DatabaseUtils.COLUMN_REPORT_TITLE + " LIKE \'%"+reportTitle+"%\'; ");
                break;
            default:
                break;
        }

        Cursor cursor = queryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, groupBy, having, sortOrder);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)){
            case AppConfig.ALLROWS:
                return "all_rows_request";
            case AppConfig.SINGLE_ROW:
                return "single_row_request";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return  null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
