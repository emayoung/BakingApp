package androiddegree.udacity.ememobong.bakingapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiClient;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiInterface;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import androiddegree.udacity.ememobong.bakingapp.utils.JsonUtils;
import androiddegree.udacity.ememobong.bakingapp.widget.RecipeProviderWidget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class is now defunct keeping it for reference
 */

public class UpdateWithNextRecipeService extends IntentService{

    List<Recipe> recipes;
    Context mContext;

    public static final String ACTION_WATER_PLANTS = "androiddegree.udacity.ememobong.bakingapp.action.update_widget_with_next_recipe";
    public UpdateWithNextRecipeService() {
                super("UpdateWithNextRecipeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mContext = this;
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WATER_PLANTS.equals(action)) {
                handleUpdateNextWidget();
            }
        }
    }
    public static void startActionWaterPlants(Context context) {
        Intent intent = new Intent(context, UpdateWithNextRecipeService.class);
        intent.setAction(ACTION_WATER_PLANTS);
        context.startService(intent);

    }
    public void handleUpdateNextWidget(){
//        get the next information to be displayed in the widget
        fetchDataFromServer();
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
                recipes = response.body();

                int[] nextWidgetInfo = DataBaseUtils.nextRecipeToWatch(mContext, recipes);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, RecipeProviderWidget.class));
                //Now update all widgets
                //Trigger data update to handle the GridView widgets and force a data refresh
                RecipeProviderWidget.updateWidgetWithNextInfo(mContext, appWidgetManager, nextWidgetInfo, recipes, appWidgetIds);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString() + "f");
            }
        });
    }
}
