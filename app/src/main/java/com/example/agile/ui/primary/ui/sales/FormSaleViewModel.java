package com.example.agile.ui.primary.ui.sales;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agile.models.Categoria;
import com.example.agile.models.Producto;
import com.example.agile.models.VentaItem;
import com.example.agile.request.ApiClient;
import com.example.agile.request.EndpointAgile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormSaleViewModel extends AndroidViewModel {

    private Context context;
    String query = "";

    private MutableLiveData<String> precioTotal = new MutableLiveData<>("$0.00");

//    Sheet menu
    private MutableLiveData<Boolean> isMenuExpanded = new MutableLiveData<>();

//    Loading
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

//    Categorías
    MutableLiveData<ArrayList<Categoria>> categorias = new MutableLiveData<>();
    Set<Integer> categoriasSeleccionadas = new HashSet<>();

//    Productos
    MutableLiveData<ArrayList<Producto>> productos = new MutableLiveData<>();
    MutableLiveData<ArrayList<Producto>> productosFiltrados = new MutableLiveData<>();
    MutableLiveData<ArrayList<VentaItem>> productosSeleccionados = new MutableLiveData<>(new ArrayList<>());

    public FormSaleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        isMenuExpanded.setValue(false);
        obtenerCategorias();
        obtenerProductos();
    }

    //    Getters and setters
    public MutableLiveData<String> getPrecioTotal() { return precioTotal; }

    public MutableLiveData<Boolean> getIsMenuExpanded() { return isMenuExpanded; }

    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }

    public MutableLiveData<ArrayList<Categoria>> getCategorias() { return categorias; }
    public MutableLiveData<ArrayList<Producto>> getProductosFiltrados() { return productosFiltrados; }

    public MutableLiveData<ArrayList<VentaItem>> getProductosSeleccionados() { return productosSeleccionados; }

    public void setQuery(String query) { this.query = query; }
    public void setPrecioTotal(String precioTotal) { this.precioTotal.setValue(precioTotal); }
    public void setIsMenuExpanded(boolean isMenuExpanded) { this.isMenuExpanded.setValue(isMenuExpanded); }

    public void setCategoriasSeleccionadas(Set<Integer> categoriasSeleccionadas) { this.categoriasSeleccionadas = categoriasSeleccionadas; }

//    Methods
    public void obtenerCategorias() {
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storeId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<List<Categoria>> call = endpoint.obtenerCategorias(token, storeId);

        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categorias.setValue((ArrayList<Categoria>) response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener las categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerProductos() {
        isLoading.setValue(true);
        SharedPreferences sp = context.getSharedPreferences("agile.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        int storeId = sp.getInt("selected_store_id", -1);

        if (token.isEmpty()) {
            return;
        }

        EndpointAgile endpoint = ApiClient.getEndpoint();
        Call<List<Producto>> call = endpoint.obtenerProductos(token, storeId);

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Producto> productosList = (ArrayList<Producto>) response.body();
                    productos.setValue(productosList);
                    productosFiltrados.setValue(productosList);

                    isLoading.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener los productos", Toast.LENGTH_SHORT).show();

                isLoading.setValue(false);
            }
        });
    }

    public void filtrarProductos() {
        if (productos.getValue() == null) return;

        ArrayList<Producto> filteredProducts = new ArrayList<>();
        for (Producto p : productos.getValue()) {
            boolean matchesSearch = p.getNombre().toLowerCase().contains(query.toLowerCase());
            boolean matchesCategory = categoriasSeleccionadas.isEmpty() || categoriasSeleccionadas.contains(p.getCategoriaId());
            if (matchesSearch && matchesCategory) {
                filteredProducts.add(p);
            }
        }

        productosFiltrados.setValue(filteredProducts);
    }

    public void agregarProductoALaVenta(int id) {
        ArrayList<VentaItem> listaActual = productosSeleccionados.getValue();

//        Verificar que no esté el producto ya en la lista
        for (VentaItem i : listaActual) {
            if (i.getProductoId() == id) {
                Toast.makeText(context, "Producto ya agregado", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Producto producto = obtenerProductoConId(id);
        if (producto == null) return;

//        Agregamos uno nuevo a la lista
        listaActual.add(new VentaItem(id, 1, producto.getPrecio(), producto.getNombre()));
        productosSeleccionados.setValue(listaActual);
        setIsMenuExpanded(true);
        actualizarPrecioTotal();
    }

    public void codigoEscaneado(String codigo) {
        ArrayList<Producto> listaProductos = productos.getValue();
        Producto producto = null;
        if (listaProductos == null) return;

//        Buscamos el producto
        for (Producto p : listaProductos) {
            if (p.getCodigo().equals(codigo)) {
                producto = p;
                break;
            }
        }

        if (producto == null) {
            Toast.makeText(context, "No se encontro el producto", Toast.LENGTH_SHORT).show();
        }

        agregarProductoALaVenta(producto.getProductoId());
    }

    public void sumarCantidad(int productoId) {
        ArrayList<VentaItem> listaActual = productosSeleccionados.getValue();
        Producto producto = obtenerProductoConId(productoId);

        for (VentaItem item : listaActual) {
            if (item.getProductoId() == productoId) {
                if (item.getCantidad() < producto.getStock()) {
                    item.setCantidad(item.getCantidad() + 1);
                    productosSeleccionados.setValue(listaActual);
                } else {
                    Toast.makeText(context, "Stock insuficiente", Toast.LENGTH_SHORT).show();
                }

                actualizarPrecioTotal();
                return;
            }
        }
    }

    public void restarCantidad(int productoId) {
        ArrayList<VentaItem> listaActual = productosSeleccionados.getValue();

        for (VentaItem item : listaActual) {
            if (item.getProductoId() == productoId) {
                if (item.getCantidad() > 1) {
                    item.setCantidad(item.getCantidad() - 1);
                } else {
                    listaActual.remove(item); // Sacar si la cantidad es 0
                }

                productosSeleccionados.setValue(listaActual);
                actualizarPrecioTotal();
                return;
            }
        }
    }

    private Producto obtenerProductoConId(int id) {
        ArrayList<Producto> listaProductos = productos.getValue();

        if (listaProductos != null) {
            for (Producto p : listaProductos) {
                if (p.getProductoId() == id) return p;
            }
        }

        return null;
    }

    private void actualizarPrecioTotal() {
        ArrayList<VentaItem> listaActual = productosSeleccionados.getValue();
        float total = 0f;

        for (VentaItem item : listaActual) {
            total += item.getPrecioUnidad() * item.getCantidad();
        }

        precioTotal.setValue(formatPrecio(total));
    }

    private String formatPrecio(float precio) {
        float f = Math.round(precio * 100f) / 100f;

        DecimalFormat df = new DecimalFormat("$#0.00");
        return df.format(f);
    }
}