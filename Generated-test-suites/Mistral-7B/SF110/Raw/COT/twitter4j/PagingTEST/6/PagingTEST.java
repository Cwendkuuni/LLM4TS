import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
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
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithAllParams() {
        Paging paging = new Paging(1, 10, 1L, 11L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(11L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativePage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(5);
        assertEquals(5, paging.getCount());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(11L);
        assertEquals(11L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 11L);
        Paging paging2 = new Paging(1, 10, 1L, 11L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(1, 10, 1L, 12L);
        assertFalse(paging1.equals(paging3));

        Object obj = new Object();
        assertFalse(paging1.equals(obj));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 11L);
        Paging paging2 = new Paging(1, 10, 1L, 11L);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging(1, 10, 1L, 12L);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 11L);
        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=11}", paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(4, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("1", params.get(0).getValue());
        assertEquals("count", params.get(1).getName());
        assertEquals("10", params.get(1).getValue());
        assertEquals("since_id", params.get(2).getName());
        assertEquals("-1", params.get(2).getValue());
        assertEquals("max_id", params.get(3).getName());
        assertEquals("-1", params.get(3).getValue());
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "per_page");
        assertEquals(4, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("1", params.get(0).getValue());
        assertEquals("per_page", params.get(1).getName());
        assertEquals("10", params.get(1).getValue());
        assertEquals("since_id", params.get(2).getName());
        assertEquals("-1", params.get(2).getValue());
        assertEquals("max_id", params.get(3).getName());
        assertEquals("-1", params.get(3).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> params = paging.asPostParameterList(Paging.S, "unsupported_param");
        fail("Should throw IllegalStateException");
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10);
        HttpParameter[] params = paging.asPostParameterArray();
        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("1", params[0].getValue());
        assertEquals("count", params[1].getName());
        assertEquals("10", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("-1", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("-1", params[3].getValue());
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging(1, 10);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "per_page");
        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("1", params[0].getValue());
        assertEquals("per_page", params[1].getName());
        assertEquals("10", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("-1", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("-1", params[3].getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging(1, 10);
        HttpParameter[] params = paging.asPostParameterArray(Paging.S, "unsupported_param");
        fail("Should throw IllegalStateException");
    }
}