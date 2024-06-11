package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.ProductDetails;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.utils.JOXBeanInputStream;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    Product product;
    FileUtil fileUtil;
    FullProduct expectedFullProduct;

    @Before
    public void setUp() {
        product = new Product();
        fileUtil = new FileUtil();
        expectedFullProduct = new FullProduct();
    }

    @Test
    public void testGetProduct_ValidInput_ReturnsExpectedProduct() throws IOException, ClassNotFoundException {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/validASIN.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        expectedFullProduct.setDetails(productInfoBean.getDetails()[0]);
        expectedFullProduct.addAccessory(new MiniProduct());
        expectedFullProduct.addSimilarItem(new MiniProduct());

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertEquals(expectedFullProduct.getDetails().getProductName(), actualFullProduct.getDetails().getProductName());
        assertEquals(1, actualFullProduct.getAccessories().size());
        assertEquals(1, actualFullProduct.getSimilarItems().size());
    }

    @Test
    public void testGetProduct_NullInputFile_ReturnsNull() {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean_ReturnsNull() {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/emptyASIN.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();

        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        inputStream.close();

        File accessoryInputFile = new File("src/test/resources/inputFiles/emptyASIN.xml");
        InputStream accessoryInputStream = new FileInputStream(accessoryInputFile);

        ProductInfo accessoryProductInfoBean = new ProductInfo();
        accessoryProductInfoBean = (ProductInfo) new JOXBeanInputStream(accessoryInputStream).readObject(ProductInfo.class);
        accessoryInputStream.close();

        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(inputFile);
        when(fileUtil.fetchAccessories(asin, Arrays.asList(new String[]{}))).thenReturn(accessoryInputFile);

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtil).fetchAccessories(asin, Arrays.asList(new String[]{}));

        assertNull(actualFullProduct);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProduct_NullProductDetails_ThrowsNullPointerException() throws IOException, ClassNotFoundException {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/validASINWithNullDetails.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        inputStream.close();

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        fail("Expected NullPointerException, but got " + actualFullProduct);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProduct_NullAccessoriesArray_ThrowsNullPointerException() throws IOException, ClassNotFoundException {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/validASINWithEmptyAccessories.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        inputStream.close();

        File accessoryInputFile = new File("src/test/resources/inputFiles/emptyASIN.xml");
        InputStream accessoryInputStream = new FileInputStream(accessoryInputFile);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        accessoryProductInfoBean = (ProductInfo) new JOXBeanInputStream(accessoryInputStream).readObject(ProductInfo.class);
        accessoryInputStream.close();

        ArrayList<String> accessoryArray = new ArrayList<>();

        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(inputFile);
        when(fileUtil.fetchAccessories(asin, accessoryArray)).thenReturn(accessoryInputFile);

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtil).fetchAccessories(asin, accessoryArray);

        assertNotNull(actualFullProduct);
        assertEquals(0, actualFullProduct.getAccessories().size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetProduct_NullAccessoryProductDetails_ThrowsNullPointerException() throws IOException, ClassNotFoundException {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/validASIN.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        inputStream.close();

        File accessoryInputFile = new File("src/test/resources/inputFiles/validASINWithNullAccessory.xml");
        InputStream accessoryInputStream = new FileInputStream(accessoryInputFile);
        ProductInfo accessoryProductInfoBean = new ProductInfo();
        accessoryProductInfoBean = (ProductInfo) new JOXBeanInputStream(accessoryInputStream).readObject(ProductInfo.class);
        accessoryInputStream.close();

        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        ProductDetails accessoryProductDetails = new ProductDetails();
        MiniProduct miniProduct = new MiniProduct();

        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(inputFile);
        when(fileUtil.fetchAccessories(asin, Arrays.asList(new String[]{"accessory1"}))).thenReturn(accessoryInputFile);

        when(productInfoBean.getDetails()).thenReturn(detailsArray.toArray(new ProductDetails[0]));
        when(accessoryProductInfoBean.getProductsArrayList()).thenReturn(Collections.singletonList(accessoryProductDetails));

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtil).fetchAccessories(asin, Arrays.asList(new String[]{"accessory1"}));

        assertEquals(1, actualFullProduct.getAccessories().size());
        assertNull(actualFullProduct.getAccessories().get(0));
    }

    @Test
    public void testGetProduct_MultipleAccessories_ReturnsExpectedProductWithMultipleAccessories() throws IOException, ClassNotFoundException {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/validASIN.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        inputStream.close();

        File accessoryInputFile1 = new File("src/test/resources/inputFiles/accessory1.xml");
        InputStream accessoryInputStream1 = new FileInputStream(accessoryInputFile1);
        ProductInfo accessoryProductInfoBean1 = new ProductInfo();
        accessoryProductInfoBean1 = (ProductInfo) new JOXBeanInputStream(accessoryInputStream1).readObject(ProductInfo.class);
        accessoryInputStream1.close();

        File accessoryInputFile2 = new File("src/test/resources/inputFiles/accessory2.xml");
        InputStream accessoryInputStream2 = new FileInputStream(accessoryInputFile2);
        ProductInfo accessoryProductInfoBean2 = new ProductInfo();
        accessoryProductInfoBean2 = (ProductInfo) new JOXBeanInputStream(accessoryInputStream2).readObject(ProductInfo.class);
        accessoryInputStream2.close();

        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        ProductDetails productDetail = new ProductDetails();
        productDetail.setProductName("Product1");
        detailsArray.add(productDetail);

        ProductDetails accessoryProductDetail1 = new ProductDetails();
        accessoryProductDetail1.setProductName("Accessory1");
        ProductInfo accessoryProductInfoBean1 = new ProductInfo();
        accessoryProductInfoBean1.setProductsArrayList(Arrays.asList(accessoryProductDetail1));
        when(productInfoBean.getDetails()).thenReturn(detailsArray.toArray(new ProductDetails[0]));
        when(accessoryProductInfoBean1.getProductsArrayList()).thenReturn(accessoryProductInfoBean1.getProductsArrayList());
        when(fileUtil.fetchAccessories(asin, Arrays.asList(new String[]{"accessory1", "accessory2"}))).thenReturn(accessoryInputFile1, accessoryInputFile2);

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
        verify(fileUtil).fetchAccessories(asin, Arrays.asList(new String[]{"accessory1", "accessory2"}));

        assertEquals(2, actualFullProduct.getAccessories().size());
        assertEquals("Accessory1", actualFullProduct.getAccessories().get(0).getName());
        assertEquals("Accessory2", actualFullProduct.getAccessories().get(1).getName());
    }

    @Test
    public void testGetProduct_SimilarItems_ReturnsExpectedProductWithSimilarItems() throws IOException, ClassNotFoundException {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/validASIN.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        inputStream.close();

        File similarItemsInputFile = new File("src/test/resources/inputFiles/validASINWithSimilarItems.xml");
        InputStream similarItemsInputStream = new FileInputStream(similarItemsInputFile);
        ProductInfo similarItemsProductInfoBean = new ProductInfo();
        similarItemsProductInfoBean = (ProductInfo) new JOXBeanInputStream(similarItemsInputStream).readObject(ProductInfo.class);
        similarItemsInputStream.close();

        ArrayList<ProductDetails> detailsArray = new ArrayList<>();
        ProductDetails productDetail = new ProductDetails();
        productDetail.setProductName("Product1");
        detailsArray.add(productDetail);

        List<MiniProduct> expectedAccessories = Arrays.asList(new MiniProduct(), new MiniProduct());
        FullProduct expectedFullProduct = new FullProduct(detailsArray.toArray(new ProductDetails[0]), expectedAccessories);

        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(inputFile);
        when(search.SimilaritesSearch(asin, "1")).thenReturn(similarItemsProductInfoBean);

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
        verify(search).SimilaritesSearch(asin, "1");

        assertEquals(expectedFullProduct.getDetails().getProductName(), actualFullProduct.getDetails().getProductName());
        assertEquals(expectedFullProduct.getAccessories().size(), actualFullProduct.getAccessories().size());
        assertEquals(expectedFullProduct.getSimilarItems().get(0).getAsin(), actualFullProduct.getSimilarItems().get(0).getAsin());
        assertEquals(expectedFullProduct.getSimilarItems().get(0).getName(), actualFullProduct.getSimilarItems().get(0).getName());
        assertEquals(expectedFullProduct.getSimilarItems().get(1).getAsin(), actualFullProduct.getSimilarItems().get(1).getAsin());
        assertEquals(expectedFullProduct.getSimilarItems().get(1).getName(), actualFullProduct.getSimilarItems().get(1).getName());
    }

    @Test
    public void testGetProduct_EmptySimilarItems_ReturnsExpectedProductWithEmptySimilarItemsList() throws IOException, ClassNotFoundException {
        String asin = "B08N5L1X9W";
        String offer = "New";
        String page = "1";

        File inputFile = new File("src/test/resources/inputFiles/validASIN.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        ProductInfo productInfoBean = new ProductInfo();
        productInfoBean = (ProductInfo) new JOXBeanInputStream(inputStream).readObject(ProductInfo.class);
        inputStream.close();

        when(fileUtil.fetchASINFile(asin, "heavy", offer, page)).thenReturn(inputFile);
        when(search.SimilaritesSearch(asin, "1")).thenReturn(new ProductInfo());

        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        verify(fileUtil).fetchASINFile(asin, "heavy", offer, page);
        verify(search).SimilaritesSearch(asin, "1");

        assertEquals(0, actualFullProduct.getSimilarItems().size());
    }
}