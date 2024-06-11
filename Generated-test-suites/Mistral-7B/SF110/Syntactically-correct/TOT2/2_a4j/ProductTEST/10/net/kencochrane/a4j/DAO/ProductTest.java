import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {

    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("ASIN_VALUE", "OFFER_VALUE", "PAGE_VALUE");
        assertNotNull(fullProduct);
    }
}