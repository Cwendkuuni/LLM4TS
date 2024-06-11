package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.beans.Accessories;
import net.kencochrane.a4j.DAO.beans.FullProduct;
import net.kencochrane.a4j.DAO.beans.MiniProduct;
import net.kencochrane.a4j.DAO.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.util.JOXBeanInputStream;

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

 @Test
 public void testGetProductNullFile() {
 File file = null;
 FullProduct result = product.getProduct("ASIN123", "offer", "page");
 assertNull(result);
 }

 @Test
 public void testGetProductNoDetails() {
 File file = new File("src/test/resources/ProductTests/ProductWithNoDetails.xml");
 FullProduct result = product.getProduct("ASIN123", "offer", "page");
 assertNull(result);
 }

 @Test
 public void testGetProductNullAccessoryDetails() {
 File file = new File("src/test/resources/ProductTests/ProductWithNoAccessoryDetails.xml");
 FullProduct result = product.getProduct("ASIN123", "offer", "page");
 assertNotNull(result);
 assertEquals(0, result.getAccessories().size());
 }

 @Test
 public void testGetProductNullSimilarItems() {
 File file = new File("src/test/resources/ProductTests/ProductWithNoSimilarItems.xml");
 FullProduct result = product.getProduct("ASIN123", "offer", "page");
 assertNotNull(result);
 assertEquals(0, result.getAccessories().size());
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetProductMissingAccessoryFile() {
 File productFile = new File("src/test/resources/ProductTests/ProductWithMissingAccessoryFile.xml");
 File accessoryFile = new File("src/test/resources/ProductTests/MissingAccessoryFile.xml");
 FullProduct result = product.getProduct("ASIN123", "offer", "page");
 assertNotNull(result);
 assertFalse(result.getAccessories().isEmpty());

 ProductDetails productDetails = result.getDetails();
 Accessories accessories = productDetails.getAccessories();
 List<MiniProduct> accessoryArray = accessories.getAccessoryArray();
 assertNull(accessoryFile.exists());
 assertThrows(FileNotFoundException.class, () -> product.getProduct("ASIN123", "offer", "page"));
 }

 @Test(expected = IOException.class)
 public void testGetProductIOException() throws IOException {
 File productFile = new File("src/test/resources/ProductTests/ProductWithIOException.xml");
 FileInputStream fileIn = fileUtil.fetchASINFile("ASIN123", "heavy", "offer", "page");
 assertNull(fileIn);
 assertThrows(IOException.class, () -> product.getProduct("ASIN123", "offer", "page"));
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetProductClassNotFoundException() throws IOException, ClassNotFoundException {
 File productFile = new File("src/test/resources/ProductTests/ProductWithClassNotFoundException.xml");
 FileInputStream fileIn = new FileInputStream(productFile);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 assertNotNull(productInfoBean);
 assertNull(product.getProduct("ASIN123", "offer", "page"));
 }

 @Test
 public void testGetProduct() throws IOException, ClassNotFoundException {
 File productFile = new File("src/test/resources/ProductTests/Product.xml");
 FullProduct result = product.getProduct("ASIN123", "offer", "page");
 assertNotNull(result);
 assertFalse(result.getAccessories().isEmpty());
 assertFalse(result.getSimilarItems().isEmpty());
 }
}