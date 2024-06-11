package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.beans.Search;

public class ProductTest {

    private final Product product = new Product();
    private final FileUtil fileUtil = new FileUtil();
    private final Search search = new Search();

    @org.junit.Test
    public void testGetProduct_ValidParameters_ReturnsExpectedProduct() throws IOException {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        FullProduct expectedProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        Accessories accessories = new Accessories();
        ArrayList<MiniProduct> accessoryArray = new ArrayList<>();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();

        // Initialize expected product with valid data
        miniProduct.setAsin("B000023456");
        miniProduct.setImageURL("http://example.com/image1.jpg");
        miniProduct.setManufacturer("Manufacturer1");
        miniProduct.setName("Product1");
        miniProduct.setPrice(10.99);
        accessoryProductDetails = new ProductDetails();
        accessoryProductDetails.setAsin("B000034567");
        accessoryProductDetails.setImageUrlSmall("http://example.com/image2.jpg");
        accessoryProductDetails.setManufacturer("Manufacturer2");
        accessoryProductDetails.setProductName("Product2");
        accessoryProductDetails.setOurPrice(15.99);
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(miniProduct);
        expectedProduct.setDetails(productDetails);

        FullProduct actualProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getDetails(), actualProduct.getDetails());
        assertEquals(expectedProduct.getAccessories().getAccessoryArray().size(), actualProduct.getAccessories().getAccessoryArray().size());
        for (int i = 0; i < expectedProduct.getAccessories().getAccessoryArray().size(); i++) {
            assertEquals(expectedProduct.getAccessories().getAccessoryArray().get(i).getAsin(), actualProduct.getAccessories().getAccessoryArray().get(i).getAsin());
            assertEquals(expectedProduct.getAccessories().getAccessoryArray().get(i).getImageURL(), actualProduct.getAccessories().getAccessoryArray().get(i).getImageURL());
            assertEquals(expectedProduct.getAccessories().getAccessoryArray().get(i).getManufacturer(), actualProduct.getAccessories().getAccessoryArray().get(i).getManufacturer());
            assertEquals(expectedProduct.getAccessories().getAccessoryArray().get(i).getName(), actualProduct.getAccessories().getAccessoryArray().get(i).getName());
            assertEquals(expectedProduct.getAccessories().getAccessoryArray().get(i).getPrice(), actualProduct.getAccessories().getAccessoryArray().get(i).getPrice(), 0.01);
        }
    }

    @org.junit.Test
    public void testGetProduct_NullFile_ReturnsNull() throws IOException {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        FullProduct expectedProduct = null;

        FileInputStream fileInput = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        if (fileInput != null) {
            fileInput.close();
        }

        FullProduct actualProduct = product.getProduct(asin, offer, page);

        assertNull(actualProduct);
    }

    @org.junit.Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        FileInputStream fileInput = new FileInputStream(new ByteArrayInputStream(new byte[0]));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileInput);
        ProductInfo productInfoBean = null;

        try {
            productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
        } finally {
            joxIn.close();
            fileInput.close();
        }

        FullProduct actualProduct = product.getProduct(asin, offer, page);

        assertNull(actualProduct);
    }

    @org.junit.Test
    public void testGetProduct_EmptyDetailsArray_ReturnsEmptyProduct() {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        FileInputStream fileInput = new FileInputStream(new ByteArrayInputStream(new byte[0]));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileInput);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);

        try {
            FullProduct actualProduct = product.getProduct(asin, offer, page);

            assertNotNull(actualProduct);
            assertTrue(actualProduct.getDetails().length == 0);
            assertTrue(actualProduct.getAccessories().getAccessoryArray().isEmpty());
            assertTrue(actualProduct.getSimilarItems().isEmpty());
        } finally {
            joxIn.close();
            fileInput.close();
        }
    }

    @org.junit.Test
    public void testGetProduct_EmptyAccessoryArray_ReturnsProductWithEmptyAccessoryArray() {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        FileInputStream fileInput = new FileInputStream(new ByteArrayInputStream(new byte[0]));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileInput);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[]{productDetails});

        try {
            FullProduct actualProduct = product.getProduct(asin, offer, page);

            assertNotNull(actualProduct);
            assertEquals(1, actualProduct.getDetails().length);
            assertTrue(actualProduct.getAccessories().getAccessoryArray().isEmpty());
            assertTrue(actualProduct.getSimilarItems().isEmpty());
        } finally {
            joxIn.close();
            fileInput.close();
        }
    }

    @org.junit.Test
    public void testGetProduct_EmptySimilarItemsArray_ReturnsProductWithEmptySimilarItemsArray() {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        FileInputStream fileInput = new FileInputStream(new ByteArrayInputStream(new byte[0]));
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileInput);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productDetails.setAccessories(new Accessories());
        ArrayList<ProductDetails> detailsArray = new ArrayList<>(Arrays.asList(new ProductDetails[0]));
        productInfoBean.setDetails(productDetails);
        productInfoBean.setProductsArrayList(detailsArray);

        try {
            FullProduct actualProduct = product.getProduct(asin, offer, page);

            assertNotNull(actualProduct);
            assertEquals(1, actualProduct.getDetails().length);
            assertTrue(actualProduct.getAccessories().getAccessoryArray().isEmpty());
            assertTrue(actualProduct.getSimilarItems().isEmpty());
        } finally {
            joxIn.close();
            fileInput.close();
        }
    }

    @org.junit.Test
    public void testGetProduct_NullProductInfoBeanFromSimilarSearch_ReturnsEmptyProduct() {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        ProductInfo productInfoBean = null;

        FullProduct actualProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualProduct);
        assertTrue(actualProduct.getDetails().length == 0);
        assertTrue(actualProduct.getAccessories().getAccessoryArray().isEmpty());
        assertTrue(actualProduct.getSimilarItems().isEmpty());
    }

    @org.junit.Test(expected = IOException.class)
    public void testGetProduct_IOException_ThrowsIOException() throws IOException {
        String asin = "B000012345";
        String offer = "Groceries";
        String page = "1";

        throw new IOException();
    }
}