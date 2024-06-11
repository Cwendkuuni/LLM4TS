package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.Search;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProductTest {

    @Test
    public void testGetProduct_NullInputs() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProduct_NullFile() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = new FileInputStream(new File("nonexistentfile.xml"));
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullProductInfoBean() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_EmptyDetailsArray() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullAccessoriesArray() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullProductsArrayList() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullAccessoryProductDetails() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullMiniProduct() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(new ProductDetails());
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_EmptyAccessoryArray() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullSearchBean() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(accessoryProductDetails);
        productDetails.setAsin("accessoryASIN");
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_EmptySimilarItemsArrayList() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(accessoryProductDetails);
        productDetails.setAsin("accessoryASIN");
        Search search = new Search();
        productInfoBean.setProductsArrayList(new ArrayList());
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_NullMiniProductInSimilarItemsArrayList() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = null;
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(accessoryProductDetails);
        productDetails.setAsin("accessoryASIN");
        Search search = new Search();
        ArrayList detailsArray = new ArrayList();
        ProductInfo similarProductInfoBean = new ProductInfo();
        ProductDetails similarProductDetails = new ProductDetails();
        MiniProduct similarMiniProduct = new MiniProduct();
        detailsArray.add(similarProductDetails);
        similarProductDetails.setProductsArrayList(new ArrayList());
        similarProductDetails.setAsin("similarProductASIN");
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        productInfoBean.setProductsArrayList(detailsArray);
        assertNull(product.getProduct("asin", null, null, fileIn));
        fileIn.close();
    }

    @Test
    public void testGetProduct_ValidInputs() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        ProductInfo productInfoBean = new ProductInfo();
        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();
        productDetails.setAccessories(accessories);
        accessories.setAccessoryArray(accessoryArray);
        accessoryArray.add(accessoryProductDetails);
        accessoryProductDetails.setAsin("accessoryASIN");
        accessoryProductDetails.setImageUrlSmall("accessoryImageURL");
        accessoryProductDetails.setManufacturer("accessoryManufacturer");
        accessoryProductDetails.setProductName("accessoryProductName");
        accessoryProductDetails.setOurPrice(new BigDecimal("accessoryPrice"));
        productDetails.setAsin("productASIN");
        productDetails.setImageUrlSmall("productImageURL");
        productDetails.setManufacturer("productManufacturer");
        productDetails.setProductName("productName");
        productDetails.setOurPrice(new BigDecimal("productPrice"));
        Search search = new Search();
        ArrayList detailsArray = new ArrayList();
        ProductInfo similarProductInfoBean = new ProductInfo();
        ProductDetails similarProductDetails = new ProductDetails();
        MiniProduct similarMiniProduct = new MiniProduct();
        detailsArray.add(similarProductDetails);
        similarProductDetails.setAsin("similarProductASIN");
        similarProductDetails.setImageUrlSmall("similarProductImageURL");
        similarProductDetails.setManufacturer("similarProductManufacturer");
        similarProductDetails.setProductName("similarProductName");
        similarProductDetails.setOurPrice(new BigDecimal("similarProductPrice"));
        productInfoBean.setDetails(new ProductDetails[]{productDetails});
        productInfoBean.setProductsArrayList(detailsArray);
        product.getProduct("asin", null, null, fileIn);
        assertTrue(product.getProduct(null, null, null) instanceof FullProduct);
        FileInputStream accessoryFileIn = fileUtil.fetchAccessories("asin", accessoryArray);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        ArrayList accessoryDetailsArray = new ArrayList();
        ProductDetails accessoryProductDetails = null;
        for (int i = 0; i < accessoryArray.size(); i++) {
            accessoryProductDetails = new ProductDetails();
            MiniProduct accessoryMiniProduct = new MiniProduct();
            accessoryProductDetails.setAsin("accessoryASIN" + i);
            accessoryProductDetails.setImageUrlSmall("accessoryImageURL" + i);
            accessoryProductDetails.setManufacturer("accessoryManufacturer" + i);
            accessoryProductDetails.setProductName("accessoryProductName" + i);
            accessoryProductDetails.setOurPrice(new BigDecimal("accessoryPrice" + i));
            accessoryDetailsArray.add(accessoryProductDetails);
            accessoryMiniProduct.setAsin(accessoryProductDetails.getAsin());
            accessoryMiniProduct.setImageURL(accessoryProductDetails.getImageUrlSmall());
            accessoryMiniProduct.setManufacturer(accessoryProductDetails.getManufacturer());
            accessoryMiniProduct.setName(accessoryProductDetails.getProductName());
            accessoryMiniProduct.setPrice(accessoryProductDetails.getOurPrice());
            product.addAccessory(accessoryMiniProduct);
        }
        productInfoBean = search.SimilaritesSearch("asin", "1");
        assertNotNull(productInfoBean);
        ArrayList similarProductsArrayList = productInfoBean.getProductsArrayList();
        for (int i = 0; i < similarProductsArrayList.size(); i++) {
            similarProductDetails = (ProductDetails)similarProductsArrayList.get(i);
            MiniProduct similarMiniProduct = new MiniProduct();
            similarMiniProduct.setAsin(similarProductDetails.getAsin());
            similarMiniProduct.setImageURL(similarProductDetails.getImageUrlSmall());
            similarMiniProduct.setManufacturer(similarProductDetails.getManufacturer());
            similarMiniProduct.setName(similarProductDetails.getProductName());
            similarMiniProduct.setPrice(similarProductDetails.getOurPrice());
            product.addSimilarItem(similarMiniProduct);
        }
        assertEquals(product.getDetails().getAsin(), "productASIN");
        assertEquals(product.getDetails().getImageUrlSmall(), "productImageURL");
        assertEquals(product.getDetails().getManufacturer(), "productManufacturer");
        assertEquals(product.getDetails().getProductName(), "productName");
        assertEquals(product.getDetails().getOurPrice(), new BigDecimal("productPrice"));
        assertEquals(product.getAccessories().getAccessoryArray().size(), accessoryArray.size());
        for (int i = 0; i < product.getAccessories().getAccessoryArray().size(); i++) {
            assertEquals(product.getAccessories().getAccessoryArray().get(i).getAsin(), "accessoryASIN" + i);
            assertEquals(product.getAccessories().getAccessoryArray().get(i).getImageURL(), "accessoryImageURL" + i);
            assertEquals(product.getAccessories().getAccessoryArray().get(i).getManufacturer(), "accessoryManufacturer" + i);
            assertEquals(product.getAccessories().getAccessoryArray().get(i).getName(), "accessoryProductName" + i);
            assertEquals(product.getAccessories().getAccessoryArray().get(i).getPrice(), new BigDecimal("accessoryPrice" + i));
        }
        assertEquals(product.getSimilarItems().size(), similarProductsArrayList.size());
        for (int i = 0; i < product.getSimilarItems().size(); i++) {
            assertEquals(product.getSimilarItems().get(i).getAsin(), "similarProductASIN" + i);
            assertEquals(product.getSimilarItems().get(i).getImageURL(), "similarProductImageURL" + i);
            assertEquals(product.getSimilarItems().get(i).getManufacturer(), "similarProductManufacturer" + i);
            assertEquals(product.getSimilarItems().get(i).getName(), "similarProductName" + i);
            assertEquals(product.getSimilarItems().get(i).getPrice(), new BigDecimal("similarProductPrice" + i));
        }
        fileIn.close();
        accessoryFileIn.close();
    }

    @Test
    public void testGetProduct_ThrowsException() {
        FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream fileIn = fileUtil.fetchASINFile("asin", "heavy", null, null);
        try {
            product.getProduct("asin", null, null, fileIn);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            //expected
        } finally {
            fileIn.close();
        }
    }
}