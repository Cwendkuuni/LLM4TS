import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

 @Test
 public void testPagingConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());

 Paging paging1 = new Paging(1);
 assertEquals(1, paging1.getPage());
 assertEquals(-1, paging1.getCount());
 assertEquals(-1L, paging1.getSinceId());
 assertEquals(-1L, paging1.getMaxId());

 Paging paging2 = new Paging(1, 10);
 assertEquals(1, paging2.getPage());
 assertEquals(10, paging2.getCount());
 assertEquals(-1L, paging2.getSinceId());
 assertEquals(-1L, paging2.getMaxId());

 Paging paging3 = new Paging(1, 10, 1L);
 assertEquals(1, paging3.getPage());
 assertEquals(10, paging3.getCount());
 assertEquals(1L, paging3.getSinceId());
 assertEquals(-1L, paging3.getMaxId());

 Paging paging4 = new Paging(1, 10, 1L, 2L);
 assertEquals(1, paging4.getPage());
 assertEquals(10, paging4.getCount());
 assertEquals(1L, paging4.getSinceId());
 assertEquals(2L, paging4.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();

 // Testing page getter and setter
 paging.setPage(5);
 assertEquals(5, paging.getPage());

 // Testing count getter and setter
 paging.setCount(20);
 assertEquals(20, paging.getCount());

 // Testing sinceId getter and setter
 paging.setSinceId(123L);
 assertEquals(123L, paging.getSinceId());

 // Testing maxId getter and setter
 paging.setMaxId(321L);
 assertEquals(321L, paging.getMaxId());
 }

 @Test
 public void testEqualsAndHashCode() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 assertTrue(paging1.equals(paging2));
 assertEquals(paging1.hashCode(), paging2.hashCode());

 Paging paging3 = new Paging(1, 10, 1L, 3L);
 assertFalse(paging1.equals(paging3));
 assertNotSame(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=2}", paging.toString());
 }

 @Test
 public void testAsPostParameterArrayAndList() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 HttpParameter[] parametersArray = paging.asPostParameterArray('s', "since_id");
 List<HttpParameter> parametersList = paging.asPostParameterList();

 assertEquals(4, parametersList.size());
 assertEquals("since_id=1", parametersArray[0].getName());
 assertEquals("page=1", parametersList.get(1).getName());
 assertEquals("count=10", parametersList.get(2).getName());
 assertEquals("max_id=2", parametersList.get(3).getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = new Paging();
 List<HttpParameter> parametersList = paging.asPostParameterList(Paging.SMCP, "unsupportedParam");
 }
}