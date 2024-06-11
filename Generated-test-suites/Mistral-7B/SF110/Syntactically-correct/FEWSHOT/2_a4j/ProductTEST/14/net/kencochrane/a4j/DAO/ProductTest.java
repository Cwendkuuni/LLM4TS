package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.file.*;
import net.kencochrane.a4j.beans.*;

public class ProductTest {

    @Test
    public void testGetProduct_NullInputs() {
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_EmptyFile() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        assertNull(product.getProduct("asin", null, null));
    }

    @Test
    public void testGetProduct_ValidInputs_NoAccessories() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FullProduct expectedProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        search search = new search();
        ProductInfo productInfoBean = new ProductInfo();
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();
            }
            if (productInfoBean != null) {
                productDetails = productInfoBean.getDetails()[0];
                expectedProduct.setDetails(productDetails);
                ProductInfo productInfoBeanFromSearch = search.SimilaritesSearch("asin", "1");
                List<MiniProduct> similarItems = new ArrayList<>();
                if (productInfoBeanFromSearch != null && productInfoBeanFromSearch.getProductsArrayList() != null) {
                    for (Object o : productInfoBeanFromSearch.getProductsArrayList()) {
                        ProductDetails accessoryProductDetails = (ProductDetails) o;
                        MiniProduct miniProductFromSearch = new MiniProduct();
                        miniProductFromSearch.setAsin(accessoryProductDetails.getAsin());
                        miniProductFromSearch.setImageURL(accessoryProductDetails.getImageUrlSmall());
                        miniProductFromSearch.setManufacturer(accessoryProductDetails.getManufacturer());
                        miniProductFromSearch.setName(accessoryProductDetails.getProductName());
                        miniProductFromSearch.setPrice(accessoryProductDetails.getOurPrice());
                        similarItems.add(miniProductFromSearch);
                    }
                }
                expectedProduct.setSimilarItems(similarItems.toArray(new MiniProduct[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FullProduct actualProduct = product.getProduct("asin", null, null);
        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getDetails().getClass(), actualProduct.getDetails().getClass());
        assertEquals(expectedProduct.getSimilarItems().length, actualProduct.getSimilarItems().length);
    }

    @Test
    public void testGetProduct_ValidInputs_WithAccessories() {
        Product product = new Product();
        FileUtil fileUtil = new FileUtil();
        FullProduct expectedProduct = new FullProduct();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        ProductInfo productInfoBean = new ProductInfo();
        ProductInfo productInfoBeanFromAccessories = new ProductInfo();
        try {
            FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
            if (fileIn != null) {
                JOXBeanInputStream joxIn = new JOXBeanInputStream(fileIn);
                productInfoBean = (ProductInfo) joxIn.readObject(ProductInfo.class);
                joxIn.close();
                fileIn.close();
            }
            if (productInfoBean != null && productInfoBean.getDetails().length > 0) {
                productDetails = productInfoBean.getDetails()[0];
                expectedProduct.setDetails(productDetails);
                accessories = productDetails.getAccessories();
                accessoryArray = accessories.getAccessoryArray();
                if (accessoryArray != null && accessoryArray.size() > 0) {
                    FileInputStream fileInAccessories = fileUtil.fetchAccessories("asin", accessoryArray);
                    if (fileInAccessories != null) {
                        JOXBeanInputStream joxInAccessories = new JOXBeanInputStream(fileInAccessories);
                        productInfoBeanFromAccessories = (ProductInfo) joxInAccessories.readObject(ProductInfo.class);
                        joxInAccessories.close();
                        fileInAccessories.close();
                        if (productInfoBeanFromAccessories != null && productInfoBeanFromAccessories.getProductsArrayList() != null && productInfoBeanFromAccessories.getProductsArrayList().size() > 0) {
                            List<MiniProduct> accessoriesList = new ArrayList<>();
                            for (Object o : productInfoBeanFromAccessories.getProductsArrayList()) {
                                ProductDetails accessoryProductDetails = (ProductDetails) o;
                                MiniProduct miniProductAccessory = new MiniProduct();
                                miniProductAccessory.setAsin(accessoryProductDetails.getAsin());
                                miniProductAccessory.setImageURL(accessoryProductDetails.getImageUrlSmall());
                                miniProductAccessory.setManufacturer(accessoryProductDetails.getManufacturer());
                                miniProductAccessory.setName(accessoryProductDetails.getProductName());
                                miniProductAccessory.setPrice(accessoryProductDetails.getOurPrice());
                                accessoriesList.add(miniProductAccessory);
                            }
                            expectedProduct.setAccessories(accessoriesList.toArray(new MiniProduct[0]));
                        }
                    }
                }
                ProductInfo productInfoBeanFromSearch = search.SimilaritesSearch("asin", "1");
                List<MiniProduct> similarItems = new ArrayList<>();
                if (productInfoBeanFromSearch != null && productInfoBeanFromSearch.getProductsArrayList() != null) {
                    for (Object o : productInfoBeanFromSearch.getProductsArrayList()) {
                        ProductDetails accessoryProductDetails = (ProductDetails) o;
                        MiniProduct miniProductFromSearch = new MiniProduct();
                        miniProductFromSearch.setAsin(accessoryProductDetails.getAsin());
                        miniProductFromSearch.setImageURL(accessoryProductDetails.getImageUrlSmall());
                        miniProductFromSearch.setManufacturer(accessoryProductDetails.getManufacturer());
                        miniProductFromSearch.setName(accessoryProductDetails.getProductName());
                        miniProductFromSearch.setPrice(accessoryProductDetails.getOurPrice());
                        similarItems.add(miniProductFromSearch);
                    }
                }
                expectedProduct.setSimilarItems(similarItems.toArray(new MiniProduct[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FullProduct actualProduct = product.getProduct("asin", null, null);
        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getDetails().getClass(), actualProduct.getDetails().getClass());
        assertEquals(expectedProduct.getAccessories().length, actualProduct.getAccessories().length);
        assertEquals(expectedProduct.getSimilarItems().length, actualProduct.getSimilarItems().length);
    }

    @Test
    public void testGetProduct_Exception() {
        Product product = new Product();
        assertNull(product.getProduct("asin", null, null));
    }
}