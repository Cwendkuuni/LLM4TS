package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.file.*;
import net.kencochrane.a4j.DAO.Product;
import net.wutka.jox.*;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private FileUtil fileUtilMock;
    private Product product;

    @Before
    public void setup() {
        fileUtilMock = mock(FileUtil.class);
        product = new Product();
    }

    @Test
    public void testGetProduct_NullAsin_ReturnsNull() {
        when(fileUtilMock.fetchASINFile((String) anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        assertNull(product.getProduct("", "", ""));
        verify(fileUtilMock, times(1)).fetchASINFile((String) anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetProduct_ValidAsin_ReturnsFullProduct() throws IOException {
        String asin = "ASIN-1";
        String offer = "Offer1";
        String page = "Page1";

        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        when(fileIn).thenReturn(joxIn);

        ProductInfo productInfoBean = new ProductInfo();
        when(joxIn.readObject((Class) ProductInfo.class)).thenReturn(productInfoBean);

        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        product.setDetails(productDetails);

        joxIn.close();
        fileIn.close();

        assertEquals(productDetails, product.getDetails());
        verify(fileUtilMock, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtilMock, never()).fetchAccessories(asin, anyList());
    }

    @Test
    public void testGetProduct_ValidAsinWithAccessories_ReturnsFullProductWithAccessories() throws IOException {
        String asin = "ASIN-1";
        String offer = "Offer1";
        String page = "Page1";

        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        when(fileIn).thenReturn(joxIn);

        ProductInfo productInfoBean = new ProductInfo();
        when(joxIn.readObject((Class) ProductInfo.class)).thenReturn(productInfoBean);

        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = new ArrayList();
        accessoryArray.add("Accessory1");
        accessories.setAccessoryArray(accessoryArray);

        product.setDetails(productDetails);

        FileInputStream fileIn2 = mock(FileInputStream.class);
        when(fileUtilMock.fetchAccessories(asin, accessoryArray)).thenReturn(fileIn2);

        JOXBeanInputStream joxIn2 = mock(JOXBeanInputStream.class);
        when(fileIn2).thenReturn(joxIn2);

        ProductInfo productInfoBean2 = new ProductInfo();
        when(joxIn2.readObject((Class) ProductInfo.class)).thenReturn(productInfoBean2);

        ArrayList detailsArray = productInfoBean2.getProductsArrayList();
        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("Accessory1-ASIN");
        miniProduct.setImageURL("http://image.com/Accessory1.jpg");
        miniProduct.setManufacturer("Manufacturer1");
        miniProduct.setName("Accessory1-Name");
        miniProduct.setPrice(12.5);
        detailsArray.add(miniProduct);

        productInfoBean2.setProductsArrayList(detailsArray);

        joxIn2.close();
        fileIn2.close();

        assertEquals(accessoryArray.size(), product.getAccessories().size());
        assertTrue(product.getAccessories().containsAll(Arrays.asList("Accessory1")));
        verify(fileUtilMock, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtilMock, times(1)).fetchAccessories(asin, accessoryArray);
    }

    @Test
    public void testGetProduct_ValidAsinWithNoAccessories_ReturnsFullProductWithoutAccessories() throws IOException {
        String asin = "ASIN-1";
        String offer = "Offer1";
        String page = "Page1";

        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        when(fileIn).thenReturn(joxIn);

        ProductInfo productInfoBean = new ProductInfo();
        when(joxIn.readObject((Class) ProductInfo.class)).thenReturn(productInfoBean);

        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        product.setDetails(productDetails);

        joxIn.close();
        fileIn.close();

        assertTrue(product.getAccessories().isEmpty());
        verify(fileUtilMock, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtilMock, never()).fetchAccessories(asin, anyList());
    }

    @Test
    public void testGetProduct_ValidAsinWithSimilarItems_ReturnsFullProductWithSimilarItems() throws IOException {
        String asin = "ASIN-1";
        String offer = "Offer1";
        String page = "Page1";

        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        when(fileIn).thenReturn(joxIn);

        ProductInfo productInfoBean = new ProductInfo();
        when(joxIn.readObject((Class) ProductInfo.class)).thenReturn(productInfoBean);

        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        product.setDetails(productDetails);

        Search searchMock = mock(Search.class);
        when(product.getSearch()).thenReturn(searchMock);

        ProductInfo productInfoBean2 = new ProductInfo();
        when(searchMock.SimilaritesSearch(asin, "1")).thenReturn(productInfoBean2);

        ArrayList detailsArray = productInfoBean2.getProductsArrayList();
        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("SimilarItem1-ASIN");
        miniProduct.setImageURL("http://image.com/SimilarItem1.jpg");
        miniProduct.setManufacturer("Manufacturer1");
        miniProduct.setName("SimilarItem1-Name");
        miniProduct.setPrice(12.5);
        detailsArray.add(miniProduct);

        productInfoBean2.setProductsArrayList(detailsArray);

        joxIn.close();
        fileIn.close();

        assertEquals(1, product.getSimilarItems().size());
        assertTrue(product.getSimilarItems().contains(miniProduct));
        verify(fileUtilMock, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(searchMock, times(1)).SimilaritesSearch(asin, "1");
    }

    @Test
    public void testGetProduct_ValidAsinWithNoSimilarItems_ReturnsFullProductWithoutSimilarItems() throws IOException {
        String asin = "ASIN-1";
        String offer = "Offer1";
        String page = "Page1";

        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        when(fileIn).thenReturn(joxIn);

        ProductInfo productInfoBean = new ProductInfo();
        when(joxIn.readObject((Class) ProductInfo.class)).thenReturn(productInfoBean);

        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        product.setDetails(productDetails);

        Search searchMock = mock(Search.class);
        when(product.getSearch()).thenReturn(searchMock);

        when(searchMock.SimilaritesSearch(asin, "1")).thenReturn(null);

        joxIn.close();
        fileIn.close();

        assertTrue(product.getSimilarItems().isEmpty());
        verify(fileUtilMock, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(searchMock, times(1)).SimilaritesSearch(asin, "1");
    }
}