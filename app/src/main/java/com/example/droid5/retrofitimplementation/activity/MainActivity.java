package com.example.droid5.retrofitimplementation.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.droid5.retrofitimplementation.R;
import com.example.droid5.retrofitimplementation.adapter.MyRecyclerviewAdapter;
import com.example.droid5.retrofitimplementation.model.Video;
import com.example.droid5.retrofitimplementation.model.VideoRealm;
import com.example.droid5.retrofitimplementation.model.YoutubeApiResponse;
import com.example.droid5.retrofitimplementation.rest.ApiClient;
import com.example.droid5.retrofitimplementation.rest.ApiInterface;
import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyAgPMCaROM-JhmPhbfJMJ-sfNag3cwcxGo";
        private static final String playlistID = "PLOcMSsuppV4qUrw9bv_YXGsXuAIzRUiQf";
//    private static final String playlistID = "PLPwunoahP3QujeB0joP3we9zyRssfwXCu";
    private static final String TAG = MainActivity.class.getSimpleName();
    int pastVisiblesItems, visibleItemCount, totalItemCount, position;
    private MyRecyclerviewAdapter myRecyclerviewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private List<Video> videoList;
    private String pageToken = "";
    private boolean loading = true;
    private int totalItemCountResponse = 0;
    private VideoRealm videoRealm;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    RealmResults<VideoRealm> videoRealms;
    private RealmChangeListener realmChangeListener;
    private Realm realm;
    boolean isconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                myRecyclerviewAdapter.notifyDataSetChanged();
            }
        };

        videoList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myRecyclerviewAdapter = new MyRecyclerviewAdapter(videoList, this);
        recyclerView.setAdapter(myRecyclerviewAdapter);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            isconnected = true;

        }
        else{
            isconnected = false;

        }
        getOfflineContent();
        fetchVideos();
        setListenter();

    }

    private void getOfflineContent() {
        videoRealms = realm.where(VideoRealm.class).findAll();
        for (int i=0;i<videoRealms.size();i++){
            Log.e(TAG, "getOfflineContent  iteration is " +i);
            Video video = new Video();
            VideoRealm videoRealm1 = videoRealms.get(i);
            video.setThumbUrl(videoRealm1.getThumbUrl());
            video.setTitle(videoRealm1.getTitle());
            videoList.add(i,video);


        }


    }

    private void setListenter() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    //Log.e(TAG, "onScrolled: visibelItemcount is " + visibleItemCount + "past visibles is "+pastVisiblesItems+ " totalItemcount is " + totalItemCount);
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        Log.e(TAG, "scrolled to bottom");
                        if (!loading && totalItemCount <= totalItemCountResponse) {

                            Log.e(TAG, "onScrolled: postion is " + totalItemCount + "total items are " + totalItemCountResponse);

                            fetchVideos();
                        }
                    }
                }
            }
        });

    }

    public void btn_GO(View view) {
        RealmResults<VideoRealm> realmResults = realm.where(VideoRealm.class).findAll();
        VideoRealm video = realmResults.get(1);
        Log.e(TAG, "btn_GO: title is "+video.getTitle());
//        Intent intent = new Intent(this, TempActivity.class);
//        intent.putExtra("value", video.getTitle());
//        startActivity(intent);

    }


    private void fetchVideos() {
        Log.e(TAG, "fetchVideos");
        loading = true;
        Call<YoutubeApiResponse> call = apiService.getDataOnDemand(API_KEY, "snippet", 14, playlistID, pageToken);
        call.enqueue(new Callback<YoutubeApiResponse>() {
            @Override
            public void onResponse(Call<YoutubeApiResponse> call, Response<YoutubeApiResponse> response) {
                int statusCode = response.code();
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Failed to fetch data! " + response + ", statusCode: " + statusCode);
                    return;
                }
                totalItemCountResponse = response.body().getPageInfo().getTotalResults();

                for (int i = 0; i < response.body().getItems().size(); i++) {

                    pageToken = response.body().getNextPageToken();
                    position = response.body().getItems().get(i).getSnippet().getPosition();


                    String thumburl = response.body().getItems().get(i).getSnippet().getThumbnails().getHigh().getUrl();
                    String title = response.body().getItems().get(i).getSnippet().getTitle();
                    realm.beginTransaction();
                    videoRealm = realm.createObject(VideoRealm.class);
                    videoRealm.setThumbUrl(thumburl);
                    videoRealm.setTitle(title);
                    realm.commitTransaction();


                    Video video = new Video(thumburl, title);
                    videoList.add(position, video);
                    if (position == totalItemCountResponse) break;
                }
                myRecyclerviewAdapter.notifyDataSetChanged();
                loading = false;
            }

            @Override
            public void onFailure(Call<YoutubeApiResponse> call, Throwable t) {
                loading = false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(realmChangeListener != null && videoRealms != null){
            videoRealms.removeChangeListener(realmChangeListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(realmChangeListener != null && videoRealms != null){
            videoRealms.addChangeListener(realmChangeListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



        realm.close();
    }
}
