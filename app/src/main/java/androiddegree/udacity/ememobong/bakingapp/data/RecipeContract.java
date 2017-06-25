package androiddegree.udacity.ememobong.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bless on 6/23/2017.
 */

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "androiddegree.udacity.ememobong.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String RECIPE_PATH = "recipes";

    public static class RecipesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(RECIPE_PATH)
                .build();

        public static final String TABLE_NAME = "recipe";

        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_RECIPE_STEP_ID = "recipe_step_id";

        public static Uri buildRecipeUriWithID(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

}
