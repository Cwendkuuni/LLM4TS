package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
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
        Paging paging = new Paging(5, 10, 20L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(30L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
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
        new Paging(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxId() {
        new Paging(0L);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        HttpParameter[] expected = new HttpParameter[] {
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "20"),
                new HttpParameter("max_id", "30")
        };
        HttpParameter[] actual = paging.asPostParameterArray(new char[] {'s', 'm', 'c', 'p'});
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        List<HttpParameter> expected = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "20"),
                new HttpParameter("max_id", "30")
        );
        List<HttpParameter> actual = paging.asPostParameterList(new char[] {'s', 'm', 'c', 'p'});
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        HttpParameter[] expected = Paging.NULL_PARAMETER_ARRAY;
        HttpParameter[] actual = paging.asPostParameterArray(new char[0]);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        List<HttpParameter> expected = Paging.NULL_PARAMETER_LIST;
        List<HttpParameter> actual = paging.asPostParameterList(new char[0]);
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithInvalidParamKey() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        List<HttpParameter> actual = paging.asPostParameterList(new char[] {'x'});
        assertEquals(0, actual.size());
    }

    @Test
    public void testAsPostParameterListWithInvalidParamValue() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        List<HttpParameter> actual = paging.asPostParameterList(new char[] {'s', 'x'});
        assertEquals(0, actual.size());
    }
}