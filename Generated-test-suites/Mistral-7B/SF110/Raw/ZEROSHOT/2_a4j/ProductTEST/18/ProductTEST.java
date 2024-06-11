package net.kencochrane.a4j.DAO.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kencochrane.a4j.DAO.FileUtil;
import net.kencochrane.a4j.beans.Accessories;
import net.kencochrane.a4j.beans.FullProduct;
import net.kencochrane.a4j.beans.MiniProduct;
import net.kencochrane.a4j.beans.ProductDetails;
import net.kencochrane.a4j.file.JOXBeanInputStream;
import net.kencochrane.a4j.file.ProductInfo;
import net.kencochrane.a4j.file.Search;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Test;

public class ProductTest extends MockObjectTestCase {

	private static final String ASIN = "B000012345";
	private static final String OFFER = "New";
	private static final String PAGE = "1";
	private static final String ACCESSORY_ASIN = "B000045678";
	private static final String ACCESSORY_IMAGE_URL = "http://example.com/accessory1.jpg";
	private static final String ACCESSORY_MANUFACTURER = "AccessoryManufacturer";
	private static final String ACCESSORY_NAME = "AccessoryName";
	private static final Double ACCESSORY_PRICE = 10.5;

	private Product product;
	private FileUtil fileUtil;
	private JOXBeanInputStream joxIn;
	private ProductInfo productInfo;
	private ProductDetails productDetails;
	private Accessories accessories;
	private ArrayList accessoryArray;
	private ArrayList detailsArray;
	private MiniProduct miniProduct;
	private Search search;

	@Test
	public void testGetProductNullInputs() {
		fileUtil = mock(FileUtil.class);
		expect(fileUtil.fetchASINFile(ASIN, "heavy", OFFER, PAGE)).andReturn(null);

		product = new Product();
		assertNull(product.getProduct(ASIN, OFFER, PAGE));
	}

	@Test
	public void testGetProductNoDetails() {
		fileUtil = mock(FileUtil.class);
		FileInputStream fileIn = mock(FileInputStream.class);
		expect(fileUtil.fetchASINFile(ASIN, "heavy", OFFER, PAGE)).andReturn(fileIn);
		expect(fileIn.available()).andReturn(0L);

		product = new Product();
		assertNull(product.getProduct(ASIN, OFFER, PAGE));
	}

	@Test
	public void testGetProductWithDetailsNoAccessories() {
		fileUtil = mock(FileUtil.class);
		FileInputStream fileIn = mock(FileInputStream.class);
		ProductInfo productInfoBean = new ProductInfo();
		ProductDetails productDetails = new ProductDetails();
		expect(fileUtil.fetchASINFile(ASIN, "heavy", OFFER, PAGE)).andReturn(fileIn);
		expect(fileIn.available()).andReturn((long)getObjectSize(productInfoBean));
		expect(new JOXBeanInputStream(fileIn)).andReturn(joxIn);
		expect(joxIn.readObject(ProductInfo.class)).andReturn(productInfoBean);
		joxIn.close();
		fileIn.close();

		product = new Product();
		productDetails = product.getProduct(ASIN, OFFER, PAGE).getDetails();
		assertNotNull(productDetails);
		assertEquals(ASIN, productDetails.getAsin());
		assertNull(product.getProduct(ASIN, OFFER, PAGE).getAccessories());
	}

	@Test
	public void testGetProductWithAccessories() {
		fileUtil = mock(FileUtil.class);
		FileInputStream fileIn = mock(FileInputStream.class);
		ProductInfo productInfoBean = new ProductInfo();
		ProductDetails productDetails = new ProductDetails();
		Accessories accessories = new Accessories();
		ArrayList accessoryArray = new ArrayList();
		AccessoryProductDetails accessoryProductDetails = new AccessoryProductDetails();
		MiniProduct miniProduct = new MiniProduct();

		productDetails.setAccessories(accessories);
		accessories.setAccessoryArray(accessoryArray);
		accessoryProductDetails.setAsin(ACCESSORY_ASIN);
		accessoryProductDetails.setImageUrlSmall(ACCESSORY_IMAGE_URL);
		accessoryProductDetails.setManufacturer(ACCESSORY_MANUFACTURER);
		accessoryProductDetails.setProductName(ACCESSORY_NAME);
		accessoryProductDetails.setOurPrice(ACCESSORY_PRICE);
		miniProduct.setAsin(ACCESSORY_ASIN);
		miniProduct.setImageURL(ACCESSORY_IMAGE_URL);
		miniProduct.setManufacturer(ACCESSORY_MANUFACTURER);
		miniProduct.setName(ACCESSORY_NAME);
		miniProduct.setPrice(ACCESSORY_PRICE);
		accessoryArray.add(accessoryProductDetails);

		expect(fileUtil.fetchASINFile(ASIN, "heavy", OFFER, PAGE)).andReturn(fileIn);
		expect(fileIn.available()).andReturn((long)getObjectSize(productInfoBean));
		expect(new JOXBeanInputStream(fileIn)).andReturn(joxIn);
		expect(joxIn.readObject(ProductInfo.class)).andReturn(productInfoBean);
		joxIn.close();
		fileIn.close();

		expect(fileUtil.fetchAccessories(ASIN, accessoryArray)).andReturn(new ByteArrayInputStream("".getBytes()));
		expect(new JOXBeanInputStream(new ByteArrayInputStream("".getBytes()))).andReturn(joxIn);
		expect(joxIn.readObject(ProductInfo.class)).andReturn(new ProductInfo());
		joxIn.close();

		product = new Product();
		ProductFullDetails productFullDetails = product.getProduct(ASIN, OFFER, PAGE);
		assertNotNull(productFullDetails);
		assertNotNull(productFullDetails.getDetails());
		assertNotNull(productFullDetails.getAccessories());

		ProductDetails details = productFullDetails.getDetails();
		assertEquals(ASIN, details.getAsin());
		assertNotNull(details.getAccessories());
		assertEquals(1, details.getAccessories().size());

		Accessories accessories2 = details.getAccessories();
		assertEquals(ACCESSORY_MANUFACTURER, accessories2.getManufacturer());
		assertEquals(ACCESSORY_NAME, accessories2.getName());
		assertEquals(ACCESSORY_PRICE, accessories2.getPrice(), 0.01);
		assertEquals(1, accessories2.getAccessoryArray().size());

		MiniProduct miniProduct2 = (MiniProduct) accessories2.getAccessoryArray().get(0);
		assertEquals(ACCESSORY_ASIN, miniProduct2.getAsin());
		assertEquals(ACCESSORY_IMAGE_URL, miniProduct2.getImageURL());
		assertEquals(ACCESSORY_MANUFACTURER, miniProduct2.getManufacturer());
		assertEquals(ACCESSORY_NAME, miniProduct2.getName());
		assertEquals(ACCESSORY_PRICE, miniProduct2.getPrice(), 0.01);
	}

	@Test
	public void testGetProductWithSimilarItems() {
		fileUtil = mock(FileUtil.class);
		FileInputStream fileIn = mock(FileInputStream.class);
		ProductInfo productInfoBean = new ProductInfo();
		ProductDetails productDetails = new ProductDetails();
		MiniProduct miniProduct = new MiniProduct();
		expect(fileUtil.fetchASINFile(ASIN, "heavy", OFFER, PAGE)).andReturn(fileIn);
		expect(fileIn.available()).andReturn((long)getObjectSize(productInfoBean));
		expect(new JOXBeanInputStream(fileIn)).andReturn(joxIn);
		expect(joxIn.readObject(ProductInfo.class)).andReturn(productInfoBean);
		joxIn.close();
		fileIn.close();

		ProductInfo similarProductInfoBean = new ProductInfo();
		List<ProductDetails> similarDetailsArray = Arrays.asList(new ProductDetails());
		miniProduct.setAsin(ACCESSORY_ASIN);
		miniProduct.setImageURL(ACCESSORY_IMAGE_URL);
		miniProduct.setManufacturer(ACCESSORY_MANUFACTURER);
		miniProduct.setName(ACCESSORY_NAME);
		miniProduct.setPrice(ACCESSORY_PRICE);
		ProductDetails similarProductDetails = new ProductDetails();
		similarProductDetails.setProductName(ACCESSORY_NAME);
		similarProductDetails.setMiniProduct(miniProduct);

		expect(new Search()).andReturn(search);
		expect(search.SimilaritesSearch(ASIN, "1")).andReturn(similarProductInfoBean);
		expect(similarProductInfoBean.getProductsArrayList()).andReturn(similarDetailsArray);

		product = new Product();
		ProductFullDetails productFullDetails = product.getProduct(ASIN, OFFER, PAGE);
		assertNotNull(productFullDetails);
		assertNotNull(productFullDetails.getDetails());
		assertNotNull(productFullDetails.getSimilarItems());

		ProductDetails details = productFullDetails.getDetails();
		assertEquals(ASIN, details.getAsin());
		assertNotNull(details.getAccessories());
		assertEquals(0, details.getAccessories().size());

		List<MiniProduct> similarItems = productFullDetails.getSimilarItems();
		assertEquals(1, similarItems.size());
		MiniProduct similarItem = similarItems.get(0);
		assertEquals(ACCESSORY_ASIN, similarItem.getAsin());
		assertEquals(ACCESSORY_IMAGE_URL, similarItem.getImageURL());
		assertEquals(ACCESSORY_MANUFACTURER, similarItem.getManufacturer());
		assertEquals(ACCESSORY_NAME, similarItem.getName());
		assertEquals(ACCESSORY_PRICE, similarItem.getPrice(), 0.01);
	}

	private long getObjectSize(Object object) {
		if (object == null) {
			return 0L;
		}
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
			objectOutput.writeObject(object);
			outputStream.close();
			return outputStream.size();
		} catch (IOException e) {
			ClassImposteriser.getInstance().imposterise(IOException.class);
			throw (IOException) Class.forName("java.io.IOException").newInstance();
		}
	}

	@Before
	public void setUp() {
		jmock.ThreadContext.push();
		jmock.ThreadContext.pushPropertyValue("jmock.threadName", "ProductTest");
	}

	@After
	public void tearDown() {
		jmock.ThreadContext.pop();
	}
}