import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import org.junit.Test;

public class ProductTest {
    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct expectedProduct = new FullProduct();
        FullProduct actualProduct = product.getProduct("ASIN_1", "offer1", "page1");
        assertEquals(expectedProduct, actualProduct);
    }
}