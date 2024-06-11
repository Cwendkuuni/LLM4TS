package net.kencochrane.a4j.DAO;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.file.Search;

public class ProductTest {

    @Test
    public void testGetProduct_NullInputs() {
        FileUtil fileUtil = mock(FileUtil.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        Product product = new Product();
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProduct_ValidInputs() throws IOException {
        FileUtil fileUtil = mock(FileUtil.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));
        when(fileUtil.fetchAccessories(anyString(), anyList())).thenReturn(new FileInputStream("path/to/accessories/file"));
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[0]);
        when(productInfoBean.getProductsArrayList()).thenReturn(new ArrayList<>());
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
    }

    @Test
    public void testGetProduct_NullProductInfoBean() {
        FileUtil fileUtil = mock(FileUtil.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));
        Product product = new Product();
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProduct_NoDetails() {
        FileUtil fileUtil = mock(FileUtil.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));
        when(productInfoBean.getDetails()).thenReturn(null);
        Product product = new Product();
        assertNull(product.getProduct("asin", "offer", "page"));
    }

    @Test
    public void testGetProduct_AccessoriesPresent() throws IOException {
        FileUtil fileUtil = mock(FileUtil.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        Accessories accessories = mock(Accessories.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));
        when(fileUtil.fetchAccessories(anyString(), anyList())).thenReturn(new FileInputStream("path/to/accessories/file"));
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[]{new ProductDetails()});
        when(productInfoBean.getProductsArrayList()).thenReturn(new ArrayList<>());
        when(productInfoBean.getProductsArrayList().size()).thenReturn(1);
        when(accessories.getAccessoryArray()).thenReturn(new ArrayList<>());
        when(accessories.getAccessoryArray().size()).thenReturn(1);
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getAccessories());
        List<MiniProduct> accessoriesList = fullProduct.getAccessories().getAccessoryArray();
        assertEquals(1, accessoriesList.size());
    }

    @Test
    public void testGetProduct_SimilarItemsPresent() throws IOException {
        FileUtil fileUtil = mock(FileUtil.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        Search search = mock(Search.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[]{new ProductDetails()});
        when(productInfoBean.getProductsArrayList()).thenReturn(new ArrayList<>());
        when(productInfoBean.getProductsArrayList().size()).thenReturn(1);
        when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        List<MiniProduct> similarItemsList = fullProduct.getSimilarItems();
        assertEquals(1, similarItemsList.size());
    }

    @Test
    public void testGetProduct_BothAccessoriesAndSimilarItemsPresent() throws IOException {
        FileUtil fileUtil = mock(FileUtil.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        Accessories accessories = mock(Accessories.class);
        Search search = mock(Search.class);
        when(fileUtil.fetchASINFile(anyString(), anyString(), anyString(), anyString())).thenReturn(new FileInputStream("path/to/file"));
        when(fileUtil.fetchAccessories(anyString(), anyList())).thenReturn(new FileInputStream("path/to/accessories/file"));
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[]{new ProductDetails()});
        when(productInfoBean.getProductsArrayList()).thenReturn(new ArrayList<>());
        when(productInfoBean.getProductsArrayList().size()).thenReturn(1);
        when(productInfoBean.getAccessories()).thenReturn(accessories);
        when(accessories.getAccessoryArray()).thenReturn(new ArrayList<>());
        when(accessories.getAccessoryArray().size()).thenReturn(1);
        when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(productInfoBean);
        Product product = new Product();
        FullProduct fullProduct = product.getProduct("asin", "offer", "page");
        assertNotNull(fullProduct);
        assertNotNull(fullProduct.getAccessories());
        List<MiniProduct> accessoriesList = fullProduct.getAccessories().getAccessoryArray();
        assertEquals(1, accessoriesList.size());
        assertNotNull(fullProduct.getSimilarItems());
        List<MiniProduct> similarItemsList = fullProduct.getSimilarItems();
        assertEquals(1, similarItemsList.size());
    }
}