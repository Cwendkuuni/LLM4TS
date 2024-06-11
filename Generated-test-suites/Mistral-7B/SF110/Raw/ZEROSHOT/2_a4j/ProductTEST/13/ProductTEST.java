package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.JOXBeanOutputStream;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.file.Search;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private Product product;
    private FileUtil fileUtil;
    private Search search;

    @Before
    public void setUp() {
        fileUtil = new FileUtil();
        search = new Search();
        product = new Product();
    }

    @Test
    public void testGetProductNullInput() {
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProductEmptyInput() {
        assertNull(product.getProduct("", "", ""));
    }

    @Test
    public void testGetProductNoFile() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        assertNull(product.getProduct(asin, offer, page));
    }

    @Test
    public void testGetProductNullBean() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        JOXBeanInputStream inputStream = new JOXBeanInputStream(new ByteArrayInputStream(new byte[0]));
        when(inputStream.readObject(ProductInfo.class)).thenReturn(null);

        assertNull(product.getProduct(asin, offer, page));
    }

    @Test
    public void testGetProductNoDetails() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        ProductInfo productInfoBean = new ProductInfo();
        when(inputStream.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        assertNull(product.getProduct(asin, offer, page));
    }

    @Test
    public void testGetProductNoAccessories() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);
        when(inputStream.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        assertNull(product.getProduct(asin, offer, page));
    }

    @Test
    public void testGetProductNoAccessoryFile() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);
        when(inputStream.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        File accessoryFile = new File("path/to/accessory/file");
        when(fileUtil.fetchAccessories(asin, Arrays.asList((Serializable)new String[]{"accessory1", "accessory2"}))).thenReturn(accessoryFile);

        JOXBeanInputStream accessoryInputStream = new JOXBeanInputStream(new ByteArrayInputStream(new byte[0]));
        when(accessoryInputStream.readObject(ProductInfo.class)).thenReturn(null);

        assertNull(product.getProduct(asin, offer, page));
    }

    @Test
    public void testGetProductNullAccessoryArray() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);
        when(inputStream.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        File accessoryFile = new File("path/to/accessory/file");
        when(fileUtil.fetchAccessories(asin, Arrays.asList((Serializable)new String[]{"accessory1", "accessory2"})))
                .thenReturn(accessoryFile);

        ProductInfo accessoryProductInfoBean = new ProductInfo();
        when(accessoryInputStream.readObject(ProductInfo.class)).thenReturn(accessoryProductInfoBean);

        assertNotNull(product.getProduct(asin, offer, page));
        assertEmpty(product.getProduct(asin, offer, page).getAccessories().getAccessoryArray());
    }

    @Test
    public void testGetProductNoSimilarItems() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[0]);
        when(inputStream.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        File accessoryFile = new File("path/to/accessory/file");
        when(fileUtil.fetchAccessories(asin, Arrays.asList((Serializable)new String[]{"accessory1", "accessory2"})))
                .thenReturn(accessoryFile);

        ProductInfo accessoryProductInfoBean = new ProductInfo();
        ProductInfo similarProductInfoBean = new ProductInfo();
        when(accessoryInputStream.readObject(ProductInfo.class)).thenReturn(accessoryProductInfoBean);
        when(inputStream.readObject(ProductInfo.class)).thenReturn(similarProductInfoBean);

        assertNotNull(product.getProduct(asin, offer, page));
        assertEmpty(product.getProduct(asin, offer, page).getSimilarItems());
    }

    @Test
    public void testGetProductWithAccessoriesAndSimilarItems() {
        String asin = "testASIN";
        String offer = "testOffer";
        String page = "testPage";

        ProductDetails productDetails = new ProductDetails();
        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct1 = new MiniProduct();
        MiniProduct miniProduct2 = new MiniProduct();

        accessoryArray.add("accessory1");
        accessoryArray.add("accessory2");

        File file = new File("path/to/file");
        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(file);

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[] { productDetails });
        when(inputStream.readObject(ProductInfo.class)).thenReturn(productInfoBean);

        File accessoryFile = new File("path/to/accessory/file");
        when(fileUtil.fetchAccessories(asin, accessoryArray)).thenReturn(accessoryFile);

        ProductInfo accessoryProductInfoBean = new ProductInfo();
        accessoryProductInfoBean.setProductsArrayList(Arrays.asList(miniProduct1, miniProduct2));
        when(accessoryInputStream.readObject(ProductInfo.class)).thenReturn(accessoryProductInfoBean);

        ProductInfo similarProductInfoBean = new ProductInfo();
        MiniProduct similarMiniProduct1 = new MiniProduct();
        MiniProduct similarMiniProduct2 = new MiniProduct();
        similarMiniProduct1.setAsin("similarItem1ASIN");
        similarMiniProduct2.setAsin("similarItem2ASIN");
        similarProductInfoBean.setProductsArrayList(Arrays.asList(similarMiniProduct1, similarMiniProduct2));

        when(search.SimilaritesSearch(asin, "1")).thenReturn(similarProductInfoBean);

        Product fullProduct = product.getProduct(asin, offer, page);

        assertNotNull(fullProduct);
        assertEquals(productDetails, fullProduct.getDetails());
        assertNotNull(fullProduct.getAccessories());
        assertNotEmpty(fullProduct.getAccessories().getAccessoryArray());
        assertEquals(miniProduct1, fullProduct.getAccessories().getAccessoryArray().get(0));
        assertEquals(miniProduct2, fullProduct.getAccessories().getAccessoryArray().get(1));
        assertNotNull(fullProduct.getSimilarItems());
        assertNotEmpty(fullProduct.getSimilarItems());
        assertEquals(similarMiniProduct1, fullProduct.getSimilarItems().get(0));
        assertEquals(similarMiniProduct2, fullProduct.getSimilarItems().get(1));
    }
}