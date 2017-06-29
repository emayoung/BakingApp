package androiddegree.udacity.ememobong.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiClient;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiInterface;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bless on 6/28/2017.
 */

public class ListWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return  new ListRemoteViewsFactory(this.getApplicationContext());
    }


    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        List<Recipe> recipes;
        int currentRecipeId = -1;
        public static final String EXTRA_PLANT_ID = "extra_id";

        public ListRemoteViewsFactory(Context applicationContext) {
                    mContext = applicationContext;

        }
        @Override
        public void onCreate() {


        }
        //called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
            Log.d("TAG", "about to fetch the recipes for the list view");
            if (recipes.isEmpty()){
                fetchDataFromServer();
            }

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(currentRecipeId == -1 && recipes == null) return 0;
            return recipes.get(currentRecipeId).getRecipeIngredients().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if(recipes.isEmpty()) return null;

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_fragment_detail_card);

            views.setTextViewText(R.id.ingredient_name, recipes.get(currentRecipeId).getRecipeIngredients().get(position).getIngredients());
            views.setTextViewText(R.id.measure_tv, mContext.getResources().getString(R.string.measure) + recipes.get(currentRecipeId).getRecipeIngredients().get(position).getMeasure());
            views.setTextViewText(R.id.quantity_tv,mContext.getResources().getString(R.string.quantity) +  recipes.get(currentRecipeId).getRecipeIngredients().get(position).getQuantity());

            Bundle extras = new Bundle();
            extras.putLong(EXTRA_PLANT_ID, 1);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.ingredient_name, fillInIntent);

            return views;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
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
                    currentRecipeId = nextWidgetInfo[0];
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("TAG", t.toString() + "f");
                }
            });
        }
    }
}
