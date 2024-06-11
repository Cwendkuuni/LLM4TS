import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {

    @Test
    public void testGetProductWithValidArguments() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        // Prepare expected FullProduct object based on known test data
        String asin = "B000012345";
        String offer = "New";
        String page = "1";
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    // Add edge cases and exception handling test cases here
}