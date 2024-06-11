import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

 private final Paging defaultPaging = new Paging();
 private final Paging pagePaging = new Paging(1);
 private final Paging sinceIdPaging = new Paging(-1L);
 private final Paging countPaging = new Paging(-1, 2);
 private final Paging pageCountPaging = new Paging(1, 2);
 private final Paging pageSinceIdPaging = new Paging(1, -1L);
 private final Paging pageCountSinceIdPaging = new Paging(1, 2, -1L);
 private final Paging pageCountSinceIdMaxIdPaging = new Paging(1, 2, -1L, 3L);

 @Test
 public void testDefaultPaging() {
     assertEquals(-1, defaultPaging.getPage());
     assertEquals(-1, defaultPaging.getCount());
     assertEquals(-1L, defaultPaging.getSinceId());
     assertEquals(-1L, defaultPaging.getMaxId());
 }

 @Test
 public void testPagePaging() {
     assertEquals(1, pagePaging.getPage());
     assertEquals(-1, pagePaging.getCount());
     assertEquals(-1L, pagePaging.getSinceId());
     assertEquals(-1L, pagePaging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativePage() {
     try {
         new Paging(-2);
         fail();
     } catch (IllegalArgumentException e) {
         assertEquals("page should be positive integer. passed:-2", e.getMessage());
     }
 }

 @Test
 public void testSinceIdPaging() {
     assertEquals(-1, sinceIdPaging.getPage());
     assertEquals(-1, sinceIdPaging.getCount());
     assertEquals(-1L, sinceIdPaging.getSinceId());
     assertEquals(-1L, sinceIdPaging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeSinceId() {
     try {
         new Paging(-2L);
         fail();
     } catch (IllegalArgumentException e) {
         assertEquals("since_id should be positive integer. passed:-2", e.getMessage());
     }
 }

 @Test
 public void testCountPaging() {
     assertEquals(-1, countPaging.getPage());
     assertEquals(2, countPaging.getCount());
     assertEquals(-1L, countPaging.getSinceId());
     assertEquals(-1L, countPaging.getMaxId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCount() {
     try {
         new Paging(-2, 2);
         fail();
     } catch (IllegalArgumentException e) {
         assertEquals("count should be positive integer. passed:-2", e.getMessage());
     }
 }

 @Test
 public void testPageCountPaging() {
     assertEquals(1, pageCountPaging.getPage());
     assertEquals(2, pageCountPaging.getCount());
     assertEquals(-1L, pageCountPaging.getSinceId());
     assertEquals(-1L, pageCountPaging.getMaxId());
 }

 @Test
 public void testPageSinceIdPaging() {
     assertEquals(1, pageSinceIdPaging.getPage());
     assertEquals(-1, pageSinceIdPaging.getCount());
     assertEquals(-1L, pageSinceIdPaging.getSinceId());
     assertEquals(-1L, pageSinceIdPaging.getMaxId());
 }

 @Test
 public void testPageCountSinceIdPaging() {
     assertEquals(1, pageCountSinceIdPaging.getPage());
     assertEquals(2, pageCountSinceIdPaging.getCount());
     assertEquals(-1L, pageCountSinceIdPaging.getSinceId());
     assertEquals(3L, pageCountSinceIdPaging.getMaxId());
 }

 @Test
 public void testPageCountSinceIdMaxIdPaging() {
     assertEquals(1, pageCountSinceIdMaxIdPaging.getPage());
     assertEquals(2, pageCountSinceIdMaxIdPaging.getCount());
     assertEquals(-1L, pageCountSinceIdMaxIdPaging.getSinceId());
     assertEquals(3L, pageCountSinceIdMaxIdPaging.getMaxId());
 }

 @Test
 public void testAsPostParameterList() {
     final List<HttpParameter> expected = pageCountSinceIdMaxIdPaging.asPostParameterList(Paging.SMCP, "per_page");
     final List<HttpParameter> actual = pageCountSinceIdMaxIdPaging.asPostParameterList();
     assertEquals(expected.size(), actual.size());
     for (int i = 0; i < expected.size(); i++) {
         final HttpParameter expectedParameter = expected.get(i);
         final HttpParameter actualParameter = actual.get(i);
         assertEquals(expectedParameter.getName(), actualParameter.getName());
         assertEquals(expectedParameter.getValue(), actualParameter.getValue());
     }
 }

 @Test
 public void testAsPostParameterArray() {
     final HttpParameter[] expected = pageCountSinceIdMaxIdPaging.asPostParameterArray(Paging.SMCP, "per_page");
     final HttpParameter[] actual = pageCountSinceIdMaxIdPaging.asPostParameterArray();
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddPostParameter() {
     final List<HttpParameter> pagingParams = new ArrayList<>();
     Paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", -1L);
     assertTrue(pagingParams.isEmpty());
     Paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 1L);
     assertEquals(1, pagingParams.size());
     final HttpParameter actual = pagingParams.get(0);
     assertEquals("since_id", actual.getName());
     assertEquals("1", actual.getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsupportedParam() {
     try {
         new Paging().addPostParameter(Paging.SMCP, 'x', null, "unsupported", 1L);
         fail();
     } catch (IllegalStateException e) {
         assertEquals("Paging parameter [unsupported] is not supported with this operation.", e.getMessage());
     }
 }
}