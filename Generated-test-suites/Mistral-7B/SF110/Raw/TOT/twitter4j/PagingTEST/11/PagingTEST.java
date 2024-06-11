import org.junit.Test;import static org.junit.Assert.*;import twitter4j.Paging;

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
 Paging paging = new Paging(1, 10);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
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
 Paging paging = new Paging(1, 10, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test
 public void testSettersAndGetters() {
 Paging paging = new Paging();
 paging.setPage(1);
 paging.setCount(10);
 paging.setSinceId(1L);
 paging.setMaxId(100L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test
 public void testInvalidSetters() {
 Paging paging = new Paging();
 try {
 paging.setPage(-1);
 fail("page should not be negative.");
 } catch (IllegalArgumentException e) {
 //expected
 }
 try {
 paging.setCount(-1);
 fail("count should be positive integer.");
 } catch (IllegalArgumentException e) {
 //expected
 }
 try {
 paging.setSinceId(-1L);
 fail("since_id should be positive integer.");
 } catch (IllegalArgumentException e) {
 //expected
 }
 try {
 paging.setMaxId(-1L);
 fail("max_id should be positive integer.");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(4, params.size());
 assertEquals("count", params.get(0).getName());
 assertEquals("10", params.get(1).getValue());
 assertEquals("page", params.get(2).getName());
 assertEquals("1", params.get(2).getValue());
 assertEquals("since_id", params.get(3).getName());
 assertEquals("1", params.get(3).getValue());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, params.length);
 assertEquals("count", params[0].getName());
 assertEquals("10", params[0].getValue());
 assertEquals("page", params[1].getName());
 assertEquals("1", params[1].getValue());
 assertEquals("since_id", params[2].getName());
 assertEquals("1", params[2].getValue());
 }
}