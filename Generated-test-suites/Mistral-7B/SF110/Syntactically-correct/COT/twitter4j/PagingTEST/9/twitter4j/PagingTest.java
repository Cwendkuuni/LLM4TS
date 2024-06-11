import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import twitter4j.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

 private Paging createPagingObject(int page, int count, long sinceId, long maxId) {
 return new Paging(page, count, sinceId, maxId);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging();
 List<HttpParameter> params = paging.asPostParameterList();
 assertNotNull(params);
 assertEquals(0, params.size());
 }

 @Test
 public void testAsPostParameterListWithSupportedParams() {
 Paging paging = new Paging(1, 20, 1L, 2L);
 List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
 assertNotNull(params);
 assertEquals(4, params.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAsPostParameterListWithUnsupportedParam() {
 Paging paging = new Paging(1, 20, 1L, 2L);
 List<HttpParameter> params = paging.asPostParameterList(new char[]{'x'}, "count");
 }

 @Test
 public void testAsPostParameterListWithEmptyList() {
 Paging paging = new Paging();
 HttpParameter[] paramsArray = paging.asPostParameterArray();
 assertNotNull(paramsArray);
 assertEquals(0, paramsArray.length);
 }

 @Test
 public void testAsPostParameterListWithSupportedParamsAndArray() {
 Paging paging = new Paging(1, 20, 1L, 2L);
 HttpParameter[] paramsArray = paging.asPostParameterArray(Paging.SMCP, "count");
 assertNotNull(paramsArray);
 assertEquals(4, paramsArray.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsPostParameterListWithUnsupportedParamAndArray() {
 Paging paging = new Paging(1, 20, 1L, 2L);
 HttpParameter[] paramsArray = paging.asPostParameterArray(new char[]{'x'}, "count");
 }

 @Test
 public void testAddPostParameterWithSupportedParam() {
 List<HttpParameter> params = new ArrayList<>();
 Paging.addPostParameter(Paging.SMCP, 's', params, "since_id", 1L);
 assertEquals(1, params.size());
 HttpParameter param = params.get(0);
 assertEquals("since_id", param.getName());
 assertEquals("1", param.getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 List<HttpParameter> params = new ArrayList<>();
 Paging.addPostParameter(new char[]{'x'}, 's', params, "since_id", 1L);
 }

 @Test
 public void testConstructors() {
 Paging paging1 = new Paging();
 Paging paging2 = new Paging(1);
 Paging paging3 = new Paging(1, 20);
 Paging paging4 = new Paging(1, 20, 1L);
 Paging paging5 = new Paging(1, 20, 1L, 2L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithNegativePage() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithNegativeCount() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithNegativeSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxIdWithNegativeMaxId() {
 Paging paging = new Paging();
 paging.setMaxId(-1L);
 }

}