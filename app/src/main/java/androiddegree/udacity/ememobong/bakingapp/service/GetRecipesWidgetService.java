package androiddegree.udacity.ememobong.bakingapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.BakingAppWidget;
import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiClient;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiInterface;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import androiddegree.udacity.ememobong.bakingapp.widget.RecipeProviderWidget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androiddegree.udacity.ememobong.bakingapp.service.UpdateWithNextRecipeService.ACTION_WATER_PLANTS;

/**
 * this class is defunc not providing anhy functionality again
 */

public class GetRecipesWidgetService extends IntentService {

    public Context mContext;
    public static final String ACTION_GET_WIDGETS = "androiddegree.udacity.ememobong.bakingapp.action.update_widget_with_next_recipe";

    public GetRecipesWidgetService() {
        super("GetRecipesWidgetService");
    }

    public static void startGetRecipeForWidgetService(Context context) {
        Intent intent = new Intent(context, GetRecipesWidgetService.class);
        intent.setAction(ACTION_GET_WIDGETS);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mContext = this;
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_WIDGETS.equals(action)) {
                handleUpdateNextWidget();
            }
        }
    }
    public void handleUpdateNextWidget(){

    }

}
