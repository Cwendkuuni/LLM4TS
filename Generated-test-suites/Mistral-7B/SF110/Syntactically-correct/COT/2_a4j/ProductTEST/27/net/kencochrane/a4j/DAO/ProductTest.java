package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private Product product;
    private FileUtil fileUtil;
    private Search search;

    @Before
    public void setup() {
        fileUtil = new FileUtil();
        search = new Search();
        product = new Product();
    }

    @Test
    public void testGetProductNullFile() {
        assertNull(product.getProduct("asin1", "offer1", "page1"));
    }

    @Test
    public void testGetProductNullProductInfoBean() {
        FileInputStream fileIn = new FileInputStream(new File("testFile.xml"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNull(product.getProduct("asin1", "offer1", "page1"));
    }

    @Test
    public void testGetProductNullAccessoriesArray() {
        FileInputStream fileIn = new FileInputStream(new File("testFile.xml"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        productInfoBean.setDetails(new ProductDetails[0]);
        assertNull(product.getProduct("asin1", "offer1", "page1"));
    }

    @Test
    public void testGetProductEmptyAccessoryArrayList() {
        FileInputStream fileIn = new FileInputStream(new File("testFile.xml"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productDetails.setAccessoryArray(new ArrayList());
        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(product.getProduct("asin1", "offer1", "page1").hasAccessories());
    }

    @Test
    public void testGetProductNullSimilarItemsArrayList() {
        FileInputStream fileIn = new FileInputStream(new File("testFile.xml"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setProductsArrayList(new ArrayList());
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(product.getProduct("asin1", "offer1", "page1").hasSimilarItems());
    }

    @Test
    public void testGetProductNullAccessoryProductDetailsAndMiniProduct() {
        FileInputStream fileIn = new FileInputStream(new File("testFile.xml"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = null;
        productDetails.setAccessories(accessories);
        productDetails.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(product.getProduct("asin1", "offer1", "page1").hasAccessories());
    }

    @Test
    public void testGetProductNullSearchResult() {
        FileInputStream fileIn = new FileInputStream(new File("testFile.xml"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo searchResult = null;
        productDetails.setAccessories(accessories);
        productDetails.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(product.getProduct("asin1", "offer1", "page1").hasSimilarItems());
    }

    @Test
    public void testGetProductSuccess() {
        FileInputStream fileIn = new FileInputStream(new File("testFile.xml"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo searchResult = new ProductInfo();
        ProductInfo productInfo = new ProductInfo();
        productDetails.setAccessories(accessories);
        productDetails.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        productInfo.setProductsArrayList(Arrays.asList(productInfoBean, searchResult));
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FullProduct fullProduct = product.getProduct("asin1", "offer1", "page1");
        assertTrue(fullProduct.hasAccessories());
        assertTrue(fullProduct.hasSimilarItems());
    }

    @Test(expected = NullPointerException.class)
    public void testGetProductNullAsin() {
        product.getProduct(null, "offer1", "page1");
    }

    @Test(expected = NullPointerException.class)
    public void testGetProductNullOffer() {
        product.getProduct("asin1", null, "page1");
    }

    @Test(expected = NullPointerException.class)
    public void testGetProductNullPage() {
        product.getProduct("asin1", "offer1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetProductEmptyAsin() {
        product.getProduct("", "offer1", "page1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetProductEmptyOffer() {
        product.getProduct("asin1", "", "page1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetProductEmptyPage() {
        product.getProduct("asin1", "offer1", "");
    }

    @Test(expected = IOException.class)
    public void testGetProductIOException() throws IOException {
        FileInputStream fileIn = new FileInputStream(new File("nonexistentFile.xml"));
        assertNull(product.getProduct("asin1", "offer1", "page1"));
        fileIn.close();
    }
}