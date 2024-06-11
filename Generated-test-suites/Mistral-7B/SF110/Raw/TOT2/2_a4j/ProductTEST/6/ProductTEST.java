import static org.junit.Assert.assertNotNull;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.MiniProduct;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.Test;

public class ProductTest {
    @Test
    public void testGetProduct() {
        Product product = new Product();
        FullProduct expectedProduct = new FullProduct();
        String asin = "sampleAsin";
        String offer = "sampleOffer";
        String page = "samplePage";
        FileInputStream fileIn = new FileUtil().fetchASINFile(asin, "heavy", offer, page);
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertNotNull(actualProduct);
        fileIn.close();
    }
}