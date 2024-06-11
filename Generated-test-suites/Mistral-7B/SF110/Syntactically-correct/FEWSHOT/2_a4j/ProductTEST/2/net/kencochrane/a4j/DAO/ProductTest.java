package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import net.kencochrane.a4j.file.*;
import net.kencochrane.a4j.beans.*;
import org.junit.Test;
import com.wutka.jox.*;

public class ProductTest {

 FullProduct expectedProduct = new FullProduct();
 FileUtil fileUtilMock = mock(FileUtil.class);
 Search searchMock = mock(Search.class);

 @Test
 public void testGetProduct_nullAsin_returnsNull() {
 given(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).willReturn(null);

 Product product = new Product();
 FullProduct actualProduct = product.getProduct("asin123", "offer", "page");
 assertNull(actualProduct);
 }

 @Test
 public void testGetProduct_validAsin_populatesFullProduct() throws Exception {
 given(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString()))
 .willReturn(new FileInputStream(new File("validAsinFile.xml")));
 given(searchMock.SimilaritesSearch(anyString(), anyString()))
 .willReturn(new ProductInfo(new ProductDetails[]{new ProductDetails(), new ProductDetails()}));

 Product product = new Product();
 product.setFileUtil(fileUtilMock);
 product.setSearch(searchMock);
 FullProduct actualProduct = product.getProduct("validAsin", "offer", "page");

 assertEquals(expectedProduct, actualProduct);

 verify(fileUtilMock).fetchASINFile(eq("validAsin"), eq("heavy"), eq("offer"), eq("page"));
 verify(searchMock).SimilaritesSearch(eq("validAsin"), anyString());
 }

 @Test
 public void testGetProduct_emptyAccessoryDetails_returnsEmptyAccessoryList() throws Exception {
 given(fileUtilMock.fetchAccessories(anyString(), anyList()))
 .willReturn(null);

 Product product = new Product();
 FullProduct actualProduct = product.getProduct("asin123", "offer", "page");
 assertNull(actualProduct.getAccessoryList());
 }

 @Test
 public void testGetProduct_validAccessoryDetails_populatesAccessoryList() throws Exception {
 given(fileUtilMock.fetchAccessories(anyString(), anyList()))
 .willReturn(new FileInputStream(new File("validAccessoryDetailsFile.xml")));
 given(searchMock.SimilaritesSearch(anyString(), anyString()))
 .willReturn(new ProductInfo(new ProductDetails[]{new ProductDetails(), new ProductDetails()}, new ArrayList()));

 Product product = new Product();
 product.setFileUtil(fileUtilMock);
 product.setSearch(searchMock);
 FullProduct expectedProduct = new FullProduct();
 expectedProduct.setAccessoryList(new ArrayList());
 FullProduct actualProduct = product.getProduct("asin123", "offer", "page");

 assertEquals(expectedProduct, actualProduct);

 verify(fileUtilMock, times(2)).fetchASINFile(anyString(), anyString(), anyString(), anyString());
 verify(searchMock).SimilaritesSearch(eq("asin123"), anyString());

 ArrayList accessoryArray = (ArrayList) ((ProductInfo)anyObject()).getProductsArrayList();
 MiniProduct miniProduct = new MiniProduct();
 miniProduct.setAsin("accessoryAsin1");
 miniProduct.setImageURL("accessoryImageURL1");
 miniProduct.setManufacturer("accessoryManufacturer1");
 miniProduct.setName("accessoryName1");
 miniProduct.setPrice(12.3);
 when(accessoryArray.get(0)).thenReturn(new ProductDetails(miniProduct));

 ProductDetails productDetails = actualProduct.getDetails();
 assertEquals(productDetails.getAccessories().getAccessoryArray(), accessoryArray);
 }

}