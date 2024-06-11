package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

    @Test
    public void testGetProduct_NullInputs() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_EmptyFile() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProduct_InvalidProductInfo() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", "offer", "page");
        JOXBeanInputStream joxIn = null;
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            assertNull(product.getProduct("asin", "offer", "page"));
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testGetProduct_ValidProduct() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", "offer", "page");
        JOXBeanInputStream joxIn = null;
        FullProduct fullProduct = null;
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            fullProduct = product.getProduct("asin", "offer", "page");
            assertNotNull(fullProduct);
            assertNotNull(fullProduct.getDetails());
            ProductDetails productDetails = fullProduct.getDetails();
            assertNotNull(productDetails.getAccessories());
            Accessories accessories = productDetails.getAccessories();
            ArrayList accessoryArray = accessories.getAccessoryArray();
            assertNotNull(accessoryArray);
            ArrayList detailsArray = productInfoBean.getProductsArrayList();
            for (int x = 0; x < detailsArray.size(); ++x) {
                ProductDetails accessoryProductDetails = (ProductDetails) detailsArray.get(x);
                MiniProduct miniProduct = new MiniProduct();
                miniProduct.setAsin(accessoryProductDetails.getAsin());
                miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                miniProduct.setName(accessoryProductDetails.getProductName());
                miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                fullProduct.addAccessory(miniProduct);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetProduct_EmptyAccessoriesFile() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        ProductDetails productDetails = fullProduct.getDetails();
        assertNotNull(productDetails.getAccessories());
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();
        assertEquals(0, accessoryArray.size());
    }

    @Test
    public void testGetProduct_InvalidAccessoryFile() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchAccessories("asin", Collections.singletonList("invalidAsin"));
        JOXBeanInputStream joxIn = null;
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            assertNull(product.getProduct("asin", "offer", "page"));
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testGetProduct_ValidAccessories() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        ProductDetails productDetails = fullProduct.getDetails();
        assertNotNull(productDetails.getAccessories());
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();
        assertNotNull(accessoryArray);
        FileInputStream fileIn = fileUtil.fetchAccessories("asin", accessoryArray);
        JOXBeanInputStream joxIn = null;
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            ArrayList detailsArray = productInfoBean.getProductsArrayList();
            for (int x = 0; x < detailsArray.size(); ++x) {
                ProductDetails accessoryProductDetails = (ProductDetails) detailsArray.get(x);
                MiniProduct miniProduct = new MiniProduct();
                miniProduct.setAsin(accessoryProductDetails.getAsin());
                miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                miniProduct.setName(accessoryProductDetails.getProductName());
                miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                fullProduct.addAccessory(miniProduct);
            }
            assertEquals(2, fullProduct.getAccessories().getAccessoryArray().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetProduct_ValidSimilarItems() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        ProductDetails productDetails = fullProduct.getDetails();
        assertNotNull(productDetails.getAccessories());
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();
        assertNotNull(accessoryArray);
        FileInputStream fileIn = fileUtil.fetchAccessories("asin", accessoryArray);
        JOXBeanInputStream joxIn = null;
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            ArrayList detailsArray = productInfoBean.getProductsArrayList();
            for (int x = 0; x < detailsArray.size(); ++x) {
                ProductDetails accessoryProductDetails = (ProductDetails) detailsArray.get(x);
                MiniProduct miniProduct = new MiniProduct();
                miniProduct.setAsin(accessoryProductDetails.getAsin());
                miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                miniProduct.setName(accessoryProductDetails.getProductName());
                miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                fullProduct.addAccessory(miniProduct);
            }
            Search search = new Search();
            ProductInfo productInfoBean2 = search.SimilaritesSearch("asin", "1");
            assertNotNull(productInfoBean2);
            assertNotNull(productInfoBean2.getProductsArrayList());
            detailsArray = productInfoBean2.getProductsArrayList();
            for (int x = 0; x < detailsArray.size(); ++x) {
                ProductDetails accessoryProductDetails2 = (ProductDetails) detailsArray.get(x);
                MiniProduct miniProduct2 = new MiniProduct();
                miniProduct2.setAsin(accessoryProductDetails2.getAsin());
                miniProduct2.setImageURL(accessoryProductDetails2.getImageUrlSmall());
                miniProduct2.setManufacturer(accessoryProductDetails2.getManufacturer());
                miniProduct2.setName(accessoryProductDetails2.getProductName());
                miniProduct2.setPrice(accessoryProductDetails2.getOurPrice());
                fullProduct.addSimilarItem(miniProduct2);
            }
            assertEquals(3, fullProduct.getAccessories().getAccessoryArray().size());
            assertEquals(2, fullProduct.getSimilarItems().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetProduct_EmptySimilarItemsFile() {
        Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getDetails());
        ProductDetails productDetails = fullProduct.getDetails();
        assertNotNull(productDetails.getAccessories());
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();
        assertNotNull(accessoryArray);
        FileInputStream fileIn = fileUtil.fetchAccessories("asin", accessoryArray);
        JOXBeanInputStream joxIn = null;
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            ArrayList detailsArray = productInfoBean.getProductsArrayList();
            for (int x = 0; x < detailsArray.size(); ++x) {
                ProductDetails accessoryProductDetails = (ProductDetails) detailsArray.get(x);
                MiniProduct miniProduct = new MiniProduct();
                miniProduct.setAsin(accessoryProductDetails.getAsin());
                miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                miniProduct.setName(accessoryProductDetails.getProductName());
                miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                fullProduct.addAccessory(miniProduct);
            }
            ProductInfo productInfoBean2 = search.SimilaritesSearch("asin", "1");
            assertNull(productInfoBean2);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}