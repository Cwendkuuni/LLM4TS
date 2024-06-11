import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testPagingDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithAllParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        paging.setPage(2);
        paging.setCount(5);
        paging.setSinceId(2L);
        paging.setMaxId(50L);
        assertEquals(2, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(2L, paging.getSinceId());
        assertEquals(50L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(1, 10, 1L, 200L);
        assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging(1, 10, 1L, 200L);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", paging.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPage() {
        new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCount() {
        new Paging(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceId() {
        new Paging(1, 10, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxId() {
        new Paging(1, 10, 1L, -1L);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArrayEquals(new HttpParameter[] { new HttpParameter("count", "10")}, params);

        params = paging.asPostParameterArray(Paging.S, "max_id");
        assertArrayEquals(new HttpParameter[] {}, params);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(1, params.size());
        assertEquals("count", params.get(0).getName());
        assertEquals("10", params.get(0).getValue());

        params = paging.asPostParameterList(Paging.S, "max_id");
        assertEquals(0, params.size());
    }
}