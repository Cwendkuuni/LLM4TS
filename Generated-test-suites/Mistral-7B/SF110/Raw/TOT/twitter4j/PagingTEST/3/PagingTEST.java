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
 Paging paging = new Paging(1, 20, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
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
 public void testSettersWithInvalidArguments() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithNegativeCount() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithNegativeSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithNegativeMaxId() {
 Paging paging = new Paging();
 paging.setMaxId(-1L);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 20, 1L, 100L);
 List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(3, params.size());
 assertEquals("count=20", params.get(0).getName());
 assertEquals("1", params.get(1).getName());
 assertEquals("page=1", params.get(2).getName());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 20, 1L, 100L);
 HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(3, params.length);
 assertEquals("count=20", params[0].getName());
 assertEquals("1", params[1].getName());
 assertEquals("page=1", params[2].getName());
 }
}