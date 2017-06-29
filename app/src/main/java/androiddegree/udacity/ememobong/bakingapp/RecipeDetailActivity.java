package androiddegree.udacity.ememobong.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.model.PreparationSteps;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeCardAdapter;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeDetailCardAdapter;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeDetailFragment;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeIngredientFragment;
import androiddegree.udacity.ememobong.bakingapp.ui.RecipeStepDetailFragment;

public class RecipeDetailActivity extends AppCompatActivity  implements RecipeDetailCardAdapter.ReplaceFragment{

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;
    public static final String TWO_PANE_BUNDLE_KEY = "is_two_pane";
    public static final String POSITION = "position";
    Recipe recipe;
    Intent intent;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipe);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        intent = getIntent();
        if (findViewById(R.id.second_fragment_linear_layout) != null){
//            we are in a two pane layout
            mTwoPane = true;
            if(savedInstanceState != null ){

                recipe = savedInstanceState.getParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);

            }else{
                recipe =  intent.getParcelableExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);
            }
            inflateForTwoPaneLayout(recipe);
            }

        else {
//            we are in a single pane layout
            mTwoPane = false;
                if(savedInstanceState != null ){
                    recipe = savedInstanceState.getParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);
                    inflateForSinglePaneLayout(recipe);
                }else{
                    recipe =  intent.getParcelableExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);
                }
                inflateForSinglePaneLayout(recipe);
        }


    }

    public void inflateForTwoPaneLayout(Recipe recipe) {
        if (intent.hasExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY)) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipe);
            bundle.putBoolean(TWO_PANE_BUNDLE_KEY, mTwoPane);

            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            // Add the fragment to its container using a transaction
            recipeDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.master_list_fragment, recipeDetailFragment)
                    .commit();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            // Add the fragment to its container using a transaction
            recipeStepDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.steps_and_ingredient_instruction_container, recipeStepDetailFragment)
                    .commit();
        }
    }

    public void inflateForSinglePaneLayout(Recipe recipe){

        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipe);
        bundle.putBoolean(TWO_PANE_BUNDLE_KEY, mTwoPane);


        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        // Add the fragment to its container using a transaction
        recipeDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.master_list_fragment, recipeDetailFragment)
                .commit();
    }

    @Override
    public void replaceWithIngredientFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipe);
        bundle.putInt(POSITION, position);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeIngredientFragment recipeIngredientFragment = new RecipeIngredientFragment();
        // Add the fragment to its container using a transaction
        recipeIngredientFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.steps_and_ingredient_instruction_container, recipeIngredientFragment)
                .commit();
    }

    @Override
    public void replaceFragmentAtPosition(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipe);
        bundle.putInt(POSITION, position);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        // Add the fragment to its container using a transaction
        recipeStepDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.steps_and_ingredient_instruction_container, recipeStepDetailFragment)
                .commit();
    }
}
