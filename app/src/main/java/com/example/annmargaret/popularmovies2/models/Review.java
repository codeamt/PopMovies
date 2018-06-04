package com.example.annmargaret.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    public String author;
    public String content;
    public String url;



    /* Empty Constructor */
    public Review() {}




    /* Overrides */
    @Override
    public int describeContents() {
        return 0;
    }





    /* Getters */
    public String getAuthor() {
        return author;
    }
    public String getContent() {
        return content;
    }
    public String getUrl() {
        return url;
    }

    /* Setters */
    public void setAuthor(String contentCrafter) {
        author = contentCrafter;
    }
    public void setContent(String body) {
        content = body;
    }
    public void setUrl(String link) {
        url = link;
    }





    /* Review Parceling */
    protected Review(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review [] newArray(int size) {
            return new Review[size];
        }
    };

}
