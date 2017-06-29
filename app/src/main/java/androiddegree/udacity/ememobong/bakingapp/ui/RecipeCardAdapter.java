package androiddegree.udacity.ememobong.bakingapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.RecipeDetailActivity;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import androiddegree.udacity.ememobong.bakingapp.service.UpdateWithNextRecipeService;
import androiddegree.udacity.ememobong.bakingapp.utils.DataBaseUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bless on 6/16/2017.
 */

public class RecipeCardAdapter   extends RecyclerView.Adapter<RecipeCardAdapter.ReceipeCardViewHolder> {

    List<Recipe> recipes = new ArrayList<Recipe>();
    Activity activity;

    public static final String PARCEABLE_RECIPE_KEY = "parcelable";

    public RecipeCardAdapter(Activity activity){
        this.activity = activity;
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

        holder.imageView.setVisibility(View.VISIBLE);
        holder.servingsTv.setVisibility(View.VISIBLE);
        setImageOnCard(holder.recipeCardImage, position);
        holder.receipeTextView.setText(recipes.get(position).getRecipeName());
        holder.receipeTextView.setContentDescription(recipes.get(position).getRecipeName());
        holder.servingsTv.setText(""+ recipes.get(position).getServings());
        holder.setIsRecyclable(true);

        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateWidgetAsyncTask().execute(recipes.get(position).getId());
                Intent intent = new Intent(activity, RecipeDetailActivity.class);
                intent.putExtra(PARCEABLE_RECIPE_KEY, recipes.get(position));
                activity.startActivity(intent);
            }
        });
    }

    public void setRecipeData(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == recipes) return 0;
        return recipes.size();
    }

    public void setImageOnCard(ImageView imageView, int positon){
        switch (positon){
            case 0:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.nutella_pie));
                break;
            case 1:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.brownies));
                break;
            case 2:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.yellow_cake));
                break;
            case 3:
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.cheesecake));
                break;
        }
    }

    public class ReceipeCardViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.recipe_card_name) TextView receipeTextView;
        @BindView(R.id.recipe_card_image) ImageView recipeCardImage;
        @BindView(R.id.floatingActionButton) Button fab;
        @BindView(R.id.serving_tv) TextView servingsTv;
        @BindView(R.id.imageView) ImageView imageView;
        public ReceipeCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    private class UpdateWidgetAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            DataBaseUtils.insertOrUpdateRecipeId(activity, integers[0]);
            UpdateWithNextRecipeService.startActionWaterPlants(activity);
            return null;
        }
    }
}
