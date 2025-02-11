package com.example.agile.request;

import com.example.agile.models.Categoria;
import com.example.agile.models.Producto;
import com.example.agile.models.Tienda;
import com.example.agile.models.Usuario;
import com.example.agile.models.Venta;
import com.example.agile.models.VentaItem;

import java.util.ArrayList;
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

//    ===================================
//    ===== Funciones de categorias =====
//    ===================================

//    Obtener lista de categorias
    @GET("categorias/listar/{tiendaId}")
    Call<List<Categoria>> obtenerCategorias(@Header("Authorization") String token, @Path("tiendaId") int tiendaId);

//    ==================================
//    ===== Funciones de productos =====
//    ==================================

//    Obtener lista de productos
    @GET("productos/listar/{tiendaId}")
    Call<List<Producto>> obtenerProductos(@Header("Authorization") String token, @Path("tiendaId") int tiendaId);

//    Obtener producto
    @GET("productos/obtener/{tiendaId}/{productoId}")
    Call<Producto> obtenerProducto(@Header("Authorization") String token, @Path("tiendaId") int tiendaId, @Path("productoId") int productoId);

//    Crear producto
    @POST("productos/crear/{tiendaId}")
    Call<Void> crearProducto(@Header("Authorization") String token, @Path("tiendaId") int tiendaId, @Body Producto producto);

//    Editar producto
    @PUT("productos/editar/{tiendaId}/{productoId}")
    Call<Void> editarProducto(@Header("Authorization") String token, @Path("tiendaId") int tiendaId, @Path("productoId") int productoId, @Body Producto producto);

//    Eliminar producto
    @DELETE("productos/eliminar/{tiendaId}/{productoId}")
    Call<Void> eliminarProducto(@Header("Authorization") String token, @Path("tiendaId") int tiendaId, @Path("productoId") int productoId);

//    ===============================
//    ===== Funciones de ventas =====
//    ===============================

//    Crear Venta
    @POST("ventas/crear/{tiendaId}")
    Call<Void> crearVenta(@Header("Authorization") String token, @Path("tiendaId") int tiendaId, @Body ArrayList<VentaItem> items);

//    Obtener lista de ventas
    @GET("ventas/listar/{tiendaId}")
    Call<List<Venta>> obtenerVentas(@Header("Authorization") String token, @Path("tiendaId") int tiendaId);
}
