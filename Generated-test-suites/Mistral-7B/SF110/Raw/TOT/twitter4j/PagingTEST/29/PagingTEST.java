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
    public void testConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
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

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1L);
        paging.setMaxId(100L);

        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterListAndArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);

        List<HttpParameter> list = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] array = paging.asPostParameterArray(Paging.SMCP, "count");

        assertEquals(4, list.size());
        assertEquals("page", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());

        assertEquals("count", list.get(3).getName());
        assertEquals("10", list.get(3).getValue());

        assertEquals(4, array.length);
        assertEquals("page", array[0].getName());
        assertEquals("1", array[0].getValue());

        assertEquals("count", array[3].getName());
        assertEquals("10", array[3].getValue());
    }

    @Test
    public void testAddPostParameterWithUnsupportedParamKey() {
        char[] supportedParams = new char[]{'s', 'm'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging.addPostParameter(supportedParams, 'c', pagingParams, "count", -1L);
        assertTrue(pagingParams.isEmpty());
    }

    @Test
    public void testAddPostParameterWithSupportedParamKeyAndValidValue() {
        char[] supportedParams = new char[]{'s', 'm', 'c'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging.addPostParameter(supportedParams, 'c', pagingParams, "count", 10);
        assertEquals(1, pagingParams.size());
        assertEquals("count", pagingParams.get(0).getName());
        assertEquals("10", pagingParams.get(0).getValue());
    }

    @Test
    public void testAddPostParameterWithSupportedParamKeyAndInvalidValue() {
        char[] supportedParams = new char[]{'s', 'm', 'c'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        try {
            Paging.addPostParameter(supportedParams, 'c', pagingParams, "count", -1L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Paging parameter [count] is not supported with this operation.", e.getMessage());
        }
    }
}