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
 List<HttpParameter> expectedList = new ArrayList<>();
 expectedList.add(new HttpParameter("page", "1"));
 expectedList.add(new HttpParameter("count", "10"));
 List<HttpParameter> actualList = paging.asPostParameterList();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = createPagingObject();
 HttpParameter[] expectedArray = new HttpParameter[2];
 expectedArray[0] = new HttpParameter("page", "1");
 expectedArray[1] = new HttpParameter("count", "10");
 HttpParameter[] actualArray = paging.asPostParameterArray();
 assertArrayEquals(expectedArray, actualArray);
 }

 @Test
 public void testAsPostParameterListWithEmptySupportedParams() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedList = Paging.NULL_PARAMETER_LIST;
 List<HttpParameter> actualList = paging.asPostParameterList(Paging.SMCP, "");
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAsPostParameterListWithEmptyPerPageParamName() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedList = Paging.NULL_PARAMETER_LIST;
 List<HttpParameter> actualList = paging.asPostParameterList(Paging.SMCP, null);
 assertEquals(expectedList, actualList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPostParameterWithUnsupportedParamKey() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "paramName", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPostParameterWithNegativeParamValue() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", -1);
 }

 @Test
 public void testGetters() {
 Paging paging = createPagingObject();
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1, paging.getSinceId());
 assertEquals(-1, paging.getMaxId());
 }

 @Test
 public void testSetters() {
 Paging paging = new Paging();
 paging.setCount(5);
 assertEquals(5, paging.getCount());
 paging.setSinceId(10L);
 assertEquals(10, paging.getSinceId());
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 10);
 Paging paging2 = new Paging(1, 10);
 assertTrue(paging1.equals(paging2));
 Paging paging3 = new Paging(1, 9);
 assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 10);
 Paging paging2 = new Paging(1, 10);
 assertEquals(paging1.hashCode(), paging2.hashCode());
 }

}