package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;

public class ProductTest {

    @Test
    public void testGetProduct_NullInputs() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_EmptyFiles() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        assertNull(product.getProduct("123", "offer", "page"));
    }

    @Test
    public void testGetProduct_EmptyDetailsArray() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        File asinFile = new File("path/to/asin/file");
        FileInputStream fileIn = new FileInputStream(asinFile);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        FullProduct fullProduct = product.getProduct("123", "offer", "page");
        assertEquals(0, fullProduct.getDetails().length);
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        File asinFile = new File("path/to/asin/file");
        FileInputStream fileIn = new FileInputStream(asinFile);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        ProductDetails productDetails = productInfoBean.getDetails()[0];
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();

        File accessoryFile = new File("path/to/accessory/file");
        FileInputStream accessoryFileInput = new FileInputStream(accessoryFile);
        JOXBeanInputStream accessoryJoxIn = new JOXBeanInputStream(accessoryFileInput);
        ProductInfo accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
        accessoryJoxIn.close();
        accessoryFileInput.close();

        fullProduct = product.getProduct("123", "offer", "page");
        assertEquals(0, fullProduct.getAccessories().size());
    }

    @Test
    public void testGetProduct_NullAccessoryProductDetails() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        File asinFile = new File("path/to/asin/file");
        FileInputStream fileIn = new FileInputStream(asinFile);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        ProductDetails productDetails = productInfoBean.getDetails()[0];
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();

        File accessoryFile = new File("path/to/accessory/file");
        FileInputStream accessoryFileInput = new FileInputStream(accessoryFile);
        JOXBeanInputStream accessoryJoxIn = new JOXBeanInputStream(accessoryFileInput);
        ProductInfo accessoryProductInfoBean = null;
        try {
            accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
        } catch (Exception e) {
            // Do nothing.
        }
        accessoryJoxIn.close();
        accessoryFileInput.close();

        fullProduct = product.getProduct("123", "offer", "page");
        assertEquals(0, fullProduct.getAccessories().size());
    }

    @Test
    public void testGetProduct_NonEmptyAccessoryArray() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        File asinFile = new File("path/to/asin/file");
        FileInputStream fileIn = new FileInputStream(asinFile);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        ProductDetails productDetails = productInfoBean.getDetails()[0];
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();

        File accessoryFile = new File("path/to/accessory/file");
        FileInputStream accessoryFileInput = new FileInputStream(accessoryFile);
        JOXBeanInputStream accessoryJoxIn = new JOXBeanInputStream(accessoryFileInput);
        ProductInfo accessoryProductInfoBean = null;
        try {
            accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
        } catch (Exception e) {
            fail("Failed to read accessory file.");
        }
        joxIn.close();
        accessoryJoxIn.close();
        accessoryFileInput.close();

        MiniProduct miniProduct = null;
        for (int i = 0; i < accessoryArray.size(); i++) {
            miniProduct = new MiniProduct();
            miniProduct.setAsin(accessoryProductInfoBean.getProductsArrayList().get(i).getAsin());
            miniProduct.setImageURL(accessoryProductInfoBean.getProductsArrayList().get(i).getImageUrlSmall());
            miniProduct.setManufacturer(accessoryProductInfoBean.getProductsArrayList().get(i).getManufacturer());
            miniProduct.setName(accessoryProductInfoBean.getProductsArrayList().get(i).getProductName());
            miniProduct.setPrice(accessoryProductInfoBean.getProductsArrayList().get(i).getOurPrice());
            fullProduct.addAccessory(miniProduct);
            accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
        }

        assertEquals(accessoryArray.size(), fullProduct.getAccessories().size());
    }

    @Test
    public void testGetProduct_NonEmptySimilarItemsArray() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        File asinFile = new File("path/to/asin/file");
        FileInputStream fileIn = new FileInputStream(asinFile);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        fullProduct = product.getProduct("123", "offer", "page");
        List<MiniProduct> similarItems = fullProduct.getSimilarItems();

        assertFalse(similarItems.isEmpty());
        for (MiniProduct currentItem : similarItems) {
            assertNotNull(currentItem);
            assertNotNull(currentItem.getAsin());
            assertNotNull(currentItem.getImageURL());
            assertNotNull(currentItem.getManufacturer());
            assertNotNull(currentItem.getName());
            assertNotNull(currentItem.getPrice());
        }
    }
}