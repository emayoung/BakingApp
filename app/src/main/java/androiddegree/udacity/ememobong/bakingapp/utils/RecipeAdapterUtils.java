package androiddegree.udacity.ememobong.bakingapp.utils;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.R;

/**
 * Created by Bless on 6/16/2017.
 */

public class RecipeAdapterUtils {
    static List<Integer> recipeDrawables = new ArrayList<>();

    static void addAllDrawables(){
        recipeDrawables.add(R.drawable.ic_launcher);
        recipeDrawables.add(R.drawable.ic_launcher);
        recipeDrawables.add(R.drawable.ic_launcher);
        recipeDrawables.add(R.drawable.ic_launcher);
    }
}
