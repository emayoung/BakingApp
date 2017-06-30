package androiddegree.udacity.ememobong.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.model.Ingredients;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiClient;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiInterface;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bless on 6/30/2017.
 */

public class BakingAppListWidget extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext());
    }


    class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        List<Ingredients> ingredients;
        int currentRecipeId;

        ListViewRemoteViewsFactory(Context context){
            mContext = context;

        }
        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            if(ingredients == null){
                fetchDataFromServer();
            }
        }

        @Override
        public void onDestroy() {
            ingredients = null;
        }

        @Override
        public int getCount() {
            if(ingredients == null) return 0;
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if (ingredients == null) return null;

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget);

            views.setTextViewText(R.id.appwidget_text, ingredients.get(position).getIngredients());

            // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.appwidget_text, fillInIntent);
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
                    List<Recipe> recipes = response.body();
                    int[] nextWidgetInfo = DataBaseUtils.nextRecipeToWatch(mContext, recipes);
                    currentRecipeId = nextWidgetInfo[0];
                    ingredients = response.body().get(currentRecipeId).getRecipeIngredients();
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
