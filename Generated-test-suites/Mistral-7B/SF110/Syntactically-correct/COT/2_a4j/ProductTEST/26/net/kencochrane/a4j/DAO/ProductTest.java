package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.utils.JOXBeanInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private Product product;
    private FileUtil fileUtil;
    private final String asin = "B000123456";
    private final String offer = "new";
    private final String page = "1";
    private final String otherAsin = "B000234567";

    @Before
    public void setUp() throws Exception {
        fileUtil = new FileUtil();
        product = new Product();
    }

    @After
    public void tearDown() throws Exception {
        if (product != null) {
            product = null;
        }
        if (fileUtil != null) {
            fileUtil = null;
        }
    }

    @Test
    public void testGetProductWithValidArguments() {
        FullProduct expectedProduct = new FullProduct();
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        ProductDetails productDetails = productInfoBean.getDetails()[0];
        Accessories accessories = productDetails.getAccessories();
        ArrayList accessoryArray = accessories.getAccessoryArray();
        FileInputStream accessoryFileIn = null;
        JOXBeanInputStream accessoryJoxIn = null;
        ProductInfo accessoryProductInfoBean = null;

        if (accessoryArray != null && accessoryArray.size() > 0) {
            accessoryFileIn = fileUtil.fetchAccessories(asin, accessoryArray);
            if (accessoryFileIn != null) {
                accessoryJoxIn = new JOXBeanInputStream(accessoryFileIn);
                accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
                accessoryJoxIn.close();
                accessoryFileIn.close();
            }
        }

        if (accessoryProductInfoBean != null && accessoryProductInfoBean.getProductsArrayList() != null && accessoryProductInfoBean.getProductsArrayList().size() > 0) {
            List<MiniProduct> accessoriesList = new ArrayList<>();
            for (Object detail : accessoryProductInfoBean.getProductsArrayList()) {
                MiniProduct miniProduct = new MiniProduct();
                ProductDetails accessoryProductDetails = (ProductDetails) detail;
                miniProduct.setAsin(accessoryProductDetails.getAsin());
                miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                miniProduct.setName(accessoryProductDetails.getProductName());
                miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                accessoriesList.add(miniProduct);
            }
            expectedProduct.setAccessories(new Accessories(accessoriesList));
        }

        productDetails = productInfoBean.getDetails()[0];
        expectedProduct.setDetails(productDetails);

        Search search = new Search();
        ProductInfo productInfoBean2 = search.SimilaritesSearch(asin, "1");
        if (productInfoBean2 != null && productInfoBean2.getProductsArrayList() != null && productInfoBean2.getProductsArrayList().size() > 0) {
            List<MiniProduct> similarItemsList = new ArrayList<>();
            for (Object detail : productInfoBean2.getProductsArrayList()) {
                MiniProduct miniProduct = new MiniProduct();
                ProductDetails similarProductDetails = (ProductDetails) detail;
                miniProduct.setAsin(similarProductDetails.getAsin());
                miniProduct.setImageURL(similarProductDetails.getImageUrlSmall());
                miniProduct.setManufacturer(similarProductDetails.getManufacturer());
                miniProduct.setName(similarProductDetails.getProductName());
                miniProduct.setPrice(similarProductDetails.getOurPrice());
                similarItemsList.add(miniProduct);
            }
            expectedProduct.setSimilarItems(new SimilarItems(similarItemsList));
        }

        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testGetProductWithNullFile() {
        FullProduct actualProduct = product.getProduct(asin, offer, page);
        assertNull(actualProduct);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProductWithNullProductInfoBean() {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;

        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        } catch (Exception e) {
            // Ignore
        } finally {
            joxIn.close();
            fileIn.close();
        }

        product.getProduct(asin, offer, page);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetProductWithEmptyDetailsArray() {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        product.getProduct(asin, offer, page);
    }

    @Test
    public void testGetProductWithNoAccessories() {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        product.getProduct(asin, offer, page);
    }

    @Test(expected = IOException.class)
    public void testGetProductWithNullAccessoryFile() {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        FileInputStream accessoryFileIn = null;
        product.getProduct(asin, offer, page, accessoryFileIn);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProductWithNullAccessoryProductInfoBean() {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1"));
        ProductInfo accessoryProductInfoBean = null;

        try {
            accessoryProductInfoBean = (ProductInfo) new JOXBeanInputStream(accessoryFileIn).readObject(ProductInfo.class);
        } catch (Exception e) {
            // Ignore
        } finally {
            accessoryFileIn.close();
        }

        product.getProduct(asin, offer, page, accessoryFileIn, accessoryProductInfoBean);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetProductWithEmptyAccessoryProductsArrayList() {
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1"));
        ProductInfo accessoryProductInfoBean = (ProductInfo) new JOXBeanInputStream(accessoryFileIn).readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();
        accessoryFileIn.close();

        product.getProduct(asin, offer, page, accessoryFileIn, accessoryProductInfoBean);
    }

    @Test
    public void testGetProductWithInvalidImageUrl() {
        String invalidImageUrl = "invalid_image_url";
        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();

        FileInputStream accessoryFileIn = fileUtil.fetchAccessories(asin, Arrays.asList("ASIN1"));
        ProductInfo accessoryProductInfoBean = (ProductInfo) new JOXBeanInputStream(accessoryFileIn).readObject(ProductInfo.class);
        joxIn.close();
        fileIn.close();
        accessoryFileIn.close();

        FullProduct actualProduct = product.getProduct(asin, offer, page, accessoryFileIn, accessoryProductInfoBean);
        assertNull(actualProduct.getImageURL());
    }
}