package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;
import twitter4j. internal.http.HttpParameterUtils;

public class PagingTest {

 @Test
 public void testEmptyPaging() {
 Paging paging = new Paging();
 List<HttpParameter> params = paging.asPostParameterList();
 assertNull(params);
 }

 @Test
 public void testPagingWithValidParameters() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 List<HttpParameter> params = paging.asPostParameterList();
 assertNotNull(params);
 assertEquals(4, params.size());
 HttpParameter sinceIdParam = null;
 HttpParameter pageParam = null;
 HttpParameter countParam = null;
 HttpParameter maxIdParam = null;
 for (HttpParameter param : params) {
 switch (param.getName()) {
 case "page":
 pageParam = param;
 break;
 case "count":
 countParam = param;
 break;
 case "since_id":
 sinceIdParam = param;
 break;
 case "max_id":
 maxIdParam = param;
 break;
 }
 }
 assertTrue(pageParam != null);
 assertEquals("1", pageParam.getValue());
 assertTrue(countParam != null);
 assertEquals("10", countParam.getValue());
 assertTrue(sinceIdParam != null);
 assertEquals("1", sinceIdParam.getValue());
 assertTrue(maxIdParam != null);
 assertEquals("2", maxIdParam.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativePageNumber() {
 Paging paging = new Paging(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCount() {
 Paging paging = new Paging(1, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeSinceId() {
 Paging paging = new Paging(1, 1, -1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidParamKey() {
 char[] supportedParams = Paging.SMCP;
 List<HttpParameter> params = paging.asPostParameterList(supportedParams, "z");
 }

 @Test
 public void testNullSinceId() {
 Paging paging = new Paging(1, 1, 0L);
 List<HttpParameter> params = paging.asPostParameterList();
 assertNotNull(params);
 assertEquals(3, params.size());
 assertTrue(params.contains(new HttpParameter("page", "1")));
 assertTrue(params.contains(new HttpParameter("count", "1")));
 assertFalse(params.contains(new HttpParameter("since_id", "0")));
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 1, 1L, 1L);
 Paging paging2 = new Paging(1, 1, 1L, 1L);
 Paging paging3 = new Paging(2, 1, 1L, 1L);
 assertTrue(paging1.equals(paging2));
 assertFalse(paging1.equals(paging3));
 }
}