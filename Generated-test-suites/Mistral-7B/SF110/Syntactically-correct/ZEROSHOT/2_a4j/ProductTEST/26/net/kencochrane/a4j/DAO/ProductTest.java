package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setUp() {
 fileUtil = new FileUtil();
 product = new Product();
 }

 @Test
 public void testGetProductNullFile() {
 File testFile = null;
 assertNull(product.getProduct("asin", "offer", "page"));
 }

 @Test
 public void testGetProductNullProductInfo() {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(inputStream);
 ProductInfo productInfoBean = null;
 try {
 productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 } catch (Exception e) {
 // Do nothing
 }
 joxIn.close();
 assertNull(product.getProduct("asin", "offer", "page"));
 }

 @Test
 public void testGetProductNullDetails() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new SerializeUtils().serialize(productInfoBean));
 JOXBeanInputStream joxIn = new JOXBeanInputStream(inputStream);
 ProductDetails productDetails = null;
 try {
 productDetails = joxIn.readObject(ProductDetails.class);
 } catch (Exception e) {
 // Do nothing
 }
 joxIn.close();
 assertNull(product.getProduct("asin", "offer", "page"));
 }

 @Test
 public void testGetProductNoAccessories() {
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 ByteArrayInputStream inputStream1 = new ByteArrayInputStream(new SerializeUtils().serialize(productInfoBean));
 JOXBeanInputStream joxIn1 = new JOXBeanInputStream(inputStream1);
 Accessories accessories = null;
 try {
 accessories = joxIn1.readObject(Accessories.class);
 } catch (Exception e) {
 // Do nothing
 }
 joxIn1.close();
 assertNull(product.getProduct("asin", "offer", "page").getAccessories());
 }

 @Test
 public void testGetProductNoSimilarItems() {
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 ArrayList accessoryArray = new ArrayList();
 Accessories accessories = new Accessories();
 accessories.setAccessoryArray(accessoryArray);
 productDetails.setAccessories(accessories);
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 ByteArrayInputStream inputStream1 = new ByteArrayInputStream(new SerializeUtils().serialize(productInfoBean));
 JOXBeanInputStream joxIn1 = new JOXBeanInputStream(inputStream1);
 ProductInfo productInfoBean2 = null;
 try {
 productInfoBean2 = (ProductInfo) joxIn1.readObject(ProductInfo.class);
 } catch (Exception e) {
 // Do nothing
 }
 joxIn1.close();
 Search search = new Search();
 ProductInfo productInfoBean3 = search.SimilaritesSearch("asin", "1");
 assertNull(product.getProduct("asin", "offer", "page").getSimilarItems());
 }

 @Test
 public void testGetProductAccessories() {
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 MiniProduct miniProduct1 = new MiniProduct();
 miniProduct1.setAsin("asin1");
 miniProduct1.setImageURL("url1");
 miniProduct1.setManufacturer("manufacturer1");
 miniProduct1.setName("name1");
 miniProduct1.setPrice(10.0);
 accessoryArray.add(miniProduct1);
 Accessories accessories1 = new Accessories();
 accessories1.setAccessoryArray(accessoryArray);
 productDetails.setAccessories(accessories1);
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 ByteArrayInputStream inputStream1 = new ByteArrayInputStream(new SerializeUtils().serialize(productInfoBean));
 JOXBeanInputStream joxIn1 = new JOXBeanInputStream(inputStream1);
 ProductInfo productInfoBean2 = null;
 try {
 productInfoBean2 = (ProductInfo) joxIn1.readObject(ProductInfo.class);
 } catch (Exception e) {
 // Do nothing
 }
 joxIn1.close();
 Search search = new Search();
 ProductInfo productInfoBean3 = search.SimilaritesSearch("asin", "1");
 FullProduct fullProduct = product.getProduct("asin", "offer", "page");
 ArrayList accessoryArray2 = fullProduct.getAccessories().getAccessoryArray();
 assertEquals(1, accessoryArray2.size());
 MiniProduct miniProduct2 = (MiniProduct) accessoryArray2.get(0);
 assertEquals("asin1", miniProduct2.getAsin());
 assertEquals("url1", miniProduct2.getImageURL());
 assertEquals("manufacturer1", miniProduct2.getManufacturer());
 assertEquals("name1", miniProduct2.getName());
 assertEquals(10.0, miniProduct2.getPrice(), 0.01);
 }

 @Test
 public void testGetProductSimilarItems() {
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniProduct1 = new MiniProduct();
 miniProduct1.setAsin("asin1");
 miniProduct1.setImageURL("url1");
 miniProduct1.setManufacturer("manufacturer1");
 miniProduct1.setName("name1");
 miniProduct1.setPrice(10.0);
 accessoryProductDetails.setProductName("accessoryName1");
 accessoryProductDetails.setAsin("accessoryAsin1");
 accessoryProductDetails.setImageUrlSmall("accessoryImageUrl1");
 accessoryProductDetails.setManufacturer("accessoryManufacturer1");
 accessoryProductDetails.setOurPrice(15.0);
 ArrayList detailsArray = new ArrayList();
 detailsArray.add(accessoryProductDetails);
 productDetails.setDetails(detailsArray.toArray(new ProductDetails[0]));
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 ByteArrayInputStream inputStream1 = new ByteArrayInputStream(new SerializeUtils().serialize(productInfoBean));
 JOXBeanInputStream joxIn1 = new JOXBeanInputStream(inputStream1);
 ProductInfo productInfoBean2 = null;
 try {
 productInfoBean2 = (ProductInfo) joxIn1.readObject(ProductInfo.class);
 } catch (Exception e) {
 // Do nothing
 }
 joxIn1.close();
 Search search = new Search();
 ProductInfo productInfoBean3 = search.SimilaritesSearch("asin", "1");
 FullProduct fullProduct = product.getProduct("asin", "offer", "page");
 ArrayList similarItems = fullProduct.getSimilarItems();
 assertEquals(1, similarItems.size());
 MiniProduct miniProduct2 = (MiniProduct) similarItems.get(0);
 assertEquals("asin1", miniProduct2.getAsin());
 assertEquals("url1", miniProduct2.getImageURL());
 assertEquals("manufacturer1", miniProduct2.getManufacturer());
 assertEquals("name1", miniProduct2.getName());
 assertEquals(10.0, miniProduct2.getPrice(), 0.01);
 }
}