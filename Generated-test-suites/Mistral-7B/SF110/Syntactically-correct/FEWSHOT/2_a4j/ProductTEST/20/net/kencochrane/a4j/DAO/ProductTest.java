package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.beans.*;
import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;

public class ProductTest {
    @Test
    public void testGetProduct() {
        final String asin = "B08N5CWG81";
        final String offer = "new";
        final String page = "1";
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        FullProduct actualFullProduct = product.getProduct(asin, offer, page);

        assertNotNull(actualFullProduct);

        if (expectedFullProduct != null) {
            assertEquals(expectedFullProduct.getDetails(), actualFullProduct.getDetails());
            assertEquals(expectedFullProduct.getAccessories(), actualFullProduct.getAccessories());
            assertEquals(expectedFullProduct.getSimilarItems(), actualFullProduct.getSimilarItems());
        }
    }

    @Test
    public void testGetProductNullAsin() {
        Product product = new Product();
        FullProduct actualFullProduct = product.getProduct(null, "new", "1");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProductNullOffer() {
        Product product = new Product();
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", null, "1");

        assertNull(actualFullProduct);
    }

    @Test
    public void testGetProductNullPage() {
        Product product = new Product();
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", null);

        assertNull(actualFullProduct);
    }

    @Test
    public void testFetchASINFileReturnsNull() {
        final FileUtil fileUtil = new FileUtil();
        Product product = new Product();
        FileInputStream inputStream = fileUtil.fetchASINFile("B08N5CWG81", "heavy", "new", "1");
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNull(actualFullProduct);
        assertNull(inputStream);
    }

    @Test
    public void testProductDetailsIsNull() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        fullProduct.setDetails(null);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNull(actualFullProduct);
    }

    @Test
    public void testAccessoriesIsNull() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        fullProduct.setAccessories(null);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNull(actualFullProduct);
    }

    @Test
    public void testSimilarItemsIsNull() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        fullProduct.setSimilarItems(null);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNotNull(actualFullProduct);
        assertEquals(0, actualFullProduct.getSimilarItems().size());
    }

    @Test
    public void testGetAccessoryArrayIsEmpty() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        List<ProductDetails> detailsArray = new ArrayList<>();
        fullProduct.setAccessories(new Accessories());
        fullProduct.getAccessories().setAccessoryArray(detailsArray);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNotNull(actualFullProduct);
        assertEquals(0, actualFullProduct.getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testGetProductsWithAccessories() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        List<ProductDetails> detailsArray = new ArrayList<>();
        ProductDetails productDetails1 = new ProductDetails();
        ProductDetails productDetails2 = new ProductDetails();
        MiniProduct miniProduct1 = new MiniProduct();
        MiniProduct miniProduct2 = new MiniProduct();
        expectedFullProduct.setDetails(productDetails1);
        productDetails1.setAccessories(new Accessories());
        productDetails1.getAccessories().setAccessoryArray(detailsArray);
        detailsArray.add(productDetails2);
        productDetails2.setAsin("B08N5CWG82");
        productDetails2.setImageUrlSmall("imageUrl1");
        miniProduct1 = new MiniProduct();
        miniProduct1.setAsin("B08N5CWG83");
        miniProduct1.setImageURL("imageUrl2");
        miniProduct1.setManufacturer("manufacturer1");
        miniProduct1.setName("name1");
        miniProduct1.setPrice(10.0);
        productDetails2.setProduct(miniProduct1);
        miniProduct2 = new MiniProduct();
        miniProduct2.setAsin("B08N5CWG84");
        miniProduct2.setImageURL("imageUrl3");
        miniProduct2.setManufacturer("manufacturer2");
        miniProduct2.setName("name2");
        miniProduct2.setPrice(20.0);
        productDetails2.setProduct(miniProduct2);
        expectedFullProduct.getAccessories().setAccessoryArray(detailsArray);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNotNull(actualFullProduct);
        assertEquals(2, actualFullProduct.getAccessories().getAccessoryArray().size());
        MiniProduct accessory1 = actualFullProduct.getAccessories().getAccessoryArray().get(0);
        MiniProduct accessory2 = actualFullProduct.getAccessories().getAccessoryArray().get(1);

        assertEquals(productDetails1.getAsin(), accessory1.getAsin());
        assertEquals(productDetails1.getImageUrlSmall(), accessory1.getImageURL());
        assertEquals(productDetails1.getManufacturer(), accessory1.getManufacturer());
        assertEquals(productDetails1.getProductName(), accessory1.getName());
        assertEquals(productDetails1.getOurPrice(), accessory1.getPrice());

        assertEquals(productDetails2.getAsin(), accessory2.getAsin());
        assertEquals(productDetails2.getImageUrlSmall(), accessory2.getImageURL());
        assertEquals(productDetails2.getManufacturer(), accessory2.getManufacturer());
        assertEquals(productDetails2.getProductName(), accessory2.getName());
        assertEquals(productDetails2.getOurPrice(), accessory2.getPrice());
    }

    @Test
    public void testGetProductsWithNoAccessories() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        List<ProductDetails> detailsArray = new ArrayList<>();
        ProductDetails productDetails = new ProductDetails();
        expectedFullProduct.setDetails(productDetails);
        productDetails.setAccessories(null);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNotNull(actualFullProduct);
        assertEquals(0, actualFullProduct.getAccessories().getAccessoryArray().size());
    }

    @Test
    public void testSimilarItemsIsEmpty() {
        Product product = new Product();
        FullProduct fullProduct = new FullProduct();
        List<ProductDetails> detailsArray = new ArrayList<>();
        fullProduct.setSimilarItems(detailsArray);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNotNull(actualFullProduct);
        assertEquals(0, actualFullProduct.getSimilarItems().size());
    }

    @Test
    public void testGetProductsWithSimilarItems() {
        Product product = new Product();
        FullProduct expectedFullProduct = new FullProduct();
        List<ProductDetails> detailsArray = new ArrayList<>();
        ProductDetails productDetails1 = new ProductDetails();
        ProductDetails productDetails2 = new ProductDetails();
        MiniProduct miniProduct1 = new MiniProduct();
        MiniProduct miniProduct2 = new MiniProduct();
        expectedFullProduct.setSimilarItems(detailsArray);
        detailsArray.add(productDetails1);
        productDetails1.setAsin("B08N5CWG82");
        productDetails1.setImageUrlSmall("imageUrl1");
        miniProduct1 = new MiniProduct();
        miniProduct1.setAsin("B08N5CWG83");
        miniProduct1.setImageURL("imageUrl2");
        miniProduct1.setManufacturer("manufacturer1");
        miniProduct1.setName("name1");
        miniProduct1.setPrice(10.0);
        productDetails1.setProduct(miniProduct1);
        miniProduct2 = new MiniProduct();
        miniProduct2.setAsin("B08N5CWG84");
        miniProduct2.setImageURL("imageUrl3");
        miniProduct2.setManufacturer("manufacturer2");
        miniProduct2.setName("name2");
        miniProduct2.setPrice(20.0);
        productDetails2.setProduct(miniProduct2);
        expectedFullProduct.setSimilarItems(detailsArray);
        FullProduct actualFullProduct = product.getProduct("B08N5CWG81", "new", "1");

        assertNotNull(actualFullProduct);
        assertEquals(2, actualFullProduct.getSimilarItems().size());
        MiniProduct similarItem1 = actualFullProduct.getSimilarItems().get(0);
        MiniProduct similarItem2 = actualFullProduct.getSimilarItems().get(1);

        assertEquals(productDetails1.getAsin(), similarItem1.getAsin());
        assertEquals(productDetails1.getImageUrlSmall(), similarItem1.getImageURL());
        assertEquals(productDetails1.getManufacturer(), similarItem1.getManufacturer());
        assertEquals(productDetails1.getProductName(), similarItem1.getName());
        assertEquals(productDetails1.getOurPrice(), similarItem1.getPrice());

        assertEquals(productDetails2.getAsin(), similarItem2.getAsin());
        assertEquals(productDetails2.getImageUrlSmall(), similarItem2.getImageURL());
        assertEquals(productDetails2.getManufacturer(), similarItem2.getManufacturer());
        assertEquals(productDetails2.getProductName(), similarItem2.getName());
        assertEquals(productDetails2.getOurPrice(), similarItem2.getPrice());
    }
}