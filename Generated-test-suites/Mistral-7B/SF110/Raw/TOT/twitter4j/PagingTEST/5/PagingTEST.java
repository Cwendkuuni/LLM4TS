import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 @Test
 public void testValidPagingObject() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 }

 @Test
 public void testEmptyPagingObject() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testSettersAndGetters() {
 Paging paging = new Paging();
 paging.setPage(2);
 paging.setCount(5);
 paging.setSinceId(11L);
 paging.setMaxId(15L);
 assertEquals(2, paging.getPage());
 assertEquals(5, paging.getCount());
 assertEquals(11L, paging.getSinceId());
 assertEquals(15L, paging.getMaxId());
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
 public void testAsPostParameterList() {
 Paging paging = new Paging(2, 15, 1L, 10L);
 List<HttpParameter> paramList = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(4, paramList.size());
 assertEquals("page", paramList.get(0).getName());
 assertEquals("2", paramList.get(0).getValue());
 assertEquals("count", paramList.get(1).getName());
 assertEquals("15", paramList.get(1).getValue());
 assertEquals("since_id", paramList.get(2).getName());
 assertEquals("1", paramList.get(2).getValue());
 assertEquals("max_id", paramList.get(3).getName());
 assertEquals("10", paramList.get(3).getValue());
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging(2, 15, 1L, 10L);
 HttpParameter[] paramArray = paging.asPostParameterArray(Paging.SMCP, "count");
 assertEquals(4, paramArray.length);
 assertEquals("page", paramArray[0].getName());
 assertEquals("2", paramArray[0].getValue());
 assertEquals("count", paramArray[1].getName());
 assertEquals("15", paramArray[1].getValue());
 assertEquals("since_id", paramArray[2].getName());
 assertEquals("1", paramArray[2].getValue());
 assertEquals("max_id", paramArray[3].getName());
 assertEquals("10", paramArray[3].getValue());
 }

 @Test
 public void testAddPostParameter() {
 Paging paging = new Paging();
 List<HttpParameter> paramList = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 's', paramList, "since_id", 1L);
 assertEquals(1, paramList.size());
 assertEquals("since_id", paramList.get(0).getName());
 assertEquals("1", paramList.get(0).getValue());
 }
}