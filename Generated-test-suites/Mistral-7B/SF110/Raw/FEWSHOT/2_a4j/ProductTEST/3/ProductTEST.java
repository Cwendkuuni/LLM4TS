package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

    @Test
    public void testGetProduct_ValidInput_ReturnsFullProduct() {
        String asin = "B08N5Z4T8J";
        String offer = "New";
        String page = "1";

        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct.getDetails(), actualFullProduct.getDetails());

        if (actualFullProduct.getAccessories() != null && actualFullProduct.getAccessories().getAccessoryArray() != null) {
            assertEquals(expectedFullProduct.getAccessories().getAccessoryArray().size(), actualFullProduct.getAccessories().getAccessoryArray().size());
        }

        if (actualFullProduct.getSimilarItems() != null && actualFullProduct.getSimilarItems().size() > 0) {
            assertEquals(expectedFullProduct.getSimilarItems().size(), actualFullProduct.getSimilarItems().size());
        }
    }

    @Test
    public void testGetProduct_InvalidFile_ReturnsNull() {
        String asin = "B08N5Z4T8J";
        String offer = "New";
        String page = "1";

        FileUtil fileUtil = new FileUtil() {
            @Override
            public FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                return null;
            }
        };

        Product product = new Product();
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        String asin = "B08N5Z4T8J";
        String offer = "New";
        String page = "1";

        Product product = new Product();

        try {
            FileUtil fileUtil = new FileUtil();
            FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn == null) {
                return;
            }

            JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();

            fileIn = fileUtil.fetchAccessories(asin, null);
            if (fileIn != null) {
                joxIn = new JOXBeanInputStream((InputStream)fileIn);
                ProductInfo productInfoBean2 = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
                joxIn.close();
                fileIn.close();
            }

            Search search = new Search();
            ProductInfo productInfoBean3 = search.SimilaritesSearch(asin, "1");

            product = new Product();
            FullProduct actualFullProduct = product.getProduct(asin, offer, page);

            assertNull(actualFullProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray_ReturnsFullProductWithoutAccessories() {
        String asin = "B08N5Z4T8J";
        String offer = "New";
        String page = "1";

        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct.getDetails(), actualFullProduct.getDetails());
        assertNull(actualFullProduct.getAccessories());
        assertNull(actualFullProduct.getSimilarItems());
    }

    @Test
    public void testGetProduct_EmptySimilarItemsList_ReturnsFullProductWithoutSimilarItems() {
        String asin = "B08N5Z4T8J";
        String offer = "New";
        String page = "1";

        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct.getDetails(), actualFullProduct.getDetails());
        assertNotNull(actualFullProduct.getAccessories());
        assertTrue(actualFullProduct.getAccessories().getAccessoryArray().isEmpty());
        assertNull(actualFullProduct.getSimilarItems());
    }
}