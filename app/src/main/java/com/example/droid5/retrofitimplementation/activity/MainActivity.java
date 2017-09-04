package com.example.droid5.retrofitimplementation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.droid5.retrofitimplementation.R;
import com.example.droid5.retrofitimplementation.adapter.MyRecyclerviewAdapter;
import com.example.droid5.retrofitimplementation.model.Video;
import com.example.droid5.retrofitimplementation.model.YoutubeApiResponse;
import com.example.droid5.retrofitimplementation.rest.ApiClient;
import com.example.droid5.retrofitimplementation.rest.ApiInterface;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyAgPMCaROM-JhmPhbfJMJ-sfNag3cwcxGo";
    private static final String playlistID = "PLOcMSsuppV4qUrw9bv_YXGsXuAIzRUiQf";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MyRecyclerviewAdapter myRecyclerviewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private List<Video> videoList;
    private String pageToken = "";
    private boolean loading = true;
    private int totalItemCountResponse;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        videoList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myRecyclerviewAdapter = new MyRecyclerviewAdapter(videoList, this);
        recyclerView.setAdapter(myRecyclerviewAdapter);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        fetchVideos();
        setListenter();

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
                        if(!loading){
                            fetchVideos();
                        }
                    }
                }
            }
        });

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
                for (int i = 0; i < response.body().getItems().size(); i++) {

                    pageToken = response.body().getNextPageToken();


                    String thumburl = response.body().getItems().get(i).getSnippet().getThumbnails().getHigh().getUrl();
                    String title = response.body().getItems().get(i).getSnippet().getTitle();
                    Video video = new Video(thumburl, title);
                    videoList.add(video);
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
}
