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

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;

 @Before
 public void setUp() {
 product = new Product();
 fileUtil = new FileUtil();
 }

 @Test
 public void testGetProduct_ValidInput_ReturnsFullProduct() throws IOException, ClassNotFoundException {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 ProductInfo productInfoBean = new ProductInfo();
 if (fileIn != null) {
 productInfoBean = (ProductInfo) new JOXBeanInputStream(fileIn).readObject(ProductInfo.class);
 fileIn.close();
 }
 assertNotNull(productInfoBean);
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productInfoBean.getDetails()[0]);
 Accessories accessories = expectedFullProduct.getDetails().getAccessories();
 ArrayList accessoryArray = accessories.getAccessoryArray();
 if (accessoryArray != null && accessoryArray.size() > 0) {
 FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 if (accessoryFileIn != null) {
 accessoryProductInfoBean = (ProductInfo) new JOXBeanInputStream(accessoryFileIn).readObject(ProductInfo.class);
 accessoryFileIn.close();
 }
 ArrayList detailsArray = accessoryProductInfoBean.getProductsArrayList();
 MiniProduct miniProduct = null;
 for (int x = 0; x < detailsArray.size(); ++x) {
 miniProduct = new MiniProduct();
 miniProduct.setAsin(detailsArray.get(x).getAsin());
 miniProduct.setImageURL(detailsArray.get(x).getImageUrlSmall());
 miniProduct.setManufacturer(detailsArray.get(x).getManufacturer());
 miniProduct.setName(detailsArray.get(x).getProductName());
 miniProduct.setPrice(detailsArray.get(x).getOurPrice());
 expectedFullProduct.addAccessory(miniProduct);
 }
 }
 product = product.getProduct(asin, offer, page);
 assertEquals(expectedFullProduct, product);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_NullFile_ReturnsNull() throws IOException, ClassNotFoundException {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
 if (fileIn != null) {
 fileIn.close();
 }
 product = product.getProduct(asin, offer, page);
 assertNull(product);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetProduct_EmptyDetailsArray_ReturnsNull() throws IOException, ClassNotFoundException {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 FileInputStream fileIn = new FileInputStream("testFile.txt");
 product = product.getProduct(asin, offer, page);
 fileIn.close();
 assertNull(product);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetProduct_InvalidInputs_ThrowsException() {
 // Testing with invalid inputs such as null or empty strings
 product = product.getProduct(null, null, null);
 assertThrows(IllegalArgumentException.class, () -> product.getProduct("", "", ""));
 }

 @Test
 public void testGetSimilarItems() throws IOException, ClassNotFoundException {
 String asin = "B000012345";
 String offer = "new";
 String page = "1";
 ArrayList<MiniProduct> expectedSimilarItems = new ArrayList<>(Arrays.asList(
 new MiniProduct("B000013456", "Test Image URL", "Test Manufacturer", "Test Product Name", 12.99),
 new MiniProduct("B000014567", "Test Image URL", "Test Manufacturer", "Test Product Name", 9.99)
 ));
 Product productWithSimilarItems = product.getProduct(asin, offer, page);
 assertEquals(expectedSimilarItems, productWithSimilarItems.getSimilarItems());
 }
}