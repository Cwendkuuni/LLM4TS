package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import com.wutka.jox.*;
import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.file.*;

public class ProductTest {

    @Test
    public void testGetProduct_ValidArgs_ReturnsFullProduct() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
    }

    @Test
    public void testGetProduct_NullAsin_ReturnsNull() {
        Product product = new Product();
        String asin = null;
        String offer = "offer1";
        String page = "page1";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray_DoesNotAddAnyAccessories() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(0, actualFullProduct.getAccessories().size());
    }

    @Test
    public void testGetProduct_NonEmptyAccessoryArray_AddsAccessories() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        String asin = "ASIN123";
        String offer = "offer1";
        String page = "page1";

        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        ProductDetails accessoryProductDetails1 = new ProductDetails();
        accessoryProductDetails1.setAsin("ASIN456");
        accessoryProductDetails1.setImageUrlSmall("image1.jpg");
        accessoryProductDetails1.setManufacturer("Manufacturer1");
        accessoryProductDetails1.setProductName("Product1");
        accessoryProductDetails1.setOurPrice(10.99f);

        ProductDetails accessoryProductDetails2 = new ProductDetails();
        accessoryProductDetails2.setAsin("ASIN789");
        accessoryProductDetails2.setImageUrlSmall("image2.jpg");
        accessoryProductDetails2.setManufacturer("Manufacturer2");
        accessoryProductDetails2.setProductName("Product2");
        accessoryProductDetails2.setOurPrice(15.99f);

        accessoryArray.add(accessoryProductDetails1);
        accessoryArray.add(accessoryProductDetails2);
        accessories.setAccessoryArray(accessoryArray);
        productDetails.setAccessories(accessories);

        expectedFullProduct.setDetails(productDetails);

        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn1 = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn1 = new JOXBeanInputStream(fileIn1);
        ProductInfo productInfoBean1 = (ProductInfo)joxIn1.readObject(ProductInfo.class);
        joxIn1.close();
        fileIn1.close();

        ProductInfo productInfoBean2 = new ProductInfo();
        ProductInfo productInfoBean3 = new ProductInfo();

        if (productInfoBean1 != null && productInfoBean1.getDetails().length > 0) {
            productDetails = productInfoBean1.getDetails()[0];
            accessories = productDetails.getAccessories();
            accessoryArray = accessories.getAccessoryArray();

            if (accessoryArray != null && accessoryArray.size() > 0) {
                FileInputStream fileIn2 = fileUtil.fetchAccessories(asin, accessoryArray);
                JOXBeanInputStream joxIn2 = new JOXBeanInputStream(fileIn2);
                ProductInfo productInfoBean4 = (ProductInfo)joxIn2.readObject(ProductInfo.class);
                joxIn2.close();
                fileIn2.close();

                if (productInfoBean4 != null && productInfoBean4.getProductsArrayList().size() > 0) {
                    ArrayList detailsArray = productInfoBean4.getProductsArrayList();
                    for (int x = 0; x < detailsArray.size(); ++x) {
                        accessoryProductDetails1 = (ProductDetails)detailsArray.get(x);
                        miniProduct = new MiniProduct();
                        miniProduct.setAsin(accessoryProductDetails1.getAsin());
                        miniProduct.setImageURL(accessoryProductDetails1.getImageUrlSmall());
                        miniProduct.setManufacturer(accessoryProductDetails1.getManufacturer());
                        miniProduct.setName(accessoryProductDetails1.getProductName());
                        miniProduct.setPrice(accessoryProductDetails1.getOurPrice());
                        expectedFullProduct.addAccessory(miniProduct);
                    }
                }
            }
        }

        FileInputStream fileIn3 = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        ProductInfo productInfoBean5 = new ProductInfo();
        JOXBeanInputStream joxIn3 = new JOXBeanInputStream(fileIn3);
        productInfoBean5 = (ProductInfo)joxIn3.readObject(ProductInfo.class);
        joxIn3.close();
        fileIn3.close();

        if (productInfoBean5 != null && productInfoBean5.getProductsArrayList() != null) {
            detailsArray = productInfoBean5.getProductsArrayList();
            for (int x = 0; x < detailsArray.size(); ++x) {
                accessoryProductDetails2 = (ProductDetails)detailsArray.get(x);
                miniProduct = new MiniProduct();
                miniProduct.setAsin(accessoryProductDetails2.getAsin());
                miniProduct.setImageURL(accessoryProductDetails2.getImageUrlSmall());
                miniProduct.setManufacturer(accessoryProductDetails2.getManufacturer());
                miniProduct.setName(accessoryProductDetails2.getProductName());
                miniProduct.setPrice(accessoryProductDetails2.getOurPrice());
                expectedFullProduct.addSimilarItem(miniProduct);
            }
        }

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);
        assertEquals(expectedFullProduct, actualFullProduct);
    }

}