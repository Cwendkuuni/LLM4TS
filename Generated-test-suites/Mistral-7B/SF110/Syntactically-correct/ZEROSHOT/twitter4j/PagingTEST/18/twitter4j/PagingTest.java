import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testPagingConstructorWithDefaultValues() {
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
 Paging paging = new Paging(1, 20);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
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
 Paging paging = new Paging(1, 20, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testPagingConstructorWithPageCountAndMaxId() {
 Paging paging = new Paging(1, 20, 1L, 100L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(20);
 paging.setSinceId(1L);
 paging.setMaxId(100L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageNegativeValue() {
 Paging paging = new Paging();
 paging.setPage(-1);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountNegativeValue() {
 Paging paging = new Paging();
 paging.setCount(-1);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdNegativeValue() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 20, 1L, 100L);
 Paging paging2 = new Paging(1, 20, 1L, 100L);
 assertTrue(paging1.equals(paging2));

 Paging paging3 = new Paging(2, 20, 1L, 100L);
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 20, 1L, 100L);
 Paging paging2 = new Paging(1, 20, 1L, 100L);
 assertEquals(paging1.hashCode(), paging2.hashCode());

 Paging paging3 = new Paging(2, 20, 1L, 100L);
 assertNotEquals(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 20, 1L, 100L);
 assertEquals("Paging{page=1, count=20, sinceId=1, maxId=100}", paging.toString());
 }

 @Test
 public void testAsPostParameterArrayWithAllParams() {
 Paging paging = new Paging(1, 20, 1L, 100L);
 HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, params.length);
 assertEquals("page", params[0].getName());
 assertEquals("1", params[0].getValue());
 assertEquals("count", params[1].getName());
 assertEquals("20", params[1].getValue());
 assertEquals("since_id", params[2].getName());
 assertEquals("1", params[2].getValue());
 assertEquals("max_id", params[3].getName());
 assertEquals("100", params[3].getValue());
 }

 @Test
 public void testAsPostParameterArrayWithOnlyCountParam() {
 Paging paging = new Paging(1, 20);
 HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(1, params.length);
 assertEquals("count", params[0].getName());
 assertEquals("20", params[0].getValue());
 }
}