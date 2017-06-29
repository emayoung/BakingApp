package androiddegree.udacity.ememobong.bakingapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiClient;
import androiddegree.udacity.ememobong.bakingapp.networking.ApiInterface;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeCardAdapter;
import androiddegree.udacity.ememobong.bakingapp.utils.JsonUtils;
import androiddegree.udacity.ememobong.bakingapp.utils.SimpleIdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecipeCardAdapter recyclerViewAdapter;
    GridLayoutManager gridLayoutManager;
    List<Recipe> recipes;

    @BindView(R.id.recycler_recipe_cards) RecyclerView recyclerView;
    @BindView(R.id.empty_recycle_tv) TextView emptyTVForRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    // The Idling Resource which will be null in production.
    @Nullable private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!isNetworkAvailable()){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            emptyTVForRecyclerView.setVisibility(View.VISIBLE);
            return;
        }else{
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        gridLayoutManager = new GridLayoutManager(this, numberOfColumns());
        recyclerViewAdapter = new RecipeCardAdapter(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);


/*
*this code was used initially to read data from our assets json file
//        try {
//            readJsonStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
*/
        fetchDataFromServer();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 3) return 1;
        return nColumns;
    }

    private void readJsonStream () throws IOException{
      recipes = JsonUtils.readJsonStream(this);
    }

    public void fetchDataFromServer(){
        Log.d("TAG", "entered fetch method");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> call = apiService.getTopRatedMovies();

        call.enqueue(new Callback<List<Recipe>> () {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recipes = response.body();
                recyclerViewAdapter.setRecipeData(recipes);

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString() + "f");
            }
        });
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
