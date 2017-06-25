package androiddegree.udacity.ememobong.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bless on 6/16/2017.
 */

public class PreparationSteps implements Parcelable {

    private Integer id ;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public PreparationSteps(Integer id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public PreparationSteps createFromParcel(Parcel in) {
            return new PreparationSteps(in);
        }

        public PreparationSteps[] newArray(int size) {
            return new PreparationSteps[size];
        }
    };
    private PreparationSteps(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }


}

