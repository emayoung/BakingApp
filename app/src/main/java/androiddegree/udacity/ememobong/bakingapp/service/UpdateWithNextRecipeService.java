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

import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import androiddegree.udacity.ememobong.bakingapp.utils.JsonUtils;
import androiddegree.udacity.ememobong.bakingapp.widget.RecipeProviderWidget;

/**
 * Created by Bless on 6/23/2017.
 */

public class UpdateWithNextRecipeService extends IntentService{

    List<Recipe> recipes;

    public static final String ACTION_WATER_PLANTS = "androiddegree.udacity.ememobong.bakingapp.action.update_widget_with_next_recipe";
    public UpdateWithNextRecipeService() {
                super("UpdateWithNextRecipeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("TAG", "entered on handle intent of the servide");
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

        Log.d("TAG", "service has been started to update the widget with the next info");
    }
    public void handleUpdateNextWidget(){
//        get the next information to be displayed in the widget
        try {
            readJsonStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] nextWidgetInfo = DataBaseUtils.nextRecipeToWatch(this, recipes);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeProviderWidget.class));
        //Now update all widgets
        Log.d("TAG", "we are redirecting to the recipe provider class to update the widget");
        RecipeProviderWidget.updateWidgetWithNextInfo(this, appWidgetManager, nextWidgetInfo, recipes, appWidgetIds);
    }

    private void readJsonStream () throws IOException{
        recipes = JsonUtils.readJsonStream(this);
    }
}
