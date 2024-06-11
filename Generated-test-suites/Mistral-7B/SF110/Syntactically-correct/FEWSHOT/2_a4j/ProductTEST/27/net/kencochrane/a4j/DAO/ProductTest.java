package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

    @Test
    public void testGetProduct_nullParams() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_validParams_noAccessories() {
        Product product = new Product();
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";
        FullProduct expectedProduct = new FullProduct();
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testGetProduct_validParams_accessories() {
        Product product = new Product();
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";
        FullProduct expectedProduct = new FullProduct();
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        ProductDetails productDetails = new ProductDetails();
        ProductDetails accessoryProductDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        // Set up productDetails
        productDetails.setAccessories(accessories);
        productDetails.setAsin(asin);
        productDetails.setOurPrice(10.0);
        productDetails.setProductName("Test Product");

        accessoryProductDetails = new ProductDetails();
        accessoryProductDetails.setAsin("testAccessoryASIN");
        accessoryProductDetails.setImageUrlSmall("testImageURL");
        accessoryProductDetails.setManufacturer("Test Manufacturer");
        accessoryProductDetails.setProductName("Test Accessory");
        accessoryProductDetails.setOurPrice(5.0);

        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(accessoryProductDetails);
        productDetails.setAccessories(accessories);

        expectedProduct.setDetails(productDetails);

        // Set up fileUtil behavior
        FileUtil fileUtil = new FileUtil() {
            @Override
            public FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                if (asin.equals(expectedProduct.getDetails().get(0).getAsin())) {
                    return new FileInputStream(new File("testFile.xml"));
                }
                return null;
            }

            @Override
            public FileInputStream fetchAccessories(String asin, ArrayList accessoryArray) {
                if (asin.equals(expectedProduct.getDetails().get(0).getAsin())) {
                    return new FileInputStream(new File("testAccessoryFile.xml"));
                }
                return null;
            }
        };

        // Set up Search behavior
        Search search = new Search() {
            @Override
            public ProductInfo SimilaritesSearch(String asin, String numResults) {
                if (asin.equals(expectedProduct.getDetails().get(0).getAsin())) {
                    ProductInfo productInfoBean = new ProductInfo();
                    ArrayList detailsArray = new ArrayList();
                    ProductDetails productDetails = new ProductDetails();
                    MiniProduct miniProduct = new MiniProduct();

                    productDetails.setAsin("testSimilarASIN");
                    productDetails.setImageUrlSmall("testImageURL");
                    productDetails.setManufacturer("Test Manufacturer");
                    productDetails.setProductName("Test Similar Product");
                    productDetails.setOurPrice(3.0);

                    miniProduct.setAsin(productDetails.getAsin());
                    miniProduct.setImageURL(productDetails.getImageUrlSmall());
                    miniProduct.setManufacturer(productDetails.getManufacturer());
                    miniProduct.setName(productDetails.getProductName());
                    miniProduct.setPrice(productDetails.getOurPrice());

                    detailsArray.add(productDetails);
                    productInfoBean.setProductsArrayList(detailsArray);

                    return productInfoBean;
                }
                return null;
            }
        };

        product.setFileUtil(fileUtil);
        product.setSearch(search);

        // Test
        actualProduct = product.getProduct(asin, offer, page);
        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
        assertEquals(1, actualProduct.getAccessories().size());
        assertEquals(1, actualProduct.getSimilarItems().size());
    }

    @Test
    public void testGetProduct_validParams_noSimilarItems() {
        Product product = new Product();
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";
        FullProduct expectedProduct = new FullProduct();
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        ProductDetails productDetails = new ProductDetails();
        ProductDetails accessoryProductDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        // Set up productDetails
        productDetails.setAccessories(accessories);
        productDetails.setAsin(asin);
        productDetails.setOurPrice(10.0);
        productDetails.setProductName("Test Product");

        accessoryProductDetails = new ProductDetails();
        accessoryProductDetails.setAsin("testAccessoryASIN");
        accessoryProductDetails.setImageUrlSmall("testImageURL");
        accessoryProductDetails.setManufacturer("Test Manufacturer");
        accessoryProductDetails.setProductName("Test Accessory");
        accessoryProductDetails.setOurPrice(5.0);

        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(accessoryProductDetails);
        productDetails.setAccessories(accessories);

        expectedProduct.setDetails(productDetails);

        // Set up fileUtil behavior
        FileUtil fileUtil = new FileUtil() {
            @Override
            public FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                if (asin.equals(expectedProduct.getDetails().get(0).getAsin())) {
                    return new FileInputStream(new File("testFile.xml"));
                }
                return null;
            }

            @Override
            public FileInputStream fetchAccessories(String asin, ArrayList accessoryArray) {
                if (asin.equals(expectedProduct.getDetails().get(0).getAsin())) {
                    return new FileInputStream(new File("testAccessoryFile.xml"));
                }
                return null;
            }
        };

        product.setFileUtil(fileUtil);

        // Test
        actualProduct = product.getProduct(asin, offer, page);
        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
        assertEquals(1, actualProduct.getAccessories().size());
        assertEquals(0, actualProduct.getSimilarItems().size());
    }
}