package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

    @Test
    public void testGetProduct_InvalidFile_ReturnsNull() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        assertNull(product.getProduct("invalidASIN", "offer", "page"));
    }

    @Test
    public void testGetProduct_ValidProductDetails_ReturnsFullProduct() {
        final FileUtil fileUtil = new FileUtil();
        // Provide mocked ProductInfoBean and its associated objects
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        Product product = new Product();

        productDetails.setDetails(new String[]{"testDetails"});
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add("testAccessory");
        miniProduct.setAsin("testAccessoryASIN");
        miniProduct.setImageURL("testImageURL");
        miniProduct.setManufacturer("testManufacturer");
        miniProduct.setName("testName");
        miniProduct.setPrice(10.0);

        when(fileUtil.fetchASINFile("testASIN", "heavy", "offer", "page")).thenReturn(new File("testFile.xml"));
        when(fileUtil.fetchAccessories("testASIN", accessoryArray)).thenReturn(new File("testAccessoriesFile.xml"));

        FullProduct fullProduct = product.getProduct("testASIN", "offer", "page");
        assertEquals("testDetails", fullProduct.getDetails().getDetails()[0].getDetails()[0]);
        assertEquals("testAccessory", fullProduct.getAccessories().get(0).getAsin());
        assertEquals("testImageURL", fullProduct.getAccessories().get(0).getImageURL());
        assertEquals("testManufacturer", fullProduct.getAccessories().get(0).getManufacturer());
        assertEquals("testName", fullProduct.getAccessories().get(0).getName());
        assertEquals(10.0, fullProduct.getAccessories().get(0).getPrice(), 0.0);
        verify(fileUtil, times(2)).fetchFile(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        when(fileUtil.fetchASINFile("testASIN", "heavy", "offer", "page")).thenReturn(new File("testFile.xml"));

        ProductInfo productInfoBean = null;
        FullProduct fullProduct = product.getProduct("testASIN", "offer", "page");
        assertNull(fullProduct);
        verify(fileUtil, times(1)).fetchFile(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetProduct_EmptyProductDetailsArray_ReturnsNull() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);

        when(fileUtil.fetchASINFile("testASIN", "heavy", "offer", "page")).thenReturn(new File("testFile.xml"));

        FullProduct fullProduct = product.getProduct("testASIN", "offer", "page");
        assertNull(fullProduct);
        verify(fileUtil, times(1)).fetchFile(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetProduct_NoAccessories_ReturnsFullProduct() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setDetails(new String[]{"testDetails"});
        productInfoBean.setDetails(new ProductDetails[]{productDetails});

        when(fileUtil.fetchASINFile("testASIN", "heavy", "offer", "page")).thenReturn(new File("testFile.xml"));

        FullProduct fullProduct = product.getProduct("testASIN", "offer", "page");
        assertEquals("testDetails", fullProduct.getDetails().getDetails()[0].getDetails()[0]);
        verify(fileUtil, times(1)).fetchFile(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray_ReturnsFullProduct() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});

        when(fileUtil.fetchASINFile("testASIN", "heavy", "offer", "page")).thenReturn(new File("testFile.xml"));
        when(fileUtil.fetchAccessories("testASIN", accessoryArray)).thenReturn(new File("testFile.xml"));

        FullProduct fullProduct = product.getProduct("testASIN", "offer", "page");
        verify(fileUtil, times(2)).fetchFile(anyString(), anyString(), anyString(), anyString());
        assertTrue(fullProduct.getAccessories().isEmpty());
    }

    @Test
    public void testGetProduct_ValidSimilarItems_ReturnsFullProduct() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        ArrayList detailsArray = new ArrayList();

        productDetails.setDetails(new String[]{"testDetails"});
        productInfoBean.setDetails(new ProductDetails[]{productDetails});

        accessoryProductDetails.setAsin("testSimilarItemASIN");
        accessoryProductDetails.setImageUrlSmall("testImageURL");
        accessoryProductDetails.setManufacturer("testManufacturer");
        accessoryProductDetails.setProductName("testName");
        accessoryProductDetails.setOurPrice(10.0);
        detailsArray.add(accessoryProductDetails);
        productInfoBean.setProductsArrayList(detailsArray);

        when(fileUtil.fetchASINFile("testASIN", "heavy", "offer", "page")).thenReturn(new File("testFile.xml"));

        FullProduct fullProduct = product.getProduct("testASIN", "offer", "page");
        assertEquals("testDetails", fullProduct.getDetails().getDetails()[0].getDetails()[0]);
        assertEquals(1, fullProduct.getSimilarItems().size());
        assertEquals("testSimilarItemASIN", fullProduct.getSimilarItems().get(0).getAsin());
        assertEquals("testImageURL", fullProduct.getSimilarItems().get(0).getImageURL());
        assertEquals("testManufacturer", fullProduct.getSimilarItems().get(0).getManufacturer());
        assertEquals("testName", fullProduct.getSimilarItems().get(0).getName());
        assertEquals(10.0, fullProduct.getSimilarItems().get(0).getPrice(), 0.0);
        verify(fileUtil, times(1)).fetchFile(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetProduct_EmptySimilarItemsArrayList_ReturnsFullProduct() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setDetails(new String[]{"testDetails"});
        productInfoBean.setDetails(new ProductDetails[]{productDetails});

        when(fileUtil.fetchASINFile("testASIN", "heavy", "offer", "page")).thenReturn(new File("testFile.xml"));

        FullProduct fullProduct = product.getProduct("testASIN", "offer", "page");
        assertEquals("testDetails", fullProduct.getDetails().getDetails()[0].getDetails()[0]);
        assertTrue(fullProduct.getSimilarItems().isEmpty());
        verify(fileUtil, times(1)).fetchFile(anyString(), anyString(), anyString(), anyString());
    }
}