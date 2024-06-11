import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import com.wutka.jox.ProductInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 String asin = "B07K3WR597";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 fileIn.close();
 }
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNotNull(fullProduct);
 assertEquals(productDetails, fullProduct.getDetails());
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullAsin_ReturnsNull() {
 String asin = null;
 String offer = "New";
 String page = "1";
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullOffer_ReturnsNull() {
 String asin = "B07K3WR597";
 String offer = null;
 String page = "1";
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullPage_ReturnsNull() {
 String asin = "B07K3WR597";
 String offer = "New";
 String page = null;
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test
 public void testGetProduct_InvalidASIN_ReturnsNull() throws IOException {
 String asin = "InvalidASIN";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 assertNull(fileIn);
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test
 public void testGetProduct_EmptyDetailsArray_ReturnsNull() throws IOException {
 String asin = "B07K3WR597";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 productInfoBean.setDetails(new ProductDetails[0]);
 fileIn.close();
 }
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertNull(fullProduct);
 }

 @Test
 public void testGetProduct_AccessoryArrayIsEmpty_ReturnsFullProductWithoutAccessories() throws IOException {
 String asin = "B07K3WR597";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = productDetails.getAccessories();
 ArrayList accessoryArray = new ArrayList();
 if (accessories != null) {
 accessoryArray = accessories.getAccessoryArray();
 }
 productInfoBean.setAccessories(new Accessories());
 productInfoBean.getAccessories().setAccessoryArray(accessoryArray);
 fileIn.close();
 }
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertEquals(productDetails, fullProduct.getDetails());
 assertNull(fullProduct.getAccessories());
 }

 @Test
 public void testGetProduct_InvalidAccessoryFile_ReturnsFullProductWithoutAccessories() throws IOException {
 String asin = "B07K3WR597";
 String offer = "New";
 String page = "1";
 FileInputStream asinFile = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 FileInputStream accessoryFile = fileUtil.fetchAccessories(asin, Arrays.asList("InvalidAccessoryASIN1", "InvalidAccessoryASIN2"));
 assertNull(accessoryFile);
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertEquals(productDetails, fullProduct.getDetails());
 assertNull(fullProduct.getAccessories());
 }

 @Test
 public void testGetProduct_EmptySimilarItemsArrayList_ReturnsFullProductWithoutSimilarItems() throws IOException {
 String asin = "B07K3WR597";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 productInfoBean.setDetails(new ProductDetails[]{productDetails});
 productInfoBean.getDetails()[0].setAccessories(new Accessories());
 productInfoBean.getDetails()[0].getAccessories().setAccessoryArray(accessoryArray);
 productInfoBean.setProductsArrayList(new ArrayList());
 fileIn.close();
 }
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertEquals(productDetails, fullProduct.getDetails());
 assertNull(fullProduct.getAccessories());
 assertNull(fullProduct.getSimilarItems());
 }

 @Test
 public void testGetProduct_ValidInputWithAccessories_ReturnsFullProductWithAccessories() throws IOException {
 String asin = "B07K3WR597";
 String offer = "New";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo)new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 FileInputStream accessoryFile = fileUtil.fetchAccessories(asin, accessoryArray);
 ProductInfo productInfoBeanWithAccessories = new ProductInfo();
 if (accessoryFile != null) {
 productInfoBeanWithAccessories = (ProductInfo)new JOXBeanInputStream(accessoryFile).readObject(ProductInfo.class);
 productInfoBeanWithAccessories.setProductsArrayList(new ArrayList(Arrays.asList(detailsArray.toArray(new ProductInfo[0]))));
 }
 productInfoBean.setDetails(new ProductDetails[]{productDetails});
 productInfoBean.getDetails()[0].setAccessories(new Accessories());
 productInfoBean.getDetails()[0].getAccessories().setAccessoryArray(accessoryArray);
 productInfoBean.setProductsArrayList(new ArrayList(Arrays.asList(productInfoBeanWithAccessories)));
 fileIn.close();
 accessoryFile.close();
 }
 FullProduct fullProduct = product.getProduct(asin, offer, page);
 assertEquals(productDetails, fullProduct.getDetails());
 assertNotNull(fullProduct.getAccessories());
 assertNotNull(fullProduct.getSimilarItems());
 }
}