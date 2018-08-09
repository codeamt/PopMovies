package com.example.annmargaret.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    //attributes
    public String url;
    public String label;
    public String id;


    /* Empty Constructor */
    public Trailer() {};


    /* Overrides */
    @Override
    public int describeContents() {
        return 0;
    }



    /* Getters */
    public String getUrl(){
        return url;
    }
    public String getLabel(){
        return label;
    }
    public String getTrailerId() {
        return id;
    }

    /* Setters */
    public void setUrl(String link) {
        url = link;
    }
    public void setLabel(String key) {
        label = key;
    }
    public void setTrailerId(String _id) {
        id = _id;
    }



    /* Trailer Parceling */
    protected Trailer(Parcel in) {
        url = in.readString();
        label = in.readString();
        id = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(label);
        dest.writeString(id);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer [] newArray(int size) {
            return new Trailer[size];
        }
    };

}
