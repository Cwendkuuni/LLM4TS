package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private static final int VALID_PAGE = 1;
 private static final int VALID_COUNT = 10;
 private static final long VALID_SINCE_ID = 1L;
 private static final long VALID_MAX_ID = 100L;

 @Test
 public void testDefaultConstructor() {
     Paging paging = new Paging();
     Assert.assertEquals(-1, paging.getPage());
     Assert.assertEquals(-1, paging.getCount());
     Assert.assertEquals(-1L, paging.getSinceId());
     Assert.assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPage() {
     Paging paging = new Paging(VALID_PAGE);
     Assert.assertEquals(VALID_PAGE, paging.getPage());
     Assert.assertEquals(-1, paging.getCount());
     Assert.assertEquals(-1L, paging.getSinceId());
     Assert.assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithSinceId() {
     Paging paging = new Paging(VALID_SINCE_ID);
     Assert.assertEquals(-1, paging.getPage());
     Assert.assertEquals(-1, paging.getCount());
     Assert.assertEquals(VALID_SINCE_ID, paging.getSinceId());
     Assert.assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCount() {
     Paging paging = new Paging(VALID_PAGE, VALID_COUNT);
     Assert.assertEquals(VALID_PAGE, paging.getPage());
     Assert.assertEquals(VALID_COUNT, paging.getCount());
     Assert.assertEquals(-1L, paging.getSinceId());
     Assert.assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndSinceId() {
     Paging paging = new Paging(VALID_PAGE, VALID_SINCE_ID);
     Assert.assertEquals(VALID_PAGE, paging.getPage());
     Assert.assertEquals(-1, paging.getCount());
     Assert.assertEquals(VALID_SINCE_ID, paging.getSinceId());
     Assert.assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCountAndSinceId() {
     Paging paging = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID);
     Assert.assertEquals(VALID_PAGE, paging.getPage());
     Assert.assertEquals(VALID_COUNT, paging.getCount());
     Assert.assertEquals(VALID_SINCE_ID, paging.getSinceId());
     Assert.assertEquals(-1L, paging.getMaxId());
 }

 @Test
 public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
     Paging paging = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID, VALID_MAX_ID);
     Assert.assertEquals(VALID_PAGE, paging.getPage());
     Assert.assertEquals(VALID_COUNT, paging.getCount());
     Assert.assertEquals(VALID_SINCE_ID, paging.getSinceId());
     Assert.assertEquals(VALID_MAX_ID, paging.getMaxId());
 }

 @Test
 public void testSetters() {
     Paging paging = new Paging();
     paging.setPage(VALID_PAGE);
     paging.setCount(VALID_COUNT);
     paging.setSinceId(VALID_SINCE_ID);
     paging.setMaxId(VALID_MAX_ID);
     Assert.assertEquals(VALID_PAGE, paging.getPage());
     Assert.assertEquals(VALID_COUNT, paging.getCount());
     Assert.assertEquals(VALID_SINCE_ID, paging.getSinceId());
     Assert.assertEquals(VALID_MAX_ID, paging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidPage() {
     Paging paging = new Paging();
     paging.setPage(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidCount() {
     Paging paging = new Paging();
     paging.setCount(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidSinceId() {
     Paging paging = new Paging();
     paging.setSinceId(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidMaxId() {
     Paging paging = new Paging();
     paging.setMaxId(-1L);
 }

 @Test
 public void testEquals() {
     Paging paging1 = new Paging();
     Paging paging2 = new Paging();
     Assert.assertFalse(paging1.equals(paging2));
     Paging paging3 = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID, VALID_MAX_ID);
     Paging paging4 = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID, VALID_MAX_ID);
     Assert.assertTrue(paging3.equals(paging4));
 }

 @Test
 public void testHashCode() {
     Paging paging1 = new Paging();
     Paging paging2 = new Paging();
     Paging paging3 = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID, VALID_MAX_ID);
     Assert.assertNotEquals(paging1.hashCode(), paging2.hashCode());
     Assert.assertNotEquals(paging1.hashCode(), paging3.hashCode());
     Assert.assertEquals(paging3.hashCode(), paging3.hashCode());
 }

 @Test
 public void testToString() {
     Paging paging = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID, VALID_MAX_ID);
     String expectedToString = "Paging{page=" + VALID_PAGE + ", count=" + VALID_COUNT + ", sinceId=" + VALID_SINCE_ID + ", maxId=" + VALID_MAX_ID + '}';
     Assert.assertEquals(expectedToString, paging.toString());
 }

 @Test
 public void testAsPostParameterArray() {
     Paging paging = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID, VALID_MAX_ID);
     HttpParameter[] expectedArray = new HttpParameter[] {
         new HttpParameter("page", String.valueOf(VALID_PAGE)),
         new HttpParameter("count", String.valueOf(VALID_COUNT)),
         new HttpParameter("since_id", String.valueOf(VALID_SINCE_ID)),
         new HttpParameter("max_id", String.valueOf(VALID_MAX_ID))
     };
     HttpParameter[] actualArray = paging.asPostParameterArray();
     Assert.assertArrayEquals(expectedArray, actualArray);
 }

 @Test
 public void testAsPostParameterList() {
     Paging paging = new Paging(VALID_PAGE, VALID_COUNT, VALID_SINCE_ID, VALID_MAX_ID);
     List<HttpParameter> expectedList = Arrays.asList(
         new HttpParameter("page", String.valueOf(VALID_PAGE)),
         new HttpParameter("count", String.valueOf(VALID_COUNT)),
         new HttpParameter("since_id", String.valueOf(VALID_SINCE_ID)),
         new HttpParameter("max_id", String.valueOf(VALID_MAX_ID))
     );
     List<HttpParameter> actualList = paging.asPostParameterList();
     Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testAsPostParameterListWithNullSupportedParams() {
     Paging paging = new Paging();
     List<HttpParameter> actualList = paging.asPostParameterList();
     Assert.assertEquals(Paging.NULL_PARAMETER_LIST, actualList);
 }

 @Test
 public void testAsPostParameterArrayWithNullSupportedParams() {
     Paging paging = new Paging();
     HttpParameter[] actualArray = paging.asPostParameterArray();
     Assert.assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, actualArray);
 }

 @Test
 public void testAsPostParameterListWithEmptySupportedParams() {
     Paging paging = new Paging();
     char[] supportedParams = {};
     List<HttpParameter> actualList = paging.asPostParameterList(supportedParams, "count");
     Assert.assertEquals(Paging.NULL_PARAMETER_LIST, actualList);
 }

 @Test
 public void testAsPostParameterArrayWithEmptySupportedParams() {
     Paging paging = new Paging();
     char[] supportedParams = {};
     HttpParameter[] actualArray = paging.asPostParameterArray(supportedParams, "count");
     Assert.assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, actualArray);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsPostParameterListWithInvalidSupportedParam() {
     Paging paging = new Paging();
     char[] supportedParams = {'x'};
     List<HttpParameter> actualList = paging.asPostParameterList(supportedParams, "count");
 }

 @Test(expected = IllegalStateException.class)
 public void testAsPostParameterArrayWithInvalidSupportedParam() {
     Paging paging = new Paging();
     char[] supportedParams = {'x'};
     HttpParameter[] actualArray = paging.asPostParameterArray(supportedParams, "count");
 }

 @Test
 public void testAddPostParameterWithSupportedParam() {
     Paging paging = new Paging();
     List<HttpParameter> pagingParams = new ArrayList<>();
     paging.addPostParameter(Paging.S, 's', pagingParams, "since_id", VALID_SINCE_ID);
     List<HttpParameter> expectedList = Arrays.asList(new HttpParameter("since_id", String.valueOf(VALID_SINCE_ID)));
     Assert.assertEquals(expectedList, pagingParams);
 }

 @Test
 public void testAddPostParameterWithUnsupportedParam() {
     Paging paging = new Paging();
     List<HttpParameter> pagingParams = new ArrayList<>();
     paging.addPostParameter(Paging.S, 'z', pagingParams, "since_id", VALID_SINCE_ID);
 }

 @Test
 public void testAddPostParameterWithUnsupportedParamAndValue() {
     Paging paging = new Paging();
     List<HttpParameter> pagingParams = new ArrayList<>();
     paging.addPostParameter(Paging.S, 'z', pagingParams, "since_id", VALID_SINCE_ID);
 }

 @Test
 public void testAddPostParameterWithSupportedParamAndValue() {
     Paging paging = new Paging();
     List<HttpParameter> pagingParams = new ArrayList<>();
     paging.addPostParameter(Paging.S, 's', pagingParams, "since_id", VALID_SINCE_ID);
     List<HttpParameter> expectedList = Arrays.asList(new HttpParameter("since_id", String.valueOf(VALID_SINCE_ID)));
     Assert.assertEquals(expectedList, pagingParams);
 }

 @Test
 public void testAddPostParameterWithSupportedParamAndValueAndMaxSize() {
     Paging paging = new Paging();
     List<HttpParameter> pagingParams = new ArrayList<>();
     for (char supportedParam : Paging.SMCP) {
         for (int i = 0; i < 10; i++) {
             paging.addPostParameter(Paging.SMCP, supportedParam, pagingParams, "param" + i, i);
         }
     }
     List<HttpParameter> expectedList = new ArrayList<>();
     for (char supportedParam : Paging.SMCP) {
         for (int i = 0; i < 10; i++) {
             expectedList.add(new HttpParameter("param" + i, String.valueOf(i)));
         }
     }
     Assert.assertEquals(expectedList, pagingParams);
 }
}