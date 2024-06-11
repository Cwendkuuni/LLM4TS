package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.DAO.Product;
import net.kencochrane.a4j.DAO.ProductDetails;
import net.kencochrane.a4j.DAO.Accessories;
import net.kencochrane.a4j.file.FileUtil;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;

import org.junit.Test;

public class ProductTest {

	@Test
	public void testGetProduct_ValidInput_ReturnFullProduct() throws Exception {
		String asin = "B07TP81VJX";
		String offer = "New";
		String page = "1";

		// Prepare Test Data
		ProductInfo productInfoBean = new ProductInfo();
		ProductDetails productDetails = new ProductDetails();
		ProductDetails accessoryProductDetails = new ProductDetails();
		Accessories accessories = new Accessories();
		ArrayList<ProductDetails> detailsArray = new ArrayList<ProductDetails>();
		MiniProduct miniProduct = new MiniProduct();
		List<MiniProduct> accessoryList = new ArrayList<MiniProduct>();
		List<MiniProduct> similarItemList = new ArrayList<MiniProduct>();

		productDetails.setAccessories(accessories);
		accessories.setAccessoryArray(accessoryArray);
		miniProduct.setAsin("ASIN1");
		miniProduct.setImageURL("URL1");
		miniProduct.setManufacturer("Manufacturer1");
		miniProduct.setName("Product1");
		miniProduct.setPrice(1.0);
		accessoryProductDetails.setDetails(productDetails);
		accessoryProductDetails.setProductName("Product1");
		accessoryProductDetails.setImageUrlSmall("URL1");
		accessoryProductDetails.setOurPrice(1.0);
		detailsArray.add(accessoryProductDetails);

		productInfoBean.setDetails(new ProductDetails[1]);
		productInfoBean.setDetails(0, productDetails);
		productInfoBean.setProductsArrayList(detailsArray);

		Product underTest = new Product();
		FileUtil fileUtil = new FileUtil();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(getBytesFromResource("/ProductInfo.xml", productInfoBean));
		FileInputStream fileIn = new FileInputStream(new File("tempFile.xml"));
		fileIn = fileUtil.fetchASINFile(asin, "heavy", offer, page);
		JOXBeanInputStream joxIn = null;

		// Exercise System Under Test
		FullProduct result = underTest.getProduct(asin, offer, page);

		// Verify Results
		assertNotNull(result);
		assertEquals(productDetails, result.getDetails());
		assertEquals(accessoryList, result.getAccessories());
		assertEquals(similarItemList, result.getSimilarItems());

		// Clean up
		fileIn.close();
		fileUtil.deleteFile("tempFile.xml");
	}

	@Test(expected = NullPointerException.class)
	public void testGetProduct_InvalidASIN_ReturnNull() throws Exception {
		String asin = null;
		String offer = "New";
		String page = "1";

		Product underTest = new Product();
		Product result = underTest.getProduct(asin, offer, page);

		assertNull(result);
	}

	private byte[] getBytesFromResource(String fileName, Object object) throws IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		byte[] buffer = new byte[1024];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.close();
		return outputStream.toByteArray();
	}

}