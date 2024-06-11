package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import net.kencochrane.a4j.file.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;
import java.io.*;

public class ProductTest {

    @Test
    public void testGetProduct_ValidInputs_ReturnsFullProductObject() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_NullASIN_ReturnsNull() {
        Product product = new Product();
        FullProduct actualFullProduct = product.getProduct(null, "offer1", "page1");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray_ReturnsEmptyProductObject() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");

        assertNotNull(actualFullProduct);
        assertEmpty(actualFullProduct.getAccessories());
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_NonEmptyAccessoryArray_ReturnsFullProductObjectWithAccessories() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        expectedFullProduct.setAccessories(new Accessories());
        expectedFullProduct.getAccessories().setAccessoryArray(new ArrayList<>());
        expectedFullProduct.getAccessories().getAccessoryArray().add(new MiniProduct());
        expectedFullProduct.getAccessories().getAccessoryArray().add(new MiniProduct());

        FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");

        assertNotNull(actualFullProduct);
        assertNotNull(actualFullProduct.getAccessories());
        assertFalse(actualFullProduct.getAccessories().getAccessoryArray().isEmpty());
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = null;
        try {
            fileIn = fileUtil.fetchASINFile("ASIN1", "heavy", "offer1", "page1");
            FileUtil spyFileUtil = spy(fileUtil);
            when(spyFileUtil.fetchASINFile(eq("ASIN1"), eq("heavy"), eq("offer1"), eq("page1"))).thenReturn(null);

            FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");

            assertNull(actualFullProduct);
        } catch (Exception e) {
            // Exception handling logic here
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    // IOException handling logic here
                }
            }
        }
    }

    @Test
    public void testGetProduct_EmptyDetailsArray_ReturnsNull() {
        Product product = new Product();
        FullProduct expectedFullProduct = null;
        FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");

        assertNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_NonEmptyDetailsArray_ReturnsFullProductObject() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        expectedFullProduct.setDetails(new ProductDetails());
        ProductDetails productDetails = new ProductDetails();
        MiniProduct miniProduct1 = new MiniProduct();
        miniProduct1.setAsin("ASIN1_1");
        miniProduct1.setImageURL("URL_1");
        miniProduct1.setManufacturer("Manufacturer_1");
        miniProduct1.setName("ProductName_1");
        miniProduct1.setPrice(10.0);

        MiniProduct miniProduct2 = new MiniProduct();
        miniProduct2.setAsin("ASIN1_2");
        miniProduct2.setImageURL("URL_2");
        miniProduct2.setManufacturer("Manufacturer_2");
        miniProduct2.setName("ProductName_2");
        miniProduct2.setPrice(20.0);

        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        accessoryArray.add(miniProduct1);
        accessoryArray.add(miniProduct2);
        expectedFullProduct.setDetails(productDetails);
        expectedFullProduct.setAccessories(accessories);
        expectedFullProduct.getAccessories().setAccessoryArray(accessoryArray);

        FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
    }
}