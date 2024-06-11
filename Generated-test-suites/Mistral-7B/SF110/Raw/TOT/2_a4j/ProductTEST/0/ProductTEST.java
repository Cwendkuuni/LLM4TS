import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setUp() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @After
 public void tearDown() {
 try {
 product = null;
 fileUtil = null;
 } finally {
 System.gc();
 }
 }

 @Test
 public void testGetProduct_ValidInput_ReturnsFullProduct() throws IOException {
 String asin = "B08N69XJ87";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo) new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 fileIn.close();
 }
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = productDetails.getAccessories();
 List<MiniProduct> expectedAccessories = Arrays.asList(
 new MiniProduct("ASIN1", "http://image.com/image1.jpg", "Manufacturer1", "Accessory1", 9.99),
 new MiniProduct("ASIN2", "http://image.com/image2.jpg", "Manufacturer2", "Accessory2", 19.99)
 );
 Accessories actualAccessories = accessories;
 List<MiniProduct> actualAccessoriesList = product.getAccessories();
 assertTrue(productDetails != null);
 assertEquals(productDetails.getTitle(), "Product Title");
 assertEquals(actualAccessories, actualAccessoriesList.get(0).getAccessories());
 assertEquals(expectedAccessories, actualAccessoriesList);
 }

 @Test
 public void testGetProduct_NullASINFile_ReturnsNull() {
 FileInputStream fileIn = fileUtil.fetchASINFile("ASIN_NOT_FOUND", "heavy", "new", "1");
 assertNull(product.getProduct(fileIn.getFileDescriptor(), "heavy", "new", "1"));
 fileIn.close();
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetProduct_InvalidASINFile_ThrowsFileNotFoundException() throws FileNotFoundException {
 String asin = "B08N69XJ87";
 String offer = "new";
 String page = "2";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNull(product.getProduct(fileIn.getFileDescriptor(), "heavy", offer, page));
 fileIn.close();
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetProduct_InvalidProductInfoFile_ThrowsClassNotFoundException() throws ClassNotFoundException, IOException {
 String asin = "B08N69XJ87";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = new FileInputStream("invalidFile.dat");
 assertNull(product.getProduct(fileIn.getFileDescriptor(), "heavy", offer, page));
 fileIn.close();
 }

 @Test
 public void testGetProduct_NoAccessories_ReturnsEmptyList() {
 ProductDetails productDetails = new ProductDetails();
 Accessories accessories = new Accessories();
 productDetails.setAccessories(accessories);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 FileInputStream fileIn = new FileInputStream("accessoriesFile.dat");
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo actualProductInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 List<MiniProduct> actualAccessoriesList = product.getAccessories(actualProductInfoBean);
 assertEquals(0, actualAccessoriesList.size());
 }

 @Test
 public void testGetProduct_ValidAccessoriesFile_ReturnsAccessoriesList() throws IOException {
 String asin = "B08N69XJ87";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1", "ASIN2"));
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(Arrays.asList(
 new ProductDetails.AccessoryDetails("ASIN1", new MiniProduct("ASIN1", "http://image.com/image1.jpg", "Manufacturer1", "Accessory1", 9.99)),
 new ProductDetails.AccessoryDetails("ASIN2", new MiniProduct("ASIN2", "http://image.com/image2.jpg", "Manufacturer2", "Accessory2", 19.99))
 ));
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo actualProductInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 List<MiniProduct> expectedAccessoriesList = Arrays.asList(
 new MiniProduct("ASIN1", "http://image.com/image1.jpg", "Manufacturer1", "Accessory1", 9.99),
 new MiniProduct("ASIN2", "http://image.com/image2.jpg", "Manufacturer2", "Accessory2", 19.99)
 );
 List<MiniProduct> actualAccessoriesList = product.getAccessories(actualProductInfoBean);
 assertEquals(expectedAccessoriesList, actualAccessoriesList);
 }

 @Test
 public void testGetProduct_InvalidAccessoriesFile_ReturnsEmptyList() throws IOException {
 String asin = "B08N69XJ87";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1", "ASIN2"));
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
 ProductInfo actualProductInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 List<MiniProduct> expectedAccessoriesList = Arrays.asList();
 List<MiniProduct> actualAccessoriesList = product.getAccessories(actualProductInfoBean);
 assertEquals(expectedAccessoriesList, actualAccessoriesList);
 }
}