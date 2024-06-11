import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductTest {

    private Product product;
    private FileUtil fileUtilMock;

    @Before
    public void setup() {
        fileUtilMock = mock(FileUtil.class);
        product = new Product();
        product.setFileUtil(fileUtilMock);
    }

    @Test
    public void testGetProduct_ValidInput_ReturnsFullProduct() throws IOException {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productDetails.getAccessories().setAccessoryArray(new ArrayList<>());
        expectedFullProduct.setDetails(productDetails);

        when(fileUtilMock.fetchASINFile(ArgumentMatchers.eq(asin), ArgumentMatchers.eq("heavy"), ArgumentMatchers.eq(offer), ArgumentMatchers.eq(page)))
                .thenReturn(new FileInputStream(new ByteArrayInputStream(getFileContent("validASIN.xml").getBytes())));

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(fileUtilMock.fetchAccessories(ArgumentMatchers.eq(asin), ArgumentMatchers.eq(productDetails.getAccessories().getAccessoryArray())))
                .thenReturn(new FileInputStream(new ByteArrayInputStream(getFileContent("validAccessories.xml").getBytes())));

        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("ASIN456");
        miniProduct.setImageURL("imageURL1");
        miniProduct.setManufacturer("manufacturer1");
        miniProduct.setName("name1");
        miniProduct.setPrice(10.5);

        ProductDetails accessoryProductDetails = new ProductDetails();
        accessoryProductDetails.setAccessoryArray(new ArrayList<>());
        accessoryProductDetails.getAccessoryArray().add("ASIN456");

        when(fileUtilMock.SimilaritesSearch(ArgumentMatchers.eq(asin), ArgumentMatchers.eq("1")))
                .thenReturn(new ProductInfo());

        List<MiniProduct> expectedAccessories = Arrays.asList(miniProduct);
        List<MiniProduct> expectedSimilarItems = new ArrayList<>();

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedFullProduct, actualFullProduct);
        assertEquals(expectedAccessories, actualFullProduct.getAccessories());
        assertNull(actualFullProduct.getSimilarItems());
    }

    @Test
    public void testGetProduct_NullFile_ReturnsNull() throws IOException {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        when(fileUtilMock.fetchASINFile(ArgumentMatchers.eq(asin), ArgumentMatchers.eq("heavy"), ArgumentMatchers.eq(offer), ArgumentMatchers.eq(page)))
                .thenReturn(null);

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);
        assertNull(actualFullProduct);
    }

    @Test(expected = IOException.class)
    public void testGetProduct_IOException_ThrowsException() throws IOException {
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        when(fileUtilMock.fetchASINFile(ArgumentMatchers.eq(asin), ArgumentMatchers.eq("heavy"), ArgumentMatchers.eq(offer), ArgumentMatchers.eq(page)))
                .thenThrow(new IOException("IOException"));

        product.getProduct(asin, offer, page);
    }

    private byte[] getFileContent(String fileName) {
        // Provide a method to read file content as byte array for mocking
        // ...
    }
}