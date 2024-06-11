package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.file.Search;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private FileUtil fileUtil;
    private Product product;

    @Before
    public void setup() {
        fileUtil = new FileUtil();
        product = new Product();
    }

    @Test
    public void testGetProduct_ValidArgs_ReturnFullProduct() {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductInfo productInfoBean = createProductInfoBean();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(productInfoBean.getBytes());
        File file = new File("tempFile.xml");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = productInfoBean.getBytes();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.close();

            FullProduct expectedResult = new FullProduct();
            ProductDetails productDetails = new ProductDetails();
            productDetails.setName("Product Name");
            expectedResult.setDetails(productDetails);

            Accessories accessories = new Accessories();
            List<MiniProduct> accessoryArray = new ArrayList<>();
            MiniProduct accessoryProduct = new MiniProduct();
            accessoryProduct.setName("Accessory Name");
            accessoryProduct.setPrice(10.0);
            accessoryArray.add(accessoryProduct);
            accessories.setAccessoryArray(accessoryArray);
            expectedResult.setAccessories(accessories);

            FullProduct result = product.getProduct(asin, offer, page);

            assertEquals(expectedResult, result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
    }

    @Test
    public void testGetProduct_NullFile_ReturnNull() {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        FullProduct expectedResult = null;

        FullProduct result = product.getProduct(asin, offer, page);

        assertEquals(expectedResult, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProduct_NullDetails_ReturnNull() {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductInfo productInfoBean = createProductInfoBean();
        productInfoBean.getDetails()[0] = null;

        FileInputStream fileIn = new FileInputStream("tempFile.xml");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);

        try {
            FullProduct result = product.getProduct(asin, offer, page);

            fail("Expected NullPointerException but got " + result);
        } finally {
            fileIn.close();
            joxIn.close();
        }
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray_ReturnFullProductWithoutAccessories() {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductInfo productInfoBean = createProductInfoBean();
        productInfoBean.getDetails()[0].getAccessories().setAccessoryArray(new ArrayList<MiniProduct>());

        FileInputStream fileIn = new FileInputStream("tempFile.xml");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);

        try {
            FullProduct result = product.getProduct(asin, offer, page);

            assertTrue(result.getAccessories().getAccessoryArray().isEmpty());
        } finally {
            fileIn.close();
            joxIn.close();
        }
    }

    @Test
    public void testGetProduct_NullAccessoryProductDetails_DontAddToFullProduct() {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductInfo productInfoBean = createProductInfoBean();
        productInfoBean.getProductsArrayList().get(0).setAccessoryProductDetails(null);

        FileInputStream fileIn = new FileInputStream("tempFile.xml");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);

        try {
            FullProduct result = product.getProduct(asin, offer, page);

            assertTrue(result.getAccessories().getAccessoryArray().isEmpty());
        } finally {
            fileIn.close();
            joxIn.close();
        }
    }

    @Test
    public void testGetProduct_EmptySimilarItemsArray_ReturnFullProductWithoutSimilarItems() {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductInfo productInfoBean = createProductInfoBean();
        productInfoBean.getProductsArrayList().clear();

        FileInputStream fileIn = new FileInputStream("tempFile.xml");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);

        try {
            FullProduct result = product.getProduct(asin, offer, page);

            assertTrue(result.getSimilarItems().isEmpty());
        } finally {
            fileIn.close();
            joxIn.close();
        }
    }

    @Test
    public void testGetProduct_NullSimilarItem_DontAddToFullProduct() {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductInfo productInfoBean = createProductInfoBean();
        productInfoBean.getProductsArrayList().get(0).getAccessoryProductDetails().setMiniProduct(null);

        FileInputStream fileIn = new FileInputStream("tempFile.xml");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);

        try {
            FullProduct result = product.getProduct(asin, offer, page);

            assertTrue(result.getSimilarItems().isEmpty());
        } finally {
            fileIn.close();
            joxIn.close();
        }
    }

    private ProductInfo createProductInfoBean() {
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setName("Product Name");
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        Accessories accessories = new Accessories();
        List<MiniProduct> accessoryArray = new ArrayList<>();
        MiniProduct accessoryProduct = new MiniProduct();
        accessoryProduct.setName("Accessory Name");
        accessoryProduct.setPrice(10.0);
        accessoryArray.add(accessoryProduct);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setAccessories(accessories);

        List<ProductInfo> productsArrayList = new ArrayList<>();
        productInfoBean.setProductsArrayList(productsArrayList);

        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("ASIN456");
        miniProduct.setImageURL("Image URL");
        miniProduct.setManufacturer("Manufacturer Name");
        miniProduct.setName("Similar Item Name");
        miniProduct.setPrice(5.0);

        ProductInfo similarProductInfoBean = new ProductInfo();
        similarProductInfoBean.setProductsArrayList(Collections.singletonList(miniProduct));
        productsArrayList.add(similarProductInfoBean);

        return productInfoBean;
    }
}