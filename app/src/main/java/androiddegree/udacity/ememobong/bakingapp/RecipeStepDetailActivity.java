package androiddegree.udacity.ememobong.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeCardAdapter;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeDetailCardAdapter;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeDetailFragment;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeStepDetailFragment;

import static androiddegree.udacity.ememobong.bakingapp.RecipeDetailActivity.POSITION;
import static androiddegree.udacity.ememobong.bakingapp.RecipeDetailActivity.TWO_PANE_BUNDLE_KEY;

public class RecipeStepDetailActivity extends AppCompatActivity{

    public static final String POSITION = "position";
    Recipe recipe;
    int position;
    public static final String TWO_PANE_BUNDLE_KEY = "is_two_pane";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail_sup);

        Intent intent = getIntent();
        if (intent.hasExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY)) {

            recipe =  intent.getParcelableExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);
            position = intent.getIntExtra(POSITION, 0);

            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipe);
            bundle.putInt(POSITION, position);
            bundle.putBoolean(TWO_PANE_BUNDLE_KEY, false);

            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            // Add the fragment to its container using a transaction
            recipeStepDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_container, recipeStepDetailFragment)
                    .commit();
        }
    }


}
