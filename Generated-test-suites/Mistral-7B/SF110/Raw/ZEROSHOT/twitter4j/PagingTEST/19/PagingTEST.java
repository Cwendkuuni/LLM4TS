import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testEmptyPaging() {
 Paging paging = new Paging();
 List<HttpParameter> httpParameters = paging.asPostParameterList();
 assertNotNull(httpParameters);
 assertFalse(httpParameters.isEmpty());
 assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, httpParameters.toArray(new HttpParameter[0]));
 }

 @Test
 public void testDefaultPaging() {
 Paging paging = new Paging();
 List<HttpParameter> httpParameters = paging.asPostParameterList(Paging.SMCP, "count");
 assertNotNull(httpParameters);
 assertFalse(httpParameters.isEmpty());
 assertEquals(4, httpParameters.size());
 assertNull(httpParameters.get(0).getName());
 assertNull(httpParameters.get(0).getValue());
 assertEquals("count", httpParameters.get(1).getName());
 assertNull(httpParameters.get(1).getValue());
 assertEquals("page", httpParameters.get(2).getName());
 assertNull(httpParameters.get(2).getValue());
 assertEquals("since_id", httpParameters.get(3).getName());
 assertNull(httpParameters.get(3).getValue());
 }

 @Test
 public void testSettersAndGetters() {
 Paging paging = new Paging(2, 10);
 assertEquals(2, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1, paging.getSinceId());
 assertEquals(-1, paging.getMaxId());

 paging.setPage(3);
 assertEquals(3, paging.getPage());

 paging.setCount(5);
 assertEquals(5, paging.getCount());

 paging.setSinceId(1L);
 assertEquals(1, paging.getSinceId());

 paging.setMaxId(2L);
 assertEquals(2, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPage() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCount() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsupportedParam() {
 Paging paging = new Paging();
 paging.asPostParameterList('x', "unsupportedParam");
 }

 @Test
 public void testAddPostParameter() {
 Paging paging = new Paging();
 List<HttpParameter> httpParameters = paging.asPostParameterList(Paging.SMCP, "count");
 paging.addPostParameter(Paging.SMCP, 's', httpParameters, "since_id", 1L);
 assertEquals(5, httpParameters.size());
 assertEquals("since_id", httpParameters.get(4).getName());
 assertEquals("1", httpParameters.get(4).getValue());
 }
}