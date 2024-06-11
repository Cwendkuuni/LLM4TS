import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;

public class ProductTest {

 private Product product = new Product();

 @Test
 public void testGetProductNullAsin() {
 FullProduct expected = null;
 FullProduct actual = product.getProduct(null, "offer", "page");
 assertEquals(expected, actual);
 }

 @Test
 public void testGetProductNullOffer() {
 FullProduct expected = null;
 FullProduct actual = product.getProduct("asin", null, "page");
 assertEquals(expected, actual);
 }

 @Test
 public void testGetProductNullPage() {
 FullProduct expected = null;
 FullProduct actual = product.getProduct("asin", "offer", null);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetProductNoFile() {
 FullProduct expected = null;
 FullProduct actual = product.getProduct("asin", "offer", "page");
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductNullProductDetails() {
 FullProduct expected = new FullProduct();
 ProductDetails productDetails = null;
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(new ArrayList<>());
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 FullProduct actual = product.getProduct("asin", "offer", "page");
 assertNotSame(expected, actual);
 }

 @Test
 public void testGetProductEmptyAccessoryArray() {
 FullProduct expected = new FullProduct();
 FullProduct actual = product.getProduct("asin", "offer", "page");
 assertTrue(expected.getAccessories().getAccessoryArray().isEmpty());
 assertTrue(actual.getAccessories().getAccessoryArray().isEmpty());
 }

 @Test
 public void testGetProductNullAccessoryProductDetails() {
 FullProduct expected = new FullProduct();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(new ArrayList<>());
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 FullProduct actual = product.getProduct("asin", "offer", "page");
 assertNotSame(expected, actual);
 }

 @Test
 public void testGetProductNullMiniProduct() {
 FullProduct expected = new FullProduct();
 FullProduct actual = product.getProduct("asin", "offer", "page");
 assertTrue(expected.getSimilarItems().isEmpty());
 assertTrue(actual.getSimilarItems().isEmpty());
 }

 @Test
 public void testGetProductEmptySimilarItems() {
 FullProduct expected = new FullProduct();
 FullProduct actual = product.getProduct("asin", "offer", "page");
 assertTrue(expected.getDetails() != null);
 assertTrue(actual.getDetails() != null);
 assertTrue(expected.getDetails().equals(actual.getDetails()));
 assertTrue(expected.getAccessories().getAccessoryArray().isEmpty());
 assertTrue(actual.getAccessories().getAccessoryArray().isEmpty());
 assertTrue(expected.getSimilarItems().isEmpty());
 assertTrue(actual.getSimilarItems().isEmpty());
 }

 @Test
 public void testGetProductSuccess() {
 FullProduct expected = new FullProduct();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(Arrays.asList(new MiniProduct[] { new MiniProduct("asin1", "URL1", "Manufacturer1", "Name1", 10.0) }));
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 productInfoBean.setProductsArrayList(Arrays.asList(new ProductDetails[] { new ProductDetails("asin2", "URL2", "Manufacturer2", "Name2", new MiniProduct("asin3", "URL3", "Manufacturer3", "Name3", 20.0)) }));
 when(fileUtil.fetchASINFile("asin", "heavy", "offer", "page")).thenReturn(new FileInputStream("filePath1.txt"));
 when(fileUtil.fetchAccessories("asin", Arrays.asList(new String[] { "accessory1" }))).thenReturn(new FileInputStream("filePath2.txt"));
 FullProduct actual = product.getProduct("asin", "offer", "page");
 assertEquals(expected.getDetails(), actual.getDetails());
 assertEquals(expected.getAccessories().getAccessoryArray().size(), actual.getAccessories().getAccessoryArray().size());
 assertEquals(expected.getAccessories().getAccessoryArray().get(0).getAsin(), actual.getAccessories().getAccessoryArray().get(0).getAsin());
 assertEquals(expected.getSimilarItems().size(), actual.getSimilarItems().size());
 assertEquals(expected.getSimilarItems().get(0).getAsin(), actual.getSimilarItems().get(0).getAsin());
 }
}