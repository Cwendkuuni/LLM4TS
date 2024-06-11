package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
 public void setUp() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProduct_ValidASINOfferPage_ReturnsFullProduct() throws Exception {
 String asin = "B000NHJZLA";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 fileIn.close();
 }
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = productDetails.getAccessories();
 ArrayList<AccessoryDetails> accessoryArray = accessories.getAccessoryArray();
 if (accessoryArray != null && accessoryArray.size() > 0) {
 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 if (accessoryFileIn != null) {
 accessoryProductInfoBean = (ProductInfo)new JOXBeanInputStream(accessoryFileIn).readObject(ProductInfo.class);
 accessoryFileIn.close();
 }
 List<MiniProduct> detailsArray = accessoryProductInfoBean.getProductsArrayList();
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);
 for (AccessoryDetails accessoryDetails : accessoryArray) {
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin(accessoryDetails.getAsin());
 miniProduct.setImageURL(accessoryDetails.getImageUrlSmall());
 miniProduct.setManufacturer(accessoryDetails.getManufacturer());
 miniProduct.setName(accessoryDetails.getProductName());
 miniProduct.setPrice(accessoryDetails.getOurPrice());
 expectedFullProduct.addAccessory(miniProduct);
 }
 for (MiniProduct miniProduct : detailsArray) {
 expectedFullProduct.addSimilarItem(miniProduct);
 }
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 }
}

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullASIN_ThrowsNullPointerException() {
 String asin = null;
 String offer = "new";
 String page = "1";
 Product fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetProduct_InvalidASINFile_ThrowsFileNotFoundException() throws FileNotFoundException {
 String asin = "B000NHJZLA";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNull(fileIn);
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test(expected = ClassCastException.class)
 public void testGetProduct_InvalidProductInfoBean_ThrowsClassCastException() throws Exception {
 String asin = "B000NHJZLA";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = new FileInputStream(new File("src/test/resources/testData/invalidProductInfo.xml"));
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 fileIn.close();
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test
 public void testGetProduct_EmptyAccessories_ReturnsFullProductWithEmptyAccessoriesList() throws Exception {
 String asin = "B000NHJZLA";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 fileIn.close();
 }
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = productDetails.getAccessories();
 ArrayList<AccessoryDetails> emptyAccessoryArray = new ArrayList<>();
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);
 expectedFullProduct.setAccessories(accessories);
 expectedFullProduct.setAccessories(emptyAccessoryArray);
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 }

 @Test
 public void testGetProduct_AccessoriesFileNotFound_ReturnsFullProductWithEmptyAccessoriesList() throws Exception {
 String asin = "B000NHJZLA";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 fileIn.close();
 }
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = productDetails.getAccessories();
 ArrayList<AccessoryDetails> accessoryArray = accessories.getAccessoryArray();
 if (accessoryArray != null && accessoryArray.size() > 0) {
 String asinAccessory = accessoryArray.get(0).getAsin();
 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
 assertNull(accessoryFileIn);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);
 expectedFullProduct.setAccessories(accessories);
 expectedFullProduct.setAccessories(new ArrayList<>());
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 }
 }
}