package androiddegree.udacity.ememobong.bakingapp.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.category;

/**
 * Created by Bless on 6/16/2017.
 */

public class Recipe implements Parcelable {

    Integer id;
    String recipeName;
    ArrayList<Ingredients> recipeIngredients = new ArrayList<Ingredients>();
    ArrayList<PreparationSteps> steps = new ArrayList<PreparationSteps>();
    Integer servings;
    String imageUrl;

    private static final String INGREDIENT_KEY = "ingredients";
    private static final String STEPS_KEY = "steps";

    public Recipe(Integer id, String recipeName, ArrayList<Ingredients> recipeIngredients, ArrayList<PreparationSteps> steps, Integer servings, String imageUrl) {
        this.id = id;
        this.recipeName = recipeName;
        this.recipeIngredients = recipeIngredients;
        this.steps = steps;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(recipeName);

        Bundle b = new Bundle();
        b.putParcelableArrayList(INGREDIENT_KEY, recipeIngredients);
        b.putParcelableArrayList(STEPS_KEY, steps);
        parcel.writeBundle(b);

        parcel.writeInt(servings);
        parcel.writeString(imageUrl);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<Ingredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(ArrayList<Ingredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<PreparationSteps> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<PreparationSteps> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        id = in.readInt();
        recipeName = in.readString();

        Bundle b = in.readBundle(getClass().getClassLoader());

        recipeIngredients = b.getParcelableArrayList(INGREDIENT_KEY);
        steps = b.getParcelableArrayList(STEPS_KEY);


        servings = in.readInt();
        imageUrl = in.readString();
    }

}
