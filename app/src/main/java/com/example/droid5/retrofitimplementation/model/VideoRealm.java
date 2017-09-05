package com.example.droid5.retrofitimplementation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by droid5 on 5/9/17.
 */

public class VideoRealm extends RealmObject {
    private String thumbUrl;

    private String title;

    public VideoRealm() {
    }

    public VideoRealm(String thumbUrl, String title) {
        this.thumbUrl = thumbUrl;
        this.title = title;
    }

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
}
