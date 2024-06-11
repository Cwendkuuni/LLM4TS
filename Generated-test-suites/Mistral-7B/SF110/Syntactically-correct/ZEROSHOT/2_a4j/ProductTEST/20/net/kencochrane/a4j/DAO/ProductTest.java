package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.utils.Search;

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
    public void testGetProductWithValidInputs() throws IOException {
        String asin = "B07NX985DJ";
        String offer = "new";
        String page = "1";

        ProductInfo productInfoBean = createProductInfoBean();
        byte[] fileContent = serializeObjectToByteArray(productInfoBean);
        FileInputStream fileIn = new FileInputStream(new ByteArrayInputStream(fileContent));

        FileInputStream inputFile = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(inputFile);
        ProductInfo expectedProductInfoBean = joxIn.readObject(ProductInfo.class);

        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedProductInfoBean.getDetails()[0], fullProduct.getDetails());
        assertEquals(1, fullProduct.getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProductWithNullInputs() {
        String asin = null;
        String offer = null;
        String page = null;

        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProductWithEmptyFile() throws IOException {
        String asin = "B07NX985DJ";
        String offer = "new";
        String page = "1";

        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        if (fileIn != null) {
            fileIn.close();
        }

        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProductWithInvalidProductInfoBean() throws IOException, ClassNotFoundException {
        String asin = "B07NX985DJ";
        String offer = "new";
        String page = "1";

        FileInputStream fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        if (fileIn != null) {
            fileIn.close();
        }

        ProductInfo productInfoBean = new ProductInfo();
        byte[] fileContent = serializeObjectToByteArray(productInfoBean);
        FileInputStream inputFile = new FileInputStream(new ByteArrayInputStream(fileContent));

        JOXBeanInputStream joxIn = new JOXBeanInputStream(inputFile);
        ProductInfo actualProductInfoBean = joxIn.readObject(ProductInfo.class);

        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertNull(fullProduct);
    }

    @Test
    public void testGetProductWithEmptyAccessoryArray() throws IOException {
        String asin = "B07NX985DJ";
        String offer = "new";
        String page = "1";

        ProductInfo productInfoBean = createProductInfoBean();
        byte[] fileContent = serializeObjectToByteArray(productInfoBean);
        FileInputStream fileIn = new FileInputStream(new ByteArrayInputStream(fileContent));

        FileInputStream inputFile = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(inputFile);
        ProductInfo expectedProductInfoBean = joxIn.readObject(ProductInfo.class);

        Accessories accessories = expectedProductInfoBean.getDetails()[0].getAccessories();
        accessories.setAccessoryArray(new ArrayList<>());

        byte[] accessoryFileContent = serializeObjectToByteArray(accessories);
        FileInputStream accessoryFileIn = new FileInputStream(new ByteArrayInputStream(accessoryFileContent));

        ProductInfo actualProductInfoBean = joxIn.readObject(ProductInfo.class);

        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedProductInfoBean.getDetails()[0], fullProduct.getDetails());
        assertEquals(0, fullProduct.getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProductWithEmptySimilarItems() throws IOException {
        String asin = "B07NX985DJ";
        String offer = "new";
        String page = "1";

        ProductInfo productInfoBean = createProductInfoBean();
        byte[] fileContent = serializeObjectToByteArray(productInfoBean);
        FileInputStream fileIn = new FileInputStream(new ByteArrayInputStream(fileContent));

        FileInputStream inputFile = fileUtil.fetchASINFile(asin, "heavy", offer, page);
        JOXBeanInputStream joxIn = new JOXBeanInputStream(inputFile);
        ProductInfo expectedProductInfoBean = joxIn.readObject(ProductInfo.class);

        expectedProductInfoBean.setProductsArrayList(new ArrayList<>());

        byte[] fileContentWithoutSimilarItems = serializeObjectToByteArray(expectedProductInfoBean);
        FileInputStream fileInWithoutSimilarItems = new FileInputStream(new ByteArrayInputStream(fileContentWithoutSimilarItems));

        ProductInfo actualProductInfoBean = joxIn.readObject(ProductInfo.class);

        FullProduct fullProduct = product.getProduct(asin, offer, page);
        assertEquals(expectedProductInfoBean.getDetails()[0], fullProduct.getDetails());
        assertEquals(0, fullProduct.getSimilarItems().size());
    }

    private ProductInfo createProductInfoBean() {
        ProductDetails productDetails = new ProductDetails();
        productDetails.setAccessories(new Accessories());
        productDetails.setDetails(new String[]{"Details 1"});

        Accessories accessories = new Accessories();
        ArrayList accessoryArray = new ArrayList();
        MiniProduct miniProduct = new MiniProduct();
        miniProduct.setAsin("ASIN1");
        miniProduct.setImageURL("URL1");
        miniProduct.setManufacturer("Manufacturer1");
        miniProduct.setName("Name1");
        miniProduct.setPrice(10.0);
        accessoryArray.add(miniProduct);

        accessories.setAccessoryArray(accessoryArray);
        productDetails.setAccessories(accessories);

        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean.setDetails(new ProductDetails[] { productDetails });

        return productInfoBean;
    }

    private byte[] serializeObjectToByteArray(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }
}