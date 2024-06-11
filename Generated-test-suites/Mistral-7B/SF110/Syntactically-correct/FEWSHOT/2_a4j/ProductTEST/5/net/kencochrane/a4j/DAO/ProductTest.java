package net.kencochrane.a4j.DAO;

import static org.junit.Assert.*;
import org.junit.Test;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.file.FileUtilTest;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.Search;
import java.io.*;
import java.util.ArrayList;

public class ProductTest {

    @Test
    public void testGetProductNullFile() {
        final FileUtil fileUtil = new FileUtil();
        final Product product = new Product();
        assertNull(product.getProduct(null, null, null));
    }

    @Test
    public void testGetProductNullProductInfoBean() {
        final FileUtil fileUtil = new FileUtil();
        final FileInputStream fileIn = FileUtilTest.createFileMock();
        final Product product = new Product();
        when(fileUtil.fetchASINFile("asin", "heavy", null, null)).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        when(joxIn.readObject(ProductInfo.class)).thenReturn(null);
        when(fileIn).thenReturn(joxIn);
        assertNull(product.getProduct("asin", null, null));
    }

    @Test
    public void testGetProductEmptyDetailsArray() {
        final FileUtil fileUtil = new FileUtil();
        final FileInputStream fileIn = FileUtilTest.createFileMock();
        final Product product = new Product();
        when(fileUtil.fetchASINFile("asin", "heavy", null, null)).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[0]);
        when(fileIn).thenReturn(joxIn);
        assertNull(product.getProduct("asin", null, null));
    }

    @Test
    public void testGetProductAccessoryArrayNull() {
        final FileUtil fileUtil = new FileUtil();
        final FileInputStream fileIn = FileUtilTest.createFileMock();
        final Product product = new Product();
        when(fileUtil.fetchASINFile("asin", "heavy", null, null)).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[]{new ProductDetails()});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
        when(fileIn).thenReturn(joxIn);
        ArrayList accessoryArray = mock(ArrayList.class);
        Accessories accessories = mock(Accessories.class);
        when(accessories.getAccessoryArray()).thenReturn(accessoryArray);
        ProductDetails productDetails = mock(ProductDetails.class);
        when(productDetails.getAccessories()).thenReturn(accessories);
        when(productDetails.getDetailsArray()).thenReturn(new ProductDetails[0]);
        when(productInfoBean.getProductsArrayList()).thenReturn(new ArrayList());
        MiniProduct miniProduct = new MiniProduct();
        when(miniProduct.getAsin()).thenReturn("miniProductAsin");
        when(miniProduct.getImageURL()).thenReturn("miniProductImageURL");
        when(miniProduct.getManufacturer()).thenReturn("miniProductManufacturer");
        when(miniProduct.getName()).thenReturn("miniProductName");
        when(miniProduct.getPrice()).thenReturn(1.0);
        when(accessoryArray.size()).thenReturn(0);
        product.getProduct("asin", null, null);
        verify(product, never()).addAccessory(miniProduct);
    }

    @Test
    public void testGetProductAccessoryArrayNotEmpty() {
        final FileUtil fileUtil = new FileUtil();
        final FileInputStream fileIn = FileUtilTest.createFileMock();
        final Product product = new Product();
        when(fileUtil.fetchASINFile("asin", "heavy", null, null)).thenReturn(fileIn);
        JOXBeanInputStream joxIn = mock(JOXBeanInputStream.class);
        ProductInfo productInfoBean = mock(ProductInfo.class);
        when(productInfoBean.getDetails()).thenReturn(new ProductDetails[]{new ProductDetails()});
        when(joxIn.readObject(ProductInfo.class)).thenReturn(productInfoBean);
        when(fileIn).thenReturn(joxIn);
        ArrayList accessoryArray = mock(ArrayList.class);
        Accessories accessories = mock(Accessories.class);
        when(accessories.getAccessoryArray()).thenReturn(accessoryArray);
        ProductDetails productDetails = mock(ProductDetails.class);
        when(productDetails.getAccessories()).thenReturn(accessories);
        when(productDetails.getDetailsArray()).thenReturn(new ProductDetails[0]);
        when(productInfoBean.getProductsArrayList()).thenReturn(new ArrayList());
        MiniProduct miniProduct = new MiniProduct();
        when(miniProduct.getAsin()).thenReturn("miniProductAsin");
        when(miniProduct.getImageURL()).thenReturn("miniProductImageURL");
        when(miniProduct.getManufacturer()).thenReturn("miniProductManufacturer");
        when(miniProduct.getName()).thenReturn("miniProductName");
        when(miniProduct.getPrice()).thenReturn(1.0);
        when(accessoryArray.size()).thenReturn(1);
        FileInputStream fileAccessoryIn = FileUtilTest.createFileMock();
        JOXBeanInputStream joxAccessoryIn = mock(JOXBeanInputStream.class);
        ProductInfo productInfoBeanAccessory = mock(ProductInfo.class);
        when(productInfoBeanAccessory.getProductsArrayList()).thenReturn(new ArrayList());
        when(joxAccessoryIn.readObject(ProductInfo.class)).thenReturn(productInfoBeanAccessory);
        when(fileAccessoryIn).thenReturn(joxAccessoryIn);
        ProductDetails accessoryProductDetails = mock(ProductDetails.class);
        MiniProduct accessoryMiniProduct = new MiniProduct();
        when(accessoryProductDetails.getAsin()).thenReturn("accessoryProductDetailsAsin");
        when(accessoryProductDetails.getImageUrlSmall()).thenReturn("accessoryProductDetailsImageURLSmall");
        when(accessoryProductDetails.getManufacturer()).thenReturn("accessoryProductDetailsManufacturer");
        when(accessoryProductDetails.getProductName()).thenReturn("accessoryProductDetailsName");
        when(accessoryProductDetails.getOurPrice()).thenReturn(1.0);
        when(accessoryMiniProduct.getAsin()).thenReturn("accessoryMiniProductAsin");
        when(accessoryMiniProduct.getImageURL()).thenReturn("accessoryMiniProductImageURL");
        when(accessoryMiniProduct.getManufacturer()).thenReturn("accessoryMiniProductManufacturer");
        when(accessoryMiniProduct.getName()).thenReturn("accessoryMiniProductName");
        when(accessoryMiniProduct.getPrice()).thenReturn(1.0);
        when(productInfoBeanAccessory.getProductsArrayList()).thenReturn(Collections.singletonList(accessoryProductDetails));
        when(accessoryArray.get(0)).thenReturn(accessoryProductDetails);
        product.getProduct("asin", null, null);
        verify(product, times(1)).addAccessory(accessoryMiniProduct);
    }

    @Test
    public void testGetProductSimilarItemArrayNull() {
        final FileUtil fileUtil = new FileUtil();
        final Product product = new Product();
        Search search = mock(Search.class);
        when(product.getProduct("asin", null, null)).thenReturn(new FullProduct());
        when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(null);
        assertNull(product.getProduct("asin", null, null).getSimilarItems());
    }

    @Test
    public void testGetProductSimilarItemArrayNotEmpty() {
        final FileUtil fileUtil = new FileUtil();
        final Product product = new Product();
        Search search = mock(Search.class);
        when(product.getProduct("asin", null, null)).thenReturn(new FullProduct());
        MiniProduct miniProduct = mock(MiniProduct.class);
        when(miniProduct.getAsin()).thenReturn("miniProductAsin");
        when(miniProduct.getImageURL()).thenReturn("miniProductImageURL");
        when(miniProduct.getManufacturer()).thenReturn("miniProductManufacturer");
        when(miniProduct.getName()).thenReturn("miniProductName");
        when(miniProduct.getPrice()).thenReturn(1.0);
        when(search.SimilaritesSearch(anyString(), anyString())).thenReturn(new ProductInfo());
        when(new ProductInfo().getProductsArrayList()).thenReturn(Collections.singletonList(miniProduct));
        product.getProduct("asin", null, null);
        verify(product, times(1)).addSimilarItem(miniProduct);
    }
}