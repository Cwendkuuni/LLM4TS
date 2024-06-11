import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject() {
 return new Paging(1, 10);
 }

 @Test
 public void testConstructors() {
 // Test default constructor
 Paging paging1 = new Paging();
 // Test constructor with page number
 Paging paging2 = new Paging(1);
 // Test constructor with sinceId
 Paging paging3 = new Paging(1L);
 // Test constructor with page number and count
 Paging paging4 = new Paging(1, 10);
 // Test constructor with page number, sinceId, and count
 Paging paging5 = new Paging(1, 1L, 10);
 // Test constructor with page number, sinceId, count, and maxId
 Paging paging6 = new Paging(1, 1L, 10, 100L);

 assertEquals(-1, paging1.getPage());
 assertEquals(-1, paging1.getCount());
 assertEquals(-1L, paging1.getSinceId());
 assertEquals(-1L, paging1.getMaxId());

 assertEquals(1, paging2.getPage());
 assertEquals(-1, paging2.getCount());
 assertEquals(-1L, paging2.getSinceId());
 assertEquals(-1L, paging2.getMaxId());

 assertEquals(1, paging3.getPage());
 assertEquals(-1, paging3.getCount());
 assertEquals(1L, paging3.getSinceId());
 assertEquals(-1L, paging3.getMaxId());

 assertEquals(1, paging4.getPage());
 assertEquals(10, paging4.getCount());
 assertEquals(-1L, paging4.getSinceId());
 assertEquals(-1L, paging4.getMaxId());

 assertEquals(1, paging5.getPage());
 assertEquals(10, paging5.getCount());
 assertEquals(1L, paging5.getSinceId());
 assertEquals(-1L, paging5.getMaxId());

 assertEquals(1, paging6.getPage());
 assertEquals(10, paging6.getCount());
 assertEquals(1L, paging6.getSinceId());
 assertEquals(100L, paging6.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = createPagingObject();

 // Test getters and setters for page
 assertEquals(1, paging.getPage());
 paging.setPage(2);
 assertEquals(2, paging.getPage());

 // Test getters and setters for count
 assertEquals(10, paging.getCount());
 paging.setCount(5);
 assertEquals(5, paging.getCount());

 // Test getters and setters for sinceId
 assertEquals(1L, paging.getSinceId());
 paging.setSinceId(2L);
 assertEquals(2L, paging.getSinceId());

 // Test getters and setters for maxId
 assertEquals(-1L, paging.getMaxId());
 paging.setMaxId(100L);
 assertEquals(100L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidArguments() {
 Paging paging = createPagingObject();

 // Test setting negative page number
 paging.setPage(-1);

 // Test setting negative count
 paging.setCount(-1);

 // Test setting negative sinceId
 paging.setSinceId(-1L);
 }

 @Test
 public void testEqualsAndHashcode() {
 Paging paging1 = createPagingObject();
 Paging paging2 = createPagingObject();
 Paging paging3 = new Paging(2, 20);

 assertTrue(paging1.equals(paging1));
 assertTrue(paging1.equals(paging2));
 assertFalse(paging1.equals(paging3));

 int hashCode1 = paging1.hashCode();
 int hashCode2 = paging1.hashCode();
 int hashCode3 = paging3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 Paging paging = createPagingObject();
 String expectedToString = "Paging{page=1, count=10, sinceId=1, maxId=-1}";
 String actualToString = paging.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedParams = new ArrayList<>();
 expectedParams.add(new HttpParameter("page", "1"));
 expectedParams.add(new HttpParameter("count", "10"));

 List<HttpParameter> actualParams = paging.asPostParameterList();
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterListWithSupportedParams() {
 char[] supportedParams = new char[]{'s', 'm'};
 Paging paging = createPagingObject();
 List<HttpParameter> expectedParams = new ArrayList<>();
 expectedParams.add(new HttpParameter("page", "1"));
 expectedParams.add(new HttpParameter("count", "10"));
 expectedParams.add(new HttpParameter("since_id", "1"));
 expectedParams.add(new HttpParameter("max_id", "1"));

 List<HttpParameter> actualParams = paging.asPostParameterList(supportedParams);
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterListWithInvalidParamName() {
 char[] supportedParams = new char[]{'s', 'm'};
 Paging paging = createPagingObject();

 try {
 paging.asPostParameterList(supportedParams, "invalidParamName");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = createPagingObject();
 HttpParameter[] expectedParams = new HttpParameter[4];
 expectedParams[0] = new HttpParameter("page", "1");
 expectedParams[1] = new HttpParameter("count", "10");
 expectedParams[2] = new HttpParameter("since_id", "1");
 expectedParams[3] = new HttpParameter("max_id", "");

 HttpParameter[] actualParams = paging.asPostParameterArray();
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterArrayWithSupportedParams() {
 char[] supportedParams = new char[]{'s', 'm'};
 Paging paging = createPagingObject();
 HttpParameter[] expectedParams = new HttpParameter[4];
 expectedParams[0] = new HttpParameter("page", "1");
 expectedParams[1] = new HttpParameter("count", "10");
 expectedParams[2] = new HttpParameter("since_id", "1");
 expectedParams[3] = new HttpParameter("max_id", "1");

 HttpParameter[] actualParams = paging.asPostParameterArray(supportedParams);
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterArrayWithInvalidParamName() {
 char[] supportedParams = new char[]{'s', 'm'};
 Paging paging = createPagingObject();

 try {
 paging.asPostParameterArray(supportedParams, "invalidParamName");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}