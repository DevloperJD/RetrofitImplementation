package com.example.droid5.retrofitimplementation.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by droid5 on 1/9/17.
 */

public class ApiClient {

   public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
   private static Retrofit retrofit = null;
   public static Retrofit getClient(){
       if(retrofit == null){
           retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

       }
       return retrofit;
   }



}
