package com.example.agile.request;

import com.example.agile.models.Tienda;
import com.example.agile.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
//    ===== Funciones de usuarios =====
//    User login
    @POST("usuarios/login")
    Call<String> login(@Body Usuario usuario);

//    User signup
    @POST("usuarios/signup")
    Call<String> signup(@Body Usuario usuario);

//    Verify session
    @GET("usuarios/obtener")
    Call<Usuario> obtenerUsuario(@Header("Authorization") String token);

//    ================================
//    ===== Funciones de tiendas =====
//    ================================

//    Crear tienda
    @POST("tiendas/crear")
    Call<Void> crearTienda(@Header("Authorization") String token, @Body Tienda tienda);
}
