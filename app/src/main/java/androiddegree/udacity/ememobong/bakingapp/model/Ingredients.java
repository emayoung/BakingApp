package androiddegree.udacity.ememobong.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.id;

/**
 * Created by Bless on 6/16/2017.
 */

public class Ingredients implements Parcelable{
    Double quantity;
    String measure;
    String ingredients;

    public Ingredients(Double quantity, String measure, String ingredients) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredients = ingredients;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
    private Ingredients(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredients = in.readString();
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredients);
    }



}
