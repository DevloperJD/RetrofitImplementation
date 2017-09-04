package com.example.droid5.retrofitimplementation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by droid5 on 1/9/17.
 */

public class Video implements Parcelable {
    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected Video(Parcel in) {
        thumbUrl = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thumbUrl);
    }

    private String thumbUrl;
    private String title;

    public Video(String thumbUrl, String title) {
        this.thumbUrl = thumbUrl;
        this.title = title;
    }
}
