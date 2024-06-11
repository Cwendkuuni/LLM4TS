package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtilTestDouble;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setUp() {
 product = new Product();
 fileUtil = new FileUtilTestDouble();
 }

 @Test
 public void testGetProduct_ValidASINOfferPage_ReturnsFullProduct() throws Exception {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 File file = new File("testData/ProductData.xml"); // Replace with valid file path
 FileInputStream fileIn = new FileInputStream(file);

 when(fileUtil.fetchASINFile(asin, Equality.equalTo("heavy"), offer, page)).thenReturn(fileIn);

 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { new ProductDetails() });
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = productDetails.getAccessories();
 accessories.setAccessoryArray(new ArrayList<MiniProduct>());
 productDetails.setAccessories(accessories);
 productInfoBean.setDetails(new ProductDetails[] { productDetails });

 when(fileUtil.fetchAccessories(asin, productDetails.getAccessories().getAccessoryArray())).thenReturn(new FileInputStream(new File("testData/AccessoryData.xml")));

 ProductInfo productInfoBean2 = new ProductInfo();
 productInfoBean2.setProductsArrayList(new ArrayList<ProductDetails>(Arrays.asList(new ProductDetails())));

 when(search.SimilaritesSearch(asin, Equality.equalTo("1"))).thenReturn(productInfoBean2);

 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("ASIN1");
 miniProduct.setImageURL("ImageURL1");
 miniProduct.setManufacturer("Manufacturer1");
 miniProduct.setName("ProductName1");
 miniProduct.setPrice(12.99D);
 expectedFullProduct.addAccessory(miniProduct);

 FullProduct actualFullProduct = product.getProduct(asin, offer, page);

 assertEquals(expectedFullProduct, actualFullProduct);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullASIN_ThrowsNullPointerException() {
 String asin = null;
 String offer = "New";
 String page = "1";
 product.getProduct(asin, offer, page);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullOffer_ThrowsNullPointerException() {
 String asin = "B000012345";
 String offer = null;
 String page = "1";
 product.getProduct(asin, offer, page);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullPage_ThrowsNullPointerException() {
 String asin = "B000012345";
 String offer = "New";
 String page = null;
 product.getProduct(asin, offer, page);
 }

 @Test
 public void testGetProduct_InvalidASINOfferPage_ReturnsNull() throws Exception {
 String asin = "InvalidASIN";
 String offer = "New";
 String page = "1";
 product.getProduct(asin, offer, page);
 assertNull(product.getProduct(asin, offer, page));
 }
}

class FileUtilTestDouble {
 FileUtil fileUtil = new FileUtil() {
 @Override
 public FileInputStream fetchASINFile(String asin, String offer, String page, String... extensions) throws FileNotFoundException {
 return null;
 }

 @Override
 public InputStream fetchAccessories(String asin, List<MiniProduct> accessoryArray) throws FileNotFoundException {
 return null;
 }
 };
}