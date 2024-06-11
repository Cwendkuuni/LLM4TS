package net.kencochrane.a4j.DAO;
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
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedProduct, actualProduct);
    }
}