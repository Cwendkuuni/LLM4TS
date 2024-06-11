import static org.junit.Assert.*;
import twitter4j.HttpParameter;
import twitter4j.Paging;

import java.util.ArrayList;
import java.util.List;

public class PagingTest {

 private Paging createPagingObject() {
 return new Paging(1, 20, 1L, 100L);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedParameters = new ArrayList<>();
 expectedParameters.add(new HttpParameter("page", "1"));
 expectedParameters.add(new HttpParameter("count", "20"));
 expectedParameters.add(new HttpParameter("since_id", "1"));
 expectedParameters.add(new HttpParameter("max_id", "100"));
 List<HttpParameter> actualParameters = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expectedParameters, actualParameters);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = createPagingObject();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "unsupportedParam", 1L);
 }

 @Test
 public void testGettersAndSetters() {
 Paging paging = createPagingObject();
 assertEquals(1, paging.getPage());
 assertEquals(20, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(100L, paging.getMaxId());
 paging.setPage(2);
 paging.setCount(30);
 paging.setSinceId(2L);
 paging.setMaxId(200L);
 assertEquals(2, paging.getPage());
 assertEquals(30, paging.getCount());
 assertEquals(2L, paging.getSinceId());
 assertEquals(200L, paging.getMaxId());
 }

 @Test
 public void testEquals() {
 Paging paging1 = createPagingObject();
 Paging paging2 = new Paging(1, 20, 1L, 100L);
 Paging paging3 = new Paging(1, 20, 1L, 101L);
 assertTrue(paging1.equals(paging2));
 assertTrue(paging1.equals(paging1));
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = createPagingObject();
 Paging paging2 = new Paging(1, 20, 1L, 100L);
 Paging paging3 = new Paging(1, 20, 1L, 101L);
 int hashCode1 = paging1.hashCode();
 int hashCode2 = paging2.hashCode();
 int hashCode3 = paging3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 Paging paging = createPagingObject();
 String expectedString = "Paging{page=1, count=20, sinceId=1, maxId=100}";
 assertEquals(expectedString, paging.toString());
 }
}