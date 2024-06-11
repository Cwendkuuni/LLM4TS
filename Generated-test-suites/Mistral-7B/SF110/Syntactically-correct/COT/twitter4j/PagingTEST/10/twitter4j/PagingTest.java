import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private Paging createPagingObject() {
 return new Paging(1, 10, 1L, 2L);
 }

 @Test
 public void testEmptyConstructor() {
 Paging paging = new Paging();
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPage() {
 Paging paging = new Paging(1);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithSinceId() {
 Paging paging = new Paging(1L);
 assertEquals(-1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageCount() {
 Paging paging = new Paging(1, 10);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(-1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageSinceId() {
 Paging paging = new Paging(1, 1L);
 assertEquals(1, paging.getPage());
 assertEquals(-1, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageCountSinceIdMaxId() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 assertEquals(1, paging.getPage());
 assertEquals(10, paging.getCount());
 assertEquals(1L, paging.getSinceId());
 assertEquals(2L, paging.getMaxId());
 }

 @Test
 public void testGetAndSetPage() {
 Paging paging = this.createPagingObject();
 paging.setPage(5);
 assertEquals(5, paging.getPage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithNegativeValue() {
 Paging paging = this.createPagingObject();
 paging.setPage(-1);
 }

 @Test
 public void testGetAndSetCount() {
 Paging paging = this.createPagingObject();
 paging.setCount(5);
 assertEquals(5, paging.getCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithNegativeValue() {
 Paging paging = this.createPagingObject();
 paging.setCount(-1);
 }

 @Test
 public void testGetAndSetSinceId() {
 Paging paging = this.createPagingObject();
 paging.setSinceId(5);
 assertEquals(5, paging.getSinceId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithNegativeValue() {
 Paging paging = this.createPagingObject();
 paging.setSinceId(-1);
 }

 @Test
 public void testGetAndSetMaxId() {
 Paging paging = this.createPagingObject();
 paging.setMaxId(5);
 assertEquals(5, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxIdWithNegativeValue() {
 Paging paging = this.createPagingObject();
 paging.setMaxId(-1);
 }

 @Test
 public void testEquals() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 assertTrue(paging1.equals(paging2));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = new Paging(1, 10, 1L, 2L);
 Paging paging2 = new Paging(1, 10, 1L, 2L);
 assertEquals(paging1.hashCode(), paging2.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = new Paging(1, 10, 1L, 2L);
 assertEquals("Paging{page=1, count=10, sinceId=1, maxId=2}", paging.toString());
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = this.createPagingObject();
 List<HttpParameter> expectedList = Arrays.asList(new HttpParameter("page", "1"), new HttpParameter("count", "10"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2"));
 List<HttpParameter> actualList = paging.asPostParameterList(Paging.SMCP, "count");
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAsPostParameterListWithEmptySupportedParams() {
 Paging paging = this.createPagingObject();
 List<HttpParameter> expectedList = Collections.emptyList();
 List<HttpParameter> actualList = paging.asPostParameterList(new char[0], "count");
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = this.createPagingObject();
 HttpParameter[] expectedArray = new HttpParameter[] { new HttpParameter("page", "1"), new HttpParameter("count", "10"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2") };
 HttpParameter[] actualArray = paging.asPostParameterArray(Paging.SMCP, "count");
 assertArrayEquals(expectedArray, actualArray);
 }

 @Test
 public void testAsPostParameterArrayWithEmptySupportedParams() {
 Paging paging = this.createPagingObject();
 HttpParameter[] expectedArray = Paging.NULL_PARAMETER_ARRAY;
 HttpParameter[] actualArray = paging.asPostParameterArray(new char[0], "count");
 assertArrayEquals(expectedArray, actualArray);
 }

 @Test
 public void testAddPostParameterWithSupportedParam() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 1);
 assertEquals(1, pagingParams.size());
 HttpParameter expectedParameter = new HttpParameter("since_id", "1");
 assertEquals(expectedParameter, pagingParams.get(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(new char[] {'x'}, 'x', pagingParams, "paramName", 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithNegativeValue() {
 Paging paging = new Paging();
 List<HttpParameter> pagingParams = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", -1);
 }
}