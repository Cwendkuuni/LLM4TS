import org.junit.Assert;
import org.junit.Test;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

 @Test
 public void testGetProductNullInput() {
 Assert.assertNull(product.getProduct(null, null, null));
 }

 @Test
 public void testGetProductInvalidAsin() {
 Assert.assertNull(product.getProduct("invalidASIN", null, null));
 }

 @Test
 public void testGetProductNoOfferAndPage() {
 ProductDetails productDetails = new ProductDetails();
 FullProduct fullProduct = new FullProduct();
 productDetails.setAccessories(new Accessories());
 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniProduct = new MiniProduct();
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 fullProduct.setDetails(productDetails);
 accessoryProductDetails.setAsin("accessoryASIN");
 accessoryProductDetails.setImageUrlSmall("accessoryImageURL");
 accessoryProductDetails.setManufacturer("accessoryManufacturer");
 accessoryProductDetails.setProductName("accessoryProductName");
 accessoryProductDetails.setOurPrice(new BigDecimal("10.00"));
 miniProduct.setAsin(accessoryProductDetails.getAsin());
 miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
 miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
 miniProduct.setName(accessoryProductDetails.getProductName());
 miniProduct.setPrice(accessoryProductDetails.getOurPrice());
 accessoryArray.add(accessoryProductDetails);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 accessoryProductInfoBean.setProductsArrayList(accessoryArray);
 productDetails.setAccessories(accessories);
 productDetails.setAccessories(accessoryProductInfoBean);
 fullProduct.addAccessory(miniProduct);

 Assert.assertEquals(fullProduct, product.getProduct("asin", null, null));
 }

 @Test
 public void testGetProductWithAccessories() throws IOException {
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniProduct = new MiniProduct();
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 productInfoBean.setProductsArrayList(accessoryArray);
 fullProduct.setDetails(productDetails);
 accessoryProductDetails.setAsin("accessoryASIN1");
 accessoryProductDetails.setImageUrlSmall("accessoryImageURL1");
 accessoryProductDetails.setManufacturer("accessoryManufacturer1");
 accessoryProductDetails.setProductName("accessoryProductName1");
 accessoryProductDetails.setOurPrice(new BigDecimal("10.00"));
 miniProduct.setAsin(accessoryProductDetails.getAsin());
 miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
 miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
 miniProduct.setName(accessoryProductDetails.getProductName());
 miniProduct.setPrice(accessoryProductDetails.getOurPrice());
 accessoryArray.add(accessoryProductDetails);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 accessoryProductInfoBean.setProductsArrayList(accessoryArray);
 productDetails.setAccessories(accessories);
 productDetails.setAccessories(accessoryProductInfoBean);
 fullProduct.addAccessory(miniProduct);

 ProductInfo expectedProductInfoBean = new ProductInfo();
 ProductDetails expectedProductDetails = new ProductDetails();
 expectedProductDetails.setAccessories(new Accessories());
 expectedProductDetails.setAccessories(accessoryProductInfoBean);
 expectedProductDetails.setDetails(productDetails);
 ProductDetails accessoryProductDetails2 = new ProductDetails();
 accessoryProductDetails2.setAsin("accessoryASIN2");
 accessoryProductDetails2.setImageUrlSmall("accessoryImageURL2");
 accessoryProductDetails2.setManufacturer("accessoryManufacturer2");
 accessoryProductDetails2.setProductName("accessoryProductName2");
 accessoryProductDetails2.setOurPrice(new BigDecimal("20.00"));
 MiniProduct miniProduct2 = new MiniProduct();
 miniProduct2.setAsin(accessoryProductDetails2.getAsin());
 miniProduct2.setImageURL(accessoryProductDetails2.getImageUrlSmall());
 miniProduct2.setManufacturer(accessoryProductDetails2.getManufacturer());
 miniProduct2.setName(accessoryProductDetails2.getProductName());
 miniProduct2.setPrice(accessoryProductDetails2.getOurPrice());
 ProductInfo accessoryProductInfoBean2 = new ProductInfo();
 accessoryProductInfoBean2.setProductsArrayList(Arrays.asList(miniProduct2));
 Accessories accessories2 = new Accessories();
 accessories2.setAccessoryArray(Arrays.asList(accessoryProductInfoBean2));
 expectedProductDetails.setAccessories(accessories2);
 ProductDetails expectedProductDetails2 = new ProductDetails();
 expectedProductDetails2.setAsin("expectedProductAsin");
 expectedProductDetails2.setDetails(expectedProductDetails);
 ProductInfo expectedProductInfoBean = new ProductInfo();
 expectedProductInfoBean.setDetails(new ProductDetails[]{expectedProductDetails2});

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 objectOutputStream.writeObject(expectedProductInfoBean);
 InputStream expectedFileInput = new ByteArrayInputStream(outputStream.toByteArray());

 ByteArrayOutputStream accessoryOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream accessoryObjectOutputStream = new ObjectOutputStream(accessoryOutputStream);
 accessoryObjectOutputStream.writeObject(accessoryProductInfoBean);
 InputStream accessoryFileInput = new ByteArrayInputStream(accessoryOutputStream.toByteArray());

 when(fileUtil.fetchASINFile("asin", "heavy", null, null)).thenReturn(expectedFileInput);
 when(fileUtil.fetchAccessories("asin", accessoryArray)).thenReturn(accessoryFileInput);

 FullProduct fullProductExpected = new FullProduct();
 fullProductExpected.setDetails(expectedProductDetails);

 FullProduct fullProductActual = product.getProduct("asin", null, null);

 assertEquals(fullProductExpected, fullProductActual);
 }

 @Test
 public void testGetProductWithSimilarItems() throws IOException {
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 ProductDetails accessoryProductDetails = new ProductDetails();
 MiniProduct miniProduct = new MiniProduct();
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 productInfoBean.setProductsArrayList(accessoryArray);
 fullProduct.setDetails(productDetails);
 accessoryProductDetails.setAsin("accessoryASIN1");
 accessoryProductDetails.setImageUrlSmall("accessoryImageURL1");
 accessoryProductDetails.setManufacturer("accessoryManufacturer1");
 accessoryProductDetails.setProductName("accessoryProductName1");
 accessoryProductDetails.setOurPrice(new BigDecimal("10.00"));
 miniProduct.setAsin(accessoryProductDetails.getAsin());
 miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
 miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
 miniProduct.setName(accessoryProductDetails.getProductName());
 miniProduct.setPrice(accessoryProductDetails.getOurPrice());
 accessoryArray.add(accessoryProductDetails);
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 accessoryProductInfoBean.setProductsArrayList(accessoryArray);
 productDetails.setAccessories(accessories);
 productDetails.setAccessories(accessoryProductInfoBean);
 fullProduct.addAccessory(miniProduct);

 ProductDetails similarProductDetails = new ProductDetails();
 ProductDetails accessoryProductDetails3 = new ProductDetails();
 MiniProduct miniProduct3 = new MiniProduct();
 Accessories accessories3 = new Accessories();
 ArrayList accessoryArray3 = new ArrayList();
 ProductInfo productInfoBean3 = new ProductInfo();
 productInfoBean3.setDetails(new ProductDetails[0]);
 ProductInfo productInfoBean4 = new ProductInfo();
 productInfoBean4.setProductsArrayList(accessoryArray3);
 ProductInfo productInfoBean5 = new ProductInfo();
 productInfoBean5.setProductsArrayList(Arrays.asList(miniProduct3));
 List<ProductInfo> productsArrayList = Arrays.asList(productInfoBean3, productInfoBean4, productInfoBean5);
 productInfoBean3.setProductsArrayList(productsArrayList);
 similarProductDetails.setAccessories(accessories3);
 similarProductDetails.setAccessories(accessoryProductInfoBean3);
 similarProductDetails.setDetails(productDetails);
 ProductDetails accessoryProductDetails4 = new ProductDetails();
 accessoryProductDetails4.setAsin("accessoryASIN2");
 accessoryProductDetails4.setImageUrlSmall("accessoryImageURL2");
 accessoryProductDetails4.setManufacturer("accessoryManufacturer2");
 accessoryProductDetails4.setProductName("accessoryProductName2");
 accessoryProductDetails4.setOurPrice(new BigDecimal("20.00"));
 miniProduct3 = new MiniProduct();
 miniProduct3.setAsin(accessoryProductDetails4.getAsin());
 miniProduct3.setImageURL(accessoryProductDetails4.getImageUrlSmall());
 miniProduct3.setManufacturer(accessoryProductDetails4.getManufacturer());
 miniProduct3.setName(accessoryProductDetails4.getProductName());
 miniProduct3.setPrice(accessoryProductDetails4.getOurPrice());
 accessoryArray3.add(accessoryProductDetails4);
 ProductInfo accessoryProductInfoBean3 = new ProductInfo();
 accessoryProductInfoBean3.setProductsArrayList(accessoryArray3);
 accessories3.setAccessoryArray(accessoryProductInfoBean3);
 similarProductDetails.setAccessories(accessories3);
 ProductDetails expectedProductDetails3 = new ProductDetails();
 expectedProductDetails3.setAsin("expectedProductAsin3");
 expectedProductDetails3.setDetails(similarProductDetails);
 ProductInfo expectedProductInfoBean3 = new ProductInfo();
 expectedProductInfoBean3.setDetails(new ProductDetails[]{expectedProductDetails3});

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 objectOutputStream.writeObject(expectedProductInfoBean3);
 InputStream expectedFileInput = new ByteArrayInputStream(outputStream.toByteArray());

 when(fileUtil.fetchASINFile("asin", "heavy", null, null)).thenReturn(expectedFileInput);
 when(search.SimilaritesSearch("asin", "1")).thenReturn(productInfoBean3);

 FullProduct fullProductExpected = new FullProduct();
 fullProductExpected.setDetails(expectedProductDetails3);

 FullProduct fullProductActual = product.getProduct("asin", null, null);

 assertEquals(fullProductExpected, fullProductActual);
 }
}