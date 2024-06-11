package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setup() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProductNullInput() {
 Product result = product.getProduct(null, null, null);
 assertNull(result);
 }

 @Test
 public void testGetProductNullAccessoryFile() {
 String asin = "ASIN1";
 String offer = "OFFER1";
 String page = "PAGE1";
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 try {
 Product result = product.getProduct(asin, offer, page);
 assertNull(result);
 } catch (IOException e) {
 // expected
 } finally {
 if (joxIn != null) {
 joxIn.close();
 }
 if (fileIn != null) {
 fileIn.close();
 }
 }
 }

 @Test
 public void testGetProductNullProductInfoBean() {
 String asin = "ASIN1";
 String offer = "OFFER1";
 String page = "PAGE1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(inputStream);
 try {
 Product result = product.getProduct(asin, offer, page);
 assertNull(result);
 } finally {
 if (joxIn != null) {
 joxIn.close();
 }
 if (fileIn != null) {
 fileIn.close();
 }
 }
 }

 @Test
 public void testGetProductNullDetailsArray() {
 String asin = "ASIN1";
 String offer = "OFFER1";
 String page = "PAGE1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBean = new ProductInfo();
 try {
 Product result = product.getProduct(asin, offer, page);
 assertNull(result);
 } finally {
 if (joxIn != null) {
 joxIn.close();
 }
 if (fileIn != null) {
 fileIn.close();
 }
 }
 }

 @Test
 public void testGetProductEmptyAccessoryArray() {
 String asin = "ASIN1";
 String offer = "OFFER1";
 String page = "PAGE1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 try {
 Product result = product.getProduct(asin, offer, page);
 assertEquals(0, result.getAccessories().getAccessoryArray().size());
 } finally {
 if (joxIn != null) {
 joxIn.close();
 }
 if (fileIn != null) {
 fileIn.close();
 }
 }
 }

 @Test
 public void testGetProductNonEmptyAccessoryArray() {
 String asin = "ASIN1";
 String offer = "OFFER1";
 String page = "PAGE1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { new ProductDetails() });
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add("ACCESSORY1");
 accessories.setAccessoryArray(accessoryArray);
 productInfoBean.setAccessories(accessories);
 ProductInfo productInfoBean2 = new ProductInfo();
 List<MiniProduct> detailsArray = Arrays.asList(new MiniProduct[] { new MiniProduct() });
 productInfoBean2.setProductsArrayList(detailsArray);
 try {
 Product result = product.getProduct(asin, offer, page);
 assertEquals(1, result.getAccessories().getAccessoryArray().size());
 } finally {
 if (joxIn != null) {
 joxIn.close();
 }
 if (fileIn != null) {
 fileIn.close();
 }
 }
 }

 @Test
 public void testGetProductNullSimilarItemsArray() {
 String asin = "ASIN1";
 String offer = "OFFER1";
 String page = "PAGE1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { new ProductDetails() });
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add(new ProductDetails());
 accessories.setAccessoryArray(accessoryArray);
 productInfoBean.setAccessories(accessories);
 ProductInfo productInfoBean2 = new ProductInfo();
 productInfoBean2.setProductsArrayList(new ArrayList());
 try {
 Product result = product.getProduct(asin, offer, page);
 assertEquals(0, result.getSimilarItems().size());
 } finally {
 if (joxIn != null) {
 joxIn.close();
 }
 if (fileIn != null) {
 fileIn.close();
 }
 }
 }

 @Test
 public void testGetProductNonEmptySimilarItemsArray() {
 String asin = "ASIN1";
 String offer = "OFFER1";
 String page = "PAGE1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { new ProductDetails() });
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add(new ProductDetails());
 accessories.setAccessoryArray(accessoryArray);
 productInfoBean.setAccessories(accessories);
 ProductInfo productInfoBean2 = new ProductInfo();
 List<MiniProduct> detailsArray = Arrays.asList(new MiniProduct[] { new MiniProduct(), new MiniProduct() });
 productInfoBean2.setProductsArrayList(detailsArray);
 try {
 Product result = product.getProduct(asin, offer, page);
 assertEquals(2, result.getSimilarItems().size());
 } finally {
 if (joxIn != null) {
 joxIn.close();
 }
 if (fileIn != null) {
 fileIn.close();
 }
 }
 }
}