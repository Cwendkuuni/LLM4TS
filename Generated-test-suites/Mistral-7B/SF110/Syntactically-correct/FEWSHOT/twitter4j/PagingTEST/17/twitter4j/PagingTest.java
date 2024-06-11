package twitter4j;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class PagingTest {

 @Test
 public void testConstructors() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());

 Paging pagePaging = new Paging(5);
 assertEquals(5, pagePaging.getPage());
 assertEquals(-1, pagePaging.getCount());
 assertEquals(-1L, pagePaging.getSinceId());
 assertEquals(-1L, pagePaging.getMaxId());

 Paging countPaging = new Paging(10);
 assertEquals(-1, countPaging.getPage());
 assertEquals(10, countPaging.getCount());
 assertEquals(-1L, countPaging.getSinceId());
 assertEquals(-1L, countPaging.getMaxId());

 Paging sinceIdPaging = new Paging(1, 5);
 assertEquals(1, sinceIdPaging.getPage());
 assertEquals(5, sinceIdPaging.getCount());
 assertEquals(-1L, sinceIdPaging.getSinceId());
 assertEquals(-1L, sinceIdPaging.getMaxId());

 Paging pageCountPaging = new Paging(1, 10);
 assertEquals(1, pageCountPaging.getPage());
 assertEquals(10, pageCountPaging.getCount());
 assertEquals(-1L, pageCountPaging.getSinceId());
 assertEquals(-1L, pageCountPaging.getMaxId());

 Paging pageSinceIdPaging = new Paging(1, 5, 1L);
 assertEquals(1, pageSinceIdPaging.getPage());
 assertEquals(5, pageSinceIdPaging.getCount());
 assertEquals(1L, pageSinceIdPaging.getSinceId());
 assertEquals(-1L, pageSinceIdPaging.getMaxId());

 Paging pageCountSinceIdPaging = new Paging(1, 10, 1L);
 assertEquals(1, pageCountSinceIdPaging.getPage());
 assertEquals(10, pageCountSinceIdPaging.getCount());
 assertEquals(1L, pageCountSinceIdPaging.getSinceId());
 assertEquals(-1L, pageCountSinceIdPaging.getMaxId());

 Paging pageCountSinceIdMaxIdPaging = new Paging(1, 10, 1L, 2L);
 assertEquals(1, pageCountSinceIdMaxIdPaging.getPage());
 assertEquals(10, pageCountSinceIdMaxIdPaging.getCount());
 assertEquals(1L, pageCountSinceIdMaxIdPaging.getSinceId());
 assertEquals(2L, pageCountSinceIdMaxIdPaging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(5);
 assertEquals(5, paging.getPage());
 paging.setCount(10);
 assertEquals(10, paging.getCount());
 paging.setSinceId(1L);
 assertEquals(1L, paging.getSinceId());
 paging.setMaxId(2L);
 assertEquals(2L, paging.getMaxId());

 Paging pagePaging = new Paging(5);
 pagePaging.setCount(10);
 assertEquals(10, pagePaging.getCount());
 pagePaging.setSinceId(1L);
 assertEquals(1L, pagePaging.getSinceId());
 pagePaging.setMaxId(2L);
 assertEquals(2L, pagePaging.getMaxId());
 }

 @Test
 public void testEqualsAndHashcode() {
 Paging paging1 = new Paging(5, 10, 1L, 2L);
 Paging paging2 = new Paging(5, 10, 1L, 2L);
 assertTrue(paging1.equals(paging2));
 assertEquals(paging1.hashCode(), paging2.hashCode());

 Paging paging3 = new Paging(5, 10, 1L, 3L);
 assertFalse(paging1.equals(paging3));
 assertNotEquals(paging1.hashCode(), paging3.hashCode());

 Paging paging4 = new Paging();
 assertFalse(paging1.equals(paging4));
 assertNotEquals(paging1.hashCode(), paging4.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(5, 10, 1L, 2L);
 assertEquals("Paging{page=5, count=10, sinceId=1, maxId=2}", paging.toString());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(5, 10, 1L, 2L);
 HttpParameter[] expected = new HttpParameter[] {
 new HttpParameter("page", "5"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "2")
 };
 assertEquals(expected.length, paging.asPostParameterArray(Paging.SMCP, "count").length);
 assertArrayEquals(expected, paging.asPostParameterArray(Paging.SMCP, "count"));
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(5, 10, 1L, 2L);
 List<HttpParameter> expected = Arrays.asList(
 new HttpParameter("page", "5"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "2")
 );
 assertEquals(expected, paging.asPostParameterList(Paging.SMCP, "count"));
 }

 @Test
 public void testAddPostParameter() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter('c', 'p', pagingParams, "count", 10);
 assertEquals(1, pagingParams.size());
 assertEquals("count", pagingParams.get(0).getName());
 assertEquals("10", pagingParams.get(0).getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter('x', 'p', pagingParams, "param", 10);
 fail("This line should not be executed as an IllegalStateException should be thrown.");
 }

 @Test
 public void testSetPage() {
 Paging paging = new Paging();
 paging.setPage(-1);
 assertEquals(-1, paging.getPage());

 paging.setPage(0);
 assertEquals(0, paging.getPage());

 paging.setPage(5);
 assertEquals(5, paging.getPage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithInvalidPage() {
 Paging paging = new Paging();
 paging.setPage(-2);
 fail("This line should not be executed as an IllegalArgumentException should be thrown.");
 }

 @Test
 public void testGetPage() {
 Paging paging = new Paging();
 paging.setPage(5);
 assertEquals(5, paging.getPage());
 }

 @Test
 public void testSetCount() {
 Paging paging = new Paging();
 paging.setCount(-1);
 assertEquals(-1, paging.getCount());

 paging.setCount(0);
 assertEquals(0, paging.getCount());

 paging.setCount(10);
 assertEquals(10, paging.getCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithInvalidCount() {
 Paging paging = new Paging();
 paging.setCount(-2);
 fail("This line should not be executed as an IllegalArgumentException should be thrown.");
 }

 @Test
 public void testGetCount() {
 Paging paging = new Paging();
 paging.setCount(5);
 assertEquals(5, paging.getCount());
 }

 @Test
 public void testSetSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 assertEquals(-1L, paging.getSinceId());

 paging.setSinceId(0L);
 assertEquals(0L, paging.getSinceId());

 paging.setSinceId(1L);
 assertEquals(1L, paging.getSinceId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithInvalidSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(-2L);
 fail("This line should not be executed as an IllegalArgumentException should be thrown.");
 }

 @Test
 public void testGetSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(5L);
 assertEquals(5L, paging.getSinceId());
 }

 @Test
 public void testSetMaxId() {
 Paging paging = new Paging();
 paging.setMaxId(-1L);
 assertEquals(-1L, paging.getMaxId());

 paging.setMaxId(0L);
 assertEquals(0L, paging.getMaxId());

 paging.setMaxId(1L);
 assertEquals(1L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxIdWithInvalidMaxId() {
 Paging paging = new Paging();
 paging.setMaxId(-2L);
 fail("This line should not be executed as an IllegalArgumentException should be thrown.");
 }

 @Test
 public void testGetMaxId() {
 Paging paging = new Paging();
 paging.setMaxId(5L);
 assertEquals(5L, paging.getMaxId());
 }

 @Test
 public void testAsPostParameterListWithEmptySupportedParams() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = paging.asPostParameterList(Paging.NULL_PARAMETER_LIST);
 assertEquals(0, pagingParams.size());
 }

 @Test
 public void testAsPostParameterArrayWithEmptySupportedParams() {
 Paging paging = new Paging();
 HttpParameter[] pagingParams = paging.asPostParameterArray(Paging.NULL_PARAMETER_ARRAY);
 assertEquals(0, pagingParams.length);
 }
}