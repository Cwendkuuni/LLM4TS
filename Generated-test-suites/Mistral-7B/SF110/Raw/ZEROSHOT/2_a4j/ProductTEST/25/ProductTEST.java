package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;

import org.junit.Test;

public class ProductTest {

    @Test
    public void testGetProductNullFile() {
        Product product = new Product();
        assertNull(product.getProduct("invalidASIN", "offer", "page"));
    }

    @Test
    public void testGetProductEmptyDetails() {
        Product product = new Product();
        String asin = "validASIN";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        File file = new File("src/test/resources/emptyFile.xml");
        FileInputStream fileIn = null;
        try {
            fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();

                assertNull(product.getProduct(asin, offer, page));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testGetProductEmptyAccessories() {
        Product product = new Product();
        String asin = "validASIN";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        File file = new File("src/test/resources/productWithEmptyAccessories.xml");
        FileInputStream fileIn = null;
        try {
            fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();

                FullProduct fullProduct = product.getProduct(asin, offer, page);
                assertEquals(0, fullProduct.getAccessories().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testGetProductEmptySimilarItems() {
        Product product = new Product();
        String asin = "validASIN";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        File file = new File("src/test/resources/productWithEmptySimilarItems.xml");
        FileInputStream fileIn = null;
        try {
            fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();

                FullProduct fullProduct = product.getProduct(asin, offer, page);
                assertEquals(0, fullProduct.getSimilarItems().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testGetProductValidFileWithAccessories() {
        Product product = new Product();
        String asin = "validASIN";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        File file = new File("src/test/resources/productWithAccessories.xml");
        FileInputStream fileIn = null;
        try {
            fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();

                FullProduct fullProduct = product.getProduct(asin, offer, page);
                assertNotNull(fullProduct);
                assertNotNull(fullProduct.getDetails());
                assertNotNull(fullProduct.getAccessories());
                assertFalse(fullProduct.getAccessories().isEmpty());

                Accessories accessories = fullProduct.getAccessories();
                List<MiniProduct> expectedAccessories = Arrays.asList(
                        new MiniProduct("accessory1ASIN", "accessory1ImageURL", "accessory1Manufacturer", "accessory1Name", 10.0),
                        new MiniProduct("accessory2ASIN", "accessory2ImageURL", "accessory2Manufacturer", "accessory2Name", 15.0)
                );

                List<MiniProduct> actualAccessories = fullProduct.getAccessories().getAccessoryArray();
                assertEquals(expectedAccessories, actualAccessories);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testGetProductValidFileWithSimilarItems() {
        Product product = new Product();
        String asin = "validASIN";
        String offer = "offer";
        String page = "page";

        FileUtil fileUtil = new FileUtil();
        File file = new File("src/test/resources/productWithSimilarItems.xml");
        FileInputStream fileIn = null;
        try {
            fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();

                FullProduct fullProduct = product.getProduct(asin, offer, page);
                assertNotNull(fullProduct);
                assertNotNull(fullProduct.getDetails());
                assertNotNull(fullProduct.getSimilarItems());
                assertFalse(fullProduct.getSimilarItems().isEmpty());

                List<MiniProduct> expectedSimilarItems = Arrays.asList(
                        new MiniProduct("similarItem1ASIN", "similarItem1ImageURL", "similarItem1Manufacturer", "similarItem1Name", 5.0),
                        new MiniProduct("similarItem2ASIN", "similarItem2ImageURL", "similarItem2Manufacturer", "similarItem2Name", 20.0)
                );

                List<MiniProduct> actualSimilarItems = fullProduct.getSimilarItems();
                assertEquals(expectedSimilarItems, actualSimilarItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}