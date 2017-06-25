package androiddegree.udacity.ememobong.bakingapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.R.attr.id;
import static android.R.attr.value;
import static androiddegree.udacity.ememobong.bakingapp.data.RecipeContract.CONTENT_AUTHORITY;

/**
 * Created by Bless on 6/23/2017.
 */

public class RecipeContentProvider extends ContentProvider {

    public static final int CODE_RECIPE_ID = 100;
    public static final int CODE_RECIPE_STEPS_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RecipeContract.RECIPE_PATH, CODE_RECIPE_ID);
        matcher.addURI(authority, RecipeContract.RECIPE_PATH + "/#", CODE_RECIPE_STEPS_ID);

        return matcher;
    }


    private RecipeOpenHelper mOpenHelper;
    @Override
    public boolean onCreate() {
        mOpenHelper = new RecipeOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE_ID: {
                Log.d("TAG", "we just matched the uri for CODE_MOVIE_ID in the query method");
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.RecipesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE_ID:
                Log.d("TAG", "we just matched the uri for CODE_RECIPE_ID in the bulk insert method");
                long _id = db.insert(RecipeContract.RecipesEntry.TABLE_NAME, null, contentValues);
                db.beginTransaction();

                if (id !=-1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return RecipeContract.RecipesEntry.buildRecipeUriWithID(id);
                }

            default:
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE_ID:
                Log.d("TAG", "we just matched the uri for CODE_MOVIE_ID in the delete method");
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        RecipeContract.RecipesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
//        i call this a blind update since i intend to keep only one row in this table
        int numRowsUpdated;

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE_ID:
                numRowsUpdated = mOpenHelper.getWritableDatabase().update(
                        RecipeContract.RecipesEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsUpdated;
    }

    /**
     * This is a method specifically to assist the testing
     * framework in running smoothly. You can read more at:
     */
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
