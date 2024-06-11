package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;

import org.jox.JoxBeanContext;
import org.jox.JoxBeanException;
import org.jox.JoxBeanInputStream;
import org.jox.JoxObject;
import org.jox.JoxProperty;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private Product productUnderTest;
    private FileUtil fileUtilMock;
    private JoxBeanContext joxContext;

    @Before
    public void setup() {
        productUnderTest = new Product();
        fileUtilMock = mock(FileUtil.class);
        joxContext = new JoxBeanContext();
        joxContext.setProperty("net.kencochrane.a4j.DAO.FileUtil", fileUtilMock);
        productUnderTest.setJoxContext(joxContext);
    }

    @Test
    public void testNullAsin() {
        // given
        String asin = null;
        String offer = "testOffer";
        String page = "testPage";

        // when
        Product result = productUnderTest.getProduct(asin, offer, page);

        // then
        assertNull(result);
    }

    @Test
    public void testGetProductWithValidArguments() throws Exception {
        // given
        String asin = "B000123456";
        String offer = "testOffer";
        String page = "testPage";

        ProductInfo productInfoBean = createMockProductInfoBean();
        when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page))
                .thenReturn(createMockInputStream(productInfoBean.getBytes()));

        // when
        Product result = productUnderTest.getProduct(asin, offer, page);

        // then
        assertNotNull(result);
        assertEquals(productInfoBean.getDetails()[0], result.getDetails());

        Accessories accessories = productInfoBean.getDetails()[0].getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();
        assertEquals(2, accessoryArray.size());

        MiniProduct firstAccessory = (MiniProduct) accessoryArray.get(0);
        assertEquals("ASIN1", firstAccessory.getAsin());
        assertEquals("ImageURL1", firstAccessory.getImageURL());
        assertEquals("Manufacturer1", firstAccessory.getManufacturer());
        assertEquals("ProductName1", firstAccessory.getName());
        assertEquals(19.99, firstAccessory.getPrice(), 0.01);

        MiniProduct secondAccessory = (MiniProduct) accessoryArray.get(1);
        assertEquals("ASIN2", secondAccessory.getAsin());
        assertEquals("ImageURL2", secondAccessory.getImageURL());
        assertEquals("Manufacturer2", secondAccessory.getManufacturer());
        assertEquals("ProductName2", secondAccessory.getName());
        assertEquals(29.99, secondAccessory.getPrice(), 0.01);

        ProductInfo similarProductInfoBean = createMockSimilarProductInfoBean();
        when(fileUtilMock.fetchAccessories(asin, accessoryArray))
                .thenReturn(createMockInputStream(similarProductInfoBean.getBytes()));

        List<MiniProduct> similarItems = result.getSimilarItems();
        assertEquals(2, similarItems.size());

        MiniProduct firstSimilarItem = similarItems.get(0);
        assertEquals("ASIN3", firstSimilarItem.getAsin());
        assertEquals("ImageURL3", firstSimilarItem.getImageURL());
        assertEquals("Manufacturer3", firstSimilarItem.getManufacturer());
        assertEquals("ProductName3", firstSimilarItem.getName());
        assertEquals(14.99, firstSimilarItem.getPrice(), 0.01);

        MiniProduct secondSimilarItem = similarItems.get(1);
        assertEquals("ASIN4", secondSimilarItem.getAsin());
        assertEquals("ImageURL4", secondSimilarItem.getImageURL());
        assertEquals("Manufacturer4", secondSimilarItem.getManufacturer());
        assertEquals("ProductName4", secondSimilarItem.getName());
        assertEquals(17.99, secondSimilarItem.getPrice(), 0.01);
    }

    private ProductInfo createMockProductInfoBean() throws IOException, JoxBeanException {
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productDetails.getAccessories().setAccessoryArray(Arrays.asList(new ProductDetails() {
            {
                setAsin("ASIN1");
                setImageUrlSmall("ImageURL1");
                setManufacturer("Manufacturer1");
                setProductName("ProductName1");
                setOurPrice(19.99);
            }
        }, new ProductDetails() {
            {
                setAsin("ASIN2");
                setImageUrlSmall("ImageURL2");
                setManufacturer("Manufacturer2");
                setProductName("ProductName2");
                setOurPrice(29.99);
            }
        }));

        JoxObject joxObject = new JoxObject();
        joxObject.setName("ProductInfo");
        joxObject.addProperty(new JoxProperty("details", productDetails));

        return (ProductInfo) joxContext.instantiate(joxObject);
    }

    private ProductInfo createMockSimilarProductInfoBean() throws IOException, JoxBeanException {
        JoxObject joxObject = new JoxObject();
        joxObject.setName("ProductInfo");
        joxObject.addProperty(new JoxProperty("productArrayList", Arrays.asList(new ProductDetails() {
            {
                setAsin("ASIN3");
                setImageUrlSmall("ImageURL3");
                setManufacturer("Manufacturer3");
                setProductName("ProductName3");
                setOurPrice(14.99);
            }
        }, new ProductDetails() {
            {
                setAsin("ASIN4");
                setImageUrlSmall("ImageURL4");
                setManufacturer("Manufacturer4");
                setProductName("ProductName4");
                setOurPrice(17.99);
            }
        })));

        return (ProductInfo) joxContext.instantiate(joxObject);
    }

    private InputStream createMockInputStream(byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        when(fileUtilMock.fetchASINFile(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(inputStream);
        return inputStream;
    }
}