package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;
 private String asin = "B000012345";
 private String offer = "New";
 private String page = "1";

 @Before
 public void setup() {
 fileUtil = new FileUtil();
 product = new Product();
 }

 @Test
 public void testGetProductNullFile() {
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test
 public void testGetProductInvalidProductInfo() {
 File file = new File("src/test/resources/invalidProductInfo.xml");
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 fileIn = new FileInputStream(file);
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
}
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test
 public void testGetProductValidProduct() {
 File file = new File("src/test/resources/validProduct.xml");
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 fileIn = new FileInputStream(file);
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
}
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNotNull(fullProduct);
 ProductDetails productDetails = fullProduct.getDetails();
 assertNotNull(productDetails);
 Accessories accessories = productDetails.getAccessories();
 ArrayList accessoryArray = accessories.getAccessoryArray();
 assertNotNull(accessoryArray);
 for (int i = 0; i < accessoryArray.size(); i++) {
 MiniProduct miniProduct = fullProduct.getAccessories().get(i);
 assertNotNull(miniProduct.getAsin());
 assertNotNull(miniProduct.getImageURL());
 assertNotNull(miniProduct.getManufacturer());
 assertNotNull(miniProduct.getName());
 assertNotNull(miniProduct.getPrice());
}
}

 @Test
 public void testGetProductEmptyAccessories() {
 File file = new File("src/test/resources/emptyAccessories.xml");
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 fileIn = new FileInputStream(file);
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
}
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNotNull(fullProduct);
 ProductDetails productDetails = fullProduct.getDetails();
 assertNotNull(productDetails);
 Accessories accessories = productDetails.getAccessories();
 ArrayList accessoryArray = accessories.getAccessoryArray();
 assertTrue(accessoryArray.isEmpty());
}

 @Test
 public void testGetProductValidAccessories() {
 File file = new File("src/test/resources/validAccessories.xml");
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 fileIn = new FileInputStream(file);
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
}
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNotNull(fullProduct);
 ProductDetails productDetails = fullProduct.getDetails();
 assertNotNull(productDetails);
 Accessories accessories = productDetails.getAccessories();
 ArrayList accessoryArray = accessories.getAccessoryArray();
 assertFalse(accessoryArray.isEmpty());
 for (int i = 0; i < accessoryArray.size(); i++) {
 MiniProduct miniProduct = fullProduct.getAccessories().get(i);
 assertNotNull(miniProduct.getAsin());
 assertNotNull(miniProduct.getImageURL());
 assertNotNull(miniProduct.getManufacturer());
 assertNotNull(miniProduct.getName());
 assertNotNull(miniProduct.getPrice());
}
}

 @Test
 public void testGetProductSimilarItems() {
 File file = new File("src/test/resources/validProductWithSimilarItems.xml");
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 fileIn = new FileInputStream(file);
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
}
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNotNull(fullProduct);
 ProductDetails productDetails = fullProduct.getDetails();
 assertNotNull(productDetails);
 List<MiniProduct> similarItems = fullProduct.getSimilarItems();
 assertFalse(similarItems.isEmpty());
 for (int i = 0; i < similarItems.size(); i++) {
 MiniProduct miniProduct = fullProduct.getSimilarItems().get(i);
 assertNotNull(miniProduct.getAsin());
 assertNotNull(miniProduct.getImageURL());
 assertNotNull(miniProduct.getManufacturer());
 assertNotNull(miniProduct.getName());
 assertNotNull(miniProduct.getPrice());
}
}

}