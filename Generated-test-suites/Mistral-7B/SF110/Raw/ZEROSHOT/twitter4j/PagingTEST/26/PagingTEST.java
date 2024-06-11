import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testDefaultConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPage() {
 Paging paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithSinceId() {
 Paging paging = new Paging(1L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCount() {
 Paging paging = new Paging(1, 2);
 assertEquals(1, paging.getPage());
 assertEquals(2, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndSinceId() {
 Paging paging = new Paging(1, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithAllParameters() {
 Paging paging = new Paging(1, 2, 1L, 2L);
 assertEquals(1, paging.getPage());
 assertEquals(2, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(2L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(2);
 paging.setSinceId(1L);
 paging.setMaxId(2L);
 assertEquals(1, paging.getPage());
 assertEquals(2, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(2L, paging.getMaxId());
 }

 @Test
 public void testEqualsAndHashCode() {
 Paging paging1 = new Paging(1, 2, 1L, 2L);
 Paging paging2 = new Paging(1, 2, 1L, 2L);
 Paging paging3 = new Paging(1, 3, 1L, 2L);
 assertTrue(paging1.equals(paging2));
 assertTrue(paging1.hashCode() == paging2.hashCode());
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 2, 1L, 2L);
 assertEquals("Paging{page=1, count=2, sinceId=1, maxId=2}", paging.toString());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 2, 1L, 2L);
 HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "per_page");
 assertEquals(4, params.length);
 assertEquals("page", params[0].getName());
 assertEquals("1", params[0].getValue());
 assertEquals("per_page", params[1].getName());
 assertEquals("2", params[1].getValue());
 assertEquals("since_id", params[2].getName());
 assertEquals("1", params[2].getValue());
 assertEquals("max_id", params[3].getName());
 assertEquals("2", params[3].getValue());
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 2, 1L, 2L);
 List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "per_page");
 assertEquals(4, params.size());
 assertEquals("page", params.get(0).getName());
 assertEquals("1", params.get(0).getValue());
 assertEquals("per_page", params.get(1).getName());
 assertEquals("2", params.get(1).getValue());
 assertEquals("since_id", params.get(2).getName());
 assertEquals("1", params.get(2).getValue());
 assertEquals("max_id", params.get(3).getName());
 assertEquals("2", params.get(3).getValue());
 }
}