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
    public void testCrearProducto() throws InterruptedException {
        driver.get("http://appspring:8090/productos/nuevo");

        WebElement nombreInput = driver.findElement(By.name("nombre"));
        WebElement precioInput = driver.findElement(By.name("precio"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        nombreInput.sendKeys("NuevoProducto");
        precioInput.clear();
        precioInput.sendKeys("20.0");
        submitButton.click();

        assertEquals("http://appspring:8090/productos", driver.getCurrentUrl());

        WebElement productosTable = driver.findElement(By.id("productos-table"));
        assertTrue(productosTable.getText().contains("NuevoProducto"));
    }

    @Test
    public void testActualizarProducto() throws InterruptedException {
        driver.get("http://appspring:8090/productos");

        WebElement editLink = driver.findElement(By.linkText("Editar"));
        editLink.click();

        WebElement nombreInput = driver.findElement(By.name("nombre"));
        WebElement precioInput = driver.findElement(By.name("precio"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        nombreInput.clear();
        nombreInput.sendKeys("ProductoAEliminar");
        precioInput.clear();
        precioInput.sendKeys("15.0");
        submitButton.click();

        assertEquals("http://appspring:8090/productos", driver.getCurrentUrl());

        WebElement productosTable = driver.findElement(By.id("productos-table"));
        assertTrue(productosTable.getText().contains("ProductoAEliminar"));
    }

    @Test
    public void testEliminarProducto() throws InterruptedException {
        driver.get("http://appspring:8090/productos");

        WebElement deleteLink = driver.findElement(By.linkText("Eliminar"));
        deleteLink.click();

        assertEquals("http://appspring:8090/productos", driver.getCurrentUrl());

        WebElement productosTable = driver.findElement(By.id("productos-table"));
        assertFalse(productosTable.getText().contains("ProductoAEliminar"));
    }
}
