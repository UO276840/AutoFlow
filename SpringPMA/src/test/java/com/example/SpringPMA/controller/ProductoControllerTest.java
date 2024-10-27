package com.example.SpringPMA.controller;

import com.example.SpringPMA.SpringPMAApplication;
import com.example.SpringPMA.model.Producto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringJUnitConfig
@SpringBootTest(classes = SpringPMAApplication.class)
@AutoConfigureMockMvc
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void crearActualizarProducto() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("ProductoExistente");
        producto.setPrecio(20.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/productos/nuevo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombre", producto.getNombre())
                        .param("precio", String.valueOf(producto.getPrecio())))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/productos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("productos/productos"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("productos"))
                .andExpect(MockMvcResultMatchers.model().attribute("productos", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("productos", Matchers.hasItem(
                        Matchers.hasProperty("nombre", Matchers.is("ProductoExistente"))
                )));

        Producto productoCreado = (Producto) ((List<?>) mockMvc.perform(MockMvcRequestBuilders.get("/productos"))
                .andReturn()
                .getModelAndView()
                .getModel()
                .get("productos")).get(0);

        // Actualizar el producto creado
        mockMvc.perform(MockMvcRequestBuilders.post("/productos/editar/{id}", productoCreado.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombre", "ProductoActualizado")
                        .param("precio", "25.0"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/productos"));

        // Obtener el producto actualizado
        mockMvc.perform(MockMvcRequestBuilders.get("/productos/{id}", productoCreado.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("productos/producto"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("producto"))
                .andExpect(MockMvcResultMatchers.model().attribute("producto",
                        Matchers.hasProperty("nombre", Matchers.is("ProductoActualizado"))))
                .andExpect(MockMvcResultMatchers.model().attribute("producto",
                        Matchers.hasProperty("precio", Matchers.is(25.0))));
    }

    @Test
    public void crearEliminarProducto() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("ProductoAEliminar");
        producto.setPrecio(15.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/productos/nuevo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombre", producto.getNombre())
                        .param("precio", String.valueOf(producto.getPrecio())))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/productos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("productos/productos"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("productos"))
                .andExpect(MockMvcResultMatchers.model().attribute("productos", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("productos", Matchers.hasItem(
                        Matchers.hasProperty("nombre", Matchers.is("ProductoAEliminar"))
                )));

        Producto productoCreado = (Producto) ((List<?>) mockMvc.perform(MockMvcRequestBuilders.get("/productos"))
                .andReturn()
                .getModelAndView()
                .getModel()
                .get("productos")).get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/eliminar/{id}", productoCreado.getId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/productos"));

        mockMvc.perform(MockMvcRequestBuilders.get("/productos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("productos/productos"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("productos"))
                .andExpect(MockMvcResultMatchers.model().attribute("productos", Matchers.not(
                        Matchers.hasItem(Matchers.hasProperty("nombre", Matchers.is("ProductoAEliminar"))))));
    }
}
