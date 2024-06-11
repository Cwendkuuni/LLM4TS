package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.file.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.Search;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductTest {

    @Mock
    private FileUtil fileUtil;

    @Mock
    private Search search;

    @InjectMocks
    private Product productUnderTest;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private FullProduct expectedFullProduct;
    private String asin = "ASIN123";
    private String offer = "offer1";
    private String page = "page1";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        expectedFullProduct = new FullProduct();
        expectedFullProduct.setDetails(new ProductDetails());

        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();

        accessoryProductDetails.setAsin("ASIN456");
        accessoryProductDetails.setImageUrlSmall("http://example.com/image1.jpg");
        accessoryProductDetails.setManufacturer("Manufacturer1");
        accessoryProductDetails.setProductName("Product1");
        accessoryProductDetails.setOurPrice(10.0);

        accessories.setAccessoryArray(accessoryArray);
        productDetails.setAccessories(accessories);

        miniProduct.setAsin("ASIN789");
        miniProduct.setImageURL("http://example.com/image2.jpg");
        miniProduct.setManufacturer("Manufacturer2");
        miniProduct.setName("Product2");
        miniProduct.setPrice(15.0);

        accessoryArray.add(accessoryProductDetails);
        productDetails.setDetails(miniProduct);

        expectedFullProduct.addAccessory(miniProduct);
    }

    @Test
    public void testGetProduct_ValidASINOfferPage_ReturnsExpectedProduct() throws IOException {
        File file = new File("testFile.xml");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new FileInputStream(file));

        ProductInfo productInfoBean = new ProductInfo();

        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[]{productDetails});

        when(search.SimilaritesSearch(asin, "1")).thenReturn(new ProductInfo());

        FullProduct actualFullProduct = productUnderTest.getProduct(asin, offer, page);

        assertEquals(expectedFullProduct, actualFullProduct);

        verify(fileUtil, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtil, times(1)).fetchAccessories(asin, accessoryArray);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProduct_NullASIN_ThrowsException() {
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(null);

        productUnderTest.getProduct(asin, offer, page);
    }

    @Test
    public void testGetProduct_InvalidASINOfferPage_ReturnsNull() throws IOException {
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(new FileInputStream(new File("")));

        FullProduct actualFullProduct = productUnderTest.getProduct(asin, offer, page);

        assertNull(actualFullProduct);

        verify(fileUtil, times(1)).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtil, never()).fetchAccessories(asin, accessoryArray);
    }
}