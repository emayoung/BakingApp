package androiddegree.udacity.ememobong.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.MainActivity;
import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.RecipeDetailActivity;
import androiddegree.udacity.ememobong.bakingapp.model.PreparationSteps;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;

/**
 * Created by Bless on 6/16/2017.
 */

public class RecipeDetailFragment extends Fragment {

    RecipeDetailCardAdapter recyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;

    // Mandatory empty constructor
    public RecipeDetailFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_card_detail_recyclerview, container, false);

        Recipe recipe = getArguments().getParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);
        boolean twoPane = getArguments().getBoolean(RecipeDetailActivity.TWO_PANE_BUNDLE_KEY);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_detail_recycler);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdapter = new RecipeDetailCardAdapter(getActivity(), twoPane);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        if(null != recipe){
            recyclerViewAdapter.setRecipeData(recipe);
        }

        // Return the root view
        return rootView;
    }
}
