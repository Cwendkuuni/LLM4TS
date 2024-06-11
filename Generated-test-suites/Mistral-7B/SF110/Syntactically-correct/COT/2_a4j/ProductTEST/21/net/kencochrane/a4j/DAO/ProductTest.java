import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;
 private final String asin = "ASIN_VALUE";
 private final String offer = "OFFER_VALUE";
 private final String page = "PAGE_VALUE";

 @Before
 public void setUp() {
 fileUtil = new FileUtil();
 product = new Product();
 }

 @Test
 public void testGetProduct() {
 FullProduct expectedProduct = new FullProduct();
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 if (fileIn != null) {
 joxIn = new JOXBeanInputStream(fileIn);
 productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 }
 if (productInfoBean != null && productInfoBean.getDetails() != null && productInfoBean.getDetails().length > 0) {
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 expectedProduct.setDetails(productDetails);
 }
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedProduct, actualProduct);
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
}

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullDetails() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(null);
 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(null);
 when(fileUtil.fetchAccessories(asin, new ArrayList<>())).thenReturn(null);
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);
 Product expectedProduct = null;
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNull(actualProduct);
}

 @Test(expected = FileNotFoundException.class)
 public void testGetProduct_NullASINFile() {
 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(null);
 when(fileUtil.fetchAccessories(asin, new ArrayList<>())).thenReturn(new FileInputStream(new File("accessoriesFile.txt")));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(new ProductInfo());
 FileInputStream fileIn = mock(FileInputStream.class);
 when(fileIn.available()).thenReturn(0);
 ProductDetails productDetails = new ProductDetails();
 ProductDetails accessoryProductDetails = new ProductDetails();
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{productDetails, accessoryProductDetails});
 when(fileUtil.fetchAccessories(asin, new ArrayList<>())).thenReturn(fileIn);
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add(accessories);
 productDetails.setAccessories(accessories);
 assertEquals(accessoryArray.size(), 1);
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("MINI_PRODUCT_ASIN");
 ProductDetails detailedProduct = new ProductDetails();
 detailedProduct.setProductName("MINI_PRODUCT_NAME");
 detailedProduct.setImageUrlSmall("MINI_PRODUCT_IMAGE_URL");
 detailedProduct.setOurPrice(12.5);
 miniProduct.setDetails(detailedProduct);
 accessoryProductDetails.setProductName("ACCESSORY_PRODUCT_NAME");
 accessoryProductDetails.setImageUrlSmall("ACCESSORY_PRODUCT_IMAGE_URL");
 accessoryProductDetails.setOurPrice(5.0);
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);
 product.getProduct(asin, offer, page);
 fail("FileNotFoundException should be thrown");
}

 @Test(expected = IOException.class)
 public void testGetProduct_IOException() {
 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenThrow(new IOException());
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);
 try {
 product.getProduct(asin, offer, page);
 fail("IOException should be thrown");
 } catch (IOException e) {
 // Expected
 }
}

 @Test
 public void testGetProduct_AccessoriesFileIsNull() {
 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new FileInputStream(new File("ASIN_FILE.txt")));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(new ProductInfo());
 try {
 product.getProduct(asin, offer, page);
 fail("NullPointerException should be thrown for empty accessory array");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testGetProduct_AccessoriesFileIsEmpty() {
 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new FileInputStream(new File("ASIN_FILE.txt")));
 when(fileUtil.fetchAccessories(asin, new ArrayList<>())).thenReturn(new FileInputStream(new File("ACCESSORIES_FILE.txt")));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(new ProductInfo());
 when(fileUtil.fetchAccessories(asin, new ArrayList<>())).thenReturn(new FileInputStream(new File("ACCESSORIES_FILE_EMPTY.txt")));
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 ProductDetails accessoryProductDetails = new ProductDetails();
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{productDetails, accessoryProductDetails});
 try {
 product.getProduct(asin, offer, page);
 fail("Expected empty accessory array");
 } catch (NullPointerException e) {
 // Expected
 assertEquals(0, product.getProduct(asin, offer, page).getAccessories().size());
}
}
}