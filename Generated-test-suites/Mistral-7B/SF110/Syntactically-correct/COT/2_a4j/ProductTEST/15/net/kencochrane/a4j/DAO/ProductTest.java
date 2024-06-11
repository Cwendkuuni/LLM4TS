package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.beans.Accessories;
import net.kencochrane.a4j.DAO.beans.FullProduct;
import net.kencochrane.a4j.DAO.beans.MiniProduct;
import net.kencochrane.a4j.DAO.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.utils.JOXBeanInputStream;

import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProductNullFile() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProductNullProductInfoBean() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "offer", "page", "filePath");
        assertNull(product.getProduct("asin", "offer", "page"));
        try {
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductNullAccessoryProductDetails() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        fullProduct.setDetails(productDetails);

        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "offer", "page", "filePath");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProductEmptyAccessoryArrayList() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        fullProduct.setDetails(productDetails);

        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "offer", "page", "filePath");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (productInfoBean != null && productInfoBean.getDetails() != null && productInfoBean.getDetails().length > 0) {
            productDetails = productInfoBean.getDetails()[0];
            fullProduct.setDetails(productDetails);
            assertNotNull(product.getProduct("asin", "offer", "page"));
        } else {
            assertNull(product.getProduct("asin", "offer", "page"));
        }
    }

    @Test
    public void testGetProductNullSimilarItemsArrayList() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        ProductDetails accessoryProductDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ArrayList detailsArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();

        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);

        fullProduct.setDetails(productDetails);

        FileUtil fileUtil = new FileUtil();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "offer", "page", "filePath");
        JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
        ProductInfo productInfoBean = null;
        try {
            productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (productInfoBean != null && productInfoBean.getDetails() != null && productInfoBean.getDetails().length > 0) {
            productDetails = productInfoBean.getDetails()[0];
            fullProduct.setDetails(productDetails);

            accessories = productDetails.getAccessories();
            accessoryArray = accessories.getAccessoryArray();

            if (accessoryArray != null && accessoryArray.size() > 0) {
                File file = new File("accessoryFilePath");
                fileIn = new FileInputStream(file);
                joxIn = new JOXBeanInputStream(fileIn);
                ProductInfo productInfoBeanAccessories = null;
                try {
                    productInfoBeanAccessories = (ProductInfo) joxIn.readObject(ProductInfo.class);
                    joxIn.close();
                    fileIn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (productInfoBeanAccessories != null && productInfoBeanAccessories.getProductsArrayList() != null) {
                    detailsArray = productInfoBeanAccessories.getProductsArrayList();
                    for (Object object : detailsArray) {
                        accessoryProductDetails = (ProductDetails) object;
                        if (accessoryProductDetails != null) {
                            miniProduct = new MiniProduct();
                            miniProduct.setAsin(accessoryProductDetails.getAsin());
                            miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                            miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                            miniProduct.setName(accessoryProductDetails.getProductName());
                            miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                            fullProduct.addAccessory(miniProduct);
                        }
                    }
                }
            }

            productInfoBean = product.search.SimilaritesSearch("asin", "1");
            assertNotNull(productInfoBean);
            assertNotNull(productInfoBean.getProductsArrayList());
            assertFalse(productInfoBean.getProductsArrayList().isEmpty());

            detailsArray = productInfoBean.getProductsArrayList();
            for (Object object : detailsArray) {
                accessoryProductDetails = (ProductDetails) object;
                if (accessoryProductDetails != null) {
                    miniProduct = new MiniProduct();
                    miniProduct.setAsin(accessoryProductDetails.getAsin());
                    miniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
                    miniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
                    miniProduct.setName(accessoryProductDetails.getProductName());
                    miniProduct.setPrice(accessoryProductDetails.getOurPrice());
                    fullProduct.addSimilarItem(miniProduct);
                }
            }
        }

        assertNotNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProductException() {
        Product product = new Product();
        try {
            product.getProduct(null, null, null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Expected
        }

        ProductInfo productInfoBean = new ProductInfo();
        try {
            product.getProduct("asin", null, null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            product.getProduct("asin", "offer", null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            product.getProduct("asin", "offer", "page");
            fail("IOException expected");
        } catch (IOException e) {
            // Expected
        }

        try {
            product.getProduct("asin", "offer", "page");
            fail("ClassNotFoundException expected");
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }
}