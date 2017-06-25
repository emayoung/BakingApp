package androiddegree.udacity.ememobong.bakingapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.data.RecipeContract;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeCardAdapter;
import androiddegree.udacity.ememobong.bakingapp.utils.JsonUtils;

public class MainActivity extends AppCompatActivity {

    RecipeCardAdapter recyclerViewAdapter;
    GridLayoutManager gridLayoutManager;
    List<Recipe> recipes;
    public static List<Recipe> staticRecipeListForTesting;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_recipe_cards);
        gridLayoutManager = new GridLayoutManager(this, numberOfColumns());
        recyclerViewAdapter = new RecipeCardAdapter(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        try {
            readJsonStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerViewAdapter.setRecipeData(recipes);
        staticRecipeListForTesting = recipes;

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
//        this logic holds true in that if i have a small size phones of say 320 - 700 pixels diving this by 400 will yield a
//        value less than 2. so we can display 2 columns at once but this would be crude on a tablet say > 1080px because it would be
//        too large. so in this case we can display three columns
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 3) return 1;
        return nColumns;
    }

    private void readJsonStream () throws IOException{
      recipes = JsonUtils.readJsonStream(this);
    }
    }
