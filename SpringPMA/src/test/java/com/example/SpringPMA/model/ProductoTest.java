package com.example.SpringPMA.model;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class ProductoTest {

    @Test
    public void testGetSetId() {
        Producto producto = new Producto();
        Long id = 123L;
        producto.setId(id);
        assertEquals(id, producto.getId());
    }

    @Test
    public void testGetSetNombre() {
        Producto producto = new Producto();
        String nombre = "Producto de prueba";
        producto.setNombre(nombre);
        assertEquals(nombre, producto.getNombre());
    }

    @Test
    public void testGetSetPrecio() {
        Producto producto = new Producto();
        double precio = 10.5;
        producto.setPrecio(precio);
        assertEquals(precio, producto.getPrecio(), 0.001); // Utilizamos un delta pequeño para manejar la precisión de los números decimales
    }
}
