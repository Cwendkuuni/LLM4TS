import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.*;
import net.kencochrane.a4j.file.*;
import net.kencochrane.a4j.beans.*;
import java.io.*;
import java.util.*;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setup() {
 fileUtil = new FileUtil();
 product = new Product();
 }

 @Test
 public void testGetProductNullFile() {
 FileInputStream fileIn = null;
 try {
 product = product.getProduct("ASIN123", "offer1", "page1");
 assertNull(product);
 fileIn = fileUtil.fetchASINFile("ASIN123", "heavy", "offer1", "page1");
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
 public void testGetProductValidFile() {
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 product = product.getProduct("ASIN123", "offer1", "page1");
 assertNotNull(product);
 fileIn = fileUtil.fetchASINFile("ASIN123", "heavy", "offer1", "page1");
 joxIn = new JOXBeanInputStream((InputStream)fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 assertNotNull(productInfoBean);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (fileIn != null) {
 try {
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 if (joxIn != null) {
 try {
 joxIn.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
}

 @Test
 public void testGetProductNullAccessoryFile() {
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 ProductDetails productDetails = null;
 try {
 product = product.getProduct("ASIN123", "offer1", "page1");
 productDetails = product.getDetails();
 assertNotNull(product);
 assertNotNull(productDetails);
 fileIn = fileUtil.fetchAccessories("ASIN123", productDetails.getAccessories().getAccessoryArray());
 } finally {
 if (fileIn != null) {
 try {
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
 assertNull(product.getAccessories());
}

 @Test
 public void testGetProductValidAccessoryFile() {
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 ProductDetails productDetails = null;
 ArrayList accessoryArray = null;
 MiniProduct miniProduct = null;
 try {
 product = product.getProduct("ASIN123", "offer1", "page1");
 productDetails = product.getDetails();
 assertNotNull(product);
 assertNotNull(productDetails);
 accessoryArray = productDetails.getAccessories().getAccessoryArray();
 fileIn = fileUtil.fetchAccessories("ASIN123", accessoryArray);
 joxIn = new JOXBeanInputStream((InputStream)fileIn);
 productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 fileIn.close();
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (fileIn != null) {
 try {
 fileIn.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 if (joxIn != null) {
 try {
 joxIn.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
 assertNotNull(product.getAccessories());
 for (int i = 0; i < product.getAccessories().size(); i++) {
 miniProduct = product.getAccessories().get(i);
 assertNotNull(miniProduct);
 assertNotNull(miniProduct.getAsin());
 assertNotNull(miniProduct.getImageURL());
 assertNotNull(miniProduct.getManufacturer());
 assertNotNull(miniProduct.getName());
 assertNotNull(miniProduct.getPrice());
 }
}

 @Test
 public void testGetProductNullSimilarItemsFile() {
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 try {
 product = product.getProduct("ASIN123", "offer1", "page1");
 productInfoBean = search.SimilaritesSearch("ASIN123", "1");
 } finally {
 if (productInfoBean != null && productInfoBean.getProductsArrayList() != null && productInfoBean.getProductsArrayList().size() > 0) {
 for (Object o : productInfoBean.getProductsArrayList()) {
 ProductDetails productDetails = (ProductDetails)o;
 assertNull(productDetails);
 }
 }
 }
 assertNull(product.getSimilarItems());
}

 @Test
 public void testGetProductValidSimilarItemsFile() {
 FileInputStream fileIn = null;
 JOXBeanInputStream joxIn = null;
 ProductInfo productInfoBean = null;
 ProductDetails productDetails = null;
 MiniProduct miniProduct = null;
 try {
 product = product.getProduct("ASIN123", "offer1", "page1");
 productInfoBean = search.SimilaritesSearch("ASIN123", "1");
 } finally {
 if (productInfoBean != null && productInfoBean.getProductsArrayList() != null && productInfoBean.getProductsArrayList().size() > 0) {
 for (Object o : productInfoBean.getProductsArrayList()) {
 productDetails = (ProductDetails)o;
 assertNotNull(productDetails);
 miniProduct = new MiniProduct();
 miniProduct.setAsin(productDetails.getAsin());
 miniProduct.setImageURL(productDetails.getImageUrlSmall());
 miniProduct.setManufacturer(productDetails.getManufacturer());
 miniProduct.setName(productDetails.getProductName());
 miniProduct.setPrice(productDetails.getOurPrice());
 product.addSimilarItem(miniProduct);
 }
 }
 }
 assertNotNull(product.getSimilarItems());
 for (int i = 0; i < product.getSimilarItems().size(); i++) {
 miniProduct = product.getSimilarItems().get(i);
 assertNotNull(miniProduct);
 assertNotNull(miniProduct.getAsin());
 assertNotNull(miniProduct.getImageURL());
 assertNotNull(miniProduct.getManufacturer());
 assertNotNull(miniProduct.getName());
 assertNotNull(miniProduct.getPrice());
 }
}
}