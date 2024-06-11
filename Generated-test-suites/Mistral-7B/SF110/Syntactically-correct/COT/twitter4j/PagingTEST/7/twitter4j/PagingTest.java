import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject(int page, int count, long sinceId, long maxId) {
 return new Paging(page, count, sinceId, maxId);
 }

 @Test
 public void testAsPostParameterList() {
 // Test empty object
 Paging paging = new Paging();
 List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
 assertNull(result);

 // Test valid object
 Paging validPaging = createPagingObject(1, 10, 1, 2);
 List<HttpParameter> result2 = validPaging.asPostParameterList(Paging.SMCP, "count");
 assertNotNull(result2);
 assertEquals(4, result2.size());

 // Test exception when unsupported parameter is added
 try {
 validPaging.addPostParameter(Paging.SMCP, 'x', result2, "unsupportedParam", 1L);
 fail("Should have thrown an exception");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testAsPostParameterListWithSinceId() {
 Paging pagingWithSinceId = createPagingObject(1, 10, 1, 2);
 List<HttpParameter> result = pagingWithSinceId.asPostParameterList(Paging.SMCP, "count");
 assertNotNull(result);
 assertEquals(4, result.size());
 HttpParameter sinceParam = null;
 for (HttpParameter param : result) {
 if (param.getName().equals("since_id")) {
 sinceParam = param;
 break;
 }
 }
 assertNotNull(sinceParam);
 assertEquals("1", sinceParam.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithNegativeValue() {
 Paging paging = new Paging();
 paging.setPage(-1);
 }

 @Test
 public void testSetPageWithValidValue() {
 Paging paging = new Paging();
 paging.setPage(1);
 assertEquals(1, paging.getPage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithNegativeValue() {
 Paging paging = new Paging();
 paging.setCount(-1);
 }

 @Test
 public void testSetCountWithValidValue() {
 Paging paging = new Paging();
 paging.setCount(1);
 assertEquals(1, paging.getCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithNegativeValue() {
 Paging paging = new Paging();
 paging.setSinceId(-1L);
 }

 @Test
 public void testSetSinceIdWithValidValue() {
 Paging paging = new Paging();
 paging.setSinceId(1L);
 assertEquals(1L, paging.getSinceId());
 }

 @Test
 public void testSetMaxIdWithValidValue() {
 Paging paging = new Paging();
 paging.setMaxId(1L);
 assertEquals(1L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxIdWithNegativeValue() {
 Paging paging = new Paging();
 paging.setMaxId(-1L);
 }

 @Test
 public void testEquals() {
 Paging paging1 = createPagingObject(1, 10, 1, 2);
 Paging paging2 = createPagingObject(1, 10, 1, 2);
 assertTrue(paging1.equals(paging2));

 Paging paging3 = createPagingObject(1, 10, 1, 3);
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = createPagingObject(1, 10, 1, 2);
 Paging paging2 = createPagingObject(1, 10, 1, 2);
 assertEquals(paging1.hashCode(), paging2.hashCode());

 Paging paging3 = createPagingObject(1, 10, 1, 3);
 assertNotEquals(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = createPagingObject(1, 10, 1, 2);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=2}", paging.toString());
 }

}