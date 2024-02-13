package com.example.agile.request;

import com.example.agile.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EndpointAgile {

    /*
      === POST Example ===
      @POST("usuarios/login")
      Call<String> login(@Body Usuario usuario);


      === POST Token Example ===
      @POST("usuarios/edit")
      Call<Usuario> edit(@Header("Authorization") String token, @Body Usuario usuario);


      === GET Example ===
      @GET("usuarios")
      Call<List<Usuario>> getAll();
    */

//    User login
    @POST("usuarios/login")
    Call<String> login(@Body Usuario usuario);

//    User signup
    @POST("usuarios/signup")
    Call<String> signup(@Body Usuario usuario);

}
