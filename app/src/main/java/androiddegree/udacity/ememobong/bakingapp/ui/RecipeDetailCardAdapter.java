package androiddegree.udacity.ememobong.bakingapp.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.RecipeDetailActivity;
import androiddegree.udacity.ememobong.bakingapp.RecipeStepDetailActivity;
import androiddegree.udacity.ememobong.bakingapp.model.PreparationSteps;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.service.UpdateWithNextRecipeService;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bless on 6/20/2017.
 */

public class RecipeDetailCardAdapter extends RecyclerView.Adapter<RecipeDetailCardAdapter.ReceipeCardViewHolder>{

    Recipe recipes = null;
    RecipeDetailActivity activity;
    List<PreparationSteps> preparationSteps = new ArrayList<>();
    boolean mTwoPane;
    public static final String POSITION = "position";

    public RecipeDetailCardAdapter(Activity activity, boolean twoPane){
        this.activity = (RecipeDetailActivity) activity;
        mTwoPane = twoPane;
    }

    public interface ReplaceFragment{
        public void replaceFragmentAtPosition(int position);
    }

    @Override
    public ReceipeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        boolean bindToParentOnStart = false;

        View view =  layoutInflater.inflate(R.layout.recipe_card, parent, bindToParentOnStart);
        ReceipeCardViewHolder recipeViewHolder = new ReceipeCardViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(ReceipeCardViewHolder holder, final int position) {

        setImageOnCard(holder.recipeCardImage);
        if(position == 0){
            holder.receipeTextView.setText("Recipe Ingredients");
        }
        else if (null != preparationSteps){
            holder.receipeTextView.setText(preparationSteps.get(position - 1) // minus 1 to account for the recipe ingredient space
            .getShortDescription());
            holder.receipeTextView.setContentDescription(preparationSteps.get(position - 1) // minus 1 to account for the recipe ingredient space
                    .getShortDescription());
        }

        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTwoPane){
//                    the adapter works in a two pane layout

                    if(position== 0){
//                        inflate the recipe ingredients

                    }else{
                        //                  help the widget to update it's details
                        DataBaseUtils.updateRecipeStepId(activity, preparationSteps.get(position - 1).getId());
                        UpdateWithNextRecipeService.startActionWaterPlants(activity);

//          we should replace our fragment here

                        activity.replaceFragmentAtPosition(position -1);
                    }

                }else{

                    if (position == 0){
//                        start an intent for recipe ingredient activity
                    }else{
                        //                    adapter working in a single pane layout
                        DataBaseUtils.updateRecipeStepId(activity, preparationSteps.get(position - 1).getId());
                        UpdateWithNextRecipeService.startActionWaterPlants(activity);
                        Intent intent = new Intent(activity, RecipeStepDetailActivity.class);
                        intent.putExtra(RecipeCardAdapter.PARCEABLE_RECIPE_KEY, recipes);
                        intent.putExtra(POSITION, position -1);
                        activity.startActivity(intent);
                        activity.finish();

                    }
                }

            }
        });
        holder.setIsRecyclable(true);
    }

    public void setRecipeData(Recipe recipes){
        this.recipes = recipes;
        preparationSteps = recipes.getSteps();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == preparationSteps) return 0;
        return preparationSteps.size() + 1;  // adding one  makes sure that the Ingredient card is accounted for
    }

    public void setImageOnCard(ImageView imageView){
        int id = recipes.getId();
        switch (id){
            case 1:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.nutella_pie));
                break;
            case 2:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.brownies));
                break;
            case 3:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.yellow_cake));
                break;
            case 4:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.cheesecake));
                break;
        }
    }

    public class ReceipeCardViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.recipe_card_name) TextView receipeTextView;
        @BindView(R.id.recipe_card_image) ImageView recipeCardImage;
        @BindView(R.id.floatingActionButton)
        FloatingActionButton fab;
        public ReceipeCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
