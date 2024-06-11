import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testPagingConstructorWithDefaultValues() {
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
        Paging paging = new Paging(1, 5);
        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
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
    public void testPagingConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 5, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(5);
        paging.setSinceId(1L);
        paging.setMaxId(1L);
        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersNegativeValues() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testAsPostParameterArrayAndAsPostParameterList() {
        Paging paging = new Paging(1, 5, 1L, 2L);
        HttpParameter[] array = paging.asPostParameterArray(Paging.SMCP, "count");
        List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");
        assertArrayEquals(new HttpParameter[] { new HttpParameter("page", "1"), new HttpParameter("count", "5"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2") }, array);
        assertEquals(list, paging.asPostParameterList(Paging.SMCP, "count"));
    }

    @Test
    public void testAsPostParameterArrayAndAsPostParameterListWithEmptyList() {
        Paging paging = new Paging();
        HttpParameter[] array = paging.asPostParameterArray(Paging.SMCP, "count");
        List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, array);
        assertEquals(Paging.NULL_PARAMETER_LIST, list);
    }
}