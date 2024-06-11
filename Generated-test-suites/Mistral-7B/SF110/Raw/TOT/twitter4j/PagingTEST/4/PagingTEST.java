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
 Paging paging = new Paging(1, 20);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
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
 public void testConstructorWithPageAndCountAndSinceId() {
 Paging paging = new Paging(1, 20, 1L, 2L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(2L, paging.getMaxId());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(20);
 paging.setSinceId(1L);
 paging.setMaxId(2L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(2L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithNegativeValue() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithNegativeValue() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithNegativeValue() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 20, 1L, 2L);
 HttpParameter[] postParameters = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, postParameters.length);
 assertEquals("page", postParameters[0].getName());
 assertEquals("1", postParameters[0].getValue());
 assertEquals("count", postParameters[1].getName());
 assertEquals("20", postParameters[1].getValue());
 assertEquals("since_id", postParameters[2].getName());
 assertEquals("1", postParameters[2].getValue());
 assertEquals("max_id", postParameters[3].getName());
 assertEquals("2", postParameters[3].getValue());
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 20, 1L, 2L);
 List<HttpParameter> postParameters = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(4, postParameters.size());
 assertEquals("page", postParameters.get(0).getName());
 assertEquals("1", postParameters.get(0).getValue());
 assertEquals("count", postParameters.get(1).getName());
 assertEquals("20", postParameters.get(1).getValue());
 assertEquals("since_id", postParameters.get(2).getName());
 assertEquals("1", postParameters.get(2).getValue());
 assertEquals("max_id", postParameters.get(3).getName());
 assertEquals("2", postParameters.get(3).getValue());
 }
}