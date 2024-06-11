
package net.kencochrane.a4j.DAO;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {
    
    private Product product;

    @Before
    public void setup() {
        product = new Product();
    }

    @Test
    public void testGetProduct_ValidArguments_ReturnsFullProduct() {
        String asin = "B08N5D8J4W";
        String offer = "new";
        String page = "1";
        FullProduct expectedProduct = new FullProduct();
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        expectedProduct.setDetails(productDetails);
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(miniProduct);
        miniProduct.setAsin("B07N5D8J4W");
        miniProduct.setImageURL("https://example.com/image.jpg");
        miniProduct.setManufacturer("Manufacturer X");
        miniProduct.setName("Accessory X");
        miniProduct.setPrice(10.99);
        detailsArray.add(productDetails);
        productInfoBean.setDetails(detailsArray);
        expectedProduct.setProductsArrayList(detailsArray);

        FullProduct actualProduct = product.getProduct(asin, offer, page);

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testGetProduct_InvalidASIN_ThrowsIOException() {
        String asin = "";
        String offer = "new";
        String page = "1";
        try {
            product.getProduct(asin, offer, page);
            fail("IOException expected");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        String asin = "B08N5D8J4W";
        String offer = "new";
        String page = "1";
        ProductInfo productInfoBean = null;
        FullProduct actualProduct = product.getProduct(asin, offer, page, productInfoBean);
        assertNull(actualProduct);
    }

    @Test
    public void testAddAccessory_ValidMiniProduct_AddsAccessoryToProduct() {
        String asin = "B08N5D8J4W";
        String offer = "new";
        String page = "1";
        FullProduct product = new Product().getProduct(asin, offer, page);
        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("B07N5D8J4W");
        miniProduct.setImageURL("https://example.com/image.jpg");
        miniProduct.setManufacturer("Manufacturer X");
        miniProduct.setName("Accessory X");
        miniProduct.setPrice(10.99);
        product.addAccessory(miniProduct);
        assertEquals(1, product.getAccessories().size());
    }

    @Test
    public void testAddSimilarItem_ValidMiniProduct_AddsSimilarItemToProduct() {
        String asin = "B08N5D8J4W";
        String offer = "new";
        String page = "1";
        FullProduct product = new Product().getProduct(asin, offer, page);
        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("B09N5D8J4W");
        miniProduct.setImageURL("https://example.com/image.jpg");
        miniProduct.setManufacturer("Manufacturer Y");
        miniProduct.setName("Similar Item X");
        miniProduct.setPrice(12.99);
        product.addSimilarItem(miniProduct);
        assertEquals(1, product.getSimilarItems().size());
    }
}
