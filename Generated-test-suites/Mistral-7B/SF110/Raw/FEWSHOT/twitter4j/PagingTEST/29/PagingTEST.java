package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class PagingTest {

 @Test
 public void testConstructors() {
 Paging paging1 = new Paging();
 Paging paging2 = new Paging(1);
 Paging paging3 = new Paging(1, 2);
 Paging paging4 = new Paging(1, 2, 3);
 Paging paging5 = new Paging(1, 2, 3, 4);

 assertEquals(-1, paging1.getPage());
 assertEquals(-1, paging1.getCount());
 assertEquals(-1L, paging1.getSinceId());
 assertEquals(-1L, paging1.getMaxId());

 assertEquals(1, paging2.getPage());
 assertEquals(-1, paging2.getCount());
 assertEquals(-1L, paging2.getSinceId());
 assertEquals(-1L, paging2.getMaxId());

 assertEquals(1, paging3.getPage());
 assertEquals(2, paging3.getCount());
 assertEquals(-1L, paging3.getSinceId());
 assertEquals(-1L, paging3.getMaxId());

 assertEquals(1, paging4.getPage());
 assertEquals(2, paging4.getCount());
 assertEquals(3L, paging4.getSinceId());
 assertEquals(-1L, paging4.getMaxId());

 assertEquals(1, paging5.getPage());
 assertEquals(2, paging5.getCount());
 assertEquals(3L, paging5.getSinceId());
 assertEquals(4L, paging5.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging(1, 2, 3, 4);

 assertEquals(1, paging.getPage());
 assertEquals(2, paging.getCount());
 assertEquals(3L, paging.getSinceId());
 assertEquals(4L, paging.getMaxId());

 paging.setPage(5);
 assertEquals(5, paging.getPage());

 paging.setCount(6);
 assertEquals(6, paging.getCount());

 paging.setSinceId(7L);
 assertEquals(7L, paging.getSinceId());

 paging.setMaxId(8L);
 assertEquals(8L, paging.getMaxId());
 }

 @Test
 public void testEqualsAndHashCode() {
 Paging paging1 = new Paging(1, 2, 3, 4);
 Paging paging2 = new Paging(1, 2, 3, 4);
 Paging paging3 = new Paging(1, 2, 3, 5);

 assertTrue(paging1.equals(paging2));
 assertTrue(paging1.hashCode() == paging2.hashCode());

 assertFalse(paging1.equals(paging3));
 assertFalse(paging1.hashCode() == paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 2, 3, 4);

 assertEquals("Paging{page=1, count=2, sinceId=3, maxId=4}", paging.toString());
 }

 @Test
 public void testAsPostParameterListAndArray() {
 Paging paging = new Paging(1, 2, 3, 4);

 List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");
 HttpParameter[] array = paging.asPostParameterArray(Paging.SMCP, "count");

 assertEquals(4, list.size());
 assertEquals("page", list.get(0).getName());
 assertEquals("1", list.get(0).getValue());
 assertEquals("count", list.get(3).getName());
 assertEquals("2", list.get(3).getValue());

 assertEquals(4, array.length);
 assertEquals("page", array[0].getName());
 assertEquals("1", array[0].getValue());
 assertEquals("count", array[3].getName());
 assertEquals("2", array[3].getValue());
 }

 @Test
 public void testAddPostParameter() {
 List<HttpParameter> list = new ArrayList<>();
 Paging paging = new Paging(1, 2, 3, 4);

 paging.addPostParameter(Paging.S, 's', list, "since_id", 5L);
 paging.addPostParameter(Paging.S, 'm', list, "max_id", 6L);

 assertEquals(2, list.size());
 assertEquals("since_id", list.get(0).getName());
 assertEquals("5", list.get(0).getValue());
 assertEquals("max_id", list.get(1).getName());
 assertEquals("6", list.get(1).getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterUnsupported() {
 List<HttpParameter> list = new ArrayList<>();
 Paging paging = new Paging(1, 2, 3, 4);

 paging.addPostParameter(Paging.S, 'a', list, "paramName", 5L);
 }
}