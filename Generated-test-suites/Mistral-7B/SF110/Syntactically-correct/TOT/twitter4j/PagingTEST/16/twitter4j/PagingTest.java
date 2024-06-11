package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testDefaultConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPageConstructor() {
 Paging paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testSinceIdConstructor() {
 Paging paging = new Paging(1L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPageCountConstructor() {
 Paging paging = new Paging(1, 5);
 assertEquals(1, paging.getPage());
 assertEquals(5, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPageSinceIdConstructor() {
 Paging paging = new Paging(1, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPageCountSinceIdConstructor() {
 Paging paging = new Paging(1, 5, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(5, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPageCountSinceIdMaxIdConstructor() {
 Paging paging = new Paging(1, 5, 1L, 2L);
 assertEquals(1, paging.getPage());
 assertEquals(5, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(2L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging(1, 5, 1L, 2L);
 assertEquals(1, paging.getPage());
 paging.setPage(2);
 assertEquals(2, paging.getPage());

 assertEquals(5, paging.getCount());
 paging.setCount(10);
 assertEquals(10, paging.getCount());

 assertEquals(1L, paging.getSinceId());
 paging.setSinceId(3L);
 assertEquals(3L, paging.getSinceId());

 assertEquals(2L, paging.getMaxId());
 paging.setMaxId(4L);
 assertEquals(4L, paging.getMaxId());
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 5, 1L, 2L);
 List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(4, params.size());
 HttpParameter param = params.get(0);
 assertEquals("page", param.getName());
 assertEquals("1", param.getValue());

 param = params.get(1);
 assertEquals("count", param.getName());
 assertEquals("5", param.getValue());

 param = params.get(2);
 assertEquals("since_id", param.getName());
 assertEquals("1", param.getValue());

 param = params.get(3);
 assertEquals("max_id", param.getName());
 assertEquals("2", param.getValue());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 5, 1L, 2L);
 HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, params.length);
 HttpParameter param = params[0];
 assertEquals("page", param.getName());
 assertEquals("1", param.getValue());

 param = params[1];
 assertEquals("count", param.getName());
 assertEquals("5", param.getValue());

 param = params[2];
 assertEquals("since_id", param.getName());
 assertEquals("1", param.getValue());

 param = params[3];
 assertEquals("max_id", param.getName());
 assertEquals("2", param.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPostParameterNegativeValue() {
 Paging paging = new Paging();
 List<HttpParameter> params = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 'p', params, "page", -1);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsupportedParameter() {
 Paging paging = new Paging();
 List<HttpParameter> params = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 'x', params, "param", 1);
 assertEquals(0, params.size());
 fail("IllegalStateException expected");
 }
}