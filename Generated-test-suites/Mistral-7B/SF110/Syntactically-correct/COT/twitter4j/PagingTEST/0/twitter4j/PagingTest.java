import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingInstance(int page, int count, long sinceId, long maxId) {
 return new Paging(page, count, sinceId, maxId);
 }

 @Test
 public void testDefaultConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPage() {
 Paging paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithSinceId() {
 Paging paging = new Paging(1L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCount() {
 Paging paging = new Paging(1, 10);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageCountAndSinceId() {
 Paging paging = new Paging(1, 10, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageCountSinceIdAndMaxId() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 assertEquals(1, paging.getPage());
 paging.setPage(2);
 assertEquals(2, paging.getPage());
 assertEquals(10, paging.getCount());
 paging.setCount(5);
 assertEquals(5, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 paging.setSinceId(2L);
 assertEquals(2L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 paging.setMaxId(200L);
 assertEquals(200L, paging.getMaxId());
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 10, 1L, 100L);
 Paging paging2 = new Paging(1, 10, 1L, 100L);
 assertTrue(paging1.equals(paging2));
 Paging paging3 = new Paging(1, 10, 1L, 101L);
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 10, 1L, 100L);
 Paging paging2 = new Paging(1, 10, 1L, 100L);
 assertEquals(paging1.hashCode(), paging2.hashCode());
 Paging paging3 = new Paging(1, 10, 1L, 101L);
 assertNotEquals(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", paging.toString());
 }

 @Test
 public void testAsPostParameterArrayWithSupportedParams() {
 char[] supportedParams = Paging.SMCP;
 Paging paging = new Paging(1, 10, 1L, 100L);
 HttpParameter[] expectedParams = {
 new HttpParameter("count", "10"),
 new HttpParameter("page", "1"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "100")
 };
 HttpParameter[] actualParams = paging.asPostParameterArray(supportedParams, "count");
 assertArrayEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterArrayWithUnsupportedParam() {
 char[] supportedParams = Paging.SMCP;
 Paging paging = new Paging(1, 10, 1L, 100L);
 try {
 paging.asPostParameterArray(supportedParams, "unsupportedParam");
 fail();
 } catch (IllegalStateException e) {
 assertTrue(e.getMessage().contains("Paging parameter [unsupportedParam] is not supported with this operation."));
 }
 }

 @Test
 public void testAsPostParameterListWithSupportedParams() {
 char[] supportedParams = Paging.SMCP;
 Paging paging = new Paging(1, 10, 1L, 100L);
 List<HttpParameter> expectedParams = Arrays.asList(
 new HttpParameter("count", "10"),
 new HttpParameter("page", "1"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "100")
 );
 List<HttpParameter> actualParams = paging.asPostParameterList(supportedParams, "count");
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterListWithUnsupportedParam() {
 char[] supportedParams = Paging.SMCP;
 Paging paging = new Paging(1, 10, 1L, 100L);
 try {
 paging.asPostParameterList(supportedParams, "unsupportedParam");
 fail();
 } catch (IllegalStateException e) {
 assertTrue(e.getMessage().contains("Paging parameter [unsupportedParam] is not supported with this operation."));
 }
 }

 @Test
 public void testNegativePageNumber() {
 try {
 new Paging(-1);
 fail();
 } catch (IllegalArgumentException e) {
 assertTrue(e.getMessage().contains("page should be positive integer. passed:-1"));
 }
 }

 @Test
 public void testNegativeCountNumber() {
 try {
 new Paging(1, -1);
 fail();
 } catch (IllegalArgumentException e) {
 assertTrue(e.getMessage().contains("count should be positive integer. passed:-1"));
 }
 }

 @Test
 public void testNegativeSinceId() {
 try {
 new Paging(1, 1, -1L);
 fail();
 } catch (IllegalArgumentException e) {
 assertTrue(e.getMessage().contains("since_id should be positive integer. passed:-1"));
 }
 }

 @Test
 public void testNegativeMaxId() {
 try {
 new Paging(1, 1, 1, -1L);
 fail();
 } catch (IllegalArgumentException e) {
 assertTrue(e.getMessage().contains("max_id should be positive integer. passed:-1"));
 }
 }
}