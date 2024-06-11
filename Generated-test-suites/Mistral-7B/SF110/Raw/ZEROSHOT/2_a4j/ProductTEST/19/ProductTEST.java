package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.*;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {

 private Product product;
 private FileUtil fileUtil;
 private String asin = "B000NH1A3I";
 private String offer = "New";
 private String page = "1";

 @Before
 public void setup() {
 fileUtil = new FileUtil();
 product = new Product();
 }

 @Test
 public void testGetProductWithValidArgumentsReturnsProduct() throws IOException {
 File expectedFile = new File("src/test/resources/expectedASINFile.xml");
 InputStream inputStream = new FileInputStream(expectedFile);
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(expectedFile.getBytes());
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[1]);
 productInfoBean.getDetails()[0] = new ProductDetails();
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(new ArrayList<>());
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("ASIN1");
 miniProduct.setImageURL("imageURL1");
 miniProduct.setManufacturer("Manufacturer1");
 miniProduct.setName("ProductName1");
 miniProduct.setPrice(12.5);
 productDetails.getAccessories().getAccessoryArray().add(miniProduct);
 productInfoBean.setProductsArrayList(new ArrayList<>(Arrays.asList(new ProductDetails[]{productDetails})));
 Search search = new Search();
 ProductInfo searchResult = search.SimilaritesSearch(asin, "1");
 List<MiniProduct> similarItems = Arrays.asList(new MiniProduct[]{new MiniProduct("ASIN2", "imageURL2", "Manufacturer2", "ProductName2", 15.5)});
 searchResult.setProductsArrayList(similarItems);
 Product expectedProduct = new Product();
 expectedProduct.setDetails(productDetails);
 expectedProduct.setAccessories(productDetails.getAccessories());
 expectedProduct.addAccessory(miniProduct);
 expectedProduct.setSimilarItems(similarItems.toArray(new MiniProduct[0]));

 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(expectedFile);
 when(fileUtil.fetchAccessories(asin, productDetails.getAccessories().getAccessoryArray())).thenReturn(byteArrayInputStream);
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedProduct, actualProduct);
 }

 @Test
 public void testGetProductWithNullASINReturnsNull() {
 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(null);
 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertNull(actualProduct);
 }

 @Test
 public void testGetProductWithEmptyAccessoryArrayReturnsProductWithEmptyAccessoryList() throws IOException {
 File expectedFile = new File("src/test/resources/emptyAccessoryFile.xml");
 InputStream inputStream = new FileInputStream(expectedFile);
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(expectedFile.getBytes());
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[1]);
 productInfoBean.getDetails()[0] = new ProductDetails();
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(new ArrayList<>());
 productInfoBean.setProductsArrayList(new ArrayList<>());
 Search search = new Search();
 ProductInfo searchResult = search.SimilaritesSearch(asin, "1");
 Product expectedProduct = new Product();
 expectedProduct.setDetails(productDetails);
 expectedProduct.setSimilarItems(new MiniProduct[0]);

 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new File("src/test/resources/expectedASINFile.xml"));
 when(fileUtil.fetchAccessories(asin, productDetails.getAccessories().getAccessoryArray())).thenReturn(byteArrayInputStream);
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 FullProduct actualProduct = product.getProduct(asin, offer, page);
 assertEquals(expectedProduct, actualProduct);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullProductInfoBeanThrowsNullPointerException() {
 ProductInfo productInfoBean = null;
 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new File("src/test/resources/expectedASINFile.xml"));
 when(fileUtil.fetchAccessories(asin, Arrays.asList(new Accessory[0]))).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 product.getProduct(asin, offer, page);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetProductWithEmptyDetailsArrayThrowsArrayIndexOutOfBoundsException() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);

 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new File("src/test/resources/expectedASINFile.xml"));
 when(fileUtil.fetchAccessories(asin, Arrays.asList(new Accessory[0]))).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 product.getProduct(asin, offer, page);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetProductWithEmptyAccessoryArrayThrowsArrayIndexOutOfBoundsException() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(new ArrayList<>());

 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new File("src/test/resources/expectedASINFile.xml"));
 when(fileUtil.fetchAccessories(asin, Arrays.asList(new Accessory[0]))).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 product.getProduct(asin, offer, page);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullAccessoryArrayThrowsNullPointerException() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 productDetails.setAccessories(null);

 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new File("src/test/resources/expectedASINFile.xml"));
 when(fileUtil.fetchAccessories(asin, Arrays.asList(new Accessory[0]))).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 product.getProduct(asin, offer, page);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetProductWithNullAccessoryThrowsArrayIndexOutOfBoundsException() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(Arrays.asList(new Accessory[0]));
 Accessory accessory = null;

 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new File("src/test/resources/expectedASINFile.xml"));
 when(fileUtil.fetchAccessories(asin, Arrays.asList(new Accessory[0]))).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 product.getProduct(asin, offer, page).addAccessory(accessory);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullMiniProductThrowsNullPointerException() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[]{new ProductDetails()});
 ProductDetails productDetails = productInfoBean.getDetails()[0];
 productDetails.setAccessories(new Accessories());
 productDetails.getAccessories().setAccessoryArray(Arrays.asList(new Accessory[0]));
 MiniProduct miniProduct = null;
 Product expectedProduct = new Product();
 expectedProduct.setDetails(productDetails);
 expectedProduct.setSimilarItems(new MiniProduct[0]);

 when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new File("src/test/resources/expectedASINFile.xml"));
 when(fileUtil.fetchAccessories(asin, Arrays.asList(new Accessory[0]))).thenReturn(new ByteArrayInputStream(new byte[0]));
 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);

 product.getProduct(asin, offer, page).addSimilarItem(miniProduct);
 }

 @Test(expected = NullPointerException.class)
 public void testGetProductWithNullProductInfoBeanInSimilarItemsThrowsNullPointerException() {
 ProductInfo productInfoBean = null;
 ProductInfo productInfoBean2 = new ProductInfo();
 productInfoBean2.setProductsArrayList(Arrays.asList(new ProductDetails[]{new ProductDetails()}));
 MiniProduct miniProduct = new MiniProduct();
 Product expectedProduct = new Product();
 expectedProduct.setDetails(new ProductDetails());
 expectedProduct.setSimilarItems(new MiniProduct[]{miniProduct});

 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean2);
 product.getProduct(asin, offer, page).addSimilarItem(miniProduct);
 product.setDetails(expectedProduct.getDetails());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetProductWithOutOfBoundsIndexInSimilarItemsThrowsArrayIndexOutOfBoundsException() {
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setProductsArrayList(Arrays.asList(new ProductDetails[]{new ProductDetails()}));
 MiniProduct miniProduct = new MiniProduct();
 Product expectedProduct = new Product();
 expectedProduct.setDetails(new ProductDetails());
 expectedProduct.setSimilarItems(new MiniProduct[]{miniProduct});

 when(search.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean);
 product.getProduct(asin, offer, page).setDetails(expectedProduct.getDetails());
 product.getProduct(asin, offer, page).addSimilarItem(new MiniProduct("ASIN3", "imageURL3", "Manufacturer3", "ProductName3", 18.5));
 product.getProduct(asin, offer, page).addSimilarItem(miniProduct);
 product.getProduct(asin, offer, page).addSimilarItem(miniProduct);
 }
}