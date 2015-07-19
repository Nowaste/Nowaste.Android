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
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.SyncArrays;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

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

    @GET("/test")
    void getTest(Callback<ArrayList<String>> message);

    @GET("/foods")
    void getAllFoods(Callback<List<Food>> foods);


    /*
     * CRUD for food
     */
    @GET("/foods/{foodId}")
    void getFood(@Path("foodId") long foodId, Callback<Food> food);

    @POST("/foods?bypass=1")
    void createFood(@Body Food food, Callback<Food> fridgeCallback);

    @PUT("/foods/{foodId}?bypass=1")
    void updateFood(@Path("foodId") long foodId,@Body Food food, Callback<Food> foodCallback);

    @DELETE("/foods/{foodId}?bypass=1")
    void deleteFood(@Path("foodId") long foodId, Callback<Food> foodCallback);

    /*
     * CRUD for fridge
     */
    @GET("/fridges/{fridgeId}")
    void getFridge(@Path("fridgeId") long fridgeId, Callback<Fridge> fridge);

    @POST("/fridges?bypass=1")
    void createFridge(@Body Fridge fridge, Callback<Fridge> fridgeCallback);

    @PUT("/fridges/{fridgeId}?bypass=1")
    void updateFridge(@Path("fridgeId") long fridgeId,@Body Fridge fridge, Callback<Fridge> fridgeCallback);

    @DELETE("/fridges/{fridgeId}?bypass=1")
    void deleteFridge(@Path("fridgeId") long fridgeId, Callback<Fridge> fridgeCallback);

    /*
     * CRUD for custom list
     */
    @GET("/custom-lists/{customListId}")
    void getCustomList(@Path("customListId") long customListId, Callback<CustomList> customList);

    @POST("/custom-lists?bypass=1")
    void createCustomList(@Body CustomList customList, Callback<CustomList> customListCallback);

    @PUT("/custom-lists/{customListId}?bypass=1")
    void updateCustomList(@Path("customListId") long customListId,@Body CustomList customList, Callback<CustomList> customListCallback);

    @DELETE("/custom-lists/{customListId}?bypass=1")
    void deleteCustomList(@Path("customListId") long customListId, Callback<CustomList> customListCallback);


    @GET("/fridges?bypass=1")
    void getAllFridges(Callback<List<Fridge>> fridges);

    @GET("/fridge{id}/foods")
    void getFoodsFromFridge(@Path("fridgeId") int fridgeId, Callback<List<Food>> foods);

    @GET("/sync")
    void getSync(@Query("userId") long userId, @Query("lastSync") long lastSync, Callback<SyncArrays> sync);


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
