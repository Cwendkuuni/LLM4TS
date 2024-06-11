import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject() {
 return new Paging(1, 10);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedParams = Arrays.asList(
 new HttpParameter("page", "1"),
 new HttpParameter("count", "10")
 );
 List<HttpParameter> actualParams = paging.asPostParameterList();
 Assert.assertEquals(expectedParams, actualParams);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddUnsupportedParam() {
 Paging paging = createPagingObject();
 char[] supportedParams = {'s', 'm', 'c', 'p'};
 List<HttpParameter> params = new ArrayList<HttpParameter>();
 paging.addPostParameter(supportedParams, 'a', params, "unsupported", 1L);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = createPagingObject();
 HttpParameter[] expectedParams = new HttpParameter[] {
 new HttpParameter("page", "1"),
 new HttpParameter("count", "10")
 };
 HttpParameter[] actualParams = paging.asPostParameterArray();
 Assert.assertArrayEquals(expectedParams, actualParams);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddNullParamValue() {
 Paging paging = createPagingObject();
 char[] supportedParams = {'s', 'm', 'c', 'p'};
 List<HttpParameter> params = new ArrayList<HttpParameter>();
 paging.addPostParameter(supportedParams, 'p', params, "page", null);
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = createPagingObject();
 int expectedPage = 1;
 int expectedCount = 10;
 long expectedSinceId = -1L;
 long expectedMaxId = -1L;

 paging.setPage(expectedPage);
 paging.setCount(expectedCount);
 paging.setSinceId(1L);
 paging.setMaxId(2L);

 int actualPage = paging.getPage();
 int actualCount = paging.getCount();
 long actualSinceId = paging.getSinceId();
 long actualMaxId = paging.getMaxId();

 Assert.assertEquals(expectedPage, actualPage);
 Assert.assertEquals(expectedCount, actualCount);
 Assert.assertEquals(expectedSinceId, actualSinceId);
 Assert.assertEquals(expectedMaxId, actualMaxId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativePage() {
 Paging paging = createPagingObject();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeCount() {
 Paging paging = createPagingObject();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeSinceId() {
 Paging paging = createPagingObject();
 paging.setSinceId(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMaxId() {
 Paging paging = createPagingObject();
 paging.setMaxId(-1L);
 }
}