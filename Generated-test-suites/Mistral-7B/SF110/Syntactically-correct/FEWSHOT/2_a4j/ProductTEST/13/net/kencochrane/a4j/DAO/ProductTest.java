package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductInfo;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;

public class ProductTest {

    @Test
    public void testGetProduct_validArgs_returnsFullProduct() {
        final String asin = "B07Q3JF8JM";
        final String offer = "new";
        final String page = "1";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails expectedProductDetails = new ProductDetails();
        Accessories expectedAccessories = new Accessories();
        ArrayList expectedAccessoryArray = new ArrayList();
        ArrayList expectedDetailsArray = new ArrayList();
        MiniProduct expectedMiniProduct = new MiniProduct();

        // Given
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(expectedAccessories);
        expectedProductDetails = productDetails;
        expectedAccessories.setAccessoryArray(expectedAccessoryArray);
        expectedProductDetails.setDetails(expectedDetailsArray.toArray(new ProductDetails[0]));
        expectedFullProduct.setDetails(expectedProductDetails);

        // When
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        // Then
        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct.getDetails(), actualFullProduct.getDetails());
    }

    @Test
    public void testGetProduct_nullFile_returnsNull() {
        final String asin = "B07Q3JF8JM";
        final String offer = "new";
        final String page = "1";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        // When
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        // Then
        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_nullProductInfoBean_returnsNull() {
        final String asin = "B07Q3JF8JM";
        final String offer = "new";
        final String page = "1";

        FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        try {
            FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                fileIn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // When
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        // Then
        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_emptyDetailsArray_returnsNull() {
        final String asin = "B07Q3JF8JM";
        final String offer = "new";
        final String page = "1";

        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();

        try {
            FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn == null) {
                return;
            }

            JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();

            if (productInfoBean == null || productInfoBean.getDetails() == null || productInfoBean.getDetails().length == 0) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Given
        ProductDetails productDetails = productInfoBean.getDetails()[0];

        // When
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        // Then
        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_nullAccessoryProductDetails_noAccessoriesAdded() {
        final String asin = "B07Q3JF8JM";
        final String offer = "new";
        final String page = "1";

        Product product = new Product();

        try {
            FileInputStream fileIn = new FileUtil().fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                //...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // When
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        // Then
        assertEquals(0, actualFullProduct.getAccessories().size());
    }

    @Test
    public void testGetProduct_populatedAccessoryProductDetails_accessoriesAdded() {
        final String asin = "B07Q3JF8JM";
        final String offer = "new";
        final String page = "1";

        Product product = new Product();

        try {
            FileInputStream fileIn = new FileUtil().fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                //...
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            FileInputStream fileIn2 = new FileUtil().fetchAccessories(asin, new ArrayList<String>());
            if (fileIn2 != null) {
                //...
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Given
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        // When
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        // Then
        assertNotNull(actualFullProduct);
        assertTrue(actualFullProduct.getAccessories().size() > 0);
    }

    @Test
    public void testGetProduct_similarItems_addedToList() {
        final String asin = "B07Q3JF8JM";
        final String offer = "new";
        final String page = "1";

        Product product = new Product();

        try {
            FileInputStream fileIn = new FileUtil().fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                //...
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Given
        ProductInfo productInfoBean = new ProductInfo();
        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("ASIN1");
        miniProduct.setImageURL("URL1");
        miniProduct.setManufacturer("Manufacturer1");
        miniProduct.setName("Name1");
        miniProduct.setPrice(10.0);
        accessoryProductDetails.setProductDetails(miniProduct);
        detailsArray.add(accessoryProductDetails);
        productInfoBean.setProductsArrayList(detailsArray);

        // When
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        // Then
        assertNotNull(actualFullProduct);
        assertTrue(actualFullProduct.getSimilarItems().size() > 0);
    }
}