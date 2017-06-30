package androiddegree.udacity.ememobong.bakingapp;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 100;

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

        if (Build.VERSION.SDK_INT >= 21) {
            // check for permissions first
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_NETWORK_STATE)) {

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);


                }
            }

        } else {
            // Implement this feature without material design
            if (!isNetworkAvailable()){
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                emptyTVForRecyclerView.setVisibility(View.VISIBLE);
                return;
            }else{
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isNetworkAvailable()){
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        emptyTVForRecyclerView.setVisibility(View.VISIBLE);
                        return;
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(this, "You need to approve permission to use the app", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void fetchDataFromServer(){
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
