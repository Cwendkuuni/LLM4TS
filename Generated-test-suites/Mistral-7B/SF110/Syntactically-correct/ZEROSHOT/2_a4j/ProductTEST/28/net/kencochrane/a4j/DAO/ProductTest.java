package net.kencochrane.a4j.DAO.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.*;
import net.kencochrane.a4j.beans.*;
import java.util.*;
import java.io.*;

public class ProductTest {

 private Product product;
 private FullProduct fullProduct;
 private String asin = "ASIN_1";
 private String offer = "offer1";
 private String page = "page1";
 private String asinAccessory = "ASIN_ACCESSORY_1";
 private String[] accessoryASINs = {"ASIN_ACCESSORY_1", "ASIN_ACCESSORY_2"};

 @Before
 public void setUp() {
 product = new Product();
 fullProduct = product.getProduct(asin, offer, page);
 }

 @Test
 public void testGetProductNullInputFile() {
 product = new Product();
 FileUtil fileUtil = new FileUtil();
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.setFileInputStream(fileIn);
 assertNull(product.getProduct(asin, offer, page));
 }

 @Test
 public void testGetProductNullProductInfoBean() {
 product = new Product();
 FileUtil fileUtil = new FileUtil();
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.setFileInputStream(fileIn);
 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
 ProductInfo productInfoBean = null;
 try {
 JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 try {
 fileIn.close();
 accessoryFileIn.close();
 } catch (IOException io) {
 io.printStackTrace();
 }
 }
 product.setProductInfoBean(productInfoBean);
 assertNull(product.getProduct(asin, offer, page));
 }

 @Test
 public void testGetProductNullAccessoryProductInfoBean() {
 product = new Product();
 FileUtil fileUtil = new FileUtil();
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.setFileInputStream(fileIn);
 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 try {
 JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
 product.setProductInfoBean(productInfoBean);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 try {
 fileIn.close();
 accessoryFileIn.close();
 } catch (IOException io) {
 io.printStackTrace();
 }
 }
 product.getProduct(asin, offer, page);
 assertNull(product.getFullProduct().getAccessories());
 }

 @Test
 public void testGetProductEmptyAccessoryArray() {
 product = new Product();
 FileUtil fileUtil = new FileUtil();
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.setFileInputStream(fileIn);
 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, new ArrayList());
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 productInfoBean.setProductsArrayList(new ArrayList());
 try {
 JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
 product.setProductInfoBean(productInfoBean);
 JOXBeanInputStream joxInAccessory = new JOXBeanInputStream((InputStream)accessoryFileIn);
 productInfoBean = (ProductInfo)joxInAccessory.readObject((Class)ProductInfo.class);
 product.setProductInfoBeanAccessory(productInfoBean);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 try {
 fileIn.close();
 accessoryFileIn.close();
 } catch (IOException io) {
 io.printStackTrace();
 }
 }
 product.getProduct(asin, offer, page);
 assertNotNull(product.getFullProduct());
 assertEmpty(product.getFullProduct().getAccessories());
 }

 @Test
 public void testGetProductAccessoryArrayNotEmpty() {
 product = new Product();
 FileUtil fileUtil = new FileUtil();
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.setFileInputStream(fileIn);
 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 productInfoBean.setProductsArrayList(new ArrayList());
 try {
 JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);product.setProductInfoBean(productInfoBean);
 JOXBeanInputStream joxInAccessory = new JOXBeanInputStream((InputStream)accessoryFileIn);
 ProductInfo productInfoBeanAccessory = (ProductInfo)joxInAccessory.readObject((Class)ProductInfo.class);
 product.setProductInfoBeanAccessory(productInfoBeanAccessory);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 try {
 fileIn.close();
 accessoryFileIn.close();
 } catch (IOException io) {
 io.printStackTrace();
 }
 }
 ArrayList accessoryList = productInfoBeanAccessory.getProductsArrayList();
 for (int i = 0; i < accessoryList.size(); i++) {
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin(accessoryList.get(i).getAsin());
 miniProduct.setImageURL(accessoryList.get(i).getImageUrlSmall());
 miniProduct.setManufacturer(accessoryList.get(i).getManufacturer());
 miniProduct.setName(accessoryList.get(i).getProductName());
 miniProduct.setPrice(accessoryList.get(i).getOurPrice());
 fullProduct.addAccessory(miniProduct);
 }
 product.getProduct(asin, offer, page);
 assertNotNull(product.getFullProduct());
 assertFalse(product.getFullProduct().getAccessories().isEmpty());
 }

 @Test
 public void testGetProductSimilarItemsNotEmpty() {
 product = new Product();
 FileUtil fileUtil = new FileUtil();
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.setFileInputStream(fileIn);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 productInfoBean.setProductsArrayList(new ArrayList());
 try {
 JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
 product.setProductInfoBean(productInfoBean);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 try {
 fileIn.close();
 } catch (IOException io) {
 io.printStackTrace();
 }
 }
 ProductInfo productInfoBeanSimilar = search.SimilaritesSearch(asin, "1");
 ArrayList accessoryList = productInfoBeanSimilar.getProductsArrayList();
 for (int i = 0; i < accessoryList.size(); i++) {
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin(accessoryList.get(i).getAsin());
 miniProduct.setImageURL(accessoryList.get(i).getImageUrlSmall());
 miniProduct.setManufacturer(accessoryList.get(i).getManufacturer());
 miniProduct.setName(accessoryList.get(i).getProductName());
 miniProduct.setPrice(accessoryList.get(i).getOurPrice());
 fullProduct.addSimilarItem(miniProduct);
 }
 product.getProduct(asin, offer, page);
 assertNotNull(product.getFullProduct());
 assertFalse(product.getFullProduct().getSimilarItems().isEmpty());
 }
}