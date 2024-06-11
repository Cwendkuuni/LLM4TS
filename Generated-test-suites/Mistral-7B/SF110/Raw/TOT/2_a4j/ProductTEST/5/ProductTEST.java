package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
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
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setup() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProduct_ValidInput_ReturnsFullProduct() {
 String asin = "B01N20G78G";
 String offer = "New";
 String page = "1";

 FullProduct expectedFullProduct = new FullProduct();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = productDetails.getAccessories();
 List<MiniProduct> accessoryArray = Arrays.asList(
 new MiniProduct("ASIN1", "ImageURL1", "Manufacturer1", "ProductName1", 10.99F),
 new MiniProduct("ASIN2", "ImageURL2", "Manufacturer2", "ProductName2", 12.99F)
 );
 accessories.setAccessoryArray(accessoryArray);
 productDetails.setName("ProductName");
 productDetails.setOurPrice(15.99F);
 expectedFullProduct.setDetails(productDetails);

 File testFile = new File("test.xml"); // Create a test file for this example
 try {
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page, testFile);
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 fileIn.close(); // Don't forget to close the test file
 testFile.delete(); // Delete the test file after use

 assertEquals(expectedFullProduct, actualFullProduct);

 } catch (IOException e) {
 e.printStackTrace();
 fail("Exception occurred while fetching test file.");
 }
 }

 @Test
 public void testGetProduct_InvalidInput_ReturnsNull() {
 String asin = "InvalidASIN";
 String offer = "New";
 String page = "1";

 assertNull(product.getProduct(asin, offer, page));
 }

 @Test
 public void testGetProduct_FileNotFound_ReturnsNull() {
 String asin = "B01N20G78G";
 String offer = "New";
 String page = "InvalidPage";

 assertNull(product.getProduct(asin, offer, page));
 }

 @Test
 public void testGetProduct_InvalidProductInfo_ReturnsNull() {
 String asin = "B01N20G78G";
 String offer = "New";
 String page = "1";

 File testFile = new File("test.xml"); // Create a test file for this example
 try {
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page, testFile);
 FileInputStream fileIn2 = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1"));

 byte[] fileContent = new byte[1]; // Empty test file
 FileInputStream emptyFileIn = new FileInputStream(testFile);
 emptyFileIn.read(fileContent);
 emptyFileIn.close();
 testFile.delete();

 JOXBeanInputStream joxIn = new JOXBeanInputStream(new ByteArrayInputStream(fileContent));
 ProductInfo productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();

 productInfoBean.setDetails(null);
 ProductInfo invalidProductInfoBean = new ProductInfo();
 invalidProductInfoBean.setDetails(productInfoBean);

 FullProduct actualFullProduct = product.getProduct(asin, offer, page);

 assertEquals(null, actualFullProduct);

 } catch (IOException e) {
 e.printStackTrace();
 fail("Exception occurred while fetching test files.");
 }
 }
}