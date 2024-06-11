import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testEmptyPaging() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testBasicPaging() {
        Paging paging = new Paging(1, 20);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testPagingWithSinceId() {
        Paging paging = new Paging(1, 20, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testPagingWithMaxId() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testPagingWithAllParams() {
        Paging paging = new Paging(2, 30, 1L, 2L);
        assertEquals(2, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPage() {
        new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCount() {
        new Paging(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceId() {
        new Paging(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxId() {
        new Paging(0L, 1);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("1", params[0].getValue());
        assertEquals("count", params[1].getName());
        assertEquals("20", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("1", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("2", params[3].getValue());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("1", params.get(0).getValue());
        assertEquals("count", params.get(1).getName());
        assertEquals("20", params.get(1).getValue());
        assertEquals("since_id", params.get(2).getName());
        assertEquals("1", params.get(2).getValue());
        assertEquals("max_id", params.get(3).getName());
        assertEquals("2", params.get(3).getValue());
    }
}