import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.MiniProduct;
import java.io.FileInputStream;
import java.io.IOException;

class ProductTest {
    @Test
    void testGetProduct() throws IOException {
        Product product = new Product();
        FullProduct expectedProduct = new FullProduct();
        ProductDetails expectedProductDetails = new ProductDetails();
        MiniProduct expectedMiniProduct = new MiniProduct();

        String asin = "sampleASIN";
        String offer = "sampleOffer";
        String page = "samplePage";

        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);

        FullProduct actualProduct = product.getProduct(asin, offer, page);

        if (fileIn != null) {
            expectedProductDetails = new ProductDetails();
            expectedProductDetails.setAccessories(new Accessories());
            expectedProduct.setDetails(expectedProductDetails);
            expectedProductDetails.getAccessories().setAccessoryArray(new ArrayList<>());
            expectedMiniProduct = new MiniProduct();
            expectedMiniProduct.setAsin("sampleAccessoryASIN");
            expectedMiniProduct.setImageURL("sampleImageURL");
            expectedMiniProduct.setManufacturer("sampleManufacturer");
            expectedMiniProduct.setName("sampleName");
            expectedMiniProduct.setPrice(10.5);
            expectedProductDetails.getAccessories().getAccessoryArray().add(expectedMiniProduct);
            expectedProduct.addAccessory(expectedMiniProduct);
            fileIn.close();
            assertEquals(expectedProduct, actualProduct);
        } else {
            assertNull(actualProduct);
        }
    }
}