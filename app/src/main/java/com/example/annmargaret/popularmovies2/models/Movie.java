package com.example.annmargaret.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    //attributes
    public String displayName;
    public float rating;
    public Double popularity;
    public String releasedDate;
    public String overview;
    public String posterUrl;
    public String tagLine;
    public int runtime;
    public int id;




    /* Empty Constructor */
    public Movie() {
    }





    /* Overrides */
    @Override
    public int describeContents() {
        return 0;
    }





    /* Getters */
    public String getTitle() {
        return displayName;
    }
    public String getPosterUrl() {
        return posterUrl;
    }
    public Float getRating() {
        return rating;
    }
    public String getOverview() {
        return overview;
    }
    public Double getPopularity() {
        return popularity;
    }
    public String getReleaseDate() {
        return releasedDate;
    }
    public String getTagLine() {
        return tagLine;
    }
    public int getRuntime() {
        return runtime;
    }
    public int getId(){return id; }

    /* Setters */
    public void setTitle(String display_name) {
        displayName = display_name;
    }
    public void setPosterURL(String poster_path) {
        posterUrl = poster_path;
    }
    public void setOverview(String description) {
        overview = description;
    }
    public void setPopularity(Double popularity) {popularity = popularity;}
    public void setRating(Float rating) {rating = rating;}
    public void setReleaseDate(String release_date) {
        releasedDate = release_date;
    }
    public void setTagLine(String line) {
        tagLine = line;
    }
    public void setRuntime(int time) {
        runtime = time;
    }






    /* Movie Parceling */
    protected Movie(Parcel in) {
        displayName = in.readString();
        rating = in.readFloat();
        popularity = in.readByte() == 0x00 ? null : in.readDouble();
        releasedDate = in.readString();
        overview = in.readString();
        posterUrl = in.readString();
        id = in.readInt();
        tagLine = in.readString();
        runtime = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeFloat(rating);

        if(popularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(popularity);
        }

        dest.writeString(releasedDate);
        dest.writeString(overview);
        dest.writeString(posterUrl);
        dest.writeInt(id);
        dest.writeString(tagLine);
        dest.writeInt(runtime);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie [] newArray(int size) {
            return new Movie[size];
        }
    };

}
