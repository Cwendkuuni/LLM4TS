import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testPagingConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());

 Paging pageOnlyPaging = new Paging(1);
 assertEquals(1, pageOnlyPaging.getPage());
 assertEquals(-1, pageOnlyPaging.getCount());
 assertEquals(-1L, pageOnlyPaging.getSinceId());
 assertEquals(-1L, pageOnlyPaging.getMaxId());

 Paging sinceIdOnlyPaging = new Paging(-1L);
 assertEquals(-1, sinceIdOnlyPaging.getPage());
 assertEquals(-1, sinceIdOnlyPaging.getCount());
 assertEquals(-1L, sinceIdOnlyPaging.getSinceId());
 assertEquals(-1L, sinceIdOnlyPaging.getMaxId());

 Paging pageAndCountPaging = new Paging(1, 2);
 assertEquals(1, pageAndCountPaging.getPage());
 assertEquals(2, pageAndCountPaging.getCount());
 assertEquals(-1L, pageAndCountPaging.getSinceId());
 assertEquals(-1L, pageAndCountPaging.getMaxId());

 Paging pageAndCountAndSinceIdPaging = new Paging(1, 2, 3L);
 assertEquals(1, pageAndCountAndSinceIdPaging.getPage());
 assertEquals(2, pageAndCountAndSinceIdPaging.getCount());
 assertEquals(3L, pageAndCountAndSinceIdPaging.getSinceId());
 assertEquals(-1L, pageAndCountAndSinceIdPaging.getMaxId());

 Paging pageAndCountAndMaxIdPaging = new Paging(1, 2, -1L, 3L);
 assertEquals(1, pageAndCountAndMaxIdPaging.getPage());
 assertEquals(2, pageAndCountAndMaxIdPaging.getCount());
 assertEquals(-1L, pageAndCountAndMaxIdPaging.getSinceId());
 assertEquals(3L, pageAndCountAndMaxIdPaging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();

 assertFalse(paging.isPageSet());
 assertFalse(paging.isCountSet());
 assertFalse(paging.isSinceIdSet());
 assertFalse(paging.isMaxIdSet());

 paging.setPage(1);
 assertTrue(paging.isPageSet());
 assertFalse(paging.isCountSet());
 assertFalse(paging.isSinceIdSet());
 assertFalse(paging.isMaxIdSet());

 paging.setCount(2);
 assertTrue(paging.isPageSet());
 assertTrue(paging.isCountSet());
 assertFalse(paging.isSinceIdSet());
 assertFalse(paging.isMaxIdSet());

 paging.setSinceId(3L);
 assertTrue(paging.isPageSet());
 assertTrue(paging.isCountSet());
 assertTrue(paging.isSinceIdSet());
 assertFalse(paging.isMaxIdSet());

 paging.setMaxId(4L);
 assertTrue(paging.isPageSet());
 assertTrue(paging.isCountSet());
 assertTrue(paging.isSinceIdSet());
 assertTrue(paging.isMaxIdSet());
 }

 @Test
 public void testEqualsAndHashcode() {
 Paging paging1 = new Paging(1, 2, 3L, 4L);
 Paging paging2 = new Paging(1, 2, 3L, 4L);
 Paging paging3 = new Paging(1, 2, 3L, 5L);

 assertEquals(paging1, paging2);
 assertEquals(paging1.hashCode(), paging2.hashCode());

 assertNotEquals(paging1, paging3);
 assertNotEquals(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 2, 3L, 4L);
 assertEquals("Paging{page=1, count=2, sinceId=3, maxId=4}", paging.toString());
 }

 @Test
 public void testAsPostParameterListAndArray() {
 Paging paging = new Paging(1, 2, 3L, 4L);

 List<HttpParameter> list = paging.asPostParameterList();
 HttpParameter[] array = paging.asPostParameterArray();

 assertEquals(4, list.size());
 assertEquals(4, array.length);

 for (int i = 0; i < list.size(); i++) {
 assertEquals(list.get(i).getName(), array[i].getName());
 assertEquals(list.get(i).getValue(), array[i].getValue());
 }
 }

 @Test
 public void testAddPostParameterWithUnsupportedParam() {
 char[] supportedParams = Paging.S;
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = paging.asPostParameterList(supportedParams, "unsupportedParam");
 assertEquals(0, pagingParams.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithNegativeValue() {
 char[] supportedParams = Paging.SMCP;
 Paging paging = new Paging();
 paging.addPostParameter(supportedParams, 'c', pagingParams, "count", -1);
 }
}