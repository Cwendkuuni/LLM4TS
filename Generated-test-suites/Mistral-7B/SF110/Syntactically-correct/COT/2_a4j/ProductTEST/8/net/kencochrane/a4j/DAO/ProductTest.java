package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.beans.FullProduct;
import net.kencochrane.a4j.DAO.beans.MiniProduct;
import net.kencochrane.a4j.DAO.beans.ProductDetails;
import net.kencochrane.a4j.DAO.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;

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

 // Test case for getProduct method with valid arguments
 @Test
 public void testGetProductWithValidArguments() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 FullProduct expectedFullProduct = new FullProduct();
 ProductDetails expectedProductDetails = new ProductDetails();
 ProductDetails expectedAccessoryProductDetails = new ProductDetails();
 MiniProduct expectedMiniProduct = new MiniProduct();

 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();

 expectedProductDetails = productInfoBean.getDetails()[0];
 expectedFullProduct.setDetails(expectedProductDetails);

 expectedAccessoryProductDetails = expectedProductDetails.getAccessories().getAccessoryArray().get(0);
 expectedMiniProduct.setAsin(expectedAccessoryProductDetails.getAsin());
 expectedMiniProduct.setImageURL(expectedAccessoryProductDetails.getImageUrlSmall());
 expectedMiniProduct.setManufacturer(expectedAccessoryProductDetails.getManufacturer());
 expectedMiniProduct.setName(expectedAccessoryProductDetails.getProductName());
 expectedMiniProduct.setPrice(expectedAccessoryProductDetails.getOurPrice());
 expectedFullProduct.addAccessory(expectedMiniProduct);

 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, Arrays.asList(expectedAccessoryProductDetails.getAccessories().getAccessoryArray()));
 JOXBeanInputStream accessoryJoxIn = new JOXBeanInputStream(accessoryFileIn);
 ProductInfo accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
 accessoryJoxIn.close();
 accessoryFileIn.close();

 List<ProductDetails> accessoryDetailsArray = accessoryProductInfoBean.getProductsArrayList();
 for (int i = 0; i < accessoryDetailsArray.size(); i++) {
 ProductDetails accessoryProductDetails = accessoryDetailsArray.get(i);
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin(accessoryProductDetails.getAsin());
 miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
 miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
 miniProduct.setName(accessoryProductDetails.getProductName());
 miniProduct.setPrice(accessoryProductDetails.getOurPrice());
 expectedFullProduct.addAccessory(miniProduct);
 }

 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 }

 // Test case for getProduct method with null asin
 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullAsin() {
 String offer = "New";
 String page = "1";
 product.getProduct(null, offer, page);
 }

 // Test case for getProduct method with null offer
 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullOffer() {
 String asin = "B08N6Y4XD5";
 String page = "1";
 product.getProduct(asin, null, page);
 }

 // Test case for getProduct method with null page
 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullPage() {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 product.getProduct(asin, offer, null);
 }

 // Test case for getProduct method when ASIN file not found
 @Test
 public void testGetProductWhenASINFileNotFound() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 product.getProduct(asin, offer, page);
 assertNull(product.getProduct(asin, offer, page));
 }

 // Test case for getProduct method when Accessories file not found
 @Test
 public void testGetProductWhenAccessoriesFileNotFound() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 FullProduct expectedFullProduct = new FullProduct();
 ProductDetails expectedProductDetails = new ProductDetails();
 expectedProductDetails.setAccessories(new Accessories());
 expectedFullProduct.setDetails(expectedProductDetails);
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 assertFalse(actualFullProduct.hasAccessories());
 }

 // Test case for getProduct method whenProductInfoBean is null
 @Test
 public void testGetProductWhenProductInfoBeanIsNull() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 product.getProduct(asin, offer, page);
 }

 // Test case for getProduct method when details array is empty
 @Test
 public void testGetProductWhenDetailsArrayIsEmpty() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 FileInputStream fileIn = new FileInputStream("testFile.txt");
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 product.getProduct(asin, offer, page, productInfoBean);
 assertNull(product.getProduct(asin, offer, page));
 }

 // Test case for getProduct method when accessory array is empty
 @Test
 public void testGetProductWhenAccessoryArrayIsEmpty() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 productDetails.setAccessories(new Accessories());
 FullProduct expectedFullProduct = new FullProduct();
 ProductDetails expectedProductDetails = new ProductDetails();
 expectedProductDetails.setAccessories(new Accessories());
 expectedFullProduct.setDetails(expectedProductDetails);
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBeanFromFile = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 List<ProductDetails> detailsArray = productInfoBeanFromFile.getProductsArrayList();
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("ASIN1");
 miniProduct.setImageURL("URL1");
 miniProduct.setManufacturer("Manufacturer1");
 miniProduct.setName("Product1");
 miniProduct.setPrice(10D);
 detailsArray.add(new ProductDetails());
 detailsArray.add(miniProduct);
 productInfoBeanFromFile.setProductsArrayList(detailsArray);
 product.getProduct(asin, offer, page, productInfoBeanFromFile);
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 assertFalse(actualFullProduct.hasAccessories());
 }

 // Test case for getProduct method when SimilaritesSearch returns null
 @Test
 public void testGetProductWhenSimilaritesSearchReturnsNull() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setProductsArrayList(new ArrayList<>());
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBeanFromFile = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 product.getProduct(asin, offer, page, productInfoBean);
 assertNull(product.getProduct(asin, offer, page));
 }

 // Test case for getProduct method when SimilaritesSearch returns empty list
 @Test
 public void testGetProductWhenSimilaritesSearchReturnsEmptyList() throws Exception {
 String asin = "B08N6Y4XD5";
 String offer = "New";
 String page = "1";
 ProductInfo productInfoBean = new ProductInfo();
 List<ProductDetails> productDetailsArrayList = new ArrayList<>();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAsin("ASIN1");
 productDetails.setImageUrlSmall("URL1");
 productDetailsArrayList.add(productDetails);
 productInfoBean.setProductsArrayList(productDetailsArrayList);
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBeanFromFile = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 product.getProduct(asin, offer, page, productInfoBean);
 assertNotNull(product.getProduct(asin, offer, page));
 assertTrue(product.getProduct(asin, offer, page).hasAccessories());
 assertTrue(product.getProduct(asin, offer, page).getSimilarItems().isEmpty());
 }
}