package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import net.kencochrane.a4j.file.*;
import java.io.*;
import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

    @Test
    public void testGetProduct_NullValues() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_ValidInput_NoAccessoriesOrSimilarItems() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("asinValue", "heavy", "offerValue", "pageValue");
            ProductInfo productInfoBean = new ProductInfo();
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();
                productDetails = productInfoBean.getDetails()[0];
                expectedFullProduct.setDetails(productDetails);
            }
            FullProduct actualFullProduct = product.getProduct("asinValue", "offerValue", "pageValue");
            assertEquals(expectedFullProduct, actualFullProduct);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEmpty(expectedFullProduct.getAccessories());
        assertEmpty(expectedFullProduct.getSimilarItems());
    }

    @Test
    public void testGetProduct_ValidInput_AccessoriesAndSimilarItems() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FullProduct expectedFullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        Search search = new Search();
        ProductInfo productInfoBean = null;
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("asinValue", "heavy", "offerValue", "pageValue");
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();
                productDetails = productInfoBean.getDetails()[0];
                expectedFullProduct.setDetails(productDetails);
                accessories = productDetails.getAccessories();
                accessoryArray = accessories.getAccessoryArray();
                if (accessoryArray != null && accessoryArray.size() > 0) {
                    FileInputStream accessoryFileIn = fileUtil.fetchAccessories("asinValue", accessoryArray);
                    if (accessoryFileIn != null) {
                        JOXBeanInputStream accessoryJoxIn = new JOXBeanInputStream(accessoryFileIn);
                        ProductInfo accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
                        accessoryJoxIn.close();
                        accessoryFileIn.close();
                        List accessoryDetailsList = accessoryProductInfoBean.getProductsArrayList();
                        for (Object accessoryObject : accessoryDetailsList) {
                            ProductDetails accessoryProductDetails = (ProductDetails) accessoryObject;
                            MiniProduct miniAccessoryProduct = new MiniProduct();
                            miniAccessoryProduct.setAsin(accessoryProductDetails.getAsin());
                            miniAccessoryProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                            miniAccessoryProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                            miniAccessoryProduct.setName(accessoryProductDetails.getProductName());
                            miniAccessoryProduct.setPrice(accessoryProductDetails.getOurPrice());
                            expectedFullProduct.addAccessory(miniAccessoryProduct);
                        }
                    }
                }
                productInfoBean = search.SimilaritesSearch("asinValue", "1");
                if (productInfoBean != null && productInfoBean.getProductsArrayList() != null && productInfoBean.getProductsArrayList().size() > 0) {
                    List similarItemsList = productInfoBean.getProductsArrayList();
                    for (Object similarItemObject : similarItemsList) {
                        ProductDetails similarItemProductDetails = (ProductDetails) similarItemObject;
                        MiniProduct miniSimilarItemProduct = new MiniProduct();
                        miniSimilarItemProduct.setAsin(similarItemProductDetails.getAsin());
                        miniSimilarItemProduct.setImageURL(similarItemProductDetails.getImageUrlSmall());
                        miniSimilarItemProduct.setManufacturer(similarItemProductDetails.getManufacturer());
                        miniSimilarItemProduct.setName(similarItemProductDetails.getProductName());
                        miniSimilarItemProduct.setPrice(similarItemProductDetails.getOurPrice());
                        expectedFullProduct.addSimilarItem(miniSimilarItemProduct);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        FullProduct actualFullProduct = product.getProduct("asinValue", "offerValue", "pageValue");
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    private void assertEmpty(List list) {
        assertTrue("List should be empty", list.isEmpty());
    }

}