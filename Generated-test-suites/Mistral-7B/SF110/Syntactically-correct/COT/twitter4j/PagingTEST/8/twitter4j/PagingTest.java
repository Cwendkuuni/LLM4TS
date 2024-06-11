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
 Paging paging = new Paging(1, 10);
 List<HttpParameter> expectedParams = Arrays.asList(
 new HttpParameter("page", "1"),
 new HttpParameter("count", "10")
 );
 List<HttpParameter> actualParams = paging.asPostParameterList();
 assertEquals("Unexpected number of parameters", expectedParams.size(), actualParams.size());
 for (int i = 0; i < expectedParams.size(); i++) {
 HttpParameter expectedParam = expectedParams.get(i);
 HttpParameter actualParam = actualParams.get(i);
 assertEquals("Unexpected parameter name", expectedParam.getName(), actualParam.getName());
 assertEquals("Unexpected parameter value", expectedParam.getValue(), actualParam.getValue());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "unsupportedParam", 1L);
 fail("Should have thrown an IllegalStateException");
 }

 @Test
 public void testAddPostParameterWithSupportedParam() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 1L);
 assertEquals("Unexpected number of parameters", 1, pagingParams.size());
 HttpParameter expectedParam = new HttpParameter("since_id", "1");
 HttpParameter actualParam = pagingParams.get(0);
 assertEquals("Unexpected parameter name", expectedParam.getName(), actualParam.getName());
 assertEquals("Unexpected parameter value", expectedParam.getValue(), actualParam.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithNegativeValue() {
 Paging paging = new Paging();
 paging.setPage(-1);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithNegativeValue() {
 Paging paging = new Paging();
 paging.setCount(-1);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testSetPageAndCount() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(10);
 assertEquals("Unexpected page value", 1, paging.getPage());
 assertEquals("Unexpected count value", 10, paging.getCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithNegativeValue() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testSetPageAndSinceId() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setSinceId(1L);
 assertEquals("Unexpected page value", 1, paging.getPage());
 assertEquals("Unexpected sinceId value", 1, paging.getSinceId());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1, 1);
 assertEquals("Unexpected Paging object toString", "Paging{page=1, count=10, sinceId=1, maxId=1}", paging.toString());
 }

}