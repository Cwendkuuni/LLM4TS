package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testConstructors() {
 Paging paging = new Paging();
 assertNull(paging.getPage());
 assertNull(paging.getCount());
 assertNull(paging.getSinceId());
 assertNull(paging.getMaxId());

 Paging page1Paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertNull(paging.getCount());
 assertNull(paging.getSinceId());
 assertNull(paging.getMaxId());

 Paging sinceIdPaging = new Paging(-1L);
 assertNull(paging.getPage());
 assertNull(paging.getCount());
 assertNotNull(paging.getSinceId());
 assertEquals(-1L, paging.getSinceId().longValue());

 Paging pageCountPaging = new Paging(1, 10);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertNull(paging.getSinceId());
 assertNull(paging.getMaxId());

 Paging pageSinceIdPaging = new Paging(1, -1L);
 assertEquals(1, paging.getPage());
 assertNull(paging.getCount());
 assertNotNull(paging.getSinceId());
 assertEquals(-1L, paging.getSinceId().longValue());

 Paging pageCountSinceIdPaging = new Paging(1, 10, -1L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertNotNull(paging.getSinceId());
 assertEquals(-1L, paging.getSinceId().longValue());

 Paging pageCountSinceIdMaxIdPaging = new Paging(1, 10, -1L, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertNotNull(paging.getSinceId());
 assertEquals(-1L, paging.getSinceId().longValue());
 assertNotNull(paging.getMaxId());
 assertEquals(1L, paging.getMaxId().longValue());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();

 assertNull(paging.getPage());
 paging.setPage(1);
 assertEquals(1, paging.getPage());

 assertNull(paging.getCount());
 paging.setCount(10);
 assertEquals(10, paging.getCount());

 assertNull(paging.getSinceId());
 paging.setSinceId(-1L);
 assertNotNull(paging.getSinceId());
 assertEquals(-1L, paging.getSinceId().longValue());

 assertNull(paging.getMaxId());
 paging.setMaxId(1L);
 assertNotNull(paging.getMaxId());
 assertEquals(1L, paging.getMaxId().longValue());
 }

 @Test
 public void testEqualsAndHashcode() {
 Paging paging1 = new Paging(1, 10, -1L, 1L);
 Paging paging2 = new Paging(1, 10, -1L, 1L);
 Paging paging3 = new Paging(1, 11, -1L, 1L);

 assertTrue(paging1.equals(paging2));
 assertTrue(paging1.hashCode() == paging2.hashCode());

 assertFalse(paging1.equals(paging3));
 assertFalse(paging1.hashCode() == paging3.hashCode());

 Paging paging4 = new Paging();
 assertFalse(paging1.equals(paging4));
 assertFalse(paging1.hashCode() == paging4.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, -1L, 1L);
 assertEquals("Paging{page=1, count=10, sinceId=-1, maxId=1}", paging.toString());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 10, -1L, 1L);
 HttpParameter[] expected = new HttpParameter[] {
 new HttpParameter("page", "1"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "-1"),
 new HttpParameter("max_id", "1")
 };
 HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
 assertArrayEquals(expected, result);
 }

 @Test
 public void testAsPostParameterArrayWithInvalidParameter() {
 try {
 Paging paging = new Paging();
 HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "invalidParam");
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 10, -1L, 1L);
 List<HttpParameter> expected = new ArrayList<HttpParameter>() {{
 add(new HttpParameter("page", "1"));
 add(new HttpParameter("count", "10"));
 add(new HttpParameter("since_id", "-1"));
 add(new HttpParameter("max_id", "1"));
 }};
 List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expected, result);
 }

 @Test
 public void testAsPostParameterListWithEmptyList() {
 Paging paging = new Paging();
 List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
 assertNotNull(result);
 assertEquals(0, result.size());
 }
}