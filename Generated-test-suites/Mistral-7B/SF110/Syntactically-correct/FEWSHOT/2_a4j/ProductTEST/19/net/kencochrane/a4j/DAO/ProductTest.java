package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import net.kencochrane.a4j.file.*;
import net.kencochrane.a4j.beans.*;
import com.wutka.jox.*;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ProductTest {
    private FileUtil fileUtil;
    private Product product;

    @Test
    public void testGetProductNullInput() {
        fileUtil = mock(FileUtil.class);
        ProductDetails productDetails = mock(ProductDetails.class);
        FullProduct fullProduct = mock(FullProduct.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        product = new Product();
        product.setFileUtil(fileUtil);
        assertNull(product.getProduct(anyString(), anyString(), anyString()));
        verify(fileUtil, times(1)).fetchASINFile(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetProductValidInput() throws Exception {
        MockitoAnnotations.initMocks(this);
        fileUtil = mock(FileUtil.class);
        ProductDetails productDetails = new ProductDetails();
        ProductDetails accessoryProductDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        FullProduct fullProduct = new FullProduct();
        ProductInfo productInfoBean = new ProductInfo();
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream(any(File.class)));
        when(fileUtil.fetchAccessories(anyString(), anyList())).thenReturn(new FileInputStream(any(File.class)));
        when(new JOXBeanInputStream((InputStream) anyObject())).thenReturn(new JOXBeanInputStream(any(InputStream.class)) {
            @Override
            public Object readObject(Class aClass) {
                if (aClass.equals(ProductInfo.class)) {
                    return productInfoBean;
                }
                if (aClass.equals(ProductDetails.class)) {
                    return productDetails;
                }
                if (aClass.equals(FullProduct.class)) {
                    return fullProduct;
                }
                if (aClass.equals(MiniProduct.class)) {
                    return miniProduct;
                }
                return null;
            }
        });
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[] {productDetails});
        when(productDetails.getAccessories()).thenReturn(accessories);
        when(accessories.getAccessoryArray()).thenReturn(accessoryArray);
        when(productInfoBean.getProductsArrayList()).thenReturn(detailsArray);
        product = new Product();
        product.setFileUtil(fileUtil);
        fullProduct = product.getProduct("testASIN", "testOffer", "testPage");
        assertEquals("testASIN", fullProduct.getDetails().getAsin());
        assertEquals("testOffer", fullProduct.getDetails().getOffer());
        assertEquals("testPage", fullProduct.getDetails().getPage());
        assertNotNull(fullProduct.getAccessories());
        assertNotNull(fullProduct.getAccessories().getAccessoryArray());
        assertEquals(1, fullProduct.getAccessories().getAccessoryArray().size());
        assertEquals("testAccessoryAsin", fullProduct.getAccessories().getAccessoryArray().get(0).getAsin());
        assertEquals("testAccessoryImageURL", fullProduct.getAccessories().getAccessoryArray().get(0).getImageURL());
        assertEquals("testAccessoryManufacturer", fullProduct.getAccessories().getAccessoryArray().get(0).getManufacturer());
        assertEquals("testAccessoryName", fullProduct.getAccessories().getAccessoryArray().get(0).getName());
        assertEquals("testAccessoryPrice", fullProduct.getAccessories().getAccessoryArray().get(0).getPrice(), 0.0);
        verify(fileUtil, times(2)).fetchASINFile(anyString(), anyString(), anyString(), anyString());
        verify(fileUtil, times(1)).fetchAccessories(anyString(), anyList());
    }
}