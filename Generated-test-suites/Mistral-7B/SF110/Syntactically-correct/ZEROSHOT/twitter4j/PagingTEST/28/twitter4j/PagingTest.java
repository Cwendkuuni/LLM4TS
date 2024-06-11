import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

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
 public void testPagingConstructorWithPage() {
 Paging paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithSinceId() {
 Paging paging = new Paging(1L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageAndCount() {
 Paging paging = new Paging(1, 10);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageAndSinceId() {
 Paging paging = new Paging(1, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageCountAndSinceId() {
 Paging paging = new Paging(1, 10, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageCountAndMaxId() {
 Paging paging = new Paging(1, 10, 1L, 11L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(11L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(10);
 paging.setSinceId(1L);
 paging.setMaxId(11L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(11L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidValues() {
 Paging paging = new Paging();
 paging.setPage(-1);
 paging.setCount(-1);
 paging.setSinceId(-1L);
 paging.setMaxId(-1L);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 10, 1L, 11L);
 HttpParameter[] expected = new HttpParameter[] {
 new HttpParameter("page", "1"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "11")
 };
 HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
 assertArrayEquals(expected, result);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 10, 1L, 11L);
 List<HttpParameter> expected = Arrays.asList(
 new HttpParameter("page", "1"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "11")
 );
 List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expected, result);
 }

 @Test
 public void testAsPostParameterListWithEmptySupportedParams() {
 Paging paging = new Paging();
 List<HttpParameter> result = paging.asPostParameterList();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testAsPostParameterArrayWithEmptySupportedParams() {
 Paging paging = new Paging();
 HttpParameter[] result = paging.asPostParameterArray();
 assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, result);
 }
}