package androiddegree.udacity.ememobong.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.MainActivity;
import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.RecipeDetailActivity;
import androiddegree.udacity.ememobong.bakingapp.model.PreparationSteps;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.service.ListWidgetService;
import androiddegree.udacity.ememobong.bakingapp.service.UpdateWithNextRecipeService;

import static androiddegree.udacity.ememobong.bakingapp.ui.RecipeCardAdapter.PARCEABLE_RECIPE_KEY;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeProviderWidget extends AppWidgetProvider {

    public static final String PARCEABLE_RECIPE_KEY = "parcelable";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String nextWidgetInfo,
                                Recipe recipe,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_provider_widget);
        // Set the ListWidgetService intent to act as the adapter for the ListView
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(PARCEABLE_RECIPE_KEY, recipe);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_list_view, R.id.widget_recipe_image);

        // Construct the RemoteViews object
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidgetWithNextInfo(Context context, AppWidgetManager appWidgetManager,
                                                int[] nextWidgetInfo,
                                                List<Recipe> recipes,
                                                int[] appWidgetIds) {


//        extract and build the next info to be displayed in the widget
        //this bit of code is redundant for now since the app logic has changed keeping it for review purposes
        int nextRecipeId = nextWidgetInfo[0];
        int nextRecipeStepId = nextWidgetInfo[1];
        String nextWidgetInfoStr = "";
        Recipe nextRecipe = null;

        for(Recipe recipe: recipes){
            if (recipe.getId() == nextRecipeId){
                nextRecipe = recipe;
                List<PreparationSteps> steps = recipe.getSteps();
                nextWidgetInfoStr = recipe.getRecipeName() + " \n Next Steps: ";
                for(PreparationSteps step: steps){
                    if(nextRecipeStepId == step.getId()){
                        nextWidgetInfoStr = nextWidgetInfoStr + step.getShortDescription();
                    }
                }
            }
        }
        Log.d("TAG", "this is the next info string we have been arguing about ever since " + nextWidgetInfoStr);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,nextWidgetInfoStr, nextRecipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        UpdateWithNextRecipeService.startActionWaterPlants(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

