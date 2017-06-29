package androiddegree.udacity.ememobong.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeCardAdapter;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeDetailFragment;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeIngredientFragment;

public class RecipeIngredientsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);

        Intent intent = getIntent();
        if(intent.hasExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY)){
            Recipe recipe =  intent.getParcelableExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipe);



            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeIngredientFragment recipeIngredientFragment = new RecipeIngredientFragment();
            // Add the fragment to its container using a transaction
            recipeIngredientFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_ingredient_framelayout, recipeIngredientFragment)
                    .commit();
        }

    }
}
