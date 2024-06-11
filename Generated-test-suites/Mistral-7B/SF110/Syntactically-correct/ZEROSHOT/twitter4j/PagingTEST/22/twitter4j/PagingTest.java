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
 public void testPagingWithPage() {
 Paging paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingWithSinceId() {
 Paging paging = new Paging(1L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingWithPageAndCount() {
 Paging paging = new Paging(1, 10);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingWithPageAndSinceId() {
 Paging paging = new Paging(1, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingWithPageCountAndSinceId() {
 Paging paging = new Paging(1, 10, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingWithPageCountSinceIdAndMaxId() {
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
 public void testIllegalArgumentExceptionSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionCount() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionPage() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging();
 Paging paging2 = new Paging();
 assertFalse(paging1.equals(paging2));

 Paging paging3 = new Paging(1);
 Paging paging4 = new Paging(1);
 assertTrue(paging3.equals(paging4));

 Paging paging5 = new Paging(1, 10);
 Paging paging6 = new Paging(1, 10);
 assertTrue(paging5.equals(paging6));

 Paging paging7 = new Paging(1, 10, 1L, 11L);
 Paging paging8 = new Paging(1, 10, 1L, 11L);
 assertTrue(paging7.equals(paging8));
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 10, 1L, 11L);
 HttpParameter[] paramArray = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, paramArray.length);
 assertEquals("count", paramArray[2].getName());
 assertEquals("10", paramArray[2].getValue());
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 10, 1L, 11L);
 List<HttpParameter> paramList = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(4, paramList.size());
 assertEquals("count", paramList.get(2).getName());
 assertEquals("10", paramList.get(2).getValue());
 }
}