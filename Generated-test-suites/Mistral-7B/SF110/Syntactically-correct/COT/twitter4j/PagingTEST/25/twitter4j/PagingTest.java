import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject(int page, int count, long sinceId, long maxId) {
 return new Paging(page, count, sinceId, maxId);
 }

 @Test
 public void testListMethods() {
 Paging paging = new Paging();
 List<HttpParameter> list = paging.asPostParameterList();
 assertNotNull(list);
 assertEmpty(list);

 list = paging.asPostParameterList(Paging.SMCP, "count");
 assertNotNull(list);
 assertNotEmpty(list);

 list = paging.asPostParameterList(Paging.SMCP, "per_page");
 assertNotNull(list);
 assertNotEmpty(list);

 HttpParameter[] array = paging.asPostParameterArray();
 assertNotNull(array);
 assertEmpty(array);

 array = paging.asPostParameterArray(Paging.SMCP, "count");
 assertNotNull(array);
 assertNotEmpty(array);
 }

 @Test
 public void testConstructor() {
 Paging paging1 = new Paging();
 assertEquals(-1, paging1.getPage());
 assertEquals(-1, paging1.getCount());
 assertEquals(-1L, paging1.getSinceId());
 assertEquals(-1L, paging1.getMaxId());

 Paging paging2 = new Paging(1);
 assertEquals(1, paging2.getPage());
 assertEquals(-1, paging2.getCount());
 assertEquals(-1L, paging2.getSinceId());
 assertEquals(-1L, paging2.getMaxId());

 Paging paging3 = new Paging(1, 10);
 assertEquals(1, paging3.getPage());
 assertEquals(10, paging3.getCount());
 assertEquals(-1L, paging3.getSinceId());
 assertEquals(-1L, paging3.getMaxId());

 Paging paging4 = new Paging(1, 10, 1L);
 assertEquals(1, paging4.getPage());
 assertEquals(10, paging4.getCount());
 assertEquals(1L, paging4.getSinceId());
 assertEquals(-1L, paging4.getMaxId());

 Paging paging5 = new Paging(1, 10, 1L, 2L);
 assertEquals(1, paging5.getPage());
 assertEquals(10, paging5.getCount());
 assertEquals(1L, paging5.getSinceId());
 assertEquals(2L, paging5.getMaxId());
 }

 @Test
 public void testSetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 assertEquals(1, paging.getPage());

 paging.setCount(10);
 assertEquals(10, paging.getCount());

 paging.setSinceId(1L);
 assertEquals(1L, paging.getSinceId());

 paging.setMaxId(2L);
 assertEquals(2L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPage() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCount() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMaxId() {
 Paging paging = new Paging();
 paging.setMaxId(-1L);
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 assertTrue(paging1.equals(paging2));

 Paging paging3 = new Paging(1, 10, 1L, 3L);
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 assertEquals(paging1.hashCode(), paging2.hashCode());

 Paging paging3 = new Paging(1, 10, 1L, 3L);
 assertNotEquals(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=2}", paging.toString());
 }
}