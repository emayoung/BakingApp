package androiddegree.udacity.ememobong.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiClient;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiInterface;
import androiddegree.udacity.ememobong.bakingapp.service.BakingAppListWidget;
import androiddegree.udacity.ememobong.bakingapp.service.GetRecipesWidgetService;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    Context mContext;
    static Recipe recipe;
    public static final String PARCEABLE_RECIPE_KEY = "parcelable";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_ingredients_listview_widget);

        Intent intent = new Intent(context, BakingAppListWidget.class);
        views.setRemoteAdapter(R.id.widget_list_view2, intent);

        views.setEmptyView(R.id.widget_list_view2, R.id.empty_widget_view);

//        set clicking intent for the recipe is not null
        if (recipe != null){
            Intent appIntent = new Intent(context, RecipeDetailActivity.class);
            appIntent.putExtra(PARCEABLE_RECIPE_KEY, recipe);

            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_text, appPendingIntent);
        }else{
            Intent appIntent = new Intent(context, MainActivity.class);

            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_text, appPendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view2);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("TAG", "entered the enabled method for the widget");
        mContext = context;
        fetchDataFromServer();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void fetchDataFromServer(){
        Log.d("TAG", "entered fetch method");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> call = apiService.getTopRatedMovies();

        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d("TAG", "response from server for list view is successful");
                List<Recipe> recipes = response.body();
                int[] nextWidgetInfo = DataBaseUtils.nextRecipeToWatch(mContext, recipes);
                int currentRecipeId = nextWidgetInfo[0];
                recipe = response.body().get(currentRecipeId);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString() + "f");
            }
        });
    }


}

