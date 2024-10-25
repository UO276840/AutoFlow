package com.example.SpringPMA.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoViewTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("firefox");
        capabilities.setCapability("selenoid:options", new HashMap<String, Object>() {{
            // How to add test badge
            put("name", "Test badge...");

            // How to set session timeout
            put("sessionTimeout", "15m");

            // How to set timezone
            put("env", new ArrayList<String>() {{
                add("TZ=UTC");
            }});

            // How to add "trash" button
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});

            // How to enable video recording
            put("enableVideo", true);
            put("enableVNC", true);
        }});
        driver = new RemoteWebDriver(new URL("http://host.docker.internal:4444/wd/hub"), capabilities);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCrearActualizarEliminarProducto() throws InterruptedException {
        driver.get("http://appspring:8090/productos/nuevo");

        // Crear un producto para eliminar
        WebElement nombreInput = driver.findElement(By.name("nombre"));
        WebElement precioInput = driver.findElement(By.name("precio"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        nombreInput.sendKeys("NuevoProducto");
        precioInput.clear();
        precioInput.sendKeys("20.0");
        submitButton.click();

        // Navegar a la página de edición del producto recién creado
        assertEquals("http://appspring:8090/productos", driver.getCurrentUrl());

        // Verificar que el producto aparece en la lista
        WebElement productosTable = driver.findElement(By.id("productos-table"));
        assertTrue(productosTable.getText().contains("NuevoProducto"));

        //Buscamos el boton para editar el producto
        WebElement editLink = driver.findElement(By.linkText("Editar"));
        editLink.click();
        submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        nombreInput = driver.findElement(By.name("nombre"));
        precioInput = driver.findElement(By.name("precio"));

        //Editamos el producto
        nombreInput.clear();
        nombreInput.sendKeys("ProductoAEliminar");
        precioInput.clear();
        precioInput.sendKeys("15.0");
        submitButton.click();

        assertEquals("http://appspring:8090/productos", driver.getCurrentUrl());

        // Verificar que el producto aparece en la lista
        productosTable = driver.findElement(By.id("productos-table"));
        assertTrue(productosTable.getText().contains("ProductoAEliminar"));

        // Eliminar el producto recién creado
        WebElement deleteLink = driver.findElement(By.linkText("Eliminar"));
        deleteLink.click();

        // Verificar redirección a /view/productos
        assertEquals("http://appspring:8090/productos", driver.getCurrentUrl());

        // Verificar que el producto no aparece en la lista
        productosTable = driver.findElement(By.id("productos-table"));
        assertFalse(productosTable.getText().contains("ProductoAEliminar"));
    }
}
