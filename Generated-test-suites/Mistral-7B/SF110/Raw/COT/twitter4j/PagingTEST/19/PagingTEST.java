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
 List<HttpParameter> expectedParameters = new ArrayList<>();
 expectedParameters.add(new HttpParameter("page", "1"));
 expectedParameters.add(new HttpParameter("count", "10"));
 List<HttpParameter> actualParameters = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expectedParameters, actualParameters);
 }

 @Test
 public void testAsPostParameterListWithEmptySupportedParams() {
 Paging paging = createPagingObject();
 List<HttpParameter> actualParameters = paging.asPostParameterList(new char[0], "count");
 assertTrue(actualParameters.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsupportedParameter() {
 Paging paging = createPagingObject();
 paging.addPostParameter(Paging.SMCP, 'x', new ArrayList<>(), "unsupported_param", 1L);
 }

 @Test
 public void testGetters() {
 Paging paging = createPagingObject();
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithNegativeValues() {
 Paging paging = createPagingObject();
 paging.setPage(-1);
 paging.setCount(-1);
 paging.setSinceId(-1L);
 paging.setMaxId(-1L);
 }

 @Test
 public void testEquals() {
 Paging paging1 = createPagingObject();
 Paging paging2 = createPagingObject();
 Paging paging3 = new Paging(1, 10, 1, 1);
 assertTrue(paging1.equals(paging1));
 assertTrue(paging1.equals(paging2));
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = createPagingObject();
 Paging paging2 = createPagingObject();
 Paging paging3 = new Paging(1, 10, 1, 1);
 int hashCode1 = paging1.hashCode();
 int hashCode2 = paging2.hashCode();
 int hashCode3 = paging3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}