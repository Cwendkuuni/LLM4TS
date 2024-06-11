import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProduct_ValidASIN_Success() throws IOException, ClassNotFoundException {
 String asin = "B000123456";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");
 File accessoryFile = new File("test_files/asin_files/" + asin + "/accessories.xml");

 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNotNull(fileIn);

 ProductInfo productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 assertNotNull(productInfoBean);

 ProductDetails productDetails = productInfoBean.getDetails()[0];
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);
 expectedFullProduct.addAccessory(new MiniProduct("ASIN1", "http://example.com/image1.jpg", "Manufacturer1", "ProductName1", 9.99));
 expectedFullProduct.addAccessory(new MiniProduct("ASIN2", "http://example.com/image2.jpg", "Manufacturer2", "ProductName2", 12.99));
 expectedFullProduct.addSimilarItem(new MiniProduct("ASIN3", "http://example.com/image3.jpg", "Manufacturer3", "ProductName3", 15.99));

 FileInputStream accessoryFileInput = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1", "ASIN2"));
 ProductInfo accessoryInfoBean = (ProductInfo)new JOXBeanInputStream(accessoryFileInput).readObject(ProductInfo.class);
 assertNotNull(accessoryInfoBean);

 List<ProductDetails> accessoryDetails = accessoryInfoBean.getProductsArrayList();
 for (ProductDetails detail : accessoryDetails) {
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin(detail.getAsin());
 miniProduct.setImageURL(detail.getImageUrlSmall());
 miniProduct.setManufacturer(detail.getManufacturer());
 miniProduct.setName(detail.getProductName());
 miniProduct.setPrice(detail.getOurPrice());
 expectedFullProduct.addAccessory(miniProduct);
 }

 List<MiniProduct> similarItems = product.getProduct(asin, offer, page).getSimilarItems();
 assertArrayEquals(expectedFullProduct.getAccessories().toArray(), expectedFullProduct.getAccessories().toArray());
 assertArrayEquals(expectedFullProduct.getSimilarItems().toArray(), similarItems.toArray());
 }

 @Test
 public void testGetProduct_InvalidASIN_Null() throws IOException, ClassNotFoundException {
 String asin = "invalidASIN";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");

 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNull(fileIn);

 Product product = product.getProduct(asin, offer, page);
 assertNull(product);
 }

 @Test
 public void testGetProduct_InvalidFile_Null() throws IOException, ClassNotFoundException {
 String asin = "B000123456";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");
 File accessoryFile = new File("test_files/asin_files/" + asin + "/accessories.xml");

 FileInputStream asinFileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNotNull(asinFileInput);

 asinFileInput.close();
 FileInputStream accessoryFileInput = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1", "ASIN2"));
 assertNull(accessoryFileInput);

 Product product = product.getProduct(asin, offer, page);
 assertNull(product);
 }

 @Test
 public void testGetProduct_NullDetails_Null() throws IOException, ClassNotFoundException {
 String asin = "B000123456";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");
 File accessoryFile = new File("test_files/asin_files/" + asin + "/accessories.xml");

 FileInputStream asinFileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNotNull(asinFileInput);

 ProductInfo productInfoBean = new JOXBeanInputStream(asinFileInput).readObject(ProductInfo.class);
 assertNotNull(productInfoBean);

 productInfoBean.setDetails(null);
 Product product = product.getProduct(asin, offer, page);
 assertNull(product);
 }

 @Test
 public void testGetProduct_EmptyAccessoriesArray_Empty() throws IOException, ClassNotFoundException {
 String asin = "B000123456";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");
 File accessoryFile = new File("test_files/asin_files/" + asin + "/accessories.xml");

 FileInputStream asinFileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNotNull(asinFileInput);

 ProductInfo productInfoBean = new JOXBeanInputStream(asinFileInput).readObject(ProductInfo.class);
 assertNotNull(productInfoBean);

 productInfoBean.setAccessories(new Accessories());
 Product product = product.getProduct(asin, offer, page);
 assertNotNull(product);
 assertFalse(product.hasAccessories());
 }

 @Test
 public void testGetProduct_InvalidAccessoryFile_Null() throws IOException, ClassNotFoundException {
 String asin = "B000123456";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");
 File accessoryFile = new File("test_files/asin_files/" + asin + "/accessories.xml");

 FileInputStream asinFileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNotNull(asinFileInput);

 ProductInfo productInfoBean = new JOXBeanInputStream(asinFileInput).readObject(ProductInfo.class);
 assertNotNull(productInfoBean);

 Product product = product.getProduct(asin, offer, page);
 assertNull(product);
 }

 @Test
 public void testGetProduct_NullAccessoryBean_Null() throws IOException, ClassNotFoundException {
 String asin = "B000123456";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");
 File accessoryFile = new File("test_files/asin_files/" + asin + "/accessories.xml");

 FileInputStream asinFileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNotNull(asinFileInput);

 ProductInfo productInfoBean = new JOXBeanInputStream(asinFileInput).readObject(ProductInfo.class);
 assertNotNull(productInfoBean);

 productInfoBean.setAccessories(new Accessories());
 productInfoBean.getAccessories().setAccessoryArray(null);
 Product product = product.getProduct(asin, offer, page);
 assertNull(product);
 }

 @Test
 public void testGetProduct_NullProductsArrayList_Empty() throws IOException, ClassNotFoundException {
 String asin = "B000123456";
 String offer = "new";
 String page = "1";
 File asinFile = new File("test_files/asin_files/" + asin + "/heavy/" + offer + "/" + page + ".xml");
 File accessoryFile = new File("test_files/asin_files/" + asin + "/accessories.xml");

 FileInputStream asinFileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNotNull(asinFileInput);

 ProductInfo productInfoBean = new JOXBeanInputStream(asinFileInput).readObject(ProductInfo.class);
 assertNotNull(productInfoBean);

 ProductInfo accessoryInfoBean = new ProductInfo();
 productInfoBean.setAccessories(accessoryInfoBean);
 Product product = product.getProduct(asin, offer, page);
 assertNotNull(product);
 assertFalse(product.hasSimilarItems());
 }
}