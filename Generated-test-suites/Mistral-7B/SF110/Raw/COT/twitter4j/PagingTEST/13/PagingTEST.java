import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import twitter4j.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

 private Paging createInstance() {
 return new Paging();
 }

 @Test
 public void testDefaultConstructor() {
 Paging paging = createInstance();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testIntConstructor() {
 Paging paging = new Paging(5);
 assertEquals(5, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testLongConstructor() {
 Paging paging = new Paging(10L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(10L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testIntConstructorWithCount() {
 Paging paging = new Paging(5, 10);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testIntConstructorWithSinceId() {
 Paging paging = new Paging(5, 10L);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(10L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testIntConstructorWithPageSizeSinceId() {
 Paging paging = new Paging(5, 10, 20L);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(20L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testIntConstructorWithPageSizeSinceIdMaxId() {
 Paging paging = new Paging(5, 10, 20L, 30L);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(20L, paging.getSinceId());
 assertEquals(30L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(5);
 paging.setCount(10);
 paging.setSinceId(20L);
 paging.setMaxId(30L);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(20L, paging.getSinceId());
 assertEquals(30L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageLessThanOne() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountLessThanOne() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdLessThanOne() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(5, 10);
 List<HttpParameter> pagingParams = paging.asPostParameterList(Paging.SMCP, "count");
 assertNotNull(pagingParams);
 assertEquals(3, pagingParams.size());
 assertEquals("page", pagingParams.get(0).getName());
 assertEquals("5", pagingParams.get(0).getValue());
 assertEquals("count", pagingParams.get(1).getName());
 assertEquals("10", pagingParams.get(1).getValue());
 assertEquals("per_page", pagingParams.get(2).getName());
 assertEquals("5", pagingParams.get(2).getValue());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(5, 10);
 HttpParameter[] pagingParams = paging.asPostParameterArray(Paging.SMCP, "count");
 assertNotNull(pagingParams);
 assertEquals(3, pagingParams.length);
 assertEquals("page", pagingParams[0].getName());
 assertEquals("5", pagingParams[0].getValue());
 assertEquals("count", pagingParams[1].getName());
 assertEquals("10", pagingParams[1].getValue());
 assertEquals("per_page", pagingParams[2].getName());
 assertEquals("5", pagingParams[2].getValue());
 }

 @Test
 public void testAddPostParameterWithUnsupportedParamKey() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 'e', pagingParams, "unsupported_param", 10L);
 assertThrows(IllegalStateException.class, () -> paging.addPostParameter(Paging.SMCP, 'e', pagingParams, "unsupported_param", 10L));
 }

 @Test
 public void testAddPostParameterWithNullParamValue() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", null);
 assertNull(pagingParams.get(0).getValue());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(5, 10, 20L, 30L);
 assertEquals("Paging{page=5, count=10, sinceId=20, maxId=30}", paging.toString());
 }

}