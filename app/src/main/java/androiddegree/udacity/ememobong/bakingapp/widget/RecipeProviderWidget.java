package androiddegree.udacity.ememobong.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe,
                                int appWidgetId) {
        Log.d("TAG", "we have entered method to update the widget");
        // Set the ListWidgetService intent to act as the adapter for the ListView
       /* RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_listview_widget);
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        appIntent.putExtra(PARCEABLE_RECIPE_KEY, recipe);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_list_view, R.id.empty_widget_tv);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), RecipeProviderWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,  R.id.widget_list_view);
*/
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_provider_widget);
        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        appIntent.putExtra(PARCEABLE_RECIPE_KEY, recipe);

        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ingredient_name, appPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
       Log.d("TAG", "we have called update on the widget");


    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        /*ComponentName thisAppWidget = new ComponentName(context.getPackageName(), RecipeProviderWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        Log.d("TAG", "we have called notifydatasetchanged for app widget");
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,  R.id.widget_list_view);
        UpdateWithNextRecipeService.startActionWaterPlants(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
*/
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), RecipeProviderWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            Log.d("TAG", "we have called notifydatasetchanged for app widget");
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,  R.id.widget_list_view);


        }
    }

    public static void updateWidgetWithNextInfo(Context context, AppWidgetManager appWidgetManager,
                                                int[] nextWidgetInfo,
                                                List<Recipe> recipes,
                                                int[] appWidgetIds) {
        Log.d("TAG", "we have entered updatewidgetwithNextInfo for app widget");
       //        extract and build the next info to be displayed in the widget
        //this bit of code is redundant for now since the app logic has changed keeping it for review purposes
        int nextRecipeId = nextWidgetInfo[0];
        Recipe nextRecipe = recipes.get(nextRecipeId);

        for (int appWidgetId : appWidgetIds) {
            Log.d("TAG", "we have entered the crazy for loop");
            updateAppWidget(context, appWidgetManager, nextRecipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
      /*  Intent initialUpdateIntent = new Intent(
                AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        initialUpdateIntent
                .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(initialUpdateIntent);*/
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

