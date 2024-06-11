package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;

public class ProductTest {

 private Product product = new Product();
 private FileUtil fileUtil = new FileUtil();
 private FullProduct expectedProduct;
 private FullProduct actualProduct;

 @Test
 public void testGetProductNullInput() {
 expectedProduct = null;
 actualProduct = product.getProduct("", "", "");
 assertEquals(expectedProduct, actualProduct);
 }

 @Test
 public void testGetProductValidInputWithEmptyAccessoryArray() throws Exception {
 String asin = "B074Q1KWQM";
 String offer = "new";
 String page = "1";
 expectedProduct = new FullProduct();
 File file = new File("src/test/resources/ProductFiles/Product_" + asin + "_heavy_" + offer + "_" + page + ".xml");
 FileInputStream fileIn = new FileInputStream(file);
 ProductInfo productInfoBean = new ProductInfo();
 productInfoBean.setDetails(new ProductDetails[0]);
 expectedProduct.setDetails(productInfoBean.getDetails()[0]);
 actualProduct = product.getProduct(asin, offer, page);
 fileIn.close();
 assertEquals(expectedProduct, actualProduct);
 }

 @Test
 public void testGetProductValidInputWithAccessoryArray() throws Exception {
 String asin = "B074Q1KWQM";
 String offer = "new";
 String page = "1";
 expectedProduct = new FullProduct();
 File file = new File("src/test/resources/ProductFiles/Product_" + asin + "_heavy_" + offer + "_" + page + ".xml");
 FileInputStream fileIn = new FileInputStream(file);
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = productDetails.getAccessories();
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add("ASIN1");
 accessories.setAccessoryArray(accessoryArray);
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 expectedProduct.setDetails(productInfoBean.getDetails()[0]);
 FileInputStream accessoryFileIn = new FileInputStream(new File("src/test/resources/ProductFiles/Accessories_" + asin + "_ASIN1.xml"));
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 ArrayList detailsArray = new ArrayList();
 MiniProduct miniProduct = new MiniProduct();
 detailsArray.add(new ProductDetails() {
 @Override
 public String getAsin() {
 return "ASIN2";
 }

 @Override
 public String getImageUrlSmall() {
 return "http://image.com/image1.jpg";
 }

 @Override
 public String getManufacturer() {
 return "Manufacturer1";
 }

 @Override
 public String getProductName() {
 return "ProductName1";
 }

 @Override
 public double getOurPrice() {
 return 12.99;
 }
 });
 accessoryProductInfoBean.setProductsArrayList(detailsArray);
 expectedProduct.setAccessories(accessories);
 expectedProduct.setSimilarItems(new ArrayList<>());
 actualProduct = product.getProduct(asin, offer, page);
 fileIn.close();
 accessoryFileIn.close();
 assertEquals(expectedProduct, actualProduct);
 }

 @Test
 public void testGetProductValidInputWithSimilarItems() throws Exception {
 String asin = "B074Q1KWQM";
 String offer = "new";
 String page = "1";
 expectedProduct = new FullProduct();
 File file = new File("src/test/resources/ProductFiles/Product_" + asin + "_heavy_" + offer + "_" + page + ".xml");
 FileInputStream fileIn = new FileInputStream(file);
 ProductInfo productInfoBean = new ProductInfo();
 ProductDetails productDetails = new ProductDetails();
 productDetails.setAccessories(new Accessories());
 Accessories accessories = productDetails.setAccessories(new Accessories());
 ArrayList accessoryArray = new ArrayList();
 accessoryArray.add("ASIN1");
 accessories.setAccessoryArray(accessoryArray);
 productInfoBean.setDetails(new ProductDetails[] { productDetails });
 expectedProduct.setDetails(productInfoBean.getDetails()[0]);
 FileInputStream accessoryFileIn = new FileInputStream(new File("src/test/resources/ProductFiles/Accessories_" + asin + "_ASIN1.xml"));
 ProductInfo accessoryProductInfoBean = new ProductInfo();
 ArrayList detailsArray = new ArrayList();
 MiniProduct miniProduct = new MiniProduct();
 detailsArray.add(new ProductDetails() {
 @Override
 public String getAsin() {
 return "ASIN2";
 }

 @Override
 public String getImageUrlSmall() {
 return "http://image.com/image2.jpg";
 }

 @Override
 public String getManufacturer() {
 return "Manufacturer2";
 }

 @Override
 public String getProductName() {
 return "ProductName2";
 }

 @Override
 public double getOurPrice() {
 return 14.99;
 }
 });
 accessoryProductInfoBean.setProductsArrayList(detailsArray);
 expectedProduct.setAccessories(accessories);
 expectedProduct.setSimilarItems(Arrays.asList(miniProduct));
 actualProduct = product.getProduct(asin, offer, page);
 fileIn.close();
 accessoryFileIn.close();
 assertEquals(expectedProduct, actualProduct);
 }

 @Test(expected = Exception.class)
 public void testGetProductInvalidInput() {
 expectedProduct = null;
 actualProduct = product.getProduct("InvalidASIN", "InvalidOffer", "InvalidPage");
 assertEquals(expectedProduct, actualProduct);
 }
}