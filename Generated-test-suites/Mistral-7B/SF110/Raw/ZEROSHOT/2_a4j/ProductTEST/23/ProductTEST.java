package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtilImpl;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.file.Search;

import org.junit.Test;

public class ProductTest {

 private FileUtil fileUtil = new FileUtilImpl();
 private Product product = new Product();

 @Test
 public void testGetProductNullValues() {
 Product result = product.getProduct(null, null, null);
 assertNull(result);
 }

 @Test
 public void testGetProductWithInvalidFile() {
 FileInputStream fileIn = mock(FileInputStream.class);
 when(fileIn.available()).thenReturn(0);
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileIn);
 Product result = product.getProduct("ASIN123", "offer", "page");
 assertNull(result);
 }

 @Test
 public void testGetProductWithValidFileAndNoDetails() {
 FileInputStream fileIn = mock(FileInputStream.class);
 JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileIn);
 when(fileIn.available()).thenReturn(1024);
 when(joxIn.readObject(ProductInfo.class)).thenReturn(null);
 Product result = product.getProduct("ASIN123", "offer", "page");
 assertNull(result);
 }

 @Test
 public void testGetProductWithInvalidProductInfoBean() {
 FileInputStream fileIn = mock(FileInputStream.class);
 JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileIn);
 when(fileIn.available()).thenReturn(1024);
 ProductInfo productInfoBean = mock(ProductInfo.class);
 when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
 when(productInfoBean.getDetails()).thenReturn(null);
 Product result = product.getProduct("ASIN123", "offer", "page");
 assertNull(result);
 }

 @Test
 public void testGetProductWithValidProductInfoBean() throws IOException {
 FileInputStream fileIn = mock(FileInputStream.class);
 JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productDetails.setAccessories(new ArrayList<Accessories>());
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileIn);
 when(fileIn.available()).thenReturn(1024);
 when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
 FullProduct result = product.getProduct("ASIN123", "offer", "page");
 assertNotNull(result);
 assertNotNull(result.getDetails());
 assertNotNull(result.getAccessories());
 assertNotNull(result.getAccessories().getAccessoryArray());
 }

 @Test
 public void testGetProductWithAccessoriesFile() throws IOException {
 FileInputStream fileIn = mock(FileInputStream.class);
 JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 ArrayList<Accessories> accessoryArray = new ArrayList<Accessories>();
 accessoryArray.add(accessories);
 productDetails.setAccessories(accessoryArray);
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 ArrayList<MiniProduct> detailsArray = new ArrayList<MiniProduct>();
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("ASIN456");
 miniProduct.setImageURL("http://image.com/image456.jpg");
 miniProduct.setManufacturer("Manufacturer456");
 miniProduct.setName("Product456");
 miniProduct.setPrice(12.99);
 detailsArray.add(miniProduct);
 productInfoBean.setProductsArrayList(detailsArray);
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileIn);
 when(fileIn.available()).thenReturn(1024);
 when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
 FileInputStream accessoryFileIn = mock(FileInputStream.class);
 when(fileUtil.fetchAccessories(anyString(), anyList())).thenReturn(accessoryFileIn);
 JOXBeanInputStream accessoryJoxIn = mock(JOXBeanInputStream.class);
 when(accessoryFileIn.available()).thenReturn(512);
 when(accessoryJoxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
 product = new Product();
 Product result = product.getProduct("ASIN123", "offer", "page");
 assertNotNull(result);
 assertNotNull(result.getDetails());
 assertNotNull(result.getAccessories());
 assertNotNull(result.getAccessories().getAccessoryArray());
 assertNotNull(result.getAccessories().getAccessoryArray().get(0).getName());
 assertEquals("Product456", result.getAccessories().getAccessoryArray().get(0).getName());
 }

 @Test
 public void testGetProductWithSimilarItems() throws IOException {
 FileInputStream fileIn = mock(FileInputStream.class);
 JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 ArrayList<Accessories> accessoryArray = new ArrayList<Accessories>();
 accessoryArray.add(accessories);
 productDetails.setAccessories(accessoryArray);
 ArrayList<MiniProduct> detailsArray = new ArrayList<MiniProduct>();
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("ASIN456");
 miniProduct.setImageURL("http://image.com/image456.jpg");
 miniProduct.setManufacturer("Manufacturer456");
 miniProduct.setName("Product456");
 miniProduct.setPrice(12.99);
 detailsArray.add(miniProduct);
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 productInfoBean.setProductsArrayList(detailsArray);
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(fileIn);
 when(fileIn.available()).thenReturn(1024);
 when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
 Search search = mock(Search.class);
 MiniProduct similiarItem = new MiniProduct();
 similiarItem.setAsin("ASIN789");
 similiarItem.setImageURL("http://image.com/image789.jpg");
 similiarItem.setManufacturer("Manufacturer789");
 similiarItem.setName("Product789");
 similiarItem.setPrice(9.99);
 when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);
 Product result = product.getProduct("ASIN123", "offer", "page");
 assertNotNull(result);
 assertNotNull(result.getDetails());
 assertNotNull(result.getAccessories());
 assertNotNull(result.getSimilarItems());
 assertNotNull(result.getSimilarItems().get(0).getName());
 assertEquals("Product789", result.getSimilarItems().get(0).getName());
 }
}