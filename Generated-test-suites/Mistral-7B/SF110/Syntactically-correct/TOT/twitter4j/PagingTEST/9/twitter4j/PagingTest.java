import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpParameterMap;
import twitter4j.Paging;

public class PagingTest {

 private Paging createPagingInstance() {
 return new Paging(1, 10, 1L, 100L);
 }

 @Test
 public void testAsPostParameterListEmpty() {
 Paging paging = new Paging();
 List<HttpParameter> expected = Paging.NULL_PARAMETER_LIST;
 List<HttpParameter> actual = paging.asPostParameterList();
 assertEquals(expected, actual);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = createPagingInstance();
 List<HttpParameter> expected = new ArrayList<HttpParameter>() {{
 add(new HttpParameter("page", "1"));
 add(new HttpParameter("count", "10"));
 add(new HttpParameter("since_id", "1"));
 add(new HttpParameter("max_id", "100"));
 }};
 List<HttpParameter> actual = paging.asPostParameterList();
 assertEquals(expected, actual);
 }

 @Test
 public void testAsPostParameterArrayEmpty() {
 Paging paging = new Paging();
 HttpParameter[] expected = Paging.NULL_PARAMETER_ARRAY;
 HttpParameter[] actual = paging.asPostParameterArray();
 assertEquals(expected, actual);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = createPagingInstance();
 HttpParameter[] expected = paging.asPostParameterArray();
 HttpParameter[] actual = paging.asPostParameterArray();
 assertEquals(expected.length, actual.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterInvalidParamKey() {
 Paging paging = new Paging('a', 'p', new ArrayList<HttpParameter>(), "paramName", 1L);
 paging.addPostParameter(new char[]{'s', 'm'}, 'x', paging.pagingParams, "paramName", 1L);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterNegativeParamValue() {
 Paging paging = new Paging();
 paging.addPostParameter(Paging.SMCP, 'p', paging.pagingParams, "page", -1);
 }

 @Test
 public void testToString() {
 Paging paging = createPagingInstance();
 String expected = "Paging{page=1, count=10, sinceId=1, maxId=100}";
 String actual = paging.toString();
 assertEquals(expected, actual);
 }
}