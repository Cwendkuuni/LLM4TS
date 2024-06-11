package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.FullProduct;
import net.kencochrane.a4j.DAO.MiniProduct;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.beans.ProductInfo;
import net.kencochrane.a4j.beans.Search;

import org.junit.Test;
import org.mockito.Mockito;

public class ProductTest {

    @Test
    public void testGetProduct_validInput_returnsFullProduct() {
        String asin = "B07N4X68G1";
        String offer = "new";
        String page = "1";

        FileUtil fileUtilMock = Mockito.mock(FileUtil.class);
        Search searchMock = Mockito.mock(Search.class);

        Product product = new Product();
        product.setFileUtil(fileUtilMock);
        product.setSearch(searchMock);

        ProductDetails productDetailsMock = Mockito.mock(ProductDetails.class);
        MiniProduct miniProductMock = Mockito.mock(MiniProduct.class);

        FullProduct fullProduct = new FullProduct();

        Mockito.when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenReturn(Mockito.any(FileInputStream.class));
        Mockito.when(fileUtilMock.fetchAccessories(asin, Arrays.asList("accessory1", "accessory2"))).thenReturn(Mockito.any(FileInputStream.class));
        Mockito.when(searchMock.SimilaritesSearch(asin, "1")).thenReturn(new ProductInfo());

        Mockito.when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenReturn(Mockito.any(FileInputStream.class)).thenReturn(null);
        Mockito.when(searchMock.SimilaritesSearch(asin, "1")).thenReturn(null);

        Mockito.when(fileUtilMock.fetchASINFile(asin, "heavy", offer, page)).thenThrow(new FileNotFoundException());

        Mockito.when(productInfoBean.getDetails()).thenReturn(new ProductDetails[] { productDetailsMock });
        Mockito.when(productDetailsMock.getAccessories()).thenReturn(new Accessories());
        Mockito.when(productDetailsMock.getAccessories().getAccessoryArray()).thenReturn(accessoryArrayMock());

        Mockito.when(fileUtilMock.fetchAccessories(asin, accessoryArrayMock())).thenReturn(Mockito.any(FileInputStream.class)).thenReturn(null);

        Mockito.when(productInfoBean.getProductsArrayList()).thenReturn(detailsArrayMock());
        Mockito.when(detailsArrayMock().get(0)).thenReturn(accessoryProductDetailsMock());
        Mockito.when(accessoryProductDetailsMock.getAsin()).thenReturn("accessory1");
        Mockito.when(accessoryProductDetailsMock.getImageUrlSmall()).thenReturn("url1");
        Mockito.when(accessoryProductDetailsMock.getManufacturer()).thenReturn("manufacturer1");
        Mockito.when(accessoryProductDetailsMock.getProductName()).thenReturn("name1");
        Mockito.when(accessoryProductDetailsMock.getOurPrice()).thenReturn(10.5);

        Mockito.when(productInfoBean.getProductsArrayList()).thenReturn(detailsArrayMock());
        Mockito.when(detailsArrayMock().get(1)).thenReturn(accessoryProductDetailsMock2());
        Mockito.when(accessoryProductDetailsMock2.getAsin()).thenReturn("accessory2");
        Mockito.when(accessoryProductDetailsMock2.getImageUrlSmall()).thenReturn("url2");
        Mockito.when(accessoryProductDetailsMock2.getManufacturer()).thenReturn("manufacturer2");
        Mockito.when(accessoryProductDetailsMock2.getProductName()).thenReturn("name2");
        Mockito.when(accessoryProductDetailsMock2.getOurPrice()).thenReturn(15.5);

        FullProduct result = product.getProduct(asin, offer, page);

        assertEquals("Product details should match", productDetailsMock, result.getDetails());
        assertEquals("Accessories should have 2 items", 2, result.getAccessories().getAccessoryArray().size());
        assertEquals("First accessory details should match", miniProductMock, result.getAccessories().getAccessoryArray().get(0));
        assertEquals("Second accessory details should match", miniProductMock, result.getAccessories().getAccessoryArray().get(1));
    }

    private Object accessoryArrayMock() {
        ArrayList accessoryArray = new ArrayList();
        accessoryArray.add("accessory1");
        accessoryArray.add("accessory2");
        return accessoryArray;
    }

    private Object detailsArrayMock() {
        ArrayList detailsArray = new ArrayList();
        detailsArray.add(productDetailsMock);
        detailsArray.add(accessoryProductDetailsMock2);
        return detailsArray;
    }

    private Object productDetailsMock = Mockito.mock(ProductDetails.class);
    private Object accessoryProductDetailsMock = Mockito.mock(ProductDetails.class);
    private Object accessoryProductDetailsMock2 = Mockito.mock(ProductDetails.class);
}