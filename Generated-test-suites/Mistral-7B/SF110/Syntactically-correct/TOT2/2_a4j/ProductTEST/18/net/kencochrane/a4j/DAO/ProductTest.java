import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {
    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN123", "offer1", "page1");
        assertNotNull(fullProduct);
    }
}