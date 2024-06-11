package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.beans.Search;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ProductTest {

    private FileUtil fileUtil;
    private Search search;

    @Test
    public void testGetProduct_nullInputs_returnsNull() {
        fileUtil = mock(FileUtil.class);
        Product product = new Product();
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProduct_validInputs_returnsProduct() {
        fileUtil = mock(FileUtil.class);
        search = mock(Search.class);
        Product product = new Product();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoBean = new ProductInfo();
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("testFile"));
        when(fileUtil.fetchAccessories(anyString(), anyList())).thenReturn(new FileInputStream("testFile2"));
        when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);

        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        productInfoBean.setProductsArrayList(detailsArray);
        productInfoBean.getDetails()[0].setAccessories(accessories);
        productInfoBean.getProductsArrayList().add(new ProductDetails());

        when(fileUtil.fetchASINFile(eq("asin"), eq("offer"), eq("page"), anyString())).thenReturn(new FileInputStream("testFile"));
        when(fileUtil.fetchAccessories(eq("asin"), anyList())).thenReturn(new FileInputStream("testFile2"));
        when(search.SimilaritesSearch(eq("asin"), eq("1"))).thenReturn(productInfoBean);

        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);

        ProductDetails details = fullProduct.getDetails();
        ProductDetails accessoryDetails = fullProduct.getAccessory(0);
        MiniProduct miniProduct1 = fullProduct.getAccessory(0).getMiniProduct();
        MiniProduct miniProduct2 = fullProduct.getSimilarItem(0).getMiniProduct();

        assertEquals(productDetails, details);
        assertEquals(productDetails, accessoryDetails.getDetails());
        assertEquals(miniProduct1.getAsin(), miniProduct2.getAsin());
        assertEquals(miniProduct1.getName(), miniProduct2.getName());
        assertEquals(miniProduct1.getPrice(), miniProduct2.getPrice());
    }

    @Test
    public void testGetProduct_fileReadingException_returnsNull() {
        fileUtil = mock(FileUtil.class);
        Product product = new Product();
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenThrow(new IOException());
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProduct_productInfoBeanNull_returnsNull() {
        fileUtil = mock(FileUtil.class);
        search = mock(Search.class);
        Product product = new Product();
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("testFile"));
        when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(null);
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProduct_noAccessories_returnsProduct() {
        fileUtil = mock(FileUtil.class);
        search = mock(Search.class);
        Product product = new Product();
        ProductDetails productDetails = new ProductDetails();
        ProductInfo productInfoBean = new ProductInfo();
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("testFile"));

        productDetails.setAccessories(null);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        productInfoBean.getDetails()[0].setAccessories(null);

        when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);

        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getAccessories().isEmpty());
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}