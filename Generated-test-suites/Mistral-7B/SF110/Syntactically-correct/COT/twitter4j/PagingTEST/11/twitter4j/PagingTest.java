import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject() {
 return new Paging(1, 10);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedParams = new ArrayList<HttpParameter>() {{
 add(new HttpParameter("page", "1"));
 add(new HttpParameter("count", "10"));
 }};
 List<HttpParameter> actualParams = paging.asPostParameterList();
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterListWithOnlySinceId() {
 Paging paging = new Paging(-1L, 0);
 paging.setSinceId(1L);
 List<HttpParameter> expectedParams = new ArrayList<HttpParameter>() {{
 add(new HttpParameter("since_id", "1"));
 }};
 List<HttpParameter> actualParams = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterListWithOnlyMaxId() {
 Paging paging = new Paging(0, 0);
 paging.setMaxId(1L);
 List<HttpParameter> expectedParams = new ArrayList<HttpParameter>() {{
 add(new HttpParameter("max_id", "1"));
 }};
 List<HttpParameter> actualParams = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsPostParameterListWithSinceIdAndMaxId() {
 Paging paging = new Paging(-1L, 0);
 paging.setSinceId(1L);
 paging.setMaxId(2L);
 List<HttpParameter> expectedParams = new ArrayList<HttpParameter>() {{
 add(new HttpParameter("since_id", "1"));
 add(new HttpParameter("max_id", "2"));
 }};
 List<HttpParameter> actualParams = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expectedParams, actualParams);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = new Paging(-1, 0);
 paging.addPostParameter(Paging.SMCP, 'x', new ArrayList<HttpParameter>(), "unsupported_param", 1L);
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = createPagingObject();
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 paging.setPage(2);
 paging.setCount(20);
 paging.setSinceId(3L);
 paging.setMaxId(4L);
 assertEquals(2, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(3L, paging.getSinceId());
 assertEquals(4L, paging.getMaxId());
 }

 @Test
 public void testEqualsAndHashcode() {
 Paging paging1 = new Paging(1, 10);
 Paging paging2 = new Paging(1, 10);
 assertTrue(paging1.equals(paging2));
 assertTrue(paging1.hashCode() == paging2.hashCode());
 Paging paging3 = new Paging(2, 10);
 assertFalse(paging1.equals(paging3));
 assertFalse(paging1.hashCode() == paging3.hashCode());
 }
}