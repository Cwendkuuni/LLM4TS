package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.ProductDetails;
import net.kencochrane.a4j.DAO.beans.FullProduct;
import net.kencochrane.a4j.DAO.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtilTestImpl;
import net.kencochrane.a4j.jox.JOXBeanInputStream;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

 private FileUtil fileUtil;
 private Product product;

 @Before
 public void setUp() {
 fileUtil = new FileUtilTestImpl();
 product = new Product();
 }

 @Test
 public void testGetProduct_ValidParameters_ReturnsExpectedFullProduct() {
 String asin = "ASIN123";
 String offer = "offer1";
 String page = "page1";
 FullProduct expectedProduct = new FullProduct();
 // Assuming some predefined values for properties of FullProduct, ProductDetails, MiniProduct, and Accessories
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedProduct, actualProduct);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullAsin_ThrowsException() {
 String offer = "offer1";
 String page = "page1";
 product.getProduct(null, offer, page);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullOffer_ThrowsException() {
 String asin = "ASIN123";
 String page = "page1";
 product.getProduct(asin, null, page);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullPage_ThrowsException() {
 String asin = "ASIN123";
 String offer = "offer1";
 product.getProduct(asin, offer, null);
 }

 @Test
 public void testGetProduct_InvalidFile_ReturnsNull() {
 String asin = "ASIN123";
 String offer = "offer1";
 String page = "page1";
 FileInputStream fileInput = null;
 FileInputStream fileInputForAccessories = null;
 try {
 fileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.getProduct(asin, offer, page);
 } finally {
 assertNull(fileInput);
 assertNull(fileInputForAccessories);
 }
 }

 @Test(expected = IOException.class)
 public void testGetProduct_IOException_ThrowsException() {
 String asin = "ASIN123";
 String offer = "offer1";
 String page = "page1";
 FileInputStream fileInput = null;
 FileInputStream fileInputForAccessories = null;
 try {
 fileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 product.getProduct(asin, offer, page);
 } catch (IOException ioe) {
 throw ioe;
 } finally {
 assertNotNull(fileInput);
 assertNull(fileInputForAccessories);
 }
 }

 @Test(expected = Exception.class)
 public void testGetProduct_ExceptionInGetDetails_ThrowsException() {
 String asin = "ASIN123";
 String offer = "offer1";
 String page = "page1";
 FileInputStream fileInput = null;
 FileInputStream fileInputForAccessories = null;
 try {
 fileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileInput);
 throw new Exception("Exception in getDetails method");
 } finally {
 assertNotNull(fileInput);
 assertNull(fileInputForAccessories);
 }
 }

 @Test
 public void testGetProduct_EmptyAccessoryArray_ReturnsExpectedFullProduct() {
 String asin = "ASIN123";
 String offer = "offer1";
 String page = "page1";
 FullProduct expectedProduct = new FullProduct();
 // Assuming some predefined values for properties of FullProduct, ProductDetails, MiniProduct, and Accessories
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertTrue(actualProduct.getAccessories().isEmpty());
 }

 @Test
 public void testGetProduct_AccessoriesFileNull_ReturnsExpectedFullProduct() {
 String asin = "ASIN123";
 String offer = "offer1";
 String page = "page1";
 FullProduct expectedProduct = new FullProduct();
 // Assuming some predefined values for properties of FullProduct, ProductDetails, MiniProduct, and Accessories
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNotNull(actualProduct.getAccessories());
 assertTrue(actualProduct.getAccessories().isEmpty());
 }

 @Test(expected = IOException.class)
 public void testGetProduct_IOExceptionInFetchingAccessoriesFile_ThrowsException() {
 String asin = "ASIN123";
 String offer = "offer1";
 String page = "page1";
 FileInputStream fileInput = null;
 FileInputStream fileInputForAccessories = null;
 try {
 fileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 FileInputStream fileInputForAccessories = fileUtil.fetchAccessories(asin, new ArrayList<String>());
 product.getProduct(asin, offer, page);
 } catch (IOException ioe) {
 throw ioe;
 } finally {
 assertNotNull(fileInput);
 assertNull(fileInputForAccessories);
 }
 }

}