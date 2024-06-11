import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testPagingConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());

 Paging pagePaging = new Paging(1);
 assertEquals(1, pagePaging.getPage());
 assertEquals(-1, pagePaging.getCount());
 assertEquals(-1L, pagePaging.getSinceId());
 assertEquals(-1L, pagePaging.getMaxId());

 Paging sinceIdPaging = new Paging(1L);
 assertEquals(1, sinceIdPaging.getPage());
 assertEquals(-1, sinceIdPaging.getCount());
 assertEquals(1L, sinceIdPaging.getSinceId());
 assertEquals(-1L, sinceIdPaging.getMaxId());

 Paging pageCountPaging = new Paging(1, 10);
 assertEquals(1, pageCountPaging.getPage());
 assertEquals(10, pageCountPaging.getCount());
 assertEquals(-1L, pageCountPaging.getSinceId());
 assertEquals(-1L, pageCountPaging.getMaxId());

 Paging pageSinceIdPaging = new Paging(1, 10, 1L);
 assertEquals(1, pageSinceIdPaging.getPage());
 assertEquals(10, pageSinceIdPaging.getCount());
 assertEquals(1L, pageSinceIdPaging.getSinceId());
 assertEquals(-1L, pageSinceIdPaging.getMaxId());

 Paging pageCountSinceIdPaging = new Paging(1, 10, 1L, 2L);
 assertEquals(1, pageCountSinceIdPaging.getPage());
 assertEquals(10, pageCountSinceIdPaging.getCount());
 assertEquals(1L, pageCountSinceIdPaging.getSinceId());
 assertEquals(2L, pageCountSinceIdPaging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();

 paging.setPage(5);
 assertEquals(5, paging.getPage());

 paging.setCount(25);
 assertEquals(25, paging.getCount());

 paging.setSinceId(100L);
 assertEquals(100L, paging.getSinceId());

 paging.setMaxId(200L);
 assertEquals(200L, paging.getMaxId());
 }

 @Test
 public void testEqualsAndHashCode() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 assertTrue(paging1.equals(paging2));
 assertTrue(paging1.hashCode() == paging2.hashCode());

 Paging paging3 = new Paging(1, 11, 1L, 2L);
 assertFalse(paging1.equals(paging3));
 assertFalse(paging1.hashCode() == paging3.hashCode());

 Paging paging4 = new Paging(1, 10, 1L, 3L);
 assertFalse(paging1.equals(paging4));
 assertFalse(paging1.hashCode() == paging4.hashCode());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 HttpParameter[] expected = new HttpParameter[] {
 new HttpParameter("page", "1"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "2")
 };
 HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
 assertArrayEquals(expected, result);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 List<HttpParameter> expected = new ArrayList<HttpParameter>() {{
 add(new HttpParameter("page", "1"));
 add(new HttpParameter("count", "10"));
 add(new HttpParameter("since_id", "1"));
 add(new HttpParameter("max_id", "2"));
 }};
 List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expected, result);
 }

 @Test
 public void testAddPostParameter() {
 List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
 Paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 1L);
 assertEquals(1, pagingParams.size());
 assertEquals("since_id", pagingParams.get(0).getName());
 assertEquals("1", pagingParams.get(0).getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
 Paging.addPostParameter(Paging.SMCP, 'a', pagingParams, "param", 1L);
 }
}