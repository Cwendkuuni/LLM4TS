import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.Search;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;
 private Search search;

 @Before
 public void setUp() {
 product = new Product();
 fileUtil = mock(FileUtil.class);
 search = mock(Search.class);
 }

 @Test
 public void testGetProductWithValidInputs() throws IOException {
 // Given
 String asin = "B001234567";
 String offer = "new";
 String page = "1";
 ProductDetails productDetails = new ProductDetails();
 Accessories accessories = new Accessories();
 ArrayList<ProductDetails> detailsArray = new ArrayList<>();
 MiniProduct miniProduct = new MiniProduct();
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{productDetails});
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(new ArrayList<>(Arrays.asList(new ProductDetails())));
 detailsArray.add(productDetails);
 ProductInfo productInfoForAccessories = new ProductInfo();
 productInfoForAccessories.setProductsArrayList(detailsArray);
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 when(fileUtil.fetchASINFile(asin, ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(inputStream);
 when(search.SimilaritesSearch(asin, ArgumentMatchers.anyString())).thenReturn(productInfoForAccessories);

 // When
 FullProduct fullProduct = product.getProduct(asin, offer, page);

 // Then
 assertNotNull(fullProduct);
 assertEquals(productDetails, fullProduct.getDetails());
 assertEquals(1, fullProduct.getAccessories().size());
 assertEquals(miniProduct, fullProduct.getAccessories().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullAsin() {
 // Given
 String asin = null;
 String offer = "new";
 String page = "1";

 // When
 Product fullProduct = product.getProduct(asin, offer, page);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullOffer() {
 // Given
 String asin = "B001234567";
 String offer = null;
 String page = "1";

 // When
 Product fullProduct = product.getProduct(asin, offer, page);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullPage() {
 // Given
 String asin = "B001234567";
 String offer = "new";
 String page = null;

 // When
 Product fullProduct = product.getProduct(asin, offer, page);
 }

 @Test
 public void testGetProductWithInvalidASINFile() throws IOException {
 // Given
 String asin = "B001234567";
 String offer = "new";
 String page = "1";
 when(fileUtil.fetchASINFile(asin, ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null);

 // When
 FullProduct fullProduct = product.getProduct(asin, offer, page);

 // Then
 assertNull(fullProduct);
 }

 @Test
 public void testGetProductWithEmptyDetailsArray() throws IOException {
 // Given
 String asin = "B001234567";
 String offer = "new";
 String page = "1";
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 when(fileUtil.fetchASINFile(asin, ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(inputStream);
 when(search.SimilaritesSearch(asin, ArgumentMatchers.anyString())).thenReturn(productInfoBean);

 // When
 FullProduct fullProduct = product.getProduct(asin, offer, page);

 // Then
 assertNotNull(fullProduct);
 assertEquals(0, fullProduct.getDetails().size());
 }

 @Test
 public void testGetProductWithEmptyAccessoriesArray() throws IOException {
 // Given
 String asin = "B001234567";
 String offer = "new";
 String page = "1";
 ProductDetails productDetails = new ProductDetails();
 Accessories accessories = new Accessories();
 ArrayList<ProductDetails> detailsArray = new ArrayList<>();
 detailsArray.add(productDetails);
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(new ArrayList<>());
 ProductInfo productInfoForAccessories = new ProductInfo();
 productInfoForAccessories.setProductsArrayList(detailsArray);
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 when(fileUtil.fetchASINFile(asin, ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(inputStream);
 when(search.SimilaritesSearch(asin, ArgumentMatchers.anyString())).thenReturn(productInfoForAccessories);

 // When
 FullProduct fullProduct = product.getProduct(asin, offer, page);

 // Then
 assertNotNull(fullProduct);
 assertEquals(0, fullProduct.getAccessories().size());
 }

 @Test
 public void testGetProductWithEmptySimilarItemsArray() throws IOException {
 // Given
 String asin = "B001234567";
 String offer = "new";
 String page = "1";
 ProductDetails productDetails = new ProductDetails();
 Accessories accessories = new Accessories();
 ArrayList<ProductDetails> detailsArray = new ArrayList<>();
 detailsArray.add(productDetails);
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(new ArrayList<>(Arrays.asList(new ProductDetails())));
 ProductInfo productInfoForAccessories = new ProductInfo();
 List<ProductDetails> productsArrayList = Arrays.asList(new ProductDetails());
 productInfoForAccessories.setProductsArrayList(productsArrayList);
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 when(fileUtil.fetchASINFile(asin, ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(inputStream);
 when(search.SimilaritesSearch(asin, ArgumentMatchers.anyString())).thenReturn(productInfoForAccessories);

 // When
 FullProduct fullProduct = product.getProduct(asin, offer, page);

 // Then
 assertNotNull(fullProduct);
 assertEquals(0, fullProduct.getSimilarItems().size());
 }

 @Test
 public void testGetProductWithInvalidAccessoriesFile() throws IOException {
 // Given
 String asin = "B001234567";
 String offer = "new";
 String page = "1";
 ProductDetails productDetails = new ProductDetails();
 Accessories accessories = new Accessories();
 ArrayList<ProductDetails> detailsArray = new ArrayList<>();
 detailsArray.add(productDetails);
 productDetails.setAccessories(accessories);
 accessories.setAccessoryArray(new ArrayList<>(Arrays.asList(new ProductDetails())));
 ProductInfo productInfoForAccessories = new ProductInfo();
 List<ProductDetails> productsArrayList = Arrays.asList(new ProductDetails());
 productInfoForAccessories.setProductsArrayList(productsArrayList);
 InputStream inputStream = null;
 when(fileUtil.fetchASINFile(asin, ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(inputStream);
 when(search.SimilaritesSearch(asin, ArgumentMatchers.anyString())).thenReturn(productInfoForAccessories);

 // When
 FullProduct fullProduct = product.getProduct(asin, offer, page);

 // Then
 assertNotNull(fullProduct);
 assertEquals(0, fullProduct.getAccessories().size());
 }
}