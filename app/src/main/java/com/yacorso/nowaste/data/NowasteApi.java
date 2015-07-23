/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * NoWaste team
 */

package com.yacorso.nowaste.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Interface with all Api request available
 */
public interface NowasteApi {

    /**
     * Api base url
     */
//    String BASE_URL = "http://nowaste.dev/api/";
    String BASE_URL = "http://92.222.216.226/api/";

    /**
     * Api version
     */
    String VERSION = "v1";

    /**
     * Api end point
     */
    String END_POINT = BASE_URL + VERSION;


    /**
     * All requests available
     */
    @Multipart
    @POST("/auth/login?bypass=1")
    void login(@Part("email") String email, @Part("password") String password, Callback<String> token);

    @POST("/register")
    void register(@Body User userDemand, Callback<User> user);

    @GET("/test")
    void getTest(Callback<ArrayList<String>> message);

    @GET("/foods/{foodId}")
    void getFood(@Path("foodId") int foodId, Callback<Food> food);

    @GET("/foods")
    void getAllFoods(Callback<List<Food>> foods);

    @GET("/fridges/{fridgeId}")
    void getFridge(@Path("fridgeId") int fridgeId, Callback<Fridge> fridge);

    @GET("/fridges?bypass=1")
    void getAllFridges(Callback<List<Fridge>> fridges);

    @GET("/fridge{id}/foods")
    void getFoodsFromFridge(@Path("fridgeId") int fridgeId, Callback<List<Food>> foods);


    class ApiInstance {

        /**
         * Get Api Retro fit instance builded
         *
         * @return NowasteApi
         */
        public static NowasteApi getInstance() {

            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            RestAdapter.Builder adapterBuilder = new RestAdapter.Builder();
            RestAdapter restAdapter = adapterBuilder.setEndpoint(NowasteApi.END_POINT)
                    .setClient(new OkClient(new OkHttpClient()))
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            return restAdapter.create(NowasteApi.class);
        }

    }
}
