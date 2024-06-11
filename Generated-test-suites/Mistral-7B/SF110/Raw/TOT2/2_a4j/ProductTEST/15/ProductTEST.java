import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;

public class ProductTest {

    @Test
    public void testGetProductWithValidArguments() {
        Product product = new Product();
        FullProduct expectedProduct = new FullProduct();
        // Given valid arguments
        String asin = "B000123456";
        String offer = "new";
        String page = "1";
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Then the method returns a valid FullProduct object
        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testGetProductWithNullArguments() {
        Product product = new Product();
        // Given null arguments
        String asin = null;
        String offer = null;
        String page = null;
        FullProduct actualProduct = product.getProduct(asin, offer, page);

        // Then the method returns null
        assertNull(actualProduct);
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetProductWithFileNotFoundException() {
        Product product = new Product();
        // Given an ASIN for which the file is not found
        String asin = "B000123456";
        String offer = "new";
        String page = "1";
        product.getProduct(asin, offer, page);
    }

    @Test(expected = IOException.class)
    public void testGetProductWithIOException() {
        Product product = new Product();
        // Given an error while reading the file
        String asin = "B000123456";
        String offer = "new";
        String page = "1";
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        InputStream inputStream = new ByteArrayInputStream("Error message".getBytes());
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(inputStream);
        ProductInfo productInfoBean = new ProductInfo();
        product.getProduct(asin, offer, page);
        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetProductWithClassNotFoundException() {
        Product product = new Product();
        // Given an error while reading the object from the file
        String asin = "B000123456";
        String offer = "new";
        String page = "1";
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(inputStream);
        Product productInfoBean = product.getProduct(asin, offer, page);
        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
    }

    @Test
    public void testGetProductWithEmptyAccessoryArray() {
        Product product = new Product();
        // Given valid arguments that return an empty accessory array
        String asin = "B000123456";
        String offer = "new";
        String page = "1";
        FullProduct expectedProduct = new FullProduct();
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
        assertEmpty(actualProduct.getAccessories().getAccessoryArray());
    }

    @Test
    public void testGetProductWithEmptyDetailsArray() {
        Product product = new Product();
        // Given valid arguments that return an empty details array
        String asin = "B000123456";
        String offer = "new";
        String page = "1";
        FullProduct expectedProduct = new FullProduct();
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
        assertEmpty(actualProduct.getDetails());
    }
}