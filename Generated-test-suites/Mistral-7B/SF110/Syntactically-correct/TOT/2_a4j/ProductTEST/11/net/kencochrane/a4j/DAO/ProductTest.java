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
import net.kencochrane.a4j.beans.ProductDetailsArrayList;
import net.kencochrane.a4j.beans.MiniProductArrayList;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setUp() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProductNullInputs() {
 assertNull(product.getProduct(null, null, null));
 }

 @Test
 public void testGetProductEmptyDetails() {
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 FullProduct fullProduct = new FullProduct();
 try {
 FileInputStream fileIn = fileUtil.fetchASINFile("asin1", "offer1", "page1", "heavy");
 ProductInfo emptyProductInfoBean = new ProductInfo();
 emptyProductInfoBean.setDetails(new ProductDetails[0]);
 joxIn.close();
 fileIn.close();
 assertNull(product.getProduct("asin1", "offer1", "page1"));
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetProductValidInput() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add("accessory1");
 Accessories accessoryObject = new Accessories();
 accessoryObject.setAccessoryArray(accessoryArray);
 productDetails.setAccessories(accessoryObject);
 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniproduct = new MiniProduct();
 miniproduct.setAsin("accessory1");
 miniproduct.setImageURL("imageURL1");
 miniproduct.setManufacturer("manufacturer1");
 miniproduct.setName("name1");
 miniproduct.setPrice(10.0);
 accessoryProductDetails.setProductName("name1");
 accessoryProductDetails.setOurPrice(10.0);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 accessoryProductInfoBean.setProductsArrayList(new ArrayList(Arrays.asList(accessoryProductDetails)));
 ProductDetails[] detailsArray = new ProductDetails[]{productDetails, new ProductDetails()};
 productInfoBean.setDetails(detailsArray);
 FullProduct fullProduct = new FullProduct();
 fullProduct.setDetails(productDetails);
 try {
 FileInputStream fileIn = fileUtil.fetchASINFile("asin1", "offer1", "page1", "heavy");
 FileInputStream fileIn2 = fileUtil.fetchAccessories("asin1", accessoryArray);
 joxIn.close();
 joxIn.close();
 FileInputStream productInfoFile = fileUtil.fetchASINFile("asin1", "offer1", "page1", "light");
 ProductInfo productInfo = new ProductInfo();
 productInfo = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 assertNotNull(product.getProduct("asin1", "offer1", "page1"));
 assertEquals(productInfo.getDetails()[0], product.getProduct("asin1", "offer1", "page1").getDetails()[0]);
 assertEquals(1, product.getProduct("asin1", "offer1", "page1").getAccessories().size());
 assertEquals(miniproduct, product.getProduct("asin1", "offer1", "page1").getAccessories().get(0));
 assertEquals(productInfo.getProductsArrayList().size(), product.getProduct("asin1", "offer1", "page1").getSimilarItems().size());
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetProductNullAccessoryFile() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add("accessory1");
 Accessories accessoryObject = new Accessories();
 accessoryObject.setAccessoryArray(accessoryArray);
 productDetails.setAccessories(accessoryObject);
 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniproduct = new MiniProduct();
 miniproduct.setAsin("accessory1");
 miniproduct.setImageURL("imageURL1");
 miniproduct.setManufacturer("manufacturer1");
 miniproduct.setName("name1");
 miniproduct.setPrice(10.0);
 accessoryProductDetails.setProductName("name1");
 accessoryProductDetails.setOurPrice(10.0);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 accessoryProductInfoBean.setProductsArrayList(new ArrayList(Arrays.asList(accessoryProductDetails)));
 ProductDetails[] detailsArray = new ProductDetails[]{productDetails, new ProductDetails()};
 productInfoBean.setDetails(detailsArray);
 FullProduct fullProduct = new FullProduct();
 fullProduct.setDetails(productDetails);
 try {
 FileInputStream fileIn = fileUtil.fetchASINFile("asin1", "offer1", "page1", "heavy");
 joxIn.close();
 fileIn.close();
 assertNull(product.getProduct("asin1", "offer1", "page1"));
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetProductEmptyAccessoryArray() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.clear();
 Accessories accessoryObject = new Accessories();
 accessoryObject.setAccessoryArray(accessoryArray);
 productDetails.setAccessories(accessoryObject);
 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniproduct = new MiniProduct();
 miniproduct.setAsin("accessory1");
 miniproduct.setImageURL("imageURL1");
 miniproduct.setManufacturer("manufacturer1");
 miniproduct.setName("name1");
 miniproduct.setPrice(10.0);
 accessoryProductDetails.setProductName("name1");
 accessoryProductDetails.setOurPrice(10.0);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 accessoryProductInfoBean.setProductsArrayList(new ArrayList(Arrays.asList(accessoryProductDetails)));
 ProductDetails[] detailsArray = new ProductDetails[]{productDetails, new ProductDetails()};
 productInfoBean.setDetails(detailsArray);
 FullProduct fullProduct = new FullProduct();
 fullProduct.setDetails(productDetails);
 try {
 FileInputStream fileIn = fileUtil.fetchASINFile("asin1", "offer1", "page1", "heavy");
 FileInputStream fileIn2 = fileUtil.fetchAccessories("asin1", accessoryArray);
 joxIn.close();
 joxIn.close();
 FileInputStream productInfoFile = fileUtil.fetchASINFile("asin1", "offer1", "page1", "light");
 ProductInfo productInfo = new ProductInfo();
 productInfo = (ProductInfo)joxIn.readObject(ProductInfo.class);
 joxIn.close();
 assertNotNull(product.getProduct("asin1", "offer1", "page1"));
 assertEquals(productInfo.getDetails()[0], product.getProduct("asin1", "offer1", "page1").getDetails()[0]);
 assertEquals(0, product.getProduct("asin1", "offer1", "page1").getAccessories().size());
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetProductInvalidASIN() {
 assertNull(product.getProduct("asinInvalid", "offer1", "page1"));
 }

 @Test
 public void testGetProductNullOffer() {
 assertNull(product.getProduct("asin1", null, "page1"));
 }

 @Test
 public void testGetProductNullPage() {
 assertNull(product.getProduct("asin1", "offer1", null));
 }

 @Test
 public void testGetProductEmptyOffer() {
 assertNull(product.getProduct("asin1", "", "page1"));
 }

 @Test
 public void testGetProductEmptyPage() {
 assertNull(product.getProduct("asin1", "offer1", ""));
 }
}