package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.file.Search;

public class ProductTest {

    private FileUtil fileUtilMock;
    private Search searchMock;

    @org.junit.Before
    public void setUp() {
        fileUtilMock = mock(FileUtil.class);
        searchMock = mock(Search.class);
    }

    @Test
    public void testGetProduct_nullInputs_returnsNull() {
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        Product product = new Product();
        FullProduct result = product.getProduct("asin1", "offer1", "page1");

        assertNull(result);
    }

    @Test
    public void testGetProduct_validInputs_populatesFullProductObject() throws IOException {
        Product product = new Product();

        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        productInfoBean.setProductsArrayList(detailsArray);

        when(searchMock.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));

        FileInputStream fileIn = mock(FileInputStream.class);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);

        doReturn(joxIn).when(fileUtilMock).newJOXBeanInputStream((InputStream) fileIn);
        doReturn(productInfoBean).when(joxIn).readObject((Class) ProductInfo.class);
        doClose(fileIn);

        FullProduct result = product.getProduct("asin1", "offer1", "page1");

        verify(fileUtilMock).fetchASINFile(eq("asin1"), eq("heavy"), eq("offer1"), eq("page1"));
        verify(searchMock).SimilaritesSearch(eq("asin1"), eq("1"));

        assertEquals("asin1", result.getDetails().getAccessories().getAccessoryArray().get(0).getAsin());
        assertEquals("imageURL1", result.getDetails().getAccessories().getAccessoryArray().get(0).getImageUrlSmall());
        assertEquals("manufacturer1", result.getDetails().getAccessories().getAccessoryArray().get(0).getManufacturer());
        assertEquals("productName1", result.getDetails().getAccessories().getAccessoryArray().get(0).getProductName());
        assertEquals(1.0f, result.getDetails().getAccessories().getAccessoryArray().get(0).getPrice(), 0.01f);

        assertEquals("asin2", result.getSimilarItems().get(0).getAsin());
        assertEquals("imageURL2", result.getSimilarItems().get(0).getImageURL());
        assertEquals("manufacturer2", result.getSimilarItems().get(0).getManufacturer());
        assertEquals("productName2", result.getSimilarItems().get(0).getName());
        assertEquals(2.0f, result.getSimilarItems().get(0).getPrice(), 0.01f);
    }

    @Test
    public void testGetProduct_noAccessories_emptyList() {
        Product product = new Product();

        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();

        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        when(searchMock.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));

        FileInputStream fileIn = mock(FileInputStream.class);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);

        doReturn(joxIn).when(fileUtilMock).newJOXBeanInputStream((InputStream) fileIn);
        doReturn(productInfoBean).when(joxIn).readObject((Class) ProductInfo.class);
        doClose(fileIn);

        FullProduct result = product.getProduct("asin1", "offer1", "page1");

        verify(fileUtilMock).fetchASINFile(eq("asin1"), eq("heavy"), eq("offer1"), eq("page1"));
        verify(searchMock).SimilaritesSearch(eq("asin1"), eq("1"));

        assertEmpty(result.getDetails().getAccessories().getAccessoryArray());
    }

}