package androiddegree.udacity.ememobong.bakingapp.networking;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Bless on 6/27/2017.
 */

public interface ApiInterface {

    @GET("baking.json")
    Call<List<Recipe>> getTopRatedMovies();

}
