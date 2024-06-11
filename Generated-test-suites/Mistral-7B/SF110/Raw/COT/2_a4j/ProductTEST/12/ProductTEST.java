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
import net.kencochrane.a4j.file.FileUtilTest;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private FileUtil fileUtil;
    private Product product;
    private String asin = "B01N2Z8J17";
    private String offer = "New";
    private String page = "1";

    @Before
    public void setup() {
        fileUtil = new FileUtilTest().getFileUtil();
        product = new Product();
    }

    @Test
    public void testGetProduct_ValidInput_ReturnsExpectedProduct() throws IOException {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        assertNotNull(fileIn);
        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNotNull(fullProduct);
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullInputFile_ReturnsNull() {
        FileInputStream fileIn = null;
        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        ProductInfo productInfoBean = null;
        FullProduct fullProduct = product.getProduct(asin, offer, page, productInfoBean);
        assertNull(fullProduct);
        fileIn.close();
    }

    @Test
    public void testGetProduct_EmptyDetailsArray_ReturnsEmptyProduct() throws IOException {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        ProductInfo productInfoBean = new ProductInfo();
        FullProduct fullProduct = product.getProduct(asin, offer, page, productInfoBean);
        assertNotNull(fullProduct);
        assertEmpty(fullProduct.getDetails());
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullAccessoriesArray_ReturnsEmptyAccessoriesList() throws IOException {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(Arrays.asList(productDetails));
        FullProduct fullProduct = product.getProduct(asin, offer, page, productInfoBean);
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getAccessories().isEmpty());
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullMiniProduct_AddsEmptyAccessoryToFullProduct() throws IOException {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        Accessories accessories = productDetails.getAccessories();
        MiniProduct miniProduct = null;
        ArrayList accessoryArray = new ArrayList();
        accessoryArray.add(accessories);
        productInfoBean.setDetails(Arrays.asList(productDetails));
        FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        List<ProductDetails> detailsArray = Arrays.asList(new ProductDetails());
        accessoryProductInfoBean.setProductsArrayList(detailsArray);
        miniProduct = new MiniProduct();
        fullProduct.addAccessory(miniProduct);
        FullProduct fullProduct = product.getProduct(asin, offer, page, productInfoBean, accessoryProductInfoBean);
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getAccessories().size() == 1);
        fileIn.close();
        accessoryFileIn.close();
    }

    @Test
    public void testGetProduct_NullMiniProduct_AddsEmptySimilarItemsToFullProduct() throws IOException {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        ProductInfo productInfoBean = new ProductInfo();
        ProductInfo similarProductInfoBean = new ProductInfo();
        FullProduct fullProduct = product.getProduct(asin, offer, page, productInfoBean, similarProductInfoBean);
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getSimilarItems().isEmpty());
        fileIn.close();
    }

    @Test
    public void testGetProduct_ValidInputWithSimilarItems_ReturnsExpectedProductWithSimilarItems() throws IOException {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        Accessories accessories = productDetails.getAccessories();
        MiniProduct miniProduct1 = new MiniProduct();
        miniProduct1.setAsin("B01N2Z8J18");
        MiniProduct miniProduct2 = new MiniProduct();
        miniProduct2.setAsin("B01N2Z8J19");
        ArrayList accessoryArray = new ArrayList();
        accessoryArray.add(accessories);
        ProductDetails accessoryProductDetails1 = new ProductDetails();
        ProductDetails accessoryProductDetails2 = new ProductDetails();
        MiniProduct accessoryMiniProduct1 = new MiniProduct();
        MiniProduct accessoryMiniProduct2 = new MiniProduct();
        accessoryProductDetails1.setAccessoryProductDetails(accessoryMiniProduct1);
        accessoryProductDetails2.setAccessoryProductDetails(accessoryMiniProduct2);
        accessoryMiniProduct1.setAsin(miniProduct1.getAsin());
        accessoryMiniProduct1.setImageURL(miniProduct1.getImageURL());
        accessoryMiniProduct1.setManufacturer(miniProduct1.getManufacturer());
        accessoryMiniProduct1.setName(miniProduct1.getName());
        accessoryMiniProduct1.setPrice(miniProduct1.getPrice());
        accessoryMiniProduct2.setAsin(miniProduct2.getAsin());
        accessoryMiniProduct2.setImageURL(miniProduct2.getImageURL());
        accessoryMiniProduct2.setManufacturer(miniProduct2.getManufacturer());
        accessoryMiniProduct2.setName(miniProduct2.getName());
        accessoryMiniProduct2.setPrice(miniProduct2.getPrice());
        List<ProductDetails> detailsArray = Arrays.asList(productDetails, accessoryProductDetails1, accessoryProductDetails2);
        productInfoBean.setDetails(detailsArray);
        FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        List<ProductDetails> similarDetailsArray = Arrays.asList(new ProductDetails());
        accessoryProductInfoBean.setProductsArrayList(similarDetailsArray);
        miniProduct1 = new MiniProduct();
        miniProduct1.setAsin("B01N2Z8J20");
        miniProduct1.setImageURL("https://example.com/image1.jpg");
        miniProduct1.setManufacturer("Manufacturer1");
        miniProduct1.setName("Product1");
        miniProduct1.setPrice(10.0);
        miniProduct2 = new MiniProduct();
        miniProduct2.setAsin("B01N2Z8J21");
        miniProduct2.setImageURL("https://example.com/image2.jpg");
        miniProduct2.setManufacturer("Manufacturer2");
        miniProduct2.setName("Product2");
        miniProduct2.setPrice(15.0);
        List<MiniProduct> similarItems = Arrays.asList(miniProduct1, miniProduct2);
        accessoryProductInfoBean.setProductsArrayList(similarItems);
        fullProduct.addAccessory(miniProduct1);
        fullProduct.addAccessory(miniProduct2);
        fullProduct.addSimilarItem(miniProduct1);
        fullProduct.addSimilarItem(miniProduct2);
        FullProduct fullProduct = product.getProduct(asin, offer, page, productInfoBean, accessoryProductInfoBean);
        assertNotNull(fullProduct);
        assertEquals(4, fullProduct.getAccessories().size());
        assertEquals(2, fullProduct.getSimilarItems().size());
        fileIn.close();
        accessoryFileIn.close();
    }

    @Test(expected = NullPointerException.class)
    public void testGetProduct_NullProductBean_ThrowsNullPointerException() {
        ProductInfo productInfoBean = null;
        FullProduct fullProduct = product.getProduct(asin, offer, page, productInfoBean);
        fail("Expected NullPointerException not thrown");
    }
}