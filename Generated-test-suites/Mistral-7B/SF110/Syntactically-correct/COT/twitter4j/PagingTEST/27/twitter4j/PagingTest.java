import org.junit.Assert;
import org.junit.Test;
import twitter4j.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

 private Paging createPagingObject() {
 return new Paging(1, 20, 1L, 100L);
 }

 @Test
 public void testAsPostParameterList() {
 Paging paging = createPagingObject();
 List<HttpParameter> expectedParams = new ArrayList<>();
 expectedParams.add(new HttpParameter("page", "1"));
 expectedParams.add(new HttpParameter("count", "20"));
 expectedParams.add(new HttpParameter("since_id", "1"));
 expectedParams.add(new HttpParameter("max_id", "100"));
 List<HttpParameter> actualParams = paging.asPostParameterList(Paging.SMCP, "count");
 Assert.assertEquals(expectedParams, actualParams);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPostParameterWithUnsupportedParam() {
 Paging paging = createPagingObject();
 List<HttpParameter> params = new ArrayList<>();
 paging.addPostParameter(Paging.SMCP, 'a', params, "paramName", 1L);
 }

 @Test
 public void testAsPostParameterArray() {
 Paging paging = createPagingObject();
 HttpParameter[] expectedParams = new HttpParameter[] {
 new HttpParameter("page", "1"),
 new HttpParameter("count", "20"),
 new HttpParameter("since_id", "1"),
 new HttpParameter("max_id", "100")
 };
 HttpParameter[] actualParams = paging.asPostParameterArray(Paging.SMCP, "count");
 Assert.assertArrayEquals(expectedParams, actualParams);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPageWithNegativeValue() {
 Paging paging = createPagingObject();
 paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCountWithNegativeValue() {
 Paging paging = createPagingObject();
 paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSinceIdWithNegativeValue() {
 Paging paging = createPagingObject();
 paging.setSinceId(-1L);
 }

 @Test
 public void testEquals() {
 Paging paging1 = createPagingObject();
 Paging paging2 = createPagingObject();
 Paging paging3 = new Paging(1, 2, 1L, 100L);
 Assert.assertTrue(paging1.equals(paging1));
 Assert.assertTrue(paging1.equals(paging2));
 Assert.assertFalse(paging1.equals(paging3));
 }

 @Test
 public void testHashCode() {
 Paging paging1 = createPagingObject();
 Paging paging2 = createPagingObject();
 Paging paging3 = new Paging(1, 2, 1L, 100L);
 Assert.assertEquals(paging1.hashCode(), paging1.hashCode());
 Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
 Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
 Assert.assertNotEquals(paging1.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
 Paging paging = createPagingObject();
 Assert.assertEquals("Paging{page=1, count=20, sinceId=1, maxId=100}", paging.toString());
 }

}