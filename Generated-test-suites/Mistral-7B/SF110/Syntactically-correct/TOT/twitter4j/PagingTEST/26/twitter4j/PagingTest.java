import org.junit.Test;import static org.junit.Assert.*;import twitter4j.Paging;import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testPagingConstructor() {
 Paging paging = new Paging();
 assertNotNull(paging);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPage() {
 Paging paging = new Paging(1);
 assertNotNull(paging);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithSinceId() {
 Paging paging = new Paging(1L);
 assertNotNull(paging);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageAndCount() {
 Paging paging = new Paging(1, 20);
 assertNotNull(paging);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageCountAndSinceId() {
 Paging paging = new Paging(1, 20, 1L);
 assertNotNull(paging);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageCountSinceIdMaxId() {
 Paging paging = new Paging(1, 20, 1L, 10L);
 assertNotNull(paging);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(10L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(20);
 paging.setSinceId(1L);
 paging.setMaxId(10L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(10L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPageValue() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCountValue() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSinceIdValue() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsupportedParameter() {
 Paging paging = new Paging();
 paging.setPage(-1);
 HttpParameter[] params = paging.asPostParameterArray(new char[] {'a'});
 }

 @Test
 public void testValidPostParametersList() {
 Paging paging = new Paging(1, 20, 1L, 10L);
 List<HttpParameter> params = paging.asPostParameterList(new char[] {'s', 'm', 'c', 'p'});
 assertEquals(4, params.size());
 HttpParameter sinceIdParam = null;
 HttpParameter maxIdParam = null;
 for (HttpParameter param : params) {
 if (param.getName().equals("since_id")) {
 sinceIdParam = param;
 }
 if (param.getName().equals("max_id")) {
 maxIdParam = param;
 }
 }
 assertEquals("1", sinceIdParam.getValue());
 assertEquals("10", maxIdParam.getValue());
 }

 @Test
 public void testValidPostParametersArray() {
 Paging paging = new Paging(1, 20, 1L, 10L);
 HttpParameter[] params = paging.asPostParameterArray(new char[] {'s', 'm', 'c', 'p'});
 assertEquals(4, params.length);
 HttpParameter sinceIdParam = null;
 HttpParameter maxIdParam = null;
 for (HttpParameter param : params) {
 if (param.getName().equals("since_id")) {
 sinceIdParam = param;
 }
 if (param.getName().equals("max_id")) {
 maxIdParam = param;
 }
 }
 assertEquals("1", sinceIdParam.getValue());
 assertEquals("10", maxIdParam.getValue());
 }

}