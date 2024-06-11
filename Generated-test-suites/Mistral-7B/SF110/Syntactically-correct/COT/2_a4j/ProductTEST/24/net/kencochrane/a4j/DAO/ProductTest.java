package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setUp() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 // Test method for getProduct(String asin, String offer, String page)
 @Test
 public void testGetProduct() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct(); // Initialize expected product
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 // Add assertions to check the result
 assertNotNull(actualProduct);
 if (expectedProduct != null) {
 assertEquals(expectedProduct.getDetails(), actualProduct.getDetails());
 }
 }

 // Test method for checking null input
 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullAsin() {
 product.getProduct(null, offer, page);
 }

 // Test method for checking null input offer
 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullOffer() {
 product.getProduct(asin, null, page);
 }

 // Test method for checking null input page
 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullPage() {
 product.getProduct(asin, offer, null);
 }

 // Test method for checking empty details array
 @Test
 public void testGetProductWithEmptyDetailsArray() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct();
 expectedProduct.setDetails(new ProductDetails[0]);
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct);
 assertEquals(expectedProduct.getDetails(), actualProduct.getDetails());
 }

 // Test method for checking empty accessory array
 @Test
 public void testGetProductWithEmptyAccessoryArray() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct();
 expectedProduct.setAccessories(new Accessories());
 expectedProduct.getAccessories().setAccessoryArray(new ArrayList<>());
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct);
 assertEquals(expectedProduct.getAccessories().getAccessoryArray(), actualProduct.getAccessories().getAccessoryArray());
 }

 // Test method for checking null accessoryProductDetails
 @Test
 public void testGetProductWithNullAccessoryProductDetails() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct();
 expectedProduct.addAccessory(new MiniProduct());
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct);
 assertEquals(expectedProduct.getAccessories().getAccessoryArray().size(), actualProduct.getAccessories().getAccessoryArray().size());
 }

 // Test method for checking empty similarItems array
 @Test
 public void testGetProductWithEmptySimilarItemsArray() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct();
 expectedProduct.addSimilarItem(new MiniProduct());
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct);
 assertEquals(expectedProduct.getSimilarItems().size(), actualProduct.getSimilarItems().size());
 }

 // Test method for checking null productInfoBean
 @Test
 public void testGetProductWithNullProductInfoBean() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct();
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct);
 assertNull(actualProduct.getDetails());
 }

 // Test method for checking null fileInput
 @Test(expected = FileNotFoundException.class)
 public void testGetProductWithNullFileInput() {
 FileInputStream fileInput = null;
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct();
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct);
 assertNull(actualProduct.getDetails());
 }

 // Test method for checking null productInfoBean in similarItems search
 @Test
 public void testGetProductWithNullProductInfoBeanInSimilarItemsSearch() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FullProduct expectedProduct = new FullProduct();
 expectedProduct.addSimilarItem(new MiniProduct());
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct);
 assertEquals(expectedProduct.getSimilarItems().size(), actualProduct.getSimilarItems().size());
 Search search = new Search();
 ProductInfo productInfoBean = null;
 ProductInfo expectedProductInfoBean = new ProductInfo();
 expectedProductInfoBean.setProductsArrayList(new ArrayList<>());
 assertNotNull(actualProduct.getSimilarItems());
 assertEquals(expectedProduct.getSimilarItems().size(), actualProduct.getSimilarItems().size());
 }
}