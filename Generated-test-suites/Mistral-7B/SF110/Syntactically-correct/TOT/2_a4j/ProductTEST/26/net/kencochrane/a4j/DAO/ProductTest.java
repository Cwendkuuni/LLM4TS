import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;

public class ProductTest {

    @Test
    public void testGetProductWithValidInput() {
        String asin = "B08N5Z88TG";
        String offer = "New";
        String page = "1";

        Product product = new Product();
        FullProduct expectedProduct = new FullProduct();
        MiniProduct expectedMiniProduct = new MiniProduct();
        expectedMiniProduct.setAsin("ASIN1");
        expectedMiniProduct.setImageURL("URL1");
        expectedMiniProduct.setManufacturer("Manufacturer1");
        expectedMiniProduct.setName("Name1");
        expectedMiniProduct.setPrice(10.99);
        expectedProduct.setDetails(new ProductDetails());
        expectedProduct.setAccessories(new Accessories());
        expectedProduct.getAccessories().setAccessoryArray(new ArrayList<>());
        expectedProduct.addAccessory(expectedMiniProduct);

        FullProduct actualProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);

        FileUtil fileUtil = new FileUtil();
        JOXBeanInputStream joxIn = null;
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        // Add verification for this file
        // ...
        joxIn = new JOXBeanInputStream((InputStream)fileIn);
        ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
        joxIn.close();
        fileIn.close();
        assertNotNull(productInfoBean);
    }
}