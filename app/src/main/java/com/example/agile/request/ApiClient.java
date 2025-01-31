package com.example.agile.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.0.109:5200/";
    private static final String API_URL = BASE_URL + "api/";
    private static EndpointAgile endpointAgile;

    public static EndpointAgile getEndpoint() {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        endpointAgile = retrofit.create(EndpointAgile.class);
        return endpointAgile;
    }
}
