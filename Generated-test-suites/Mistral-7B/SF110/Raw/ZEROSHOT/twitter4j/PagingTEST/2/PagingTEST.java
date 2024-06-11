import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

    private Paging paging;

    @Before
    public void setup() {
        paging = new Paging();
    }

    @Test
    public void testConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageConstructor() {
        paging = new Paging(5);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        paging = new Paging(10L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(10L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountConstructor() {
        paging = new Paging(5, 10);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        paging = new Paging(5, 10L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(10L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdCountConstructor() {
        paging = new Paging(5, 10, 20L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(20L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        paging.setPage(3);
        paging.setCount(20);
        paging.setSinceId(15L);
        paging.setMaxId(30L);

        assertEquals(3, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(15L, paging.getSinceId());
        assertEquals(30L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePage() {
        paging.setPage(-3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        paging.setCount(-20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSinceId() {
        paging.setSinceId(-15L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxId() {
        paging.setMaxId(-30L);
    }

    @Test
    public void testAsPostParameterList() {
        paging.setPage(3);
        paging.setCount(20);
        paging.setSinceId(15L);
        paging.setMaxId(30L);

        List<HttpParameter> params = paging.asPostParameterList();

        assertEquals(4, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("3", params.get(0).getValue());
        assertEquals("count", params.get(1).getName());
        assertEquals("20", params.get(1).getValue());
        assertEquals("since_id", params.get(2).getName());
        assertEquals("15", params.get(2).getValue());
        assertEquals("max_id", params.get(3).getName());
        assertEquals("30", params.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        paging.setPage(3);
        paging.setCount(20);
        paging.setSinceId(15L);
        paging.setMaxId(30L);

        HttpParameter[] params = paging.asPostParameterArray();

        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("3", params[0].getValue());
        assertEquals("count", params[1].getName());
        assertEquals("20", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("15", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("30", params[3].getValue());
    }
}