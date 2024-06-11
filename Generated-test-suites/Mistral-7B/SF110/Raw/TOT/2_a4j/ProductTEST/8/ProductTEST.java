import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.DAO.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.ProductInfo;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setup() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProduct_ValidInput_ReturnsFullProduct() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 ProductInfo expectedProductInfo = new ProductInfo(); // fill in expected ProductInfo
 ProductDetails expectedProductDetails = new ProductDetails(); // fill in expected ProductDetails
 expectedProductDetails.setAccessories(new Accessories());
 Accessories expectedAccessories = expectedProductDetails.getAccessories();
 expectedAccessories.setAccessoryArray(new ArrayList());
 ProductDetails accessoryProductDetails = new ProductDetails(); // fill in expected accessoryProductDetails
 MiniProduct expectedMiniProduct = new MiniProduct(); // fill in expected MiniProduct
 expectedMiniProduct.setAsin(accessoryProductDetails.getAsin());
 expectedMiniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
 expectedMiniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
 expectedMiniProduct.setName(accessoryProductDetails.getProductName());
 expectedMiniProduct.setPrice(accessoryProductDetails.getOurPrice());
 expectedAccessories.setAccessoryArray(new ArrayList() {{ add(expectedMiniProduct); }});
 expectedProductDetails.setAccessories(expectedAccessories);
 expectedProductInfo.setDetails(new ProductDetails[] { expectedProductDetails });
 FileInputStream expectedFileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(expectedFileIn);
 ProductInfo readProductInfo = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 expectedFileIn.close();
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(expectedProductDetails);

 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo actualProductInfo = product.getProduct(asin, offer, page);
 FileInputStream actualFileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 joxIn = new JOXBeanInputStream(actualFileIn);
 ProductInfo readActualProductInfo = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 actualFileIn.close();
 FileUtil.closeQuietly(fileIn);
 FileUtil.closeQuietly(expectedFileIn);

 assertEquals(expectedProductInfo, actualProductInfo);
}

 @Test
 public void testGetProduct_InvalidInput_ReturnsNull() {
 String asin = "invalidASIN";
 String offer = null;
 String page = null;
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(null, actualFullProduct);
 }

 @Test
 public void testGetProduct_NullFileInput_ReturnsNull() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 FileInputStream expectedFileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 FileInputStream fileIn = null;
 ProductInfo actualProductInfo = product.getProduct(asin, offer, page);
 FileInputStream actualFileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(actualFileIn);
 ProductInfo readActualProductInfo = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 actualFileIn.close();
 FileUtil.closeQuietly(expectedFileIn);
 FileUtil.closeQuietly(fileIn);
 assertEquals(null, actualProductInfo);
 }

 @Test
 public void testGetProduct_InvalidProductInfoBean_ReturnsNull() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo actualProductInfo = product.getProduct(asin, offer, page);
 ProductInfo expectedProductInfo = null;
 FileUtil.closeQuietly(fileIn);
 joxIn.close();
 assertEquals(null, actualProductInfo);
 }

 @Test
 public void testGetProduct_EmptyDetailsArray_ReturnsEmptyFullProduct() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 ProductInfo expectedProductInfo = productInfoBean;
 FileUtil.closeQuietly(fileIn);
 FullProduct expectedFullProduct = new FullProduct();
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 }

 @Test
 public void testGetProduct_EmptyAccessoriesArray_ReturnsEmptyFullProduct() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { new ProductDetails() {{ setAccessories(new Accessories()); }} });
 ProductInfo expectedProductInfo = productInfoBean;
 FileUtil.closeQuietly(fileIn);
 FullProduct expectedFullProduct = new FullProduct();
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 }

 @Test
 public void testGetProduct_NullAccessoryProductDetails_AddsNoAccessories() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { new ProductDetails() {{ setAccessories(new Accessories()); setAccessoryArray(new ArrayList() {{ add(null); }}); }} });
 ProductInfo expectedProductInfo = productInfoBean;
 FileUtil.closeQuietly(fileIn);
 FullProduct expectedFullProduct = new FullProduct();
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 assertEquals(0, actualFullProduct.getAccessories().size());
 }

 @Test
 public void testGetProduct_ValidInputWithAccessories_AddsAccessoriesToFullProduct() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { new ProductDetails() {{ setAccessories(new Accessories()); setAccessoryArray(new ArrayList() {{ add(new ProductDetails() {{ setAsin("accessoryASIN1"); setImageUrlSmall("http://image1.com"); setManufacturer("Manufacturer1"); setProductName("ProductName1"); setOurPrice(10.0); }}); }}); }} });
 ProductInfo expectedProductInfo = productInfoBean;
 FileUtil.closeQuietly(fileIn);
 FullProduct expectedFullProduct = new FullProduct();
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, actualFullProduct);
 MiniProduct miniProduct1 = expectedFullProduct.getAccessories().get(0);
 assertEquals("accessoryASIN1", miniProduct1.getAsin());
 assertEquals("http://image1.com", miniProduct1.getImageURL());
 assertEquals("Manufacturer1", miniProduct1.getManufacturer());
 assertEquals("ProductName1", miniProduct1.getName());
 assertEquals(10.0, miniProduct1.getPrice(), 0.01);
 }

 @Test
 public void testGetProduct_ValidInputWithSimilarItems_AddsSimilarItemsToFullProduct() throws IOException {
 String asin = "B000012345";
 String offer = "New";
 String page = "1";
 ProductInfo expectedProductInfoWithAccessories = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productDetails.setAccessoryArray(new ArrayList() {{ add(new ProductDetails() {{ setAsin("accessoryASIN1"); setImageUrlSmall("http://image1.com"); setManufacturer("Manufacturer1"); setProductName("ProductName1"); setOurPrice(10.0); }}); }});
 expectedProductInfoWithAccessories.setDetails(new ProductDetails[] { productDetails });
 FileInputStream expectedFileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 JOXBeanInputStream joxIn = new JOXBeanInputStream(expectedFileIn);
 ProductInfo readExpectedProductInfo = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 expectedFileIn.close();
 ProductInfo expectedProductInfo = readExpectedProductInfo;
 FileUtil.closeQuietly(expectedFileIn);
 FullProduct expectedFullProduct = new FullProduct();
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo actualProductInfo = product.getProduct(asin, offer, page);
 JOXBeanInputStream joxIn2 = new JOXBeanInputStream(fileIn);
 ProductInfo readActualProductInfo = (ProductInfo) joxIn2.readObject(ProductInfo.class);
 joxIn2.close();
 fileIn.close();
 FileUtil.closeQuietly(fileIn);
 ProductInfo productInfoBeanWithSimilarItems = new ProductInfo();
 productInfoBeanWithSimilarItems.setDetails(new ProductDetails[] { productDetails, new ProductDetails() {{ setAsin("similarItemASIN1"); setImageUrlSmall("http://image2.com"); setManufacturer("Manufacturer2"); setProductName("SimilarProductName1"); setOurPrice(5.0); }} });
 ProductInfo expectedProductInfoWithSimilarItems = productInfoBeanWithSimilarItems;
 FullProduct expectedFullProductWithSimilarItems = new FullProduct();
 FullProduct actualFullProductWithSimilarItems = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProductWithSimilarItems, actualFullProductWithSimilarItems);
 MiniProduct miniProduct1 = expectedFullProductWithSimilarItems.getAccessories().get(0);
 assertEquals("accessoryASIN1", miniProduct1.getAsin());
 assertEquals("http://image1.com", miniProduct1.getImageURL());
 assertEquals("Manufacturer1", miniProduct1.getManufacturer());
 assertEquals("ProductName1", miniProduct1.getName());
 assertEquals(10.0, miniProduct1.getPrice(), 0.01);
 MiniProduct miniProduct2 = expectedFullProductWithSimilarItems.getSimilarItems().get(0);
 assertEquals("similarItemASIN1", miniProduct2.getAsin());
 assertEquals("http://image2.com", miniProduct2.getImageURL());
 assertEquals("Manufacturer2", miniProduct2.getManufacturer());
 assertEquals("SimilarProductName1", miniProduct2.getName());
 assertEquals(5.0, miniProduct2.getPrice(), 0.01);
 }
}