package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.Accessories;

import org.junit.Test;

public class ProductTest {

    private static final String TEST_ASIN = "B000001234";
    private static final String TEST_OFFER = "New";
    private static final String TEST_PAGE = "1";
    private static final String TEST_ACCESSORY_ASIN = "B000001235";

    @Test
    public void testGetProduct_ValidArgs_ReturnsFullProduct() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();

        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();

        // Prepare test data
        List<Object> detailsArray = Arrays.asList(productDetails);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(detailsArray.toArray(new ProductDetails[0]));

        List<Object> accessoryArray = Arrays.asList(accessoryProductDetails);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        accessoryProductInfoBean.setProductsArrayList(accessoryArray);

        // Setup test file mocks
        byte[] productFileContent = getProductFileContent(productInfoBean);
        byte[] accessoryFileContent = getAccessoryFileContent(accessoryProductInfoBean);

        FileInputStream productFileMock = mockFileInputStream(productFileContent);
        FileInputStream accessoryFileMock = mockFileInputStream(accessoryFileContent);

        FileUtil fileUtilMock = mockFileUtil(productFileMock, accessoryFileMock);

        // Test method call
        FullProduct fullProduct = product.getProduct(TEST_ASIN, TEST_OFFER, TEST_PAGE);

        // Assertions
        assertNotNull(fullProduct);
        assertEquals(productDetails, fullProduct.getDetails());
        assertEquals(1, fullProduct.getAccessories().getAccessoryArray().size());
        assertEquals(accessoryProductDetails, fullProduct.getAccessories().getAccessoryArray().get(0));
        assertEquals(1, fullProduct.getSimilarItems().size());
        assertEquals(miniProduct, fullProduct.getSimilarItems().get(0));

        // Clean up mocks
        fileUtilMock.fetchASINFile(TEST_ASIN, "heavy", TEST_OFFER, TEST_PAGE).close();
        fileUtilMock.fetchAccessories(TEST_ASIN, accessoryArray).close();
    }

    private byte[] getProductFileContent(ProductInfo productInfoBean) {
        // Prepare test data for product file

        // Write test data to byte array
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            JOXBeanOutputStream joxOut = new JOXBeanOutputStream(byteStream);
            joxOut.writeObject(productInfoBean);
            joxOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteStream.toByteArray();
    }

    private byte[] getAccessoryFileContent(ProductInfo productInfoBean) {
        // Prepare test data for accessory file

        // Write test data to byte array
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            JOXBeanOutputStream joxOut = new JOXBeanOutputStream(byteStream);
            joxOut.writeObject(productInfoBean);
            joxOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteStream.toByteArray();
    }

    private FileInputStream mockFileInputStream(byte[] fileContent) {
        FileInputStream fileInputStreamMock = mock(FileInputStream.class);
        when(fileInputStreamMock.readAllBytes()).thenReturn(fileContent);
        return fileInputStreamMock;
    }

    private FileUtil mockFileUtil(FileInputStream productFileMock, FileInputStream accessoryFileMock) {
        FileUtil fileUtilMock = mock(FileUtil.class);
        when(fileUtilMock.fetchASINFile(TEST_ASIN, "heavy", TEST_OFFER, TEST_PAGE)).thenReturn(productFileMock);
        when(fileUtilMock.fetchAccessories(TEST_ASIN, anyListOf(ProductDetails.class))).thenReturn(accessoryFileMock);
        return fileUtilMock;
    }
}