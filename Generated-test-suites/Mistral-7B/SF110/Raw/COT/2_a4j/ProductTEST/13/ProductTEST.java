package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.file.JOXBeanInputStream;

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
 public void testGetProduct_ValidInput_ReturnsExpectedProduct() throws Exception {
 String asin = "testAsin";
 String offer = "testOffer";
 String page = "testPage";
 ProductInfo productInfo = new ProductInfo(); // Dummy productInfo for testing
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(accessoryArray);
 productInfo.setDetails(new ProductDetails[] { productDetails });

 File file = new File("testFile.xml"); // Dummy file for testing
 FileInputStream fileIn = new FileInputStream(file);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 productInfo = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();

 FullProduct expectedProduct = new FullProduct();
 expectedProduct.setDetails(productDetails);
 expectedProduct.setAccessories(accessoryArray);
 expectedProduct.setSimilarItems(new ArrayList<MiniProduct>());

 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("testAccessoryAsin");
 miniProduct.setImageURL("testImageURL");
 miniProduct.setManufacturer("testManufacturer");
 miniProduct.setName("testProductName");
 miniProduct.setPrice(12.5);
 accessoryProductDetails.setProductName("testProductName");
 accessoryProductDetails.setOurPrice(12.5);
 accessoryProductDetails.setAccessories(new Accessories());
 ProductDetails accessoryProductDetails2 = new ProductDetails();
 MiniProduct miniProduct2 = new MiniProduct();
 miniProduct2.setAsin("testAccessoryAsin2");
 miniProduct2.setImageURL("testImageURL2");
 miniProduct2.setManufacturer("testManufacturer2");
 miniProduct2.setName("testProductName2");
 miniProduct2.setPrice(25.0);
 accessoryProductDetails2.setProductName("testProductName2");
 accessoryProductDetails2.setOurPrice(25.0);
 accessoryProductDetails2.setAccessories(new Accessories());
 accessoryArray.add(accessoryProductDetails);
 accessoryArray.add(accessoryProductDetails2);
 productDetails.setAccessories(accessories);

 ProductInfo similarProductInfo = new ProductInfo(); // Dummy productInfo for testing
 List<ProductDetails> similarDetailsArray = Arrays.asList(new ProductDetails[] { productDetails, productDetails });
 similarProductInfo.setDetails(similarDetailsArray.toArray(new ProductDetails[0]));
 ProductDetails similarAccessoryProductDetails = new ProductDetails();
 MiniProduct similarMiniProduct = new MiniProduct();
 similarMiniProduct.setAsin("testSimilarAsin");
 similarMiniProduct.setImageURL("testImageURL3");
 similarMiniProduct.setManufacturer("testManufacturer3");
 similarMiniProduct.setName("testProductName3");
 similarMiniProduct.setPrice(17.5);
 similarAccessoryProductDetails.setProductName("testProductName3");
 similarAccessoryProductDetails.setOurPrice(17.5);
 similarAccessoryProductDetails.setAccessories(new Accessories());
 ProductDetails similarAccessoryProductDetails2 = new ProductDetails();
 MiniProduct similarMiniProduct2 = new MiniProduct();
 similarMiniProduct2.setAsin("testSimilarAsin2");
 similarMiniProduct2.setImageURL("testImageURL4");
 similarMiniProduct2.setManufacturer("testManufacturer4");
 similarMiniProduct2.setName("testProductName4");
 similarMiniProduct2.setPrice(32.0);
 similarAccessoryProductDetails2.setProductName("testProductName4");
 similarAccessoryProductDetails2.setOurPrice(32.0);
 similarAccessoryProductDetails2.setAccessories(new Accessories());
 similarDetailsArray.add(similarAccessoryProductDetails);
 similarDetailsArray.add(similarAccessoryProductDetails2);
 similarProductInfo.setDetails(similarDetailsArray.toArray(new ProductDetails[0]));

 expectedProduct.setSimilarItems(Arrays.asList(miniProduct, miniProduct2));

 FullProduct actualProduct = product.getProduct(asin, offer, page);

 assertEquals(expectedProduct.getDetails().getProductName(), actualProduct.getDetails().getProductName());
 assertEquals(expectedProduct.getDetails().getManufacturer(), actualProduct.getDetails().getManufacturer());
 assertEquals(expectedProduct.getDetails().getOurPrice(), actualProduct.getDetails().getOurPrice(), 0.01);
 assertEquals(expectedProduct.getAccessories().size(), actualProduct.getAccessories().size());
 for (int i = 0; i < expectedProduct.getAccessories().size(); i++) {
 MiniProduct expectedAccessory = (MiniProduct) expectedProduct.getAccessories().get(i);
 MiniProduct actualAccessory = (MiniProduct) actualProduct.getAccessories().get(i);
 assertEquals(expectedAccessory.getAsin(), actualAccessory.getAsin());
 assertEquals(expectedAccessory.getImageURL(), actualAccessory.getImageURL());
 assertEquals(expectedAccessory.getManufacturer(), actualAccessory.getManufacturer());
 assertEquals(expectedAccessory.getName(), actualAccessory.getName());
 assertEquals(expectedAccessory.getPrice(), actualAccessory.getPrice(), 0.01);
 }
 assertEquals(expectedProduct.getSimilarItems().size(), actualProduct.getSimilarItems().size());
 for (int i = 0; i < expectedProduct.getSimilarItems().size(); i++) {
 MiniProduct expectedSimilarItem = (MiniProduct) expectedProduct.getSimilarItems().get(i);
 MiniProduct actualSimilarItem = (MiniProduct) actualProduct.getSimilarItems().get(i);
 assertEquals(expectedSimilarItem.getAsin(), actualSimilarItem.getAsin());
 assertEquals(expectedSimilarItem.getImageURL(), actualSimilarItem.getImageURL());
 assertEquals(expectedSimilarItem.getManufacturer(), actualSimilarItem.getManufacturer());
 assertEquals(expectedSimilarItem.getName(), actualSimilarItem.getName());
 assertEquals(expectedSimilarItem.getPrice(), actualSimilarItem.getPrice(), 0.01);
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetProduct_FileNotFoundException_ThrowsExpectedException() throws Exception {
 String asin = "testAsin";
 String offer = "testOffer";
 String page = "testPage";
 File file = new File("nonExistentFile.xml");
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNull(fileIn);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetProduct_ClassNotFoundException_ThrowsExpectedException() throws Exception {
 String asin = "testAsin";
 String offer = "testOffer";
 String page = "testPage";
 ProductInfo productInfo = new ProductInfo(); // Dummy productInfo for testing
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(accessoryArray);
 productInfo.setDetails(new ProductDetails[] { productDetails });

 File file = new File("testFile.xml"); // Dummy file for testing
 FileInputStream fileIn = new FileInputStream(file);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 throw new ClassNotFoundException("ProductInfo class not found");
 joxIn.close();
 fileIn.close();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetProduct_IndexOutOfBoundsException_ThrowsExpectedException() throws Exception {
 String asin = "testAsin";
 String offer = "testOffer";
 String page = "testPage";
 ProductInfo productInfo = new ProductInfo(); // Dummy productInfo for testing
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(accessoryArray);
 productInfo.setDetails(new ProductDetails[] { productDetails });

 File file = new File("testFile.xml"); // Dummy file for testing
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo readProductInfo = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();

 ProductDetails[] detailsArray = productInfo.getDetails();
 assertArrayEquals(new ProductDetails[0], detailsArray, "Array should be empty");
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullPointerException_ThrowsExpectedException() throws Exception {
 String asin = "testAsin";
 String offer = "testOffer";
 String page = "testPage";
 ProductInfo productInfo = null;
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(accessoryArray);
 productInfo = new ProductInfo();
 productInfo.setDetails(new ProductDetails[] { productDetails });

 File file = new File("testFile.xml"); // Dummy file for testing
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 throw new NullPointerException("ProductInfo object is null");
 joxIn.close();
 fileIn.close();
 }
}