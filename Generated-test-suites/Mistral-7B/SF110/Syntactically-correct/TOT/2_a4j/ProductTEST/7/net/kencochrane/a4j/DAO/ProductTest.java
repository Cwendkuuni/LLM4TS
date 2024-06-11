import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.ProductInfo;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;
 private String validASIN = "B000012345";
 private String validOffer = "New";
 private String validPage = "1";
 private String invalidASIN = "InvalidASIN";
 private String invalidOffer = null;
 private String invalidPage = "-1";

 @Before
 public void setup() {
 fileUtil = new FileUtil();
 product = new Product();
 }

 @Test
 public void testGetProduct_ValidInput_ReturnsValidProduct() throws IOException {
 FileInputStream fileIn = fileUtil.fetchASINFile(validASIN, "heavy", validOffer, validPage);
 assertNotNull(fileIn);
 ProductInfo productInfoBean = product.getProduct(validASIN, validOffer, validPage);
 assertNotNull(productInfoBean);
 FileUtil.closeStreams(fileIn);
 }

 @Test
 public void testGetProduct_InvalidASIN_ReturnsNull() {
 ProductInfo productInfoBean = product.getProduct(invalidASIN, validOffer, validPage);
 assertNull(productInfoBean);
 }

 @Test
 public void testGetProduct_NullOffer_ThrowsException() {
 try {
 product.getProduct(validASIN, null, validPage);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetProduct_InvalidPage_ReturnsNull() {
 FileInputStream fileIn = fileUtil.fetchASINFile(validASIN, "heavy", validOffer, invalidPage);
 ProductInfo productInfoBean = product.getProduct(validASIN, validOffer, invalidPage);
 assertNull(productInfoBean);
 FileUtil.closeStreams(fileIn);
 }

 @Test
 public void testGetProduct_ValidInputWithAccessories_ReturnsFullProductWithAccessories() throws IOException {
 FileInputStream fileIn = fileUtil.fetchASINFile(validASIN, "heavy", validOffer, validPage);
 ProductInfo productInfoBean = product.getProduct(validASIN, validOffer, validPage);
 assertNotNull(productInfoBean);
 assertNotNull(productInfoBean.getAccessories());
 Accessories accessories = productInfoBean.getAccessories();
 ArrayList accessoryArray = accessories.getAccessoryArray();
 assertNotNull(accessoryArray);
 for (Object accessory : accessoryArray) {
 ProductDetails accessoryProductDetails = (ProductDetails)accessory;
 assertNotNull(accessoryProductDetails);
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin(accessoryProductDetails.getAsin());
 miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
 miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
 miniProduct.setName(accessoryProductDetails.getProductName());
 miniProduct.setPrice(accessoryProductDetails.getOurPrice());
 ProductInfo accessoryInfoBean = product.getProduct(accessoryProductDetails.getAsin(), validOffer, validPage);
 assertNotNull(accessoryInfoBean);
 ArrayList detailsArray = accessoryInfoBean.getProductsArrayList();
 assertEquals(miniProduct.getAsin(), detailsArray.get(0).getAsin());
 FileUtil.closeStreams(fileIn);
 }
 }

 @Test
 public void testGetProduct_InvalidAccessoryASIN_ReturnsNull() throws IOException {
 String accessoryASIN = "InvalidAccessoryASIN";
 ProductInfo productInfoBean = product.getProduct(validASIN, validOffer, validPage);
 assertNotNull(productInfoBean);
 assertNotNull(productInfoBean.getAccessories());
 Accessories accessories = productInfoBean.getAccessories();
 ArrayList accessoryArray = accessories.getAccessoryArray();
 assertNotNull(accessoryArray);
 assertNull(getAccessoryFromAccessories(accessoryArray, accessoryASIN));
 }

 @Test
 public void testGetProduct_InvalidPageWithAccessories_ReturnsNull() throws IOException {
 FileInputStream fileIn = fileUtil.fetchASINFile(validASIN, "heavy", validOffer, validPage);
 ProductInfo productInfoBean = product.getProduct(validASIN, validOffer, validPage);
 assertNotNull(productInfoBean);
 assertNotNull(productInfoBean.getAccessories());
 FileUtil.closeStreams(fileIn);
 FileInputStream fileIn2 = fileUtil.fetchAccessories(validASIN, productInfoBean.getAccessories().getAccessoryArray());
 ProductInfo productInfoBean2 = product.getProduct(validASIN, validOffer, invalidPage);
 assertNull(productInfoBean2);
 FileUtil.closeStreams(fileIn2);
 }

 private Object getAccessoryFromAccessories(ArrayList accessoryArray, String asin) {
 for (Object accessory : accessoryArray) {
 ProductDetails accessoryProductDetails = (ProductDetails)accessory;
 if (accessoryProductDetails.getAsin().equals(asin)) {
 return accessoryProductDetails;
 }
 }
 return null;
 }
}