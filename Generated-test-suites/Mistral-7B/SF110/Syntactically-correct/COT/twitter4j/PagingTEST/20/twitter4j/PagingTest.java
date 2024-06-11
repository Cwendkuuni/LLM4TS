import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject(int page, int count, long sinceId, long maxId) {
 return new Paging(page, count, sinceId, maxId);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = new Paging();
 List<HttpParameter> params = paging.asPostParameterList();
 assertEquals(0, params.size());

 paging = new Paging(1);
 params = paging.asPostParameterList();
 assertEquals(1, params.size());

 paging = new Paging(1, 10);
 params = paging.asPostParameterList();
 assertEquals(4, params.size());

 paging = new Paging(1, 10, 1, 1);
 params = paging.asPostParameterList();
 assertEquals(5, params.size());
 }

 @Test
 public void testAsPostParameterListWithInvalidParams() {
 Paging paging = new Paging();
 try {
 paging.asPostParameterList(new char[]{'x'});
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }

 paging = new Paging(1, 0);
 try {
 paging.asPostParameterList(new char[]{'s', 'm'});
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
}

 @Test
 public void testAsPostParameterArray() {
 Paging paging = new Paging();
 HttpParameter[] params = paging.asPostParameterArray();
 assertEquals(0, params.length);

 paging = new Paging(1);
 params = paging.asPostParameterArray();
 assertEquals(1, params.length);

 paging = new Paging(1, 10);
 params = paging.asPostParameterArray();
 assertEquals(4, params.length);

 paging = new Paging(1, 10, 1, 1);
 params = paging.asPostParameterArray();
 assertEquals(5, params.length);
 }

 @Test
 public void testAsPostParameterArrayWithInvalidParams() {
 Paging paging = new Paging();
 try {
 paging.asPostParameterArray(new char[]{'x'});
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }

 paging = new Paging(1, 0);
 try {
 paging.asPostParameterArray(new char[]{'s', 'm'});
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testAddPostParameter() {
 List<HttpParameter> params = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 's', params, "since_id", 1L);
 assertEquals(1, params.size());
 HttpParameter param = params.get(0);
 assertEquals("since_id", param.getName());
 assertEquals("1", param.getValue());

 params.clear();
 paging.addPostParameter(Paging.SMCP, 's', params, "since_id", -1L);
 assertEquals(0, params.size());
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());

 paging.setPage(1);
 assertEquals(1, paging.getPage());

 paging.setCount(10);
 assertEquals(10, paging.getCount());

 paging.setSinceId(1L);
 assertEquals(1L, paging.getSinceId());

 paging.setMaxId(1L);
 assertEquals(1L, paging.getMaxId());
 }

 @Test
 public void testEqualsAndHashCode() {
 Paging paging1 = new Paging(1, 10, 1, 1);
 Paging paging2 = new Paging(1, 10, 1, 1);
 assertEquals(paging1, paging2);
 assertEquals(paging1.hashCode(), paging2.hashCode());

 Paging paging3 = new Paging(1, 10, 2, 2);
 assertNotEquals(paging1, paging3);
 assertNotEquals(paging1.hashCode(), paging3.hashCode());

 Paging paging4 = new Paging(1, 10, 1, 2);
 assertNotEquals(paging1, paging4);
 assertNotEquals(paging1.hashCode(), paging4.hashCode());
}

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1, 1);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=1}", paging.toString());
 }
}