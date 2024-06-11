package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.Accessories;
import java.util.ArrayList;
import java.util.List;
import com.wutka.jox.JOXBeanInputStream;
import net.kencochrane.a4j.beans.ProductInfo;

public class ProductTest {

    @Test
    public void testGetProduct_ValidInputs_ReturnsFullProduct() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        ProductDetails accessoryProductDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        // Given: mock FileUtil class to return InputStream for tests
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        // When: call getProduct method with valid inputs
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns a FullProduct object
        assertNotNull(fullProduct);
        assertEquals(expectedFullProduct, fullProduct);

        // Test productDetails field
        ProductInfo productInfoBean = new ProductInfo();
        productDetails = productInfoBean.getDetails()[0];
        expectedFullProduct.setDetails(productDetails);

        // Test accessories field
        accessories = productDetails.getAccessories();
        accessoryArray = accessories.getAccessoryArray();
        if (accessoryArray != null && accessoryArray.size() > 0) {
            FileInputStream accessoryFileIn = mock(FileInputStream.class);
            when(fileUtil.fetchAccessories(asin, accessoryArray)).thenReturn(accessoryFileIn);

            ProductInfo accessoryProductInfoBean = new ProductInfo();
            when(new JOXBeanInputStream((InputStream)accessoryFileIn).readObject((Class)ProductInfo.class)).thenReturn(accessoryProductInfoBean);

            detailsArray = accessoryProductInfoBean.getProductsArrayList();
            for (int x = 0; x < detailsArray.size(); ++x) {
                accessoryProductDetails = detailsArray.get(x);
                miniProduct = new MiniProduct();
                miniProduct.setAsin(accessoryProductDetails.getAsin());
                miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                miniProduct.setName(accessoryProductDetails.getProductName());
                miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                expectedFullProduct.addAccessory(miniProduct);
            }
        }

        // Test similarItems field
        Search search = new Search();
        ProductInfo similarProductInfoBean = new ProductInfo();
        detailsArray = similarProductInfoBean.getProductsArrayList();
        for (int x = 0; x < detailsArray.size(); ++x) {
            accessoryProductDetails = detailsArray.get(x);
            miniProduct = new MiniProduct();
            miniProduct.setAsin(accessoryProductDetails.getAsin());
            miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
            miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
            miniProduct.setName(accessoryProductDetails.getProductName());
            miniProduct.setPrice(accessoryProductDetails.getOurPrice());
            expectedFullProduct.addSimilarItem(miniProduct);
        }
    }

    @Test
    public void testGetProduct_NullInputs_ReturnsNull() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_NullFileInput_ReturnsNull() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();

        // Given: mock FileUtil class to return null for tests
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(null);

        // When: call getProduct method with valid inputs
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns null
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductInfo productInfoBean = new ProductInfo();

        // Given: mock FileUtil class to return valid FileInputStream
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);

        // When: call getProduct method with valid inputs, but ProductInfoBean is null
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns null
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_EmptyProductDetails_ReturnsNull() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();

        // Given: mock FileUtil class to return valid FileInputStream and ProductInfoBean
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        when(new JOXBeanInputStream((InputStream)fileIn).readObject((Class)ProductInfo.class)).thenReturn(productInfoBean);

        // When: call getProduct method with valid inputs, but productDetails is empty
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns null
        assertNull(fullProduct);
    }

    @Test
    public void testGetProduct_EmptyAccessoriesArray_ReturnsFullProductWithEmptyAccessoryList() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();

        // Given: mock FileUtil to return valid FileInputStream and ProductInfoBean
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(new JOXBeanInputStream((InputStream)fileIn).readObject((Class)ProductInfo.class)).thenReturn(productInfoBean);

        // When: call getProduct method with valid inputs
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns FullProduct with empty accessory list
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getAccessories().isEmpty());
    }

    @Test
    public void testGetProduct_AccessoriesArray_ValidAccessoryDetails_ReturnsFullProductWithAccessoryList() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();

        // Given: mock FileUtil to return valid FileInputStream and ProductInfoBean
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(new JOXBeanInputStream((InputStream)fileIn).readObject((Class)ProductInfo.class)).thenReturn(productInfoBean);

        // Given: mock FileUtil to return valid FileInputStream and ProductInfoBean for accessory fetch
        FileInputStream accessoryFileIn = mock(FileInputStream.class);
        when(fileUtil.fetchAccessories(asin, accessoryArray)).thenReturn(accessoryFileIn);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        List<ProductDetails> accessoryDetailsList = new ArrayList<>();
        MiniProduct accessoryMiniProduct = new MiniProduct();
        ProductDetails accessoryProductDetails = new ProductDetails();
        ProductInfo miniProductInfoBean = new ProductInfo();

        accessoryProductDetails.setAsin("ASIN456");
        accessoryProductDetails.setImageUrlSmall("url");
        accessoryProductDetails.setManufacturer("manufacturer");
        accessoryProductDetails.setProductName("productName");
        accessoryProductDetails.setOurPrice(12.0);
        accessoryMiniProduct.setAsin(accessoryProductDetails.getAsin());
        accessoryMiniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
        accessoryMiniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
        accessoryMiniProduct.setName(accessoryProductDetails.getProductName());
        accessoryMiniProduct.setPrice(accessoryProductDetails.getOurPrice());

        miniProductInfoBean.setProductsArrayList(accessoryDetailsList);
        accessoryDetailsList.add(accessoryProductDetails);
        when(new JOXBeanInputStream((InputStream)accessoryFileIn).readObject((Class)ProductInfo.class)).thenReturn(accessoryProductInfoBean);

        // When: call getProduct method with valid inputs
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns FullProduct with accessory list
        assertNotNull(fullProduct);
        assertEquals(expectedFullProduct, fullProduct);
        assertEquals(1, fullProduct.getAccessories().size());
        assertEquals(accessoryMiniProduct, fullProduct.getAccessories().get(0));
    }

    @Test
    public void testGetProduct_EmptySimilarItemsArrayList_ReturnsFullProductWithEmptySimilarItemsList() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();

        // Given: mock FileUtil to return valid FileInputStream and ProductInfoBean
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(new JOXBeanInputStream((InputStream)fileIn).readObject((Class)ProductInfo.class)).thenReturn(productInfoBean);

        // When: call getProduct method with valid inputs
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns FullProduct with empty similarItems list
        assertNotNull(fullProduct);
        assertTrue(fullProduct.getSimilarItems().isEmpty());
    }

    @Test
    public void testGetProduct_SimilarItemsArrayList_ValidSimilarItems_ReturnsFullProductWithSimilarItemsList() {
        String asin = "ASIN123";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();

        // Given: mock FileUtil to return valid FileInputStream and ProductInfoBean
        FileInputStream fileIn = mock(FileInputStream.class);
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(fileIn);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo similarProductInfoBean = new ProductInfo();
        List<ProductDetails> similarProductDetailsList = new ArrayList<>();
        ProductDetails similarProductDetails = new ProductDetails();
        ProductInfo miniProductInfoBean = new ProductInfo();

        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        when(new JOXBeanInputStream((InputStream)fileIn).readObject((Class)ProductInfo.class)).thenReturn(productInfoBean);

        // Given: mock FileUtil to return valid FileInputStream and ProductInfoBean for similarItems fetch
        FileInputStream similarItemsFileIn = mock(FileInputStream.class);
        when(fileUtil.fetchAccessories(asin, null)).thenReturn(similarItemsFileIn);
        when(fileUtil.fetchAccessories(asin, Collections.emptyList())).thenReturn(similarItemsFileIn);
        when(new JOXBeanInputStream((InputStream)similarItemsFileIn).readObject((Class)ProductInfo.class)).thenReturn(similarProductInfoBean);

        similarProductDetails.setAsin("ASIN456");
        similarProductDetails.setImageUrlSmall("url");
        similarProductDetails.setManufacturer("manufacturer");
        similarProductDetails.setProductName("productName");
        similarProductDetails.setOurPrice(12.0);
        miniProductInfoBean.setProductsArrayList(Collections.singletonList(similarProductDetails));
        when(new JOXBeanInputStream((InputStream)similarItemsFileIn).readObject((Class)ProductInfo.class)).thenReturn(similarProductInfoBean);

        // When: call getProduct method with valid inputs
        FullProduct fullProduct = product.getProduct(asin, offer, page);

        // Then: assert that getProduct method returns FullProduct with similarItems list
        assertNotNull(fullProduct);
        assertEquals(expectedFullProduct, fullProduct);
        assertEquals(1, fullProduct.getSimilarItems().size());
        assertEquals(similarProductDetails, fullProduct.getSimilarItems().get(0));
    }
}