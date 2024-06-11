import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;

public class PagingTest {

 @Test
 public void testEmptyPagingObject() {
 Paging paging = new Paging();
 List<HttpParameter> postParams = paging.asPostParameterList();
 assertNotNull(postParams);
 assertEquals(0, postParams.size());
 }

 @Test
 public void testValidPagingObject() {
 Paging paging = new Paging(1, 10);
 List<HttpParameter> postParams = paging.asPostParameterList();
 assertNotNull(postParams);
 assertEquals(4, postParams.size());
 for (HttpParameter param : postParams) {
 String name = param.getName();
 String value = param.getValue();
 if ("page".equals(name)) {
 assertEquals("1", value);
 } else if ("count".equals(name)) {
 assertEquals("10", value);
 } else if ("since_id".equals(name)) {
 assertNull(value);
 } else if ("max_id".equals(name)) {
 assertNull(value);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPageNumber() {
 Paging paging = new Paging(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCountNumber() {
 Paging paging = new Paging(1, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedParameterSinceId() {
 char[] supportedParams = new char[]{'s', 'm', 'c'};
 Paging paging = new Paging(1, 10, 1L);
 paging.asPostParameterList(supportedParams, "since_id");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedParameterMaxId() {
 char[] supportedParams = new char[]{'s', 'm', 'c'};
 Paging paging = new Paging(1, 10, 1L, 1L);
 paging.asPostParameterList(supportedParams, "max_id");
 }

 @Test
 public void testValidPagingObjectWithSinceId() {
 Paging paging = new Paging(1, 10, 1L);
 List<HttpParameter> postParams = paging.asPostParameterList();
 assertNotNull(postParams);
 assertEquals(4, postParams.size());
 for (HttpParameter param : postParams) {
 String name = param.getName();
 String value = param.getValue();
 if ("page".equals(name)) {
 assertEquals("1", value);
 } else if ("count".equals(name)) {
 assertEquals("10", value);
 } else if ("since_id".equals(name)) {
 assertEquals("1", value);
 } else if ("max_id".equals(name)) {
 assertNull(value);
 }
 }
 }

 @Test
 public void testValidPagingObjectWithMaxId() {
 Paging paging = new Paging(1, 10, 1L, 100L);
 List<HttpParameter> postParams = paging.asPostParameterList();
 assertNotNull(postParams);
 assertEquals(4, postParams.size());
 for (HttpParameter param : postParams) {
 String name = param.getName();
 String value = param.getValue();
 if ("page".equals(name)) {
 assertEquals("1", value);
 } else if ("count".equals(name)) {
 assertEquals("10", value);
 } else if ("since_id".equals(name)) {
 assertNull(value);
 } else if ("max_id".equals(name)) {
 assertEquals("100", value);
 }
 }
 }
}