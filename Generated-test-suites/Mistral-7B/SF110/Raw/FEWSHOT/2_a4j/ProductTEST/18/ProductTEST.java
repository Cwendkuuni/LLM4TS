package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.file.*;

public class ProductTest {

    @Test
    public void testGetProduct_ValidArgs_ReturnsFullProduct() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_NullFileUtil_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = null;
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_EmptyDetailsArray_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_NullAccessoryProductDetails_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        ArrayList accessoryArray = new ArrayList();

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_NullAccessories_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = null;
        ArrayList accessoryArray = new ArrayList();

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList emptyAccessoryArray = new ArrayList();

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_NullSearch_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        Search search = null;

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_EmptySimilarItemsArray_ReturnsNull() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        Search search = new Search();

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_ValidProductInfoBean_ReturnsFullProductWithDetails() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        FullProduct expectedFullProduct = new FullProduct();
        expectedFullProduct.setDetails(productDetails);

        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        productInfoBean.setAccessories(accessories);
        productInfoBean.setAccessoryArray(accessoryArray);
        productInfoBean.setProductsArrayList(detailsArray);

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_ValidAccessoryProductDetails_AddsAccessoryToFullProduct() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        FullProduct expectedFullProduct = new FullProduct();
        expectedFullProduct.setDetails(productDetails);
        expectedFullProduct.addAccessory(miniProduct);

        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        productInfoBean.setAccessories(accessories);
        productInfoBean.setAccessoryArray(accessoryArray);
        productInfoBean.setProductsArrayList(detailsArray);

        FileInputStream accessoryFileIn = fileUtil.fetchAccessories("testASIN", accessoryArray);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct accessoryMiniProduct = new MiniProduct();

        accessoryProductDetails.setAsin("testAccessoryASIN");
        accessoryProductDetails.setImageUrlSmall("testAccessoryImageURL");
        accessoryProductDetails.setManufacturer("testAccessoryManufacturer");
        accessoryProductDetails.setProductName("testAccessoryProductName");
        accessoryProductDetails.setOurPrice("testAccessoryPrice");

        accessoryProductInfoBean.setProductsArrayList(Collections.singletonList(accessoryProductDetails));

        productInfoBean.setAccessoryArray(accessoryArray);

        productDetails.setAccessories(accessories);

        MiniProduct miniProductExpected = new MiniProduct();
        miniProductExpected.setAsin("testAccessoryASIN");
        miniProductExpected.setImageURL("testAccessoryImageURL");
        miniProductExpected.setManufacturer("testAccessoryManufacturer");
        miniProductExpected.setName("testAccessoryProductName");
        miniProductExpected.setPrice("testAccessoryPrice");

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
        assertEquals(miniProductExpected, actualFullProduct.getAccessories().get(0));
    }

    @Test
    public void testGetProduct_ValidSimilarItems_AddsSimilarItemToFullProduct() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile("testASIN", "heavy", "testOffer", "testPage");
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        Search search = new Search();

        FullProduct expectedFullProduct = new FullProduct();
        expectedFullProduct.setDetails(productDetails);
        expectedFullProduct.addSimilarItem(miniProduct);

        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        productInfoBean.setAccessories(accessories);
        productInfoBean.setAccessoryArray(accessoryArray);
        productInfoBean.setProductsArrayList(detailsArray);

        ProductInfo similarProductInfoBean = search.SimilaritesSearch("testASIN", "1");

        productInfoBean.setProductsArrayList(detailsArray);
        productInfoBean.setSimilaritiesSearchResult(similarProductInfoBean);

        MiniProduct miniProductExpected = new MiniProduct();
        miniProductExpected.setAsin("testSimilarItemASIN");
        miniProductExpected.setImageURL("testSimilarItemImageURL");
        miniProductExpected.setManufacturer("testSimilarItemManufacturer");
        miniProductExpected.setName("testSimilarItemProductName");
        miniProductExpected.setPrice("testSimilarItemPrice");

        FullProduct actualFullProduct = product.getProduct("testASIN", "testOffer", "testPage");

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
        assertEquals(miniProductExpected, actualFullProduct.getSimilarItems().get(0));
    }
}