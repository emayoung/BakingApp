package androiddegree.udacity.ememobong.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;

public class RecipeIngredientFragment extends Fragment{

    RecipeIngredientCardAdapter recipeIngredientCardAdapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;

    // Mandatory empty constructor
    public RecipeIngredientFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_card_detail_recyclerview, container, false);

        Recipe recipe = getArguments().getParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_detail_recycler);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recipeIngredientCardAdapter = new RecipeIngredientCardAdapter(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipeIngredientCardAdapter);

        if(null != recipe){
            recipeIngredientCardAdapter.setRecipeData(recipe);
        }

        // Return the root view
        return rootView;
    }
}
