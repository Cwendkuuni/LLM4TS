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
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
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
        Paging paging = new Paging(5, 1L);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(5, 10, 1L, 10L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(10L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(5);
        paging.setCount(10);
        paging.setSinceId(1L);
        paging.setMaxId(10L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(10L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithInvalidValues() {
        Paging paging = new Paging();
        paging.setPage(-1);
        paging.setCount(-1);
        paging.setSinceId(-1L);
        paging.setMaxId(-1L);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(5, 10, 1L, 10L);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "per_page");
        assertEquals(4, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("5", params.get(0).getValue());
        assertEquals("per_page", params.get(1).getName());
        assertEquals("10", params.get(1).getValue());
        assertEquals("since_id", params.get(2).getName());
        assertEquals("1", params.get(2).getValue());
        assertEquals("max_id", params.get(3).getName());
        assertEquals("10", params.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(5, 10, 1L, 10L);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "per_page");
        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("5", params[0].getValue());
        assertEquals("per_page", params[1].getName());
        assertEquals("10", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("1", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("10", params[3].getValue());
    }
}