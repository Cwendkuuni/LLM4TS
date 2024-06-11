import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductInfo;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductTest {
    private Product product;
    private FileUtil fileUtil;
    private final String asin = "B000012345";
    private final String offer = "New";
    private final String page = "1";

    @Before
    public void setUp() {
        fileUtil = new FileUtil();
        product = new Product();
    }

    @Test
    public void testGetProduct_ValidInput_ReturnsFullProduct() throws IOException {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        assertNotNull(fileIn);
        ProductInfo productInfoBean = product.getProduct(asin, offer, page);
        assertNotNull(productInfoBean);
        assertNotNull(productInfoBean.getDetails());
        ProductDetails productDetails = productInfoBean.getDetails()[0];
        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNotNull(fullProduct);
        assertEquals(productDetails.getDetails()[0].getProductName(), fullProduct.getDetails().get(0).getProductName());
        assertEquals(productDetails.getDetails()[0].getOurPrice(), fullProduct.getDetails().get(0).getOurPrice(), 0.01);

        Accessories accessories = productDetails.getAccessories();
        List<MiniProduct> accessoriesList = fullProduct.getAccessories();
        assertEquals(accessories.getAccessoryArray().size(), accessoriesList.size());
        for (int i = 0; i < accessories.getAccessoryArray().size(); i++) {
            MiniProduct miniProduct = accessoriesList.get(i);
            MiniProduct accessoryProductDetails = (MiniProduct) accessories.getAccessoryArray().get(i);
            assertEquals(accessoryProductDetails.getAsin(), miniProduct.getAsin());
            assertEquals(accessoryProductDetails.getImageURL(), miniProduct.getImageURL());
            assertEquals(accessoryProductDetails.getManufacturer(), miniProduct.getManufacturer());
            assertEquals(accessoryProductDetails.getName(), miniProduct.getName());
            assertEquals(accessoryProductDetails.getPrice(), miniProduct.getPrice(), 0.01);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetProduct_NullFileInputStream_ReturnsNull() throws IOException {
        FileInputStream fileIn = null;
        ProductInfo productInfoBean = product.getProduct(asin, offer, page);
        assertNull(productInfoBean);
    }
}