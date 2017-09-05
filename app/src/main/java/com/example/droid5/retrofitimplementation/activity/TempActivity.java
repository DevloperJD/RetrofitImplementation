package com.example.droid5.retrofitimplementation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.droid5.retrofitimplementation.R;
import com.example.droid5.retrofitimplementation.model.VideoRealm;

import io.realm.Realm;

public class TempActivity extends AppCompatActivity {
    TextView textView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        textView=findViewById(R.id.text);
        realm = Realm.getDefaultInstance();


        String personId = getIntent().getStringExtra("value");
        Realm realm = Realm.getDefaultInstance();
        try {
            VideoRealm video = realm.where(VideoRealm.class).equalTo("value", personId).findFirst();
            textView.append(video.getTitle());

        } finally {
            realm.close();
        }



    }
}
