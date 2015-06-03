package com.yacorso.nowaste.data;

import com.squareup.okhttp.OkHttpClient;
import com.yacorso.nowaste.model.Fridge;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;

public class APIClientLoader {

    //public static final String BASE_URL = "http://nowaste.dev/api/v1";
    public static final String BASE_URL = "http://google.fr";
    public APIClientInterface apiService;

    public APIClientLoader() {
        RestAdapter.Builder adapterBuilder = new RestAdapter.Builder();
        RestAdapter restAdapter = adapterBuilder.setEndpoint(BASE_URL).setClient(new OkClient(new OkHttpClient())).build();
        apiService = restAdapter.create(APIClientInterface.class);
    }

    public interface APIClientInterface {

        @GET("/users/fridge")
        void getFridge(@Query("bypass") int key, Callback<Fridge> fridge);

    }
}
