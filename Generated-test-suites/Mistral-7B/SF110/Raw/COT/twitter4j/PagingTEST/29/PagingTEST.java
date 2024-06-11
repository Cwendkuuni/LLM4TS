import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject(int page, int count, long sinceId, long maxId) {
 return new Paging(page, count, sinceId, maxId);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging();
 List<HttpParameter> expectedParams = new ArrayList<>();
 expectedParams.add(new HttpParameter("count", "10"));
 paging.setCount(10);
 List<HttpParameter> actualParams = paging.asPostParameterList();
 assertEquals(expectedParams, actualParams);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 char[] supportedParams = new char[]{'s', 'm'};
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(supportedParams, 'x', pagingParams, "unsupportedParam", 1L);
 }

 @Test
 public void testAsPostParameterListWithSinceId() {
 Paging paging = new Paging(1, 10, 1L);
 List<HttpParameter> expectedParams = new ArrayList<>();
 expectedParams.add(new HttpParameter("page", "1"));
 expectedParams.add(new HttpParameter("count", "10"));
 expectedParams.add(new HttpParameter("since_id", "1"));
 List<HttpParameter> actualParams = paging.asPostParameterList();
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterListWithMaxId() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 List<HttpParameter> expectedParams = new ArrayList<>();
 expectedParams.add(new HttpParameter("page", "1"));
 expectedParams.add(new HttpParameter("count", "10"));
 expectedParams.add(new HttpParameter("since_id", "1"));
 expectedParams.add(new HttpParameter("max_id", "100"));
 List<HttpParameter> actualParams = paging.asPostParameterList();
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 paging.setPage(1);
 paging.setCount(10);
 paging.setSinceId(1L);
 paging.setMaxId(100L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithNegativeValue() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithNegativeValue() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithNegativeValue() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", paging.toString());
 }

}