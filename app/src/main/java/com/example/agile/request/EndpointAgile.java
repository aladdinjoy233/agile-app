package com.example.agile.request;

import com.example.agile.models.Tienda;
import com.example.agile.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

//    Obtener lista de tiendas
    @GET("tiendas/obtenerlista")
    Call<List<Tienda>> obtenerTiendas(@Header("Authorization") String token);

//    Obtener tienda por id
    @GET("tiendas/obtener/{id}")
    Call<Tienda> obtenerTienda(@Header("Authorization") String token, @Path("id") int id);

//    Editar tienda
    @PUT("tiendas/editar/{id}")
    Call<Void> editarTienda(@Header("Authorization") String token, @Path("id") int id, @Body Tienda tienda);

//    Eliminar tienda
    @DELETE("tiendas/eliminar/{id}")
    Call<Void> eliminarTienda(@Header("Authorization") String token, @Path("id") int id);

//    Salir de la tienda
    @POST("tiendas/{id}/salir")
    Call<Void> salirTienda(@Header("Authorization") String token, @Path("id") int id);

//    Es dueño de la tienda
    @GET("tiendas/{id}/esduenio")
    Call<Boolean> esDuenio(@Header("Authorization") String token, @Path("id") int id);

//    Obtener usuarios de la tienda
    @GET("tiendas/{id}/obtenerusuarios")
    Call<List<Usuario>> obtenerUsuarios(@Header("Authorization") String token, @Path("id") int id);

//    Invitar usuario
    @POST("tiendas/{id}/invitarusuario")
    Call<Void> invitarUsuario(@Header("Authorization") String token, @Path("id") int id, @Body Usuario usuario);

//    Eliminar usuario
    @POST("tiendas/{tiendaId}/eliminarusuario/{userId}")
    Call<Void> eliminarUsuario(@Header("Authorization") String token, @Path("tiendaId") int tiendaId, @Path("userId") int userId);

//    Eliminar invitacion
    @POST("tiendas/{tiendaId}/eliminarinvitacion")
    Call<Void> eliminarInvitacion(@Header("Authorization") String token, @Path("tiendaId") int tiendaId, @Body Usuario usuario);
}
