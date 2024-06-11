package twitter4j;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class PagingTest {

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 char[] supportedParams = new char[]{'s', 'm', 'c', 'p'};
 List<HttpParameter> expected = new ArrayList<HttpParameter>(){{
 add(new HttpParameter("count", "10"));
 add(new HttpParameter("page", "1"));
 add(new HttpParameter("since_id", "1"));
 add(new HttpParameter("max_id", "100"));
 }};
 List<HttpParameter> actual = paging.asPostParameterList(supportedParams, "count");
 assertEquals(expected, actual);
 }

 @Test
 public void testAsPostParameterListWithUnsupportedParam() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 char[] supportedParams = new char[]{'s', 'm', 'c'};
 List<HttpParameter> actual = paging.asPostParameterList(supportedParams, "count");
 assertNull(actual);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 char[] supportedParams = new char[]{'s', 'm', 'c', 'p'};
 HttpParameter[] expected = new HttpParameter[] {
 new HttpParameter("count", "10"),
 new HttpParameter("page", "1"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "100")
 };
 HttpParameter[] actual = paging.asPostParameterArray(supportedParams, "count");
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAsPostParameterArrayWithUnsupportedParam() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 char[] supportedParams = new char[]{'s', 'm', 'c'};
 HttpParameter[] actual = paging.asPostParameterArray(supportedParams, "count");
 assertNull(actual);
 }

 @Test
 public void testAddPostParameter() {
 Paging paging = new Paging();
 char[] supportedParams = new char[]{'s', 'm', 'c', 'p'};
 List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
 paging.addPostParameter(supportedParams, 's', pagingParams, "since_id", 1L);
 assertEquals("since_id", pagingParams.get(0).getName());
 assertEquals("1", pagingParams.get(0).getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = new Paging();
 char[] supportedParams = new char[]{'s', 'm', 'c', 'p'};
 List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
 paging.addPostParameter(supportedParams, 'x', pagingParams, "since_id", 1L);
 }

 @Test
 public void testPagingConstructors() {
 //Empty constructor
 Paging emptyPaging = new Paging();
 assertEquals(-1, emptyPaging.getPage());
 assertEquals(-1, emptyPaging.getCount());
 assertEquals(-1L, emptyPaging.getSinceId());
 assertEquals(-1L, emptyPaging.getMaxId());

 //Constructor with page
 Paging pagePaging = new Paging(1);
 assertEquals(1, pagePaging.getPage());
 assertEquals(-1, pagePaging.getCount());
 assertEquals(-1L, pagePaging.getSinceId());
 assertEquals(-1L, pagePaging.getMaxId());

 //Constructor with sinceId
 Paging sinceIdPaging = new Paging(1L);
 assertEquals(-1, sinceIdPaging.getPage());
 assertEquals(-1, sinceIdPaging.getCount());
 assertEquals(1L, sinceIdPaging.getSinceId());
 assertEquals(-1L, sinceIdPaging.getMaxId());

 //Constructor with page and count
 Paging pageCountPaging = new Paging(1, 1);
 assertEquals(1, pageCountPaging.getPage());
 assertEquals(1, pageCountPaging.getCount());
 assertEquals(-1L, pageCountPaging.getSinceId());
 assertEquals(-1L, pageCountPaging.getMaxId());

 //Constructor with page, count, sinceId
 Paging pageCountSinceIdPaging = new Paging(1, 1, 1L);
 assertEquals(1, pageCountSinceIdPaging.getPage());
 assertEquals(1, pageCountSinceIdPaging.getCount());
 assertEquals(1L, pageCountSinceIdPaging.getSinceId());
 assertEquals(-1L, pageCountSinceIdPaging.getMaxId());

 //Constructor with page, count, sinceId, maxId
 Paging pageCountSinceIdMaxIdPaging = new Paging(1, 1, 1L, 1L);
 assertEquals(1, pageCountSinceIdMaxIdPaging.getPage());
 assertEquals(1, pageCountSinceIdMaxIdPaging.getCount());
 assertEquals(1L, pageCountSinceIdMaxIdPaging.getSinceId());
 assertEquals(1L, pageCountSinceIdMaxIdPaging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(10);
 paging.setSinceId(1L);
 paging.setMaxId(100L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }
}