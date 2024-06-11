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
 List<HttpParameter> httpParameters = paging.asPostParameterList();
 assertEquals(4, httpParameters.size());
 assertEquals("page", httpParameters.get(0).getName());
 assertEquals("1", httpParameters.get(0).getValue());
 assertEquals("count", httpParameters.get(1).getName());
 assertEquals("10", httpParameters.get(1).getValue());
 }

 @Test
 public void testAsPostParameterListWithEmptySupportedParams() {
 Paging paging = createPagingObject();
 List<HttpParameter> httpParameters = paging.asPostParameterList(new char[0]);
 assertTrue(httpParameters.isEmpty());
 }

 @Test
 public void testAsPostParameterListWithInvalidSupportedParam() {
 Paging paging = createPagingObject();
 try {
 paging.asPostParameterList(new char[]{'x'});
 fail("IllegalStateException should be thrown");
 } catch (IllegalStateException e) {
 assertEquals("Paging parameter [page] is not supported with this operation.", e.getMessage());
 }
 }

 @Test
 public void testAsPostParameterListWithInvalidPerPageParamName() {
 Paging paging = createPagingObject();
 try {
 paging.asPostParameterList(new char[]{'s'}, "invalidPerPageParamName");
 fail("IllegalStateException should be thrown");
 } catch (IllegalStateException e) {
 assertEquals("Paging parameter [page] is not supported with this operation.", e.getMessage());
 }
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = createPagingObject();
 HttpParameter[] httpParameters = paging.asPostParameterArray();
 assertEquals(4, httpParameters.length);
 assertEquals("page", httpParameters[0].getName());
 assertEquals("1", httpParameters[0].getValue());
 assertEquals("count", httpParameters[1].getName());
 assertEquals("10", httpParameters[1].getValue());
 }

 @Test
 public void testAsPostParameterArrayWithEmptySupportedParams() {
 Paging paging = createPagingObject();
 HttpParameter[] httpParameters = paging.asPostParameterArray(new char[0]);
 assertArrayEquals(new HttpParameter[0], httpParameters);
 }

 @Test
 public void testAsPostParameterArrayWithInvalidSupportedParam() {
 Paging paging = createPagingObject();
 try {
 paging.asPostParameterArray(new char[]{'x'});
 fail("IllegalStateException should be thrown");
 } catch (IllegalStateException e) {
 assertEquals("Paging parameter [page] is not supported with this operation.", e.getMessage());
 }
 }

 @Test
 public void testAsPostParameterArrayWithInvalidPerPageParamName() {
 Paging paging = createPagingObject();
 try {
 paging.asPostParameterArray(new char[]{'s'}, "invalidPerPageParamName");
 fail("IllegalStateException should be thrown");
 } catch (IllegalStateException e) {
 assertEquals("Paging parameter [page] is not supported with this operation.", e.getMessage());
 }
 }

 @Test
 public void testAddPostParameterWithValidParams() {
 Paging paging = createPagingObject();
 List<HttpParameter> httpParameters = new ArrayList<>();
 paging.addPostParameter(Paging.S, 's', httpParameters, "since_id", 1L);
 paging.addPostParameter(Paging.S, 'm', httpParameters, "max_id", 2L);
 paging.addPostParameter(Paging.S, 'c', httpParameters, "count", 3);
 paging.addPostParameter(Paging.S, 'p', httpParameters, "page", 4);
 assertEquals(4, httpParameters.size());
 assertEquals("since_id", httpParameters.get(0).getName());
 assertEquals("1", httpParameters.get(0).getValue());
 assertEquals("max_id", httpParameters.get(1).getName());
 assertEquals("2", httpParameters.get(1).getValue());
 assertEquals("count", httpParameters.get(2).getName());
 assertEquals("3", httpParameters.get(2).getValue());
 assertEquals("page", httpParameters.get(3).getName());
 assertEquals("4", httpParameters.get(3).getValue());
 }

 @Test
 public void testAddPostParameterWithInvalidParamKey() {
 Paging paging = createPagingObject();
 List<HttpParameter> httpParameters = new ArrayList<>();
 try {
 paging.addPostParameter(Paging.S, 'x', httpParameters, "since_id", 1L);
 fail("IllegalStateException should be thrown");
 } catch (IllegalStateException e) {
 assertEquals("Paging parameter [since_id] is not supported with this operation.", e.getMessage());
 }
 }

 @Test
 public void testAddPostParameterWithInvalidParamValue() {
 Paging paging = createPagingObject();
 List<HttpParameter> httpParameters = new ArrayList<>();
 try {
 paging.addPostParameter(Paging.S, 's', httpParameters, "since_id", -1L);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("since_id should be positive integer. passed:-1", e.getMessage());
 }
 }

 @Test
 public void testEquals() {
 Paging paging1 = createPagingObject();
 Paging paging2 = createPagingObject();
 Paging paging3 = new Paging(1, 10, 20, 30);
 assertTrue(paging1.equals(paging1));
 assertTrue(paging1.equals(paging2));
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = createPagingObject();
 Paging paging2 = createPagingObject();
 Paging paging3 = new Paging(1, 10, 20, 30);
 int hashCode1 = paging1.hashCode();
 int hashCode2 = paging1.hashCode();
 int hashCode3 = paging3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 Paging paging = createPagingObject();
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=1}", paging.toString());
 }
}