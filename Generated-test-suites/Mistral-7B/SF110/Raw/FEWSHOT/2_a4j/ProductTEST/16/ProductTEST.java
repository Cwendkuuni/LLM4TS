package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.*;
import net.kencochrane.a4j.file.*;
import java.util.*;
import java.io.*;

public class ProductTest {

    @Test
    public void testGetProduct_NullParameters() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_ValidParameters_NullResult() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("ASIN", "heavy", null, null);
        assertNull(product.getProduct("ASIN", null, null));
        if (fileIn != null) {
            try {
                fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetProduct_ValidParameters_EmptyDetailsArray() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("ASIN", "heavy", null, null);
        JOXBeanInputStream joxIn = null;
        if (fileIn != null) {
            try {
                joxIn = new JOXBeanInputStream((InputStream)fileIn);
                ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
                joxIn.close();
                fileIn.close();
                assertNotNull(productInfoBean);
                assertNotNull(productInfoBean.getDetails());
                assertEquals(0, productInfoBean.getDetails().length);
                assertNull(product.getProduct("ASIN", null, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetProduct_ValidParameters_NullAccessoryArray() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("ASIN", "heavy", null, null);
        JOXBeanInputStream joxIn = null;
        if (fileIn != null) {
            try {
                joxIn = new JOXBeanInputStream((InputStream)fileIn);
                ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
                joxIn.close();
                fileIn.close();
                assertNotNull(productInfoBean);
                assertNotNull(productInfoBean.getDetails());
                assertNotNull(productInfoBean.getDetails()[0].getAccessories());
                ArrayList accessoryArray = productInfoBean.getDetails()[0].getAccessories().getAccessoryArray();
                assertNotNull(accessoryArray);
                assertEquals(0, accessoryArray.size());
                assertNull(product.getProduct("ASIN", null, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetProduct_ValidParameters_EmptySimilarItemsArray() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("ASIN", "heavy", null, null);
        JOXBeanInputStream joxIn = null;
        Search search = new Search();
        ProductInfo productInfoBean = null;
        if (fileIn != null) {
            try {
                joxIn = new JOXBeanInputStream((InputStream)fileIn);
                productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
                joxIn.close();
                fileIn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (productInfoBean != null) {
            ArrayList detailsArray = productInfoBean.getProductsArrayList();
            assertNotNull(detailsArray);
            assertEquals(0, detailsArray.size());
            assertNull(product.getProduct("ASIN", null, null));
        }

        productInfoBean = search.SimilaritesSearch("ASIN", "1");
        if (productInfoBean != null && productInfoBean.getProductsArrayList() != null) {
            detailsArray = productInfoBean.getProductsArrayList();
            assertNotNull(detailsArray);
            assertEquals(0, detailsArray.size());
            assertNull(product.getProduct("ASIN", null, null));
        }
    }

    @Test
    public void testGetProduct_ValidParameters() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("ASIN", "heavy", null, null);
        JOXBeanInputStream joxIn = null;
        ProductInfo productInfoBean = null;
        FullProduct fullProduct = null;
        if (fileIn != null) {
            try {
                joxIn = new JOXBeanInputStream((InputStream)fileIn);
                productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
                joxIn.close();
                fileIn.close();
                assertNotNull(productInfoBean);
                assertNotNull(productInfoBean.getDetails());
                assertNotNull(productInfoBean.getDetails()[0].getAccessories());
                ArrayList accessoryArray = productInfoBean.getDetails()[0].getAccessories().getAccessoryArray();
                assertNotNull(accessoryArray);
                assertFalse(accessoryArray.isEmpty());
                ArrayList detailsArray = productInfoBean.getProductsArrayList();
                assertNotNull(detailsArray);
                assertFalse(detailsArray.isEmpty());
                fullProduct = product.getProduct("ASIN", null, null);
                assertNotNull(fullProduct);
                assertNotNull(fullProduct.getDetails());
                assertNotNull(fullProduct.getAccessories());
                assertFalse(fullProduct.getAccessories().isEmpty());
                assertNotNull(fullProduct.getSimilarItems());
                assertFalse(fullProduct.getSimilarItems().isEmpty());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}