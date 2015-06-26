package com.yacorso.nowaste.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Fridge;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by quentin on 20/06/15.
 */
public interface NowasteApi {

//    String BASE_URL = "http://nowaste.dev/api/";
    String BASE_URL = "http://92.222.216.226/api/";
    String VERSION = "v1";

    String END_POINT = BASE_URL + VERSION;



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





    class ApiInstance{

        public static NowasteApi getInstance(){

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
