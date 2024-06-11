package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testPagingDefaultConstruction() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructionWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructionWithSinceId() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructionWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructionWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructionWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructionWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSettersForPage() {
        Paging paging = new Paging(1);
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    @Test
    public void testGettersAndSettersForCount() {
        Paging paging = new Paging(1);
        paging.setCount(2);
        assertEquals(2, paging.getCount());
    }

    @Test
    public void testGettersAndSettersForSinceId() {
        Paging paging = new Paging(1);
        paging.setSinceId(2L);
        assertEquals(2L, paging.getSinceId());
    }

    @Test
    public void testGettersAndSettersForMaxId() {
        Paging paging = new Paging(1);
        paging.setMaxId(2L);
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 2L);
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        Paging paging3 = new Paging(1, 11, 1L, 2L);

        assertEquals(paging1.hashCode(), paging1.hashCode());
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 2L);
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        Paging paging3 = new Paging(1, 11, 1L, 2L);

        assertEquals(paging1, paging1);
        assertEquals(paging1, paging2);
        assertNotEquals(paging1, paging3);
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=2}", paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        char[] supportedParams = Paging.SMCP;
        HttpParameter[] expectedPostParams = new HttpParameter[] { new HttpParameter("count", "10"), new HttpParameter("page", "1"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2") };
        HttpParameter[] actualPostParams = paging.asPostParameterArray(supportedParams, "count");

        assertArrayEquals(expectedPostParams, actualPostParams);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        char[] supportedParams = Paging.SMCP;
        List<HttpParameter> expectedPostParams = Arrays.asList(new HttpParameter("count", "10"), new HttpParameter("page", "1"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2") );
        List<HttpParameter> actualPostParams = paging.asPostParameterList(supportedParams, "count");

        assertEquals(expectedPostParams, actualPostParams);
    }

    @Test
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        char[] supportedParams = Paging.S;
        List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();

        assertThrows(IllegalStateException.class, () -> paging.addPostParameter(supportedParams, 'x', pagingParams, "unsupportedParam", 1L));
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        char[] supportedParams = Paging.S;
        HttpParameter[] expectedPostParams = new HttpParameter[0];
        HttpParameter[] actualPostParams = paging.asPostParameterArray(supportedParams, "unsupportedParam");

        assertArrayEquals(expectedPostParams, actualPostParams);
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        char[] supportedParams = Paging.S;
        List<HttpParameter> expectedPostParams = new ArrayList<HttpParameter>();
        List<HttpParameter> actualPostParams = paging.asPostParameterList(supportedParams, "unsupportedParam");

        assertEquals(expectedPostParams, actualPostParams);
        assertNull(actualPostParams.get(0));
    }
}