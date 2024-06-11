package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.DAO.*;
import net.kencochrane.a4j.file.*;
import org.junit.Test;

public class ProductTest {

 private Product product = new Product();
 private FullProduct expectedFullProduct;
 private MiniProduct expectedMiniProduct;
 private FileUtil fileUtil = new FileUtil();

 @Test
 public void testGetProduct_ValidInputs_ReturnsExpectedFullProduct() throws Exception {
 // Arrange
 String asin = "B08N5ZV1D3";
 String offer = "New";
 String page = "1";
 expectedFullProduct = new FullProduct();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = new Accessories();
 List<MiniProduct> accessoryArray = new ArrayList<>();
 Accessory accessory = new Accessory();
 accessory.setAsin("B08N5ZV1D4");
 accessory.setImageURL("https://example.com/image1.jpg");
 accessory.setManufacturer("Manufacturer1");
 accessory.setProductName("Accessory1");
 accessory.setOurPrice(19.99);
 accessoryArray.add(accessory);
 ProductDetails accessoryProductDetails = new ProductDetails();
 accessoryProductDetails.setAccessoryArray(accessoryArray);
 MiniProduct miniProduct = new MiniProduct();
 expectedMiniProduct = new MiniProduct();
 expectedMiniProduct.setAsin(accessoryProductDetails.getAsin());
 expectedMiniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
 expectedMiniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
 expectedMiniProduct.setName(accessoryProductDetails.getProductName());
 expectedMiniProduct.setPrice(accessoryProductDetails.getOurPrice());
 productDetails.setProductName("ProductName");
 productDetails.setProductDetailsArray(new ProductDetails[1]);
 productDetails.setProductDetailsArray()[0] = productDetails;
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);

 // Act
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);

 // Assert
 assertNotNull(actualFullProduct);
 assertEquals(expectedFullProduct.getDetails().getProductName(), actualFullProduct.getDetails().getProductName());
 assertNotNull(actualFullProduct.getDetails().getAccessories());
 assertEquals(expectedFullProduct.getDetails().getAccessories().getAccessoryArray().size(), actualFullProduct.getDetails().getAccessories().getAccessoryArray().size());
 for (int i = 0; i < actualFullProduct.getDetails().getAccessories().getAccessoryArray().size(); i++) {
 MiniProduct actualMiniProduct = actualFullProduct.getDetails().getAccessories().getAccessoryArray().get(i);
 MiniProduct expectedMiniProduct = expectedFullProduct.getDetails().getAccessories().getAccessoryArray().get(i);
 assertEquals(expectedMiniProduct.getAsin(), actualMiniProduct.getAsin());
 assertEquals(expectedMiniProduct.getImageURL(), actualMiniProduct.getImageURL());
 assertEquals(expectedMiniProduct.getManufacturer(), actualMiniProduct.getManufacturer());
 assertEquals(expectedMiniProduct.getName(), actualMiniProduct.getName());
 assertEquals(expectedMiniProduct.getPrice(), actualMiniProduct.getPrice(), 0.01);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetProduct_InvalidFileInput_ReturnsNull() throws Exception {
 // Arrange
 String asin = "B08N5ZV1D3";
 String offer = "New";
 String page = "1";

 // Act
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);

 // Assert
 assertNull(actualFullProduct);
 }

 @Test
 public void testGetProduct_ValidInputsWithAccessories_ReturnsExpectedFullProductWithAccessories() throws Exception {
 // Arrange (same as testGetProduct_ValidInputs_ReturnsExpectedFullProduct)
 // Act
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);

 // Assert (same as testGetProduct_ValidInputs_ReturnsExpectedFullProduct)
 // Additional assertions for accessories
 assertEquals(expectedFullProduct.getDetails().getAccessories().getAccessoryArray().size(), actualFullProduct.getDetails().getAccessories().getAccessoryArray().size());
 for (int i = 0; i < actualFullProduct.getDetails().getAccessories().getAccessoryArray().size(); i++) {
 MiniProduct actualMiniProduct = actualFullProduct.getDetails().getAccessories().getAccessoryArray().get(i);
 MiniProduct expectedMiniProduct = expectedFullProduct.getDetails().getAccessories().getAccessoryArray().get(i);
 assertEquals(expectedMiniProduct.getAsin(), actualMiniProduct.getAsin());
 assertEquals(expectedMiniProduct.getImageURL(), actualMiniProduct.getImageURL());
 assertEquals(expectedMiniProduct.getManufacturer(), actualMiniProduct.getManufacturer());
 assertEquals(expectedMiniProduct.getName(), actualMiniProduct.getName());
 assertEquals(expectedMiniProduct.getPrice(), actualMiniProduct.getPrice(), 0.01);
 }
 }

 @Test
 public void testGetProduct_ValidInputsWithSimilarItems_ReturnsExpectedFullProductWithSimilarItems() throws Exception {
 // Arrange
 String asin = "B08N5ZV1D3";
 String offer = "New";
 String page = "1";
 expectedFullProduct = new FullProduct();
 productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productDetails.setProductName("ProductName");
 productDetails.setProductDetailsArray(new ProductDetails[1]);
 productDetails.setProductDetailsArray()[0] = productDetails;
 FullProduct expectedFullProduct = new FullProduct();
 expectedFullProduct.setDetails(productDetails);
 List<MiniProduct> similarItems = new ArrayList<>();
 MiniProduct similarItem = new MiniProduct();
 similarItem.setAsin("B08N5ZV1D5");
 similarItem.setImageURL("https://example.com/image2.jpg");
 similarItem.setManufacturer("Manufacturer2");
 similarItem.setName("SimilarItem");
 similarItem.setPrice(14.99);
 similarItems.add(similarItem);
 productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 productDetails.setProductName("AccessoryProductName");
 productDetails.setProductDetailsArray(new ProductDetails[1]);
 productDetails.setProductDetailsArray()[0] = productDetails;
 productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[2]);
 productInfoBean.setDetails(productDetails);
 productInfoBean.setDetails()[0] = productDetails;
 productInfoBean.setProductsArrayList(similarItems);
 Search search = new Search();
 productInfoBean = search.SimilaritesSearch(asin, "1");
 expectedFullProduct.setSimilarItems(productInfoBean.getProductsArrayList());

 // Act
 FullProduct actualFullProduct = product.getProduct(asin, offer, page);

 // Assert
 assertNotNull(actualFullProduct);
 assertEquals(expectedFullProduct.getDetails().getProductName(), actualFullProduct.getDetails().getProductName());
 assertEquals(expectedFullProduct.getDetails().getAccessories().getAccessoryArray().size(), actualFullProduct.getDetails().getAccessories().getAccessoryArray().size());
 for (int i = 0; i < actualFullProduct.getDetails().getAccessories().getAccessoryArray().size(); i++) {
 MiniProduct actualMiniProduct = actualFullProduct.getDetails().getAccessories().getAccessoryArray().get(i);
 MiniProduct expectedMiniProduct = expectedFullProduct.getDetails().getAccessories().getAccessoryArray().get(i);
 assertEquals(expectedMiniProduct.getAsin(), actualMiniProduct.getAsin());
 assertEquals(expectedMiniProduct.getImageURL(), actualMiniProduct.getImageURL());
 assertEquals(expectedMiniProduct.getManufacturer(), actualMiniProduct.getManufacturer());
 assertEquals(expectedMiniProduct.getName(), actualMiniProduct.getName());
 assertEquals(expectedMiniProduct.getPrice(), actualMiniProduct.getPrice(), 0.01);
 }
 assertEquals(expectedFullProduct.getDetails().getProductName(), actualFullProduct.getDetails().getProductName());
 assertEquals(expectedFullProduct.getSimilarItems().size(), actualFullProduct.getSimilarItems().size());
 for (int i = 0; i < actualFullProduct.getSimilarItems().size(); i++) {
 MiniProduct actualMiniProduct = actualFullProduct.getSimilarItems().get(i);
 MiniProduct expectedMiniProduct = expectedFullProduct.getSimilarItems().get(i);
 assertEquals(expectedMiniProduct.getAsin(), actualMiniProduct.getAsin());
 assertEquals(expectedMiniProduct.getImageURL(), actualMiniProduct.getImageURL());
 assertEquals(expectedMiniProduct.getManufacturer(), actualMiniProduct.getManufacturer());
 assertEquals(expectedMiniProduct.getName(), actualMiniProduct.getName());
 assertEquals(expectedMiniProduct.getPrice(), actualMiniProduct.getPrice(), 0.01);
 }
 }
}