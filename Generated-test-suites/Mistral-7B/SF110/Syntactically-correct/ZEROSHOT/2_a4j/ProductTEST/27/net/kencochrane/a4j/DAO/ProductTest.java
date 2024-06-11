package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.util.Search;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;
 private String asin = "B000123456";
 private String offer = "new";
 private String page = "1";
 private String asinAccessory = "B000234567";

 @Before
 public void setup() {
 fileUtil = new FileUtil();
 product = new Product();
 }

 @Test
 public void testGetProduct_ValidInput_ReturnsProduct() {
 File file = new File("src/test/resources/output/" + asin + "_" + offer + "_" + page + ".xml");
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 fileIn = (FileInputStream) fileUtil.fetchASINFile(asin, "heavy", offer, page);
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 fail("Failed to load product file");
 }

 assertNotNull(productInfoBean);
 assertNotNull(productInfoBean.getDetails());
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertEquals(productDetails.getProductName(), fullProduct.getDetails().getName());
 assertEquals(productDetails.getOurPrice().doubleValue(), fullProduct.getDetails().getOurPrice().doubleValue(), 0.01);

 Accessories accessories = productDetails.getAccessories();
 ArrayList accessoryArray = accessories.getAccessoryArray();
 if (accessoryArray != null && accessoryArray.size() > 0) {
 try {
 File accessoryFile = new File("src/test/resources/output/" + asin + "_accessories_" + asinAccessory + ".xml");
 fileIn = (FileInputStream) fileUtil.fetchAccessories(asin, accessoryArray);
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 fail("Failed to load accessories file");
 }

 assertNotNull(productInfoBean);
 assertNotNull(productInfoBean.getProductsArrayList());
 List<MiniProduct> similarItems = fullProduct.getSimilarItems();
 assertEquals(productInfoBean.getProductsArrayList().size(), similarItems.size());
 }
}

 @Test
 public void testGetProduct_InvalidASIN_ReturnsNull() {
 ProductInfo productInfoBean = product.getProduct(asin, offer, page);
 assertNull(productInfoBean);
 }

 @Test
 public void testGetProduct_InvalidOffer_ReturnsNull() {
 ProductInfo productInfoBean = product.getProduct(asin, "invalid", page);
 assertNull(productInfoBean);
 }

 @Test
 public void testGetProduct_InvalidPage_ReturnsNull() {
 ProductInfo productInfoBean = product.getProduct(asin, offer, "invalid");
 assertNull(productInfoBean);
 }
}