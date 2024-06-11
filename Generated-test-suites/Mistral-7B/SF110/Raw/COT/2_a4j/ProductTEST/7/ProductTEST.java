package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductTest {

    private FileUtil fileUtil;
    private Product product;

    @Before
    public void setup() {
        fileUtil = new FileUtil();
        product = new Product();
    }

    @Test
    public void testGetProduct_ValidInput_ReturnsFullProduct() throws Exception {
        String asin = "B000012345";
        String offer = "new";
        String page = "1";

        InputStream inputStream = new ByteArrayInputStream(getFileContent("valid_product.xml").getBytes());
        FileOutputStream outputStream = new FileOutputStream("tempFile.xml");
        fileUtil.saveFile(asin, "heavy", offer, page, inputStream, outputStream);

        FullProduct result = product.getProduct(asin, offer, page);

        assertNotNull(result);
        assertNotNull(result.getDetails());
        assertFalse(result.getAccessories().getAccessoryArray().isEmpty());
        assertFalse(result.getSimilarItems().isEmpty());

        fileUtil.deleteFile(asin, "heavy", offer, page, "tempFile.xml");
    }

    @Test
    public void testGetProduct_NullInput_ReturnsNull() {
        FullProduct result = product.getProduct(null, null, null);

        assertNull(result);
    }

    @Test
    public void testGetProduct_EmptyFile_ReturnsNull() throws Exception {
        String asin = "B000012345";
        String offer = "new";
        String page = "1";

        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        if (fileIn != null) {
            fileIn.close();
            fileUtil.deleteFile(asin, "heavy", offer, page, "tempFile.xml");
        }

        FullProduct result = product.getProduct(asin, offer, page);

        assertNull(result);
    }

    @Test
    public void testGetProduct_InvalidXml_ReturnsNull() throws Exception {
        String asin = "B000012345";
        String offer = "new";
        String page = "1";

        InputStream inputStream = new ByteArrayInputStream(getFileContent("invalid_xml.xml").getBytes());
        FileOutputStream outputStream = new FileOutputStream("tempFile.xml");
        fileUtil.saveFile(asin, "heavy", offer, page, inputStream, outputStream);

        FullProduct result = product.getProduct(asin, offer, page);

        assertNull(result);

        fileUtil.deleteFile(asin, "heavy", offer, page, "tempFile.xml");
    }

    @Test(expected = Exception.class)
    public void testGetProduct_IOException_ThrowsException() throws Exception {
        String asin = "B000012345";
        String offer = "new";
        String page = "1";

        doThrow(new IOException("Test IOException")).when(fileUtil).fetchASINFile(asin, "heavy", offer, page);

        product.getProduct(asin, offer, page);
    }

    private byte[] getFileContent(String fileName) {
        // Provide the content of the XML files for testing
        // Replace this block with the actual content loading logic
        return Arrays.valueOf(new byte[]{"<ProductInfo xmlns=\"http://xml.amazon.com/documents/2008-02-29/\">" +
                "<Details>" +
                "<ProductDetails>" +
                "<ProductName>Test Product</ProductName>" +
                "<OurPrice>12.99</OurPrice>" +
                "<ImageUrlSmall>http://example.com/test.jpg</ImageUrlSmall>" +
                "</ProductDetails>" +
                "</Details>" +
                "<Accessories>" +
                "<AccessoryArray>" +
                "<AccessoryDetails>" +
                "<AccessoryName>Test Accessory</AccessoryName>" +
                "<Asin>B000012346</Asin>" +
                "<ImageUrlSmall>http://example.com/test_accessory.jpg</ImageUrlSmall>" +
                "</AccessoryDetails>" +
                "</AccessoryArray>" +
                "</Accessories>" +
                "</ProductInfo>"}.getBytes());
    }
}