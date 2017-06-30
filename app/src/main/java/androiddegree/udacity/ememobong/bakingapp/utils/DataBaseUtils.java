package androiddegree.udacity.ememobong.bakingapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.MainActivity;
import androiddegree.udacity.ememobong.bakingapp.data.RecipeContract;
import androiddegree.udacity.ememobong.bakingapp.model.PreparationSteps;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;

import static junit.runner.Version.id;

/**
 * Created by Bless on 6/23/2017.
 */

public class DataBaseUtils {

    public static void insertOrUpdateRecipeId(Context context, int recipeId){
//        check if this is the first time we are using the database then insert else update the table
        String[] proj = new String[]{RecipeContract.RecipesEntry.COLUMN_RECIPE_ID};
//                Uri newUri = getContentResolver().insert(RecipeContract.RecipesEntry.CONTENT_URI, cv);
        Cursor cursor = context.getContentResolver().query(RecipeContract.RecipesEntry.CONTENT_URI,
                proj,
                null,
                null,
                null);


        if (cursor.getCount() != 0){
//            update the database
            ContentValues cv = new ContentValues();
            cv.put(RecipeContract.RecipesEntry.COLUMN_RECIPE_ID, recipeId);
            cv.put(RecipeContract.RecipesEntry.COLUMN_RECIPE_STEP_ID, 0); // this is  since this indicates last step we watched
            context.getContentResolver().update(RecipeContract.RecipesEntry.CONTENT_URI,
                    cv,
                    null,
                    null);

        }
        else {
//            perform an insert
            ContentValues cv = new ContentValues();
            cv.put(RecipeContract.RecipesEntry.COLUMN_RECIPE_ID, recipeId);
            cv.put(RecipeContract.RecipesEntry.COLUMN_RECIPE_STEP_ID, 0);
            context.getContentResolver().insert(RecipeContract.RecipesEntry.CONTENT_URI, cv);

        }
    }

    public static void updateRecipeStepId(Context context, int recipeStepId){

//        query for the recipe id that we will use to perform an update

        String[] proj = new String[]{RecipeContract.RecipesEntry.COLUMN_RECIPE_ID};
        Cursor cursor = context.getContentResolver().query(RecipeContract.RecipesEntry.CONTENT_URI,
                proj,
                null,
                null,
                null);
        if (cursor.moveToFirst()){
            int recipeId = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipesEntry.COLUMN_RECIPE_ID));
            ContentValues cv = new ContentValues();
            cv.put(RecipeContract.RecipesEntry.COLUMN_RECIPE_ID, recipeId);
            cv.put(RecipeContract.RecipesEntry.COLUMN_RECIPE_STEP_ID, recipeStepId); // this is  since this indicates last step we watched
            context.getContentResolver().update(RecipeContract.RecipesEntry.CONTENT_URI,
                    cv,
                    null,
                    null);

        }
    }

    public static int[] nextRecipeToWatch(Context context, List<Recipe> recipes){
        int nextRecipe = 0;
        int nextRecipeStep = 0;
        int maxRecipeStep = 0;


        String[] proj = new String[]{RecipeContract.RecipesEntry.COLUMN_RECIPE_ID,
                RecipeContract.RecipesEntry.COLUMN_RECIPE_STEP_ID};
//                Uri newUri = getContentResolver().insert(RecipeContract.RecipesEntry.CONTENT_URI, cv);
        Cursor cursor = context.getContentResolver().query(RecipeContract.RecipesEntry.CONTENT_URI,
                proj,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            int recipeId = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipesEntry.COLUMN_RECIPE_ID));
            int recipeStepId = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipesEntry.COLUMN_RECIPE_STEP_ID));


            for (Recipe recipe: recipes){
                if(recipe.getId() == recipeId){
                    List<PreparationSteps> preparationSteps = recipe.getSteps();
                    maxRecipeStep = preparationSteps.size() -1;
                }
            }
            /*check to see if we have not started watching any of the steps for a selected
            check to see if are watching if we are at the end of a recipe*/

            if(recipeStepId == 0){
//                we have not yet began watching any step so our next step id thus
                nextRecipe = recipeId;
                nextRecipeStep = recipeStepId + 1;

            }
            else if (recipeStepId < maxRecipeStep){
//                we are not done watching all the steps to prepare the recipe
                nextRecipe = recipeId;
                nextRecipeStep = recipeStepId + 1;
            }
            else {
//                we are done wathing the steps for that recipe
                if (recipeId < 4){
                    nextRecipe = recipeId + 1;
                }else{
                    nextRecipe = 1;
                }
                nextRecipeStep = 1;
            }
        }

        return  new int[]{nextRecipe, nextRecipeStep};
    }


}
