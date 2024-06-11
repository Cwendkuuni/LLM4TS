import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

 private Paging createPagingObject() {
 return new Paging(1, 10, 1L, 2L);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedParameters = new ArrayList<>();
 expectedParameters.add(new HttpParameter("page", "1"));
 expectedParameters.add(new HttpParameter("count", "10"));
 expectedParameters.add(new HttpParameter("since_id", "1"));
 expectedParameters.add(new HttpParameter("max_id", "2"));
 List<HttpParameter> actualParameters = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals("Size mismatch", expectedParameters.size(), actualParameters.size());
 for (int i = 0; i < expectedParameters.size(); i++) {
 assertEquals("Mismatch at index " + i, expectedParameters.get(i).getName(), actualParameters.get(i).getName());
 assertEquals("Mismatch at index " + i, expectedParameters.get(i).getValue(), actualParameters.get(i).getValue());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameter_UnsupportedParamKey() {
 char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
 List<HttpParameter> pagingParams = new ArrayList<>();
 Paging.addPostParameter(supportedParams, 'x', pagingParams, "unsupported_param", 1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPage_NegativeValue() {
 Paging paging = new Paging();
 try {
 paging.setPage(-1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCount_NegativeValue() {
 Paging paging = new Paging();
 try {
 paging.setCount(-1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceId_NegativeValue() {
 Paging paging = new Paging();
 try {
 paging.setSinceId(-1L);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 assertTrue(paging1.equals(paging2));
 Paging paging3 = new Paging(1, 10, 2L, 2L);
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 int hashCode1 = paging1.hashCode();
 int hashCode2 = paging2.hashCode();
 assertEquals("Hashcodes should be equal", hashCode1, hashCode2);
 Paging paging3 = new Paging(1, 10, 2L, 2L);
 assertNotEquals("Hashcodes should be different", hashCode1, paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = createPagingObject();
 String expectedString = "Paging{page=1, count=10, sinceId=1, maxId=2}";
 String actualString = paging.toString();
 assertEquals("ToString should return expected string", expectedString, actualString);
 }

}