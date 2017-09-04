package com.example.droid5.retrofitimplementation.rest;

import com.example.droid5.retrofitimplementation.model.YoutubeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by droid5 on 1/9/17.
 */

public interface ApiInterface {

    @GET("playlistItems")
    Call<YoutubeApiResponse> getData(@Query("key") String apikey, @Query("part") String part, @Query("maxResults") int maxResults, @Query("playlistId")String playlistId);

    @GET("playlistItems")
    Call<YoutubeApiResponse> getDataOnDemand(@Query("key") String apikey,@Query("part")String part,@Query("maxResults") int maxResults,@Query("playlistId")String playlistId,@Query("pageToken")String pageToken);
}
