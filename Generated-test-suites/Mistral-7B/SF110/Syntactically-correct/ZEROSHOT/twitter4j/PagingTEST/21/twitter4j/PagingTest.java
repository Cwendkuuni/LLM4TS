package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testPagingConstruction() {
        // Test default constructor
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        // Test constructor with page parameter
        Paging pagePaging = new Paging(5);
        assertEquals(5, pagePaging.getPage());
        assertEquals(-1, pagePaging.getCount());
        assertEquals(-1L, pagePaging.getSinceId());
        assertEquals(-1L, pagePaging.getMaxId());

        // Test constructor with sinceId parameter
        Paging sinceIdPaging = new Paging(10L);
        assertEquals(-1, sinceIdPaging.getPage());
        assertEquals(-1, sinceIdPaging.getCount());
        assertEquals(10L, sinceIdPaging.getSinceId());
        assertEquals(-1L, sinceIdPaging.getMaxId());

        // Test constructor with page and count parameters
        Paging pageCountPaging = new Paging(5, 10);
        assertEquals(5, pageCountPaging.getPage());
        assertEquals(10, pageCountPaging.getCount());
        assertEquals(-1L, pageCountPaging.getSinceId());
        assertEquals(-1L, pageCountPaging.getMaxId());

        // Test constructor with page, count, sinceId and maxId parameters
        Paging pageCountSinceIdMaxIdPaging = new Paging(5, 10, 20L, 30L);
        assertEquals(5, pageCountSinceIdMaxIdPaging.getPage());
        assertEquals(10, pageCountSinceIdMaxIdPaging.getCount());
        assertEquals(20L, pageCountSinceIdMaxIdPaging.getSinceId());
        assertEquals(30L, pageCountSinceIdMaxIdPaging.getMaxId());
    }

    @Test
    public void testGettersSetters() {
        Paging paging = new Paging();

        paging.setPage(5);
        assertEquals(5, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(20L);
        assertEquals(20L, paging.getSinceId());

        paging.setMaxId(30L);
        assertEquals(30L, paging.getMaxId());

        paging.setPage(7);
        assertEquals(7, paging.getPage());

        paging.setCount(15);
        assertEquals(15, paging.getCount());

        paging.setSinceId(35L);
        assertEquals(35L, paging.getSinceId());

        paging.setMaxId(45L);
        assertEquals(45L, paging.getMaxId());
    }

    @Test
    public void testEqualsHashCode() {
        Paging paging1 = new Paging(5, 10, 20L, 30L);
        Paging paging2 = new Paging(5, 10, 20L, 30L);
        Paging paging3 = new Paging(5, 11, 20L, 30L);

        assertEquals(paging1, paging2);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        Paging paging4 = new Paging();
        assertNotEquals(paging1, paging4);
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        String expected = "Paging{page=5, count=10, sinceId=20, maxId=30}";
        assertEquals(expected, paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        HttpParameter[] expected = new HttpParameter[] { new HttpParameter("page", "5"), new HttpParameter("count", "10"), new HttpParameter("since_id", "20"), new HttpParameter("max_id", "30") };
        HttpParameter[] result = paging.asPostParameterArray();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(5, 10, 20L, 30L);
        List<HttpParameter> expected = Arrays.asList(new HttpParameter("page", "5"), new HttpParameter("count", "10"), new HttpParameter("since_id", "20"), new HttpParameter("max_id", "30"));
        List<HttpParameter> result = paging.asPostParameterList();
        assertEquals(expected, result);
    }

    @Test
    public void testNullParameterArrayAndList() {
        assertEquals(Paging.NULL_PARAMETER_ARRAY, new Paging().asPostParameterArray());
        assertEquals(Paging.NULL_PARAMETER_LIST, new Paging().asPostParameterList());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        paging.addPostParameter(Paging.SMCP, 'x', new ArrayList<HttpParameter>(), "unsupported_param", 10L);
    }

    @Test
    public void testAddPostParameterWithSupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> params = new ArrayList<HttpParameter>(1);
        paging.addPostParameter(Paging.S, 's', params, "since_id", 20L);
        assertEquals(1, params.size());
        assertEquals("since_id", params.get(0).getName());
        assertEquals("20", params.get(0).getValue());
    }
}