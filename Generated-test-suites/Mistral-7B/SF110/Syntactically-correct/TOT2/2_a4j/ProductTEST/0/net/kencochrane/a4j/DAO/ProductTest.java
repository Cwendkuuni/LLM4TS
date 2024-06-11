import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {
    @Test
    public void testGetProduct() {
        String asin = "B000012345";
        String offer = "testOffer";
        String page = "testPage";
        FullProduct product = new Product().getProduct(asin, offer, page);
        assertNotNull(product);
    }
}