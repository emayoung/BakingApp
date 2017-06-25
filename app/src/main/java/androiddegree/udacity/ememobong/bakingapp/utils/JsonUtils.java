package androiddegree.udacity.ememobong.bakingapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.util.JsonToken;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



import androiddegree.udacity.ememobong.bakingapp.model.Ingredients;
import androiddegree.udacity.ememobong.bakingapp.model.PreparationSteps;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;

/**
 * Created by Bless on 6/16/2017.
 */

public class JsonUtils {


    public static List<Recipe> readJsonStream(Context context)throws IOException{
        JsonReader reader = readJSONFile(context);
        try {
            return readRecipesArray(reader);
        } finally {
            reader.close();
        }
    }

    private static JsonReader readJSONFile(Context context) throws IOException {


        AssetManager assetManager = context.getAssets();
        String uri = null;

        try {
            for (String asset : assetManager.list("")) {
                if (asset.endsWith(".json")) {
                    uri = "asset:///" + asset;

                }
            }
        } catch (IOException e) {
            Toast.makeText(context, "Error loading the json data from file", Toast.LENGTH_LONG)
                    .show();
        }

        String userAgent = Util.getUserAgent(context, "BakingAppRecyclerView");
        DataSource dataSource = new DefaultDataSource(context, null, userAgent, false);
        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        InputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);


        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

        } finally {
            Util.closeQuietly(dataSource);
        }

        return reader;
    }

    private static List<Recipe> readRecipesArray(JsonReader reader) throws IOException {
        List<Recipe> recipes = new ArrayList<Recipe>();

        reader.beginArray();
        while (reader.hasNext()) {
            recipes.add(readRecipeObject(reader));
        }
        reader.endArray();
        return recipes;
    }

    private static Recipe readRecipeObject(JsonReader reader) throws IOException {
        Integer id = -1;
        String recipeName = null;
        List<Ingredients> recipeIngredients = null;
        List<PreparationSteps> steps = null;
        Integer servings = null;
        String imageUrl = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("name")) {
                recipeName = reader.nextString();
            } else if (name.equals("ingredients") && reader.peek() != JsonToken.NULL) {
                recipeIngredients = readIngredientsArray(reader);
            } else if (name.equals("steps")) {
                steps = readPreparationStepsArray(reader);
            }else if (name.equals("servings")) {
                servings = reader.nextInt();
            }else if (name.equals("image")) {
                imageUrl = reader.nextString();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        ArrayList<Ingredients> recipeIngredientsArrayList = new ArrayList<>(recipeIngredients);
        ArrayList<PreparationSteps> stepsArrayList = new ArrayList<>(steps);
        return new Recipe(id, recipeName, recipeIngredientsArrayList, stepsArrayList, servings, imageUrl);
    }

    private static List<PreparationSteps> readPreparationStepsArray(JsonReader reader)throws IOException {
        List<PreparationSteps> ingredientsList = new ArrayList<PreparationSteps>();

        reader.beginArray();
        while (reader.hasNext()) {
            ingredientsList.add(readPreparationStepsObject(reader));
        }
        reader.endArray();
        return ingredientsList;
    }

    private static PreparationSteps readPreparationStepsObject(JsonReader reader) throws IOException {
        Integer id =-1;
        String shortDescription = null;
        String description  = null;
        String videoURL = null;
        String thumbnailURL = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("shortDescription")) {
                shortDescription = reader.nextString();
            } else if (name.equals("description")) {
                description = reader.nextString();
            } else if (name.equals("videoURL")) {
                videoURL = reader.nextString();
            } else if (name.equals("thumbnailURL")) {
                thumbnailURL = reader.nextString();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new PreparationSteps(id, shortDescription, description, videoURL, thumbnailURL);
    }

    private static List<Ingredients> readIngredientsArray(JsonReader reader)throws IOException {
        List<Ingredients> ingredientsList = new ArrayList<Ingredients>();

        reader.beginArray();
        while (reader.hasNext()) {
            ingredientsList.add(readIngredientsObject(reader));
        }
        reader.endArray();
        return ingredientsList;
    }

    private static Ingredients readIngredientsObject(JsonReader reader) throws IOException {
        Double quantity = -0.1;
        String measure = null;
        String ingredients = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("quantity")) {
                quantity = reader.nextDouble();
            } else if (name.equals("measure")) {
                measure = reader.nextString();
            } else if (name.equals("ingredients")) {
                ingredients = reader.nextString();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Ingredients(quantity, measure, ingredients);
    }

}
