package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.ProductDetails;
import net.kencochrane.a4j.DAO.ProductInfo;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.utils.Search;

import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProduct_ValidInput_ReturnsFullProduct() {
        Product product = new Product();
        FullProduct expectedProduct = new FullProduct();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testGetProduct_InvalidFile_ReturnsNull() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        Product actualProduct = product.getProduct(asin, offer, page);
        assertNull(actualProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        FileInputStream fileIn = new FileInputStream("test.txt");
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;
        try {
            productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product actualProduct = product.getProduct(asin, offer, page);
        assertNull(actualProduct);
    }

    @Test
    public void testGetProduct_NullAccessoryProductDetails_DoesNotAddAccessory() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchAccessories(asin, Arrays.asList("accessory1"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;
        try {
            productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product actualProduct = product.getProduct(asin, offer, page);
        assertEquals(0, actualProduct.getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProduct_NullSimilarItems_DoesNotAddSimilarItems() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        Search search = new Search();
        ProductInfo productInfoBean = new ProductInfo();
        List<ProductDetails> detailsArray = Arrays.asList(new ProductDetails());
        productInfoBean.setProductsArrayList(detailsArray);
        Product actualProduct = product.getProduct(asin, offer, page);
        assertEquals(0, actualProduct.getSimilarItems().size());
    }

    @Test(expected = Exception.class)
    public void testGetProduct_ExceptionThrown() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;
        try {
            productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            throw new Exception("Exception in getProduct method", e);
        }
        if (productInfoBean == null || productInfoBean.getDetails() == null || productInfoBean.getDetails().length == 0) {
            throw new Exception("Exception in getProduct method: Invalid ProductInfoBean");
        }
        Product actualProduct = product.getProduct(asin, offer, page);
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray_DoesNotAddAccessories() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchAccessories(asin, Arrays.asList("accessory1"));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBeanFromAccessories = null;
        try {
            productInfoBeanFromAccessories = (ProductInfo)joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (productInfoBeanFromAccessories != null && productInfoBeanFromAccessories.getProductsArrayList() != null && productInfoBeanFromAccessories.getProductsArrayList().size() > 0) {
            List<ProductDetails> detailsArray = productInfoBeanFromAccessories.getProductsArrayList();
            for (ProductDetails accessoryProductDetails : detailsArray) {
                MiniProduct miniProduct = new MiniProduct();
                miniProduct.setAsin(accessoryProductDetails.getAsin());
                miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                miniProduct.setName(accessoryProductDetails.getProductName());
                miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                product.getProduct(asin, offer, page).addAccessory(miniProduct);
            }
        }
        Product actualProduct = product.getProduct(asin, offer, page);
        assertEquals(0, actualProduct.getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProduct_EmptySimilarItemsList_DoesNotAddSimilarItems() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        ProductInfo productInfoBean = new ProductInfo();
        Search search = new Search();
        productInfoBean.setProductsArrayList(null);
        Product actualProduct = product.getProduct(asin, offer, page);
        assertEquals(0, actualProduct.getSimilarItems().size());
    }
}