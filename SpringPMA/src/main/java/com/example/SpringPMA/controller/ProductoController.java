package com.example.SpringPMA.controller;

import com.example.SpringPMA.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductoController {

    private final List<Producto> productos = new ArrayList<>();
    private long idActual = 1;

    @GetMapping("/productos")
    public String obtenerProductos(Model model) {
        model.addAttribute("productos", productos);
        return "productos/productos";
    }

    @GetMapping("/productos/{id}")
    public String obtenerProductoPorId(@PathVariable Long id, Model model) {
        Producto productoEncontrado = productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("producto", productoEncontrado);
        return "productos/producto"; // Producto encontrado, devuelve 200 OK
    }

    @RequestMapping(value = "/productos/nuevo", method = RequestMethod.GET)
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/nuevo";
    }

    @RequestMapping(value = "/productos/nuevo", method = RequestMethod.POST)
    public String crearProducto(@ModelAttribute Producto nuevoProducto) {
        nuevoProducto.setId(idActual++);
        productos.add(nuevoProducto);
        return "redirect:/productos";
    }

    @RequestMapping(value="/productos/editar/{id}", method = RequestMethod.GET)
    public String mostrarFormularioEditarProducto(@PathVariable Long id, Model model) {
        Producto productoEncontrado = productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst()
                .orElse(null);;
        model.addAttribute("producto", productoEncontrado);
        return "productos/editar";
    }

    @RequestMapping(value = "/productos/editar/{id}", method = RequestMethod.POST)
    public String actualizarProducto(@PathVariable Long id, Producto productoActualizado) {
        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            if (producto.getId().equals(id)) {
                producto.setNombre(productoActualizado.getNombre());
                producto.setPrecio(productoActualizado.getPrecio());
                return "redirect:/productos";
            }
        }
        return null;
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productos.removeIf(producto -> producto.getId().equals(id));
        return "redirect:/productos";
    }
}