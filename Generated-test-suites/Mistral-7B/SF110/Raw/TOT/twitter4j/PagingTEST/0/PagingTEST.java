import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

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
        Paging paging = new Paging(5);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(10L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(10L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(5, 10);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(5, 10L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(10L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(5, 10, 20L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(30L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePage() {
        new Paging(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        new Paging(5, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSinceId() {
        new Paging(5, 10, -20L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxId() {
        new Paging(5, 10, 20L, -30L);
    }

    @Test
    public void testGetters() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(30L, paging.getMaxId());
    }

    @Test
    public void testSetters() {
        Paging paging = new Paging();
        paging.setPage(5);
        paging.setCount(10);
        paging.setSinceId(20L);
        paging.setMaxId(30L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(30L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(5, 10, 20L, 30L);
        Paging paging2 = new Paging(5, 10, 20L, 30L);
        Paging paging3 = new Paging(5, 10, 21L, 31L);
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(5, 10, 20L, 30L);
        Paging paging2 = new Paging(5, 10, 20L, 30L);
        Paging paging3 = new Paging(5, 10, 21L, 31L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        assertEquals("Paging{page=5, count=10, sinceId=20, maxId=30}", paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] postParams = params.toArray(new HttpParameter[params.size()]);
        assertEquals(4, postParams.length);
        assertEquals("page", postParams[0].getName());
        assertEquals("5", postParams[0].getValue());
        assertEquals("count", postParams[1].getName());
        assertEquals("10", postParams[1].getValue());
        assertEquals("since_id", postParams[2].getName());
        assertEquals("20", postParams[2].getValue());
        assertEquals("max_id", postParams[3].getName());
        assertEquals("30", postParams[3].getValue());
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        List<HttpParameter> params = paging.asPostParameterList(new char[0], "count");
        assertTrue(params.isEmpty());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("5", params[0].getValue());
        assertEquals("count", params[1].getName());
        assertEquals("10", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("20", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("30", params[3].getValue());
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        HttpParameter[] params = paging.asPostParameterArray(new char[0], "count");
        assertTrue(params.length == 0);
    }
}