import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.MiniProduct;

public class ProductTest {

    @Test
    public void testGetProduct_ValidInput() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails expectedProductDetails = new ProductDetails();
        MiniProduct expectedMiniProduct = new MiniProduct();
        FileUtil fileUtil = new FileUtil();

        String asin = "ASIN_1";
        String offer = "Offer_1";
        String page = "Page_1";

        expectedProductDetails.setAccessories(new Accessories());
        expectedProductDetails.getAccessories().setAccessoryArray(new ArrayList());

        expectedMiniProduct.setAsin("ASIN_2");
        expectedMiniProduct.setImageURL("Image_URL_2");
        expectedMiniProduct.setManufacturer("Manufacturer_2");
        expectedMiniProduct.setName("Name_2");
        expectedMiniProduct.setPrice(12.5);

        expectedProductDetails.getAccessories().getAccessoryArray().add(expectedMiniProduct);
        expectedFullProduct.setDetails(expectedProductDetails);

        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo)joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        assertNotNull(productInfoBean);
        assertNotNull(productInfoBean.getDetails());
        assertNotNull(productInfoBean.getDetails()[0]);

        Product result = product.getProduct(asin, offer, page);
        assertEquals(expectedFullProduct, result);
    }
}