package twitter4j.test;

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
 paging.setPage(2);
 paging.setCount(20);
 paging.setSinceId(1L);
 paging.setMaxId(2L);

 assertEquals(2, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(2L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersInvalidArguments() {
 Paging paging = new Paging();
 paging.setPage(-1);
 paging.setCount(-1);
 paging.setSinceId(-1L);
 paging.setMaxId(-1L);
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 Paging paging3 = new Paging(1, 10, 1L, 3L);

 assertTrue(paging1.equals(paging2));
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 Paging paging3 = new Paging(1, 10, 1L, 3L);

 assertEquals(paging1.hashCode(), paging2.hashCode());
 assertNotSame(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=2}", paging.toString());
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(4, parameters.size());
 for (HttpParameter parameter : parameters) {
 System.out.println(parameter.getName() + "=" + parameter.getValue());
 }
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 HttpParameter[] parameters = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, parameters.length);
 for (HttpParameter parameter : Arrays.asList(parameters)) {
 System.out.println(parameter.getName() + "=" + parameter.getValue());
 }
 }
}