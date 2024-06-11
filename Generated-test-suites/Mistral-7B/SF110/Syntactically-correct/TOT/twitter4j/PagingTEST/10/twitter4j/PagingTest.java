import org.junit.Test;
import org.junit.Assert.*;
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
        Paging paging = new Paging(1, 20);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
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
    public void testConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 20, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testGetters() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(20);
        paging.setSinceId(1L);
        paging.setMaxId(2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("20", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("1", parameters.get(2).getValue());
        assertEquals("max_id", parameters.get(3).getName());
        assertEquals("2", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] parameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, parameters.length);
        assertEquals("page", parameters[0].getName());
        assertEquals("1", parameters[0].getValue());
        assertEquals("count", parameters[1].getName());
        assertEquals("20", parameters[1].getValue());
        assertEquals("since_id", parameters[2].getName());
        assertEquals("1", parameters[2].getValue());
        assertEquals("max_id", parameters[3].getName());
        assertEquals("2", parameters[3].getValue());
    }
}