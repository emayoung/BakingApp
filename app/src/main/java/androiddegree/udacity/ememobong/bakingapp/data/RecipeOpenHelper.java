package androiddegree.udacity.ememobong.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bless on 6/23/2017.
 */

public class RecipeOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 2;


    public RecipeOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPES_TABLE =
        "CREATE TABLE " + RecipeContract.RecipesEntry.TABLE_NAME + " (" +

                RecipeContract.RecipesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                RecipeContract.RecipesEntry.COLUMN_RECIPE_ID       + " REAL NOT NULL, "  +

                RecipeContract.RecipesEntry.COLUMN_RECIPE_STEP_ID + " REAL NOT NULL,"    +

                " UNIQUE (" + RecipeContract.RecipesEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
