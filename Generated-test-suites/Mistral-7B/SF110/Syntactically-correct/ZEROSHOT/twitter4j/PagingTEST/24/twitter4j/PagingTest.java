import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import twitter4j.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

 @Test
 public void testPagingInstance() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingPage() {
 Paging paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingSinceId() {
 Paging paging = new Paging(System.currentTimeMillis());
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertNotSame(-1L, paging.getSinceId());
 assertEquals(-1, paging.getMaxId());
 }

 @Test
 public void testPagingPageAndCount() {
 Paging paging = new Paging(1, 20);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(-1, paging.getSinceId());
 assertEquals(-1, paging.getMaxId());
 }

 @Test
 public void testPagingPageAndCountAndSinceId() {
 Paging paging = new Paging(1, 20, System.currentTimeMillis());
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertNotSame(-1L, paging.getSinceId());
 assertEquals(-1, paging.getMaxId());
 }

 @Test
 public void testPagingPageAndCountAndSinceIdAndMaxId() {
 Paging paging = new Paging(1, 20, System.currentTimeMillis(), 100L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertNotSame(-1L, paging.getSinceId());
 assertNotSame(-1L, paging.getMaxId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 20, System.currenttimeMillis(), 100L);
 HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, params.length);
 HttpParameter sinceIdParam = null;
 HttpParameter pageParam = null;
 HttpParameter countParam = null;
 HttpParameter maxIdParam = null;
 for (HttpParameter param : params) {
 switch (param.getName().charAt(0)) {
 case 's':
 sinceIdParam = param;
 break;
 case 'p':
 pageParam = param;
 break;
 case 'c':
 countParam = param;
 break;
 case 'm':
 maxIdParam = param;
 break;
 }
 }
 assertNotNull(sinceIdParam);
 assertEquals("since_id", sinceIdParam.getName());
 assertEquals("" + System.currenttimeMillis(), sinceIdParam.getValue());
 assertNotNull(pageParam);
 assertEquals("page", pageParam.getName());
 assertEquals("1", pageParam.getValue());
 assertNotNull(countParam);
 assertEquals("count", countParam.getName());
 assertEquals("20", countParam.getValue());
 assertNotNull(maxIdParam);
 assertEquals("max_id", maxIdParam.getName());
 assertEquals("100", maxIdParam.getValue());
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 20, System.currenttimeMillis(), 100L);
 List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(4, params.size());
 HttpParameter sinceIdParam = null;
 HttpParameter pageParam = null;
 HttpParameter countParam = null;
 HttpParameter maxIdParam = null;
 for (HttpParameter param : params) {
 switch (param.getName().charAt(0)) {
 case 's':
 sinceIdParam = param;
 break;
 case 'p':
 pageParam = param;
 break;
 case 'c':
 countParam = param;
 break;
 case 'm':
 maxIdParam = param;
 break;
 }
 }
 assertNotNull(sinceIdParam);
 assertEquals("since_id", sinceIdParam.getName());
 assertEquals("" + System.currenttimeMillis(), sinceIdParam.getValue());
 assertNotNull(pageParam);
 assertEquals("page", pageParam.getName());
 assertEquals("1", pageParam.getValue());
 assertNotNull(countParam);
 assertEquals("count", countParam.getName());
 assertEquals("20", countParam.getValue());
 assertNotNull(maxIdParam);
 assertEquals("max_id", maxIdParam.getName());
 assertEquals("100", maxIdParam.getValue());
 }

 @Test
 public void testAsPostParameterArrayAndListEmpty() {
 Paging paging = new Paging();
 HttpParameter[] paramsArray = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(0, paramsArray.length);
 List<HttpParameter> paramsList = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(0, paramsList.size());
 }

 @Test
 public void testAsPostParameterArrayAndList() {
 Paging paging = new Paging(1, 20, System.currenttimeMillis(), 100L);
 HttpParameter[] paramsArray = paging.asPostParameterArray(Paging.SMCP, "count");
 List<HttpParameter> paramsList = paging.asPostParameterList(Paging.SMCP, "count");
 assertArrayEquals(paramsArray, paramsList.toArray(new HttpParameter[paramsList.size()]));
 }
}