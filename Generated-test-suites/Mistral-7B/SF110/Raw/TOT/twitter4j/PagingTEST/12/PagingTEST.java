import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;

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
 Paging paging = new Paging(5);
 assertEquals(5, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithSinceId() {
 Paging paging = new Paging(10L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(10L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCount() {
 Paging paging = new Paging(5, 10);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageCountSinceId() {
 Paging paging = new Paging(5, 10, 1L);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageCountSinceIdMaxId() {
 Paging paging = new Paging(5, 10, 1L, 100L);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativePage() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeCount() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeSinceId() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMaxId() {
 Paging paging = new Paging();
 paging.setMaxId(-1L);
 }

 @Test
 public void testGettersSetters() {
 Paging paging = new Paging();
 paging.setPage(5);
 paging.setCount(10);
 paging.setSinceId(1L);
 paging.setMaxId(100L);
 assertEquals(5, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test
 public void testAsPostParameterListAndArray() {
 Paging paging = new Paging(5, 10, 1L, 100L);
 List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");
 HttpParameter[] array = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, list.size());
 assertEquals(4, array.length);
 for (int i = 0; i < list.size(); i++) {
 assertEquals(list.get(i).getName(), array[i].getName());
 assertEquals(list.get(i).getValue(), array[i].getValue());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAddUnsupportedParameter() {
 final char[] supportedParams = new char[]{'s', 'm'};
 Paging paging = new Paging();
 List<HttpParameter> params = new ArrayList<>();
 paging.addPostParameter(supportedParams, 'a', params, "unsupported_param", 1L);
 }
}