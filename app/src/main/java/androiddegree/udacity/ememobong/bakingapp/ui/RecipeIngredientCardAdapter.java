package androiddegree.udacity.ememobong.bakingapp.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bless on 6/26/2017.
 */

public class RecipeIngredientCardAdapter extends RecyclerView.Adapter<RecipeIngredientCardAdapter.ReceipeIngredientCardViewHolder>{

    private Activity activity;
    Recipe recipe;
    public RecipeIngredientCardAdapter(Activity activity){
        this.activity = activity;
    }
    @Override
    public ReceipeIngredientCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        boolean bindToParentOnStart = false;

        View view =  layoutInflater.inflate(R.layout.recipe_fragment_detail_card, parent, bindToParentOnStart);
        RecipeIngredientCardAdapter.ReceipeIngredientCardViewHolder recipeViewHolder = new RecipeIngredientCardAdapter.ReceipeIngredientCardViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(ReceipeIngredientCardViewHolder holder, int position) {
        holder.ingredientNameTV.setText(recipe.getRecipeIngredients().get(position + 1).getIngredients());
        holder.measureTV.setText(activity.getResources().getString(R.string.measure) + recipe.getRecipeIngredients().get(position + 1).getMeasure());
        holder.quantityTV.setText(activity.getResources().getString(R.string.quantity) + recipe.getRecipeIngredients().get(position + 1).getQuantity());
    }

    @Override
    public int getItemCount() {
        if(recipe == null) return 0;
        return recipe.getRecipeIngredients().size() -1;
    }

    public void setRecipeData(Recipe recipes){
        this.recipe = recipes;
        notifyDataSetChanged();
    }


    public class ReceipeIngredientCardViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ingredient_name)
        TextView ingredientNameTV;
        @BindView(R.id.measure_tv)
        TextView measureTV;
        @BindView(R.id.quantity_tv)
        TextView quantityTV;
        public ReceipeIngredientCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
