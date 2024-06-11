import org.junit.Test;
import static org.junit.Assert.*;
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
    public void testConstructorWithPageCountSinceId() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(30L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetAndSetPage() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test
    public void testGetAndSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testGetAndSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(20L);
        assertEquals(20L, paging.getSinceId());
    }

    @Test
    public void testGetAndSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(30L);
        assertEquals(30L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("5", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("10", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("20", parameters.get(2).getValue());
        assertEquals("max_id", parameters.get(3).getName());
        assertEquals("30", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        HttpParameter[] parameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, parameters.length);
        assertEquals("page", parameters[0].getName());
        assertEquals("5", parameters[0].getValue());
        assertEquals("count", parameters[1].getName());
        assertEquals("10", parameters[1].getValue());
        assertEquals("since_id", parameters[2].getName());
        assertEquals("20", parameters[2].getValue());
        assertEquals("max_id", parameters[3].getName());
        assertEquals("30", parameters[3].getValue());
    }
}