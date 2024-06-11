package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;
import java.io.*;

public class ProductTest {

    @Test
    public void testGetProduct_ValidArguments_ReturnsFullProduct() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_NullASIN_ReturnsNull() {
        Product product = new Product();
        String asin = null;
        String offer = "offer1";
        String page = "page1";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_EmptyDetailsArray_ReturnsNull() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productInfoBean.setDetails(new ProductDetails[0]);
        FileUtil fileUtil = new FileUtil() {
            @Override
            FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                if (asin.equals("ASIN123")) {
                    return new FileInputStream(new File("testFile.xml"));;
                }
                return null;
            }
        };

        when(fileUtil.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(new FileInputStream(new File("testFile.xml")));

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_NonEmptyDetailsArray_ReturnsFullProductWithAccessories() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        expectedFullProduct.setDetails(productDetails);
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);

        ProductInfo productInfoBean = new ProductInfo();
        productDetails = new ProductDetails();
        Accessories accessories1 = new Accessories();
        ArrayList accessoryArray1 = new ArrayList();
        MiniProduct miniProduct1 = new MiniProduct();

        productDetails.setAccessories(accessories1);
        accessories1.setAccessoryArray(accessoryArray1);
        accessoryArray1.add("accessory1");

        ProductInfo productInfoBean1 = new ProductInfo();
        ProductDetails productDetails1 = new ProductDetails();
        Accessories accessories2 = new Accessories();
        ArrayList accessoryArray2 = new ArrayList();
        MiniProduct miniProduct2 = new MiniProduct();

        productDetails1 = new ProductDetails();
        accessories2.setAccessoryArray(accessoryArray2);
        accessoryArray2.add("accessory2");

        productInfoBean1.setProductsArrayList(Arrays.asList(productDetails1));

        FileUtil fileUtil = new FileUtil() {
            @Override
            FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                if (asin.equals("ASIN123")) {
                    return new FileInputStream(new File("testFile1.xml"));;
                }
                return null;
            }

            @Override
            FileInputStream fetchAccessories(String asin, ArrayList accessoryArray) {
                if (asin.equals("ASIN123")) {
                    return new FileInputStream(new File("testFile2.xml"));;
                }
                return null;
            }
        };

        when(fileUtil.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(new FileInputStream(new File("testFile1.xml")));
        when(fileUtil.fetchAccessories(eq("ASIN123"), anyList())).thenReturn(new FileInputStream(new File("testFile2.xml")));

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertEquals(expectedFullProduct, actualFullProduct);

        // Check accessoryArray size and contents
        assertEquals(2, actualFullProduct.getAccessories().getAccessoryArray().size());
        assertEquals("accessory1", actualFullProduct.getAccessories().getAccessoryArray().get(0));
        assertEquals("accessory2", actualFullProduct.getAccessories().getAccessoryArray().get(1));
    }

    @Test
    public void testGetProduct_NullAccessoryArray_ReturnsFullProductWithoutAccessories() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        ProductInfo productInfoBean = new ProductInfo();

        expectedFullProduct.setDetails(productDetails);

        productDetails.setAccessories(null);
        productInfoBean.setDetails(new ProductDetails[0]);

        FileUtil fileUtil = new FileUtil() {
            @Override
            FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                return new FileInputStream(new File("testFile.xml"));
            }
        };

        when(fileUtil.fetchASINFile(eq(asin), eq("heavy"), anyString(), anyString())).thenReturn(new FileInputStream(new File("testFile.xml")));

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertEquals(expectedFullProduct, actualFullProduct);
        assertNull(actualFullProduct.getAccessories());
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        Product product = new Product();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        FileUtil fileUtil = new FileUtil() {
            @Override
            FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                return null;
            }
        };

        when(fileUtil.fetchASINFile(eq(asin), eq("heavy"), eq(offer), eq(page))).thenReturn(null);

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_EmptyProductsArrayList_ReturnsEmptyFullProduct() {
        Product product = new Product();
        ProductInfo productInfoBean = new ProductInfo();

        FileUtil fileUtil = new FileUtil() {
            @Override
            FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                return new FileInputStream(new File("testFile.xml"));
            }
        };

        when(fileUtil.fetchASINFile(eq(asin), eq("heavy"), anyString(), anyString())).thenReturn(new FileInputStream(new File("testFile.xml")));

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(0, actualFullProduct.getDetails().size());
        assertNull(actualFullProduct.getAccessories());
        assertNull(actualFullProduct.getSimilarItems());
    }

    @Test
    public void testGetProduct_NonEmptyProductsArrayList_ReturnsFullProductWithSimilarItems() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoBean = new ProductInfo();

        expectedFullProduct.setDetails(productDetails);
        productDetails.setAccessories(null);

        ProductDetails productDetails1 = new ProductDetails();
        miniProduct = new MiniProduct();
        miniProduct.setAsin("ASIN135");
        miniProduct.setImageURL("imageURL1");
        miniProduct.setManufacturer("manufacturer1");
        miniProduct.setName("name1");
        miniProduct.setPrice(5.5);

        productDetails1.setProductName(miniProduct.getName());
        productDetails1.setImageUrlSmall(miniProduct.getImageURL());
        productDetails1.setOurPrice(miniProduct.getPrice());

        ArrayList detailsArray = new ArrayList();
        detailsArray.add(productDetails1);
        productInfoBean.setProductsArrayList(detailsArray);

        FileUtil fileUtil = new FileUtil() {
            @Override
            FileInputStream fetchASINFile(String asin, String fileType, String offer, String page) {
                return new FileInputStream(new File("testFile.xml"));
            }
        };

        when(fileUtil.fetchASINFile(eq(asin), eq("heavy"), anyString(), anyString())).thenReturn(new FileInputStream(new File("testFile.xml")));

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertEquals(expectedFullProduct, actualFullProduct);

        // Check similarItems size and contents
        assertEquals(1, actualFullProduct.getSimilarItems().size());
        assertEquals(miniProduct.getAsin(), actualFullProduct.getSimilarItems().get(0).getAsin());
        assertEquals(miniProduct.getImageURL(), actualFullProduct.getSimilarItems().get(0).getImageURL());
        assertEquals(miniProduct.getManufacturer(), actualFullProduct.getSimilarItems().get(0).getManufacturer());
        assertEquals(miniProduct.getName(), actualFullProduct.getSimilarItems().get(0).getName());
        assertEquals(miniProduct.getPrice(), actualFullProduct.getSimilarItems().get(0).getPrice());
    }
}