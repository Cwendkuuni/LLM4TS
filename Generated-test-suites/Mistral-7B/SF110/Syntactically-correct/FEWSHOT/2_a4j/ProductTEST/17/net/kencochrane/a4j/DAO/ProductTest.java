package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

    @Test
    public void testGetProduct_NullInputs() {
        final Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_ValidInputs_EmptyFile() {
        final Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        final FullProduct fullProduct = new FullProduct();
        assertNull(product.getProduct("asin1", "offer1", "page1"));
    }

    @Test
    public void testGetProduct_ValidInputs_NullProductInfoBean() {
        final Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        final FileInputStream fileIn = new FileInputStream("testFile.txt");
        JOXBeanInputStream joxIn = null;
        final FullProduct fullProduct = new FullProduct();
        assertNull(product.getProduct("asin1", "offer1", "page1"));
    }

    @Test
    public void testGetProduct_ValidInputs_EmptyDetailsArray() {
        final Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        final FileInputStream fileIn = new FileInputStream("testFile.txt");
        JOXBeanInputStream joxIn = null;
        final FullProduct fullProduct = new FullProduct();
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBean = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            assertNull(product.getProduct("asin1", "offer1", "page1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProduct_ValidInputs_EmptyAccessoryArray() {
        final Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        final FileInputStream fileIn = new FileInputStream("testFile.txt");
        JOXBeanInputStream joxIn = null;
        final ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);
        final FullProduct fullProduct = new FullProduct();
        try {
            joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBeanFromFile = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            assertNotNull(product.getProduct("asin1", "offer1", "page1"));
            assertTrue(fullProduct.getAccessories().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProduct_ValidInputs() {
        final Product product = new Product();
        final FileUtil fileUtil = new FileUtil();
        final ProductInfo productInfoBean = new ProductInfo();
        final ProductDetails productDetails = new ProductDetails();
        final Accessories accessories = new Accessories();
        final ArrayList accessoryArray = new ArrayList();
        final MiniProduct miniProduct = new MiniProduct();
        final Search search = new Search();
        final FullProduct fullProduct = new FullProduct();

        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        productInfoBean.setProductsArrayList(Collections.singletonList(productInfoBean));
        accessoryArray.add(new ProductDetails());

        try {
            final FileInputStream fileIn = fileUtil.fetchASINFile("asin1", "heavy", "offer1", "page1");
            if (fileIn == null) {
                fail("ASIN file not found");
            }
            JOXBeanInputStream joxIn = new JOXBeanInputStream((InputStream)fileIn);
            ProductInfo productInfoBeanFromFile = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            fileIn.close();
            assertEquals(productInfoBean, product.getProduct("asin1", "offer1", "page1"));

            final FileInputStream accessoryFileIn = fileUtil.fetchAccessories("asin1", accessoryArray);
            if (accessoryFileIn == null) {
                fail("Accessory file not found");
            }
            joxIn = new JOXBeanInputStream((InputStream)accessoryFileIn);
            ProductInfo productInfoBeanFromAccessoryFile = (ProductInfo)joxIn.readObject((Class)ProductInfo.class);
            joxIn.close();
            accessoryFileIn.close();

            assertEquals(productInfoBeanFromAccessoryFile.getProductsArrayList().size(), 1);
            final MiniProduct miniProductFromFile = productInfoBeanFromAccessoryFile.getProductsArrayList().get(0);
            fullProduct.addAccessory(miniProductFromFile);

            assertEquals(fullProduct.getAccessories().getAccessoryArray().size(), 1);
            assertEquals(miniProductFromFile.getAsin(), fullProduct.getAccessories().getAccessoryArray().get(0).getAsin());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}