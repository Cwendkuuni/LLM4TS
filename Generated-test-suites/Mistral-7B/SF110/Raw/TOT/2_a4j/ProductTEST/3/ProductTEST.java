import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.DAO.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.Accessories;
import java.util.ArrayList;
import java.util.List;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setUp() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProduct_ValidInput_ReturnsFullProduct() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 ProductInfo productInfo = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 ArrayList detailsArray = new ArrayList();
 MiniProduct miniProduct = new MiniProduct();
 productInfo.setDetails(new ProductDetails[] { productDetails });
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(accessoryArray);
 accessoryArray.add("accessory1");
 detailsArray.add(new ProductDetails());
 ProductDetails accessoryProductDetails = new ProductDetails();
 accessoryProductDetails.setAsin("accessory1");
 accessoryProductDetails.setImageUrlSmall("http://example.com/image1.jpg");
 detailsArray.set(0, accessoryProductDetails);
 productInfo.setProductsArrayList(detailsArray);

 FileInputStream fileIn = null;
 try {
 fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = null;
 if (fileIn != null) {
 productInfoBean = new ProductInfo();
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 }

 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);
 Accessories expectedAccessories = new Accessories();
 expectedAccessories.setAccessoryArray(accessoryArray);
 expectedFullProduct.setAccessories(expectedAccessories);
 MiniProduct expectedMiniProduct = new MiniProduct();
 expectedMiniProduct.setAsin("accessory1");
 expectedMiniProduct.setImageURL("http://example.com/image1.jpg");
 expectedMiniProduct.setManufacturer("Manufacturer1");
 expectedMiniProduct.setName("Accessory1");
 expectedMiniProduct.setPrice(10.0);
 expectedFullProduct.addAccessory(expectedMiniProduct);

 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals("Product details should match", expectedFullProduct.getDetails(), actualFullProduct.getDetails());
 assertEquals("Accessories should match", expectedFullProduct.getAccessories(), actualFullProduct.getAccessories());
 assertEquals("Number of accessories should match", expectedFullProduct.getAccessories().getAccessoryArray().size(), actualFullProduct.getAccessories().getAccessoryArray().size());
 assertEquals("First accessory should match", expectedFullProduct.getAccessories().getAccessoryArray().get(0), actualFullProduct.getAccessories().getAccessoryArray().get(0));
 } finally {
 if (fileIn != null) {
 try {
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
 }

 @Test
 public void testGetProduct_InvalidASIN_ReturnsNull() {
 String asin = null;
 String offer = "new";
 String page = "1";
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertNull("Should return null for invalid ASIN", actualFullProduct);
 }

 @Test
 public void testGetProduct_InvalidOffer_ReturnsNull() {
 String asin = "B000012345";
 String offer = null;
 String page = "1";
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertNull("Should return null for invalid offer", actualFullProduct);
 }

 @Test
 public void testGetProduct_InvalidPage_ReturnsNull() {
 String asin = "B000012345";
 String offer = "new";
 String page = null;
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertNull("Should return null for invalid page", actualFullProduct);
 }

 @Test
 public void testGetProduct_FileDoesNotExist_ReturnsNull() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = null;
 try {
 fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 } finally {
 fileUtil.deleteFile(asin, "heavy", offer, page);
 }
 FileUtil.deleteFile(asin, "heavy", offer, page); // delete the created file
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertNull("Should return null if file does not exist", actualFullProduct);
 }

 @Test
 public void testGetProduct_ExceptionThrown_ReturnsNull() {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = null;
 try {
 fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 FileNotFoundException exception = new FileNotFoundException("Test exception");
 throw exception;
 } finally {
 if (fileIn != null) {
 try {
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 FileUtil.deleteFile(asin, "heavy", offer, page); // delete the created file
 }
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertNull("Should return null if an exception is thrown", actualFullProduct);
 }
}