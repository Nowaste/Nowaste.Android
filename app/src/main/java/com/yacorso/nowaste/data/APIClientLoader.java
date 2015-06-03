package com.yacorso.nowaste.data;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class APIClientLoader {
    public static final String BASE_URL = "http://nowaste.dev/api/v1/";
    public APIClientInterface apiService;

    public APIClientLoader() {
        RestAdapter.Builder adapterBuilder = new RestAdapter.Builder();
        RestAdapter restAdapter = adapterBuilder.setEndpoint(BASE_URL).setClient(new OkClient(new OkHttpClient())).build();
        apiService = restAdapter.create(APIClientInterface.class);
    }

}
