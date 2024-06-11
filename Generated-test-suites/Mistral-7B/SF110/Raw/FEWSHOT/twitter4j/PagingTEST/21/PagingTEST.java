package twitter4j;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class PagingTest {

 @Test
 public void testEmptyPaging() {
 Paging paging = new Paging();
 List<HttpParameter> params = paging.asPostParameterList();
 assertNull(params);
 }

 @Test
 public void testBasicPagingWithAllParams() {
 Paging paging = new Paging(2, 10, 1, 2);
 List<HttpParameter> params = paging.asPostParameterList();
 assertEquals(4, params.size());
 assertTrue(params.contains(new HttpParameter("page", "2")));
 assertTrue(params.contains(new HttpParameter("count", "10")));
 assertTrue(params.contains(new HttpParameter("since_id", "1")));
 assertTrue(params.contains(new HttpParameter("max_id", "2")));
 }

 @Test
 public void testBasicPagingWithOnlyPageParam() {
 Paging paging = new Paging(2);
 List<HttpParameter> params = paging.asPostParameterList();
 assertEquals(1, params.size());
 assertTrue(params.contains(new HttpParameter("page", "2")));
 }

 @Test
 public void testBasicPagingWithOnlyCountParam() {
 Paging paging = new Paging(10);
 List<HttpParameter> params = paging.asPostParameterList();
 assertEquals(1, params.size());
 assertTrue(params.contains(new HttpParameter("count", "10")));
 }

 @Test
 public void testBasicPagingWithOnlySinceIdParam() {
 Paging paging = new Paging(1);
 List<HttpParameter> params = paging.asPostParameterList();
 assertEquals(1, params.size());
 assertTrue(params.contains(new HttpParameter("since_id", "1")));
 }

 @Test
 public void testBasicPagingWithOnlyMaxIdParam() {
 Paging paging = new Paging(2, 10, 1, 2L);
 paging.setMaxId(3);
 List<HttpParameter> params = paging.asPostParameterList();
 assertEquals(1, params.size());
 assertTrue(params.contains(new HttpParameter("max_id", "3")));
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidParamAdditionWithNullValue() {
 Paging paging = new Paging();
 List<HttpParameter> params = new ArrayList<HttpParameter>();
 paging.addPostParameter(Paging.SMCP, 'p', params, "page", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidParamAdditionWithNegativeValue() {
 Paging paging = new Paging();
 List<HttpParameter> params = new ArrayList<HttpParameter>();
 paging.addPostParameter(Paging.SMCP, 'c', params, "count", -1);
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging(2, 10, 1, 2);
 assertEquals(2, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1, paging.getSinceId());
 assertEquals(2, paging.getMaxId());
 paging.setPage(3);
 paging.setCount(5);
 paging.setSinceId(0);
 paging.setMaxId(4);
 assertEquals(3, paging.getPage());
 assertEquals(5, paging.getCount());
 assertEquals(0, paging.getSinceId());
 assertEquals(4, paging.getMaxId());
 }
}