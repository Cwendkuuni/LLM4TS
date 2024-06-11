package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Search;

public class ProductTest {
    private FileUtil fileUtil;
    private Product product;

    @Before
    public void setup() {
        fileUtil = new FileUtil();
        product = new Product();
    }

    @Test
    public void testGetProductNullInputs() {
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProductNoFile() {
        assertNull(product.getProduct("ASIN1", "offer1", "page1"));
    }

    @Test
    public void testGetProductParsingError() {
        File testFile = new File("testFile.xml");
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("ASIN1", "heavy", "offer1", "page1");
            if (fileIn != null) {
                fileIn.close();
                throw new IOException("Expected parsing error");
            }
            assertNull(product.getProduct("ASIN1", "offer1", "page1"));
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetProductNoDetails() {
        File testFile = new File("testFileWithDetails.xml");
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("ASIN1", "heavy", "offer1", "page1");
            if (fileIn == null) {
                return;
            }
            JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
            ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
            assertNull(product.getProduct("ASIN1", "offer1", "page1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductNoAccessories() {
        File testFile = new File("testFileWithAccessories.xml");
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("ASIN1", "heavy", "offer1", "page1");
            if (fileIn == null) {
                return;
            }
            JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
            ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
            if (productInfoBean.getDetails() != null && productInfoBean.getDetails().length > 0) {
                ProductDetails productDetails = productInfoBean.getDetails()[0];
                Accessories accessories = productDetails.getAccessories();
                assertNull(accessories.getAccessoryArray());
                product.getProduct("ASIN1", "offer1", "page1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductEmptyAccessoryArray() {
        File testFile = new File("testFileWithEmptyAccessoryArray.xml");
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("ASIN1", "heavy", "offer1", "page1");
            if (fileIn == null) {
                return;
            }
            JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
            ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
            if (productInfoBean.getDetails() != null && productInfoBean.getDetails().length > 0) {
                ProductDetails productDetails = productInfoBean.getDetails()[0];
                Accessories accessories = productDetails.getAccessories();
                if (accessories.getAccessoryArray() != null && accessories.getAccessoryArray().size() > 0) {
                    File accessoryFile = fileUtil.fetchAccessories("ASIN1", accessories.getAccessoryArray());
                    if (accessoryFile != null) {
                        FileInputStream accessoryFileIn = new FileInputStream(accessoryFile);
                        JOXBeanInputStream accessoryJoxIn = new JOXBeanInputStream(accessoryFileIn);
                        ProductInfo accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
                        accessoryJoxIn.close();
                        accessoryFileIn.close();
                        if (accessoryProductInfoBean != null && accessoryProductInfoBean.getProductsArrayList() != null && accessoryProductInfoBean.getProductsArrayList().size() > 0) {
                            List<MiniProduct> accessoryDetails = new ArrayList<>(accessoryProductInfoBean.getProductsArrayList());
                            assertEquals(0, accessoryDetails.size());
                            product.getProduct("ASIN1", "offer1", "page1");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductValidInputs() {
        File testFile = new File("testFileWithValidInputs.xml");
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("ASIN1", "heavy", "offer1", "page1");
            if (fileIn == null) {
                return;
            }
            JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
            ProductInfo productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
            joxIn.close();
            fileIn.close();
            if (productInfoBean.getDetails() != null && productInfoBean.getDetails().length > 0) {
                ProductDetails productDetails = productInfoBean.getDetails()[0];
                Accessories accessories = productDetails.getAccessories();
                if (accessories.getAccessoryArray() != null && accessories.getAccessoryArray().size() > 0) {
                    File accessoryFile = fileUtil.fetchAccessories("ASIN1", accessories.getAccessoryArray());
                    if (accessoryFile != null) {
                        FileInputStream accessoryFileIn = new FileInputStream(accessoryFile);
                        JOXBeanInputStream accessoryJoxIn = new JOXBeanInputStream(accessoryFileIn);
                        ProductInfo accessoryProductInfoBean = (ProductInfo) accessoryJoxIn.readObject(ProductInfo.class);
                        accessoryJoxIn.close();
                        accessoryFileIn.close();
                        if (accessoryProductInfoBean != null && accessoryProductInfoBean.getProductsArrayList() != null && accessoryProductInfoBean.getProductsArrayList().size() > 0) {
                            List<MiniProduct> accessoryDetails = new ArrayList<>(accessoryProductInfoBean.getProductsArrayList());
                            FullProduct expectedFullProduct = new FullProduct();
                            expectedFullProduct.setDetails(productDetails);
                            for (MiniProduct accessoryDetail : accessoryDetails) {
                                MiniProduct miniProduct = new MiniProduct();
                                miniProduct.setAsin(accessoryDetail.getAsin());
                                miniProduct.setImageURL(accessoryDetail.getImageUrlSmall());
                                miniProduct.setManufacturer(accessoryDetail.getManufacturer());
                                miniProduct.setName(accessoryDetail.getProductName());
                                miniProduct.setPrice(accessoryDetail.getOurPrice());
                                expectedFullProduct.addAccessory(miniProduct);
                            }
                            FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");
                            assertEquals(expectedFullProduct, actualFullProduct);
                        }
                    }
                }
                ProductInfo similarProductInfoBean = new Search();
                ProductInfo similarProductInfoBeanResult = similarProductInfoBean.SimilaritesSearch("ASIN1", "1");
                if (similarProductInfoBeanResult != null && similarProductInfoBeanResult.getProductsArrayList() != null && similarProductInfoBeanResult.getProductsArrayList().size() > 0) {
                    List<MiniProduct> similarDetails = new ArrayList<>(similarProductInfoBeanResult.getProductsArrayList());
                    for (MiniProduct similarDetail : similarDetails) {
                        MiniProduct miniProduct = new MiniProduct();
                        miniProduct.setAsin(similarDetail.getAsin());
                        miniProduct.setImageURL(similarDetail.getImageUrlSmall());
                        miniProduct.setManufacturer(similarDetail.getManufacturer());
                        miniProduct.setName(similarDetail.getProductName());
                        miniProduct.setPrice(similarDetail.getOurPrice());
                        expectedFullProduct.addSimilarItem(miniProduct);
                    }
                    FullProduct actualFullProduct = product.getProduct("ASIN1", "offer1", "page1");
                    assertEquals(expectedFullProduct, actualFullProduct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}