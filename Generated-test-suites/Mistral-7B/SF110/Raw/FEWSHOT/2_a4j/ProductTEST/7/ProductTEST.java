package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import net.kencochrane.a4j.file.*;
import net.kencochrane.a4j.beans.*;
import com.wutka.jox.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ProductTest {

 private FileUtil fileUtil;
 private Product product;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 fileUtil = mock(FileUtil.class);
 product = new Product();
 }

 @Test
 public void testGetProduct_nullInputs_returnsNull() {
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
 assertNull(product.getProduct("asin", "offer", "page"));
 }

 @Test
 public void testGetProduct_noDetails_returnsNull() {
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(mock(FileInputStream.class));
 when(new JOXBeanInputStream((InputStream)anyObject())).thenThrow(new IOException());
 assertNull(product.getProduct("asin", "offer", "page"));
 }

 @Test
 public void testGetProduct_validInputs_returnsFullProduct() {
 when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(mock(FileInputStream.class));
 when(new JOXBeanInputStream((InputStream)anyObject())).thenReturn(mock(ProductInfo.class));
 FullProduct fullProduct = new FullProduct();
 ProductDetails productDetails = new ProductDetails();
 ProductDetails accessoryProductDetails = new ProductDetails();
 Accessories accessories = new Accessories();
 ArrayList accessoryArray = new ArrayList();
 ArrayList detailsArray = new ArrayList();
 MiniProduct miniProduct = new MiniProduct();
 when(productDetails.getAccessories()).thenReturn(accessories);
 when(accessories.getAccessoryArray()).thenReturn(accessoryArray);
 when(accessories.getAccessoryArray().size()).thenReturn(1);
 when(new FileUtil()).thenReturn(fileUtil);
 when(fileUtil.fetchAccessories(anyString(), anyList())).thenReturn(mock(FileInputStream.class));
 when(new JOXBeanInputStream((InputStream)anyObject())).thenReturn(mock(ProductInfo.class));
 when(productInfoBean.getProductsArrayList().size()).thenReturn(1);
 detailsArray = productInfoBean.getProductsArrayList();
 when(detailsArray.get(0)).thenReturn(accessoryProductDetails);
 when(accessoryProductDetails.getAsin()).thenReturn("accessoryASIN");
 when(accessoryProductDetails.getImageUrlSmall()).thenReturn("accessoryImageURL");
 when(accessoryProductDetails.getManufacturer()).thenReturn("accessoryManufacturer");
 when(accessoryProductDetails.getProductName()).thenReturn("accessoryProductName");
 when(accessoryProductDetails.getOurPrice()).thenReturn(new Double(12.99));
 miniProduct = new MiniProduct();
 when(miniProduct.setAsin(anyString())).thenReturn(miniProduct);
 when(miniProduct.setImageURL(anyString())).thenReturn(miniProduct);
 when(miniProduct.setManufacturer(anyString())).thenReturn(miniProduct);
 when(miniProduct.setName(anyString())).thenReturn(miniProduct);
 when(miniProduct.setPrice(anyDouble())).thenReturn(miniProduct);
 when(fullProduct.addAccessory(any(MiniProduct.class))).thenReturn(fullProduct);
 when(fullProduct.getAccessories().getAccessoryArray()).thenReturn(accessoryArray);
 ProductInfo productInfoBean = mock(ProductInfo.class);
 when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);
 when(productInfoBean.getProductsArrayList()).thenReturn(detailsArray);
 when(productInfoBean.getProductsArrayList().size()).thenReturn(1);
 when(detailsArray.get(0)).thenReturn(accessoryProductDetails);
 FullProduct result = product.getProduct("asin", "offer", "page");
 assertEquals(fullProduct, result);
 }

}