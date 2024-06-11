import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

 @Test
 public void testConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());

 Paging pagePaging = new Paging(5);
 assertEquals(5, pagePaging.getPage());
 assertEquals(-1, pagePaging.getCount());
 assertEquals(-1L, pagePaging.getSinceId());
 assertEquals(-1L, pagePaging.getMaxId());

 Paging sinceIdPaging = new Paging(10L);
 assertEquals(-1, sinceIdPaging.getPage());
 assertEquals(-1, sinceIdPaging.getCount());
 assertEquals(10L, sinceIdPaging.getSinceId());
 assertEquals(-1L, sinceIdPaging.getMaxId());

 Paging pageCountPaging = new Paging(5, 10);
 assertEquals(5, pageCountPaging.getPage());
 assertEquals(10, pageCountPaging.getCount());
 assertEquals(-1L, pageCountPaging.getSinceId());
 assertEquals(-1L, pageCountPaging.getMaxId());

 Paging pageCountSinceIdPaging = new Paging(5, 10, 20L);
 assertEquals(5, pageCountSinceIdPaging.getPage());
 assertEquals(10, pageCountSinceIdPaging.getCount());
 assertEquals(20L, pageCountSinceIdPaging.getSinceId());
 assertEquals(-1L, pageCountSinceIdPaging.getMaxId());

 Paging pageCountSinceIdMaxIdPaging = new Paging(5, 10, 20L, 30L);
 assertEquals(5, pageCountSinceIdMaxIdPaging.getPage());
 assertEquals(10, pageCountSinceIdMaxIdPaging.getCount());
 assertEquals(20L, pageCountSinceIdMaxIdPaging.getSinceId());
 assertEquals(30L, pageCountSinceIdMaxIdPaging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();

 paging.setPage(5);
 assertEquals(5, paging.getPage());

 paging.setCount(10);
 assertEquals(10, paging.getCount());

 paging.setSinceId(20L);
 assertEquals(20L, paging.getSinceId());

 paging.setMaxId(30L);
 assertEquals(30L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidValues() {
 Paging paging = new Paging();

 paging.setPage(-1);

 paging.setCount(-1);

 paging.setSinceId(-1L);
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 2, 3L, 4L);
 Paging paging2 = new Paging(1, 2, 3L, 4L);
 assertTrue(paging1.equals(paging2));

 Paging paging3 = new Paging(1, 2, 3L, 5L);
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 2, 3L, 4L);
 Paging paging2 = new Paging(1, 2, 3L, 4L);
 assertEquals(paging1.hashCode(), paging2.hashCode());

 Paging paging3 = new Paging(1, 2, 3L, 5L);
 assertNotEquals(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(5, 10, 20L, 30L);
 assertEquals("Paging{page=5, count=10, sinceId=20, maxId=30}", paging.toString());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(5, 10, 20L, 30L);
 HttpParameter[] expected = new HttpParameter[] {
 new HttpParameter("page", "5"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "20"),
 new HttpParameter("max_id", "30") };
 HttpParameter[] actual = paging.asPostParameterArray();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(5, 10, 20L, 30L);
 List<HttpParameter> expected = Arrays.asList(
 new HttpParameter("page", "5"),
 new HttpParameter("count", "10"),
 new HttpParameter("since_id", "20"),
 new HttpParameter("max_id", "30") );
 List<HttpParameter> actual = paging.asPostParameterList();
 assertEquals(expected, actual);
 }
}