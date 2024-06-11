package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;

import org.junit.Test;

public class ProductTest {

    private FileUtil fileUtil = new FileUtil();
    private Product product = new Product();

    @Test
    public void testGetProductNullFile() {
        assertNull(product.getProduct("1234567890", "Offer", "Page"));
    }

    @Test
    public void testGetProductValidProduct() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList<AccessoryDetails> accessoryArray = new ArrayList<>();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoExpected = new ProductInfo();

        productInfoExpected.setDetails(new ProductDetails[] { productDetails });
        productInfoExpected.setProductsArrayList(Arrays.asList(miniProduct));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(getClass().getResourceAsStream("/testProductInfo.xml").readAllBytes());
        JOXBeanInputStream joxIn = new JOXBeanInputStream(inputStream);
        ProductInfo productInfoRead = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();

        FileInputStream fileIn = new FileInputStream(new File("src/test/resources/testASINFile.txt"));
        productDetails.setAccessories(accessories);
        accessoryArray = accessories.getAccessoryArray();
        accessoryArray.add(new AccessoryDetails("accessory1", "accessoryImage1.com", "Manufacturer1", "Product1", 4.99));

        accessories.setAccessoryArray(accessoryArray);
        productDetails.setAccessories(accessories);
        productInfo.setDetails(new ProductDetails[] { productDetails });
        productInfo.setProductsArrayList(accessoryArray.size() > 0 ? Arrays.asList(miniProduct) : null);

        when(fileUtil.fetchASINFile("1234567890", "heavy", "Offer", "Page")).thenReturn(fileIn);

        FullProduct fullProduct = product.getProduct("1234567890", "Offer", "Page");

        verify(fileUtil).fetchASINFile("1234567890", "heavy", "Offer", "Page");
        assertEquals(productInfoExpected, fullProduct.getProductInfo());
        assertEquals(1, fullProduct.getAccessories().size());
        assertEquals("accessory1", fullProduct.getAccessories().get(0).getAsin());
        assertEquals("accessoryImage1.com", fullProduct.getAccessories().get(0).getImageURL());
        assertEquals("Manufacturer1", fullProduct.getAccessories().get(0).getManufacturer());
        assertEquals("Product1", fullProduct.getAccessories().get(0).getName());
        assertEquals(4.99, fullProduct.getAccessories().get(0).getPrice(), 0.01);
    }

    @Test
    public void testGetProductNoAccessoriesFile() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoExpected = new ProductInfo();

        productInfoExpected.setDetails(new ProductDetails[] { productDetails });
        productInfoExpected.setProductsArrayList(Arrays.asList(miniProduct));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(getClass().getResourceAsStream("/testProductInfoNoAccessories.xml").readAllBytes());
        JOXBeanInputStream joxIn = new JOXBeanInputStream(inputStream);
        ProductInfo productInfoRead = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();

        when(fileUtil.fetchASINFile("1234567890", "heavy", "Offer", "Page")).thenReturn(new FileInputStream(new File("src/test/resources/testASINFileNoAccessories.txt")));

        FullProduct fullProduct = product.getProduct("1234567890", "Offer", "Page");

        verify(fileUtil).fetchASINFile("1234567890", "heavy", "Offer", "Page");
        assertEquals(productInfoExpected, fullProduct.getProductInfo());
        assertNull(fullProduct.getAccessories());
    }

    @Test
    public void testGetProductInvalidProductInfo() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoExpected = new ProductInfo();

        productInfoExpected.setDetails(new ProductDetails[] { productDetails });
        productInfoExpected.setProductsArrayList(Arrays.asList(miniProduct));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(getClass().getResourceAsStream("/testProductInfoInvalid.xml").readAllBytes());
        JOXBeanInputStream joxIn = new JOXBeanInputStream(inputStream);
        ProductInfo productInfoRead = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();

        when(fileUtil.fetchASINFile("1234567890", "heavy", "Offer", "Page")).thenReturn(new FileInputStream(new File("src/test/resources/testASINFile.txt")));

        try {
            product.getProduct("1234567890", "Offer", "Page");
            fail();
        } catch (Exception e) {
            // expected
        }

        verify(fileUtil).fetchASINFile("1234567890", "heavy", "Offer", "Page");
    }

    @Test
    public void testGetProductNoSimilarItems() {
        ProductInfo productInfo = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoExpected = new ProductInfo();

        productInfoExpected.setDetails(new ProductDetails[] { productDetails });
        productInfoExpected.setProductsArrayList(null);

        FullProduct fullProduct = product.getProduct("1234567890", "Offer", "Page");

        assertEquals(productInfoExpected, fullProduct.getProductInfo());
        assertNull(fullProduct.getSimilarItems());
    }

    @Test
    public void testGetProductWithSimilarItems() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList<AccessoryDetails> accessoryArray = new ArrayList<>();
        AccessoryDetails accessoryProductDetails = new AccessoryDetails();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoExpected = new ProductInfo();

        productInfoExpected.setDetails(new ProductDetails[] { productDetails });
        productInfoExpected.setProductsArrayList(Arrays.asList(miniProduct));

        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(getClass().getResourceAsStream("/testProductInfoNoAccessories.xml").readAllBytes());
        JOXBeanInputStream joxIn1 = new JOXBeanInputStream(inputStream1);
        ProductInfo productInfoRead1 = (ProductInfo) joxIn1.readObject(ProductInfo.class);
        joxIn1.close();

        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(getClass().getResourceAsStream("/testAccessoryDetails.xml").readAllBytes());
        JOXBeanInputStream joxIn2 = new JOXBeanInputStream(inputStream2);
        AccessoryDetails accessoryDetailsRead = (AccessoryDetails) joxIn2.readObject(AccessoryDetails.class);
        joxIn2.close();

        productDetails.setAccessories(accessories);
        accessoryArray = accessories.getAccessoryArray();
        accessoryArray.add(new AccessoryDetails("accessory1", "accessoryImage1.com", "Manufacturer1", "Product1", 4.99));
        accessories.setAccessoryArray(accessoryArray);
        productDetails.setAccessories(accessories);
        productInfo.setDetails(new ProductDetails[] { productDetails });
        productInfo.setProductsArrayList(accessoryArray.size() > 0 ? Arrays.asList(miniProduct) : null);

        when(fileUtil.fetchASINFile("1234567890", "heavy", "Offer", "Page")).thenReturn(new FileInputStream(new File("src/test/resources/testASINFile.txt")));
        when(fileUtil.fetchAccessories("1234567890", accessoryArray)).thenReturn(new FileInputStream(new File("src/test/resources/testAccessoryFile.txt")));

        FullProduct fullProduct = product.getProduct("1234567890", "Offer", "Page");

        verify(fileUtil).fetchASINFile("1234567890", "heavy", "Offer", "Page");
        verify(fileUtil).fetchAccessories("1234567890", accessoryArray);
        assertEquals(productInfoExpected, fullProduct.getProductInfo());
        assertEquals(1, fullProduct.getAccessories().size());
        assertEquals("accessory1", fullProduct.getAccessories().get(0).getAsin());
        assertEquals("accessoryImage1.com", fullProduct.getAccessories().get(0).getImageURL());
        assertEquals("Manufacturer1", fullProduct.getAccessories().get(0).getManufacturer());
        assertEquals("Product1", fullProduct.getAccessories().get(0).getName());
        assertEquals(4.99, fullProduct.getAccessories().get(0).getPrice(), 0.01);
        assertEquals(1, fullProduct.getSimilarItems().size());
        assertEquals("ASIN_SIMILAR_ITEM", fullProduct.getSimilarItems().get(0).getAsin());
        assertEquals("similarItemImageURL", fullProduct.getSimilarItems().get(0).getImageURL());
        assertEquals("Similar Manufacturer", fullProduct.getSimilarItems().get(0).getManufacturer());
        assertEquals("Similar Product", fullProduct.getSimilarItems().get(0).getName());
        assertEquals(3.99, fullProduct.getSimilarItems().get(0).getPrice(), 0.01);
    }
}