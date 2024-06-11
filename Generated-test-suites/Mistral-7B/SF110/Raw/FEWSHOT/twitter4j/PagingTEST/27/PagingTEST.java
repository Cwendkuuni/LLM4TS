package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testValidConstructions() {
        Paging emptyPaging = new Paging();
        Paging pagePaging = new Paging(1);
        Paging countPaging = new Paging(1, 20);
        Paging sinceIdPaging = new Paging(1, -1L, 20);
        Paging pageCountPaging = new Paging(1, 20, -1L, 30);
        Paging pageCountSinceIdPaging = new Paging(1, 20, -1L, 30, 40);

        assertEquals(1, emptyPaging.getPage());
        assertEquals(-1, emptyPaging.getCount());
        assertEquals(-1L, emptyPaging.getSinceId());
        assertEquals(-1L, emptyPaging.getMaxId());

        assertEquals(1, pagePaging.getPage());
        assertEquals(-1, pagePaging.getCount());
        assertEquals(-1L, pagePaging.getSinceId());
        assertEquals(-1L, pagePaging.getMaxId());

        assertEquals(1, countPaging.getPage());
        assertEquals(20, countPaging.getCount());
        assertEquals(-1L, countPaging.getSinceId());
        assertEquals(-1L, countPaging.getMaxId());

        assertEquals(1, sinceIdPaging.getPage());
        assertEquals(-1, sinceIdPaging.getCount());
        assertEquals(-1L, sinceIdPaging.getSinceId());
        assertEquals(30L, sinceIdPaging.getMaxId());

        assertEquals(1, countSinceIdPaging.getPage());
        assertEquals(20, countSinceIdPaging.getCount());
        assertEquals(-1L, countSinceIdPaging.getSinceId());
        assertEquals(40L, countSinceIdPaging.getMaxId());

        assertEquals(1, pageCountPaging.getPage());
        assertEquals(20, pageCountPaging.getCount());
        assertEquals(-1L, pageCountPaging.getSinceId());
        assertEquals(30L, pageCountPaging.getMaxId());

        assertEquals(1, pageCountSinceIdPaging.getPage());
        assertEquals(20, pageCountSinceIdPaging.getCount());
        assertEquals(-1L, pageCountSinceIdPaging.getSinceId());
        assertEquals(40L, pageCountSinceIdPaging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructions() {
        new Paging(-1);
        new Paging(0, 20);
        new Paging(1, -1);
        new Paging(1, 0);
        new Paging(1, -1, -1);
        new Paging(1, 20, -1);
        new Paging(1, 20, 0);
        new Paging(1, 20, 40, -1);
        new Paging(1, 20, 40, 0);
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 20, -1L, 30);

        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(30L, paging.getMaxId());

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(30);
        assertEquals(30, paging.getCount());

        paging.setSinceId(42L);
        assertEquals(42L, paging.getSinceId());

        paging.setMaxId(12345L);
        assertEquals(12345L, paging.getMaxId());

        paging.count(15);
        assertEquals(15, paging.getCount());

        paging.sinceId(12345L);
        assertEquals(12345L, paging.getSinceId());

        paging.maxId(123456);
        assertEquals(123456L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 20, -1L, 30);

        HttpParameter[] expectedParameters = new HttpParameter[]{
            new HttpParameter("page", "1"),
            new HttpParameter("count", "20"),
            new HttpParameter("since_id", "-1"),
            new HttpParameter("max_id", "30")
        };

        HttpParameter[] resultParameters = paging.asPostParameterArray();
        assertArrayEquals(expectedParameters, resultParameters);
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging(1, 20, 's', 123456L, 30);

        HttpParameter[] expectedParameters = new HttpParameter[]{
            new HttpParameter("page", "1"),
            new HttpParameter("count", "20"),
            new HttpParameter("since_id", "123456"),
            new HttpParameter("max_id", "30")
        };

        HttpParameter[] resultParameters = paging.asPostParameterArray('s');
        assertArrayEquals(expectedParameters, resultParameters);
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging(1, 20, -1L, 30);

        try {
            HttpParameter[] resultParameters = paging.asPostParameterArray('x');
            fail("Should have thrown an exception.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, -1L, 30);

        List<HttpParameter> expectedParameters = Arrays.asList(
            new HttpParameter("page", "1"),
            new HttpParameter("count", "20"),
            new HttpParameter("since_id", "-1"),
            new HttpParameter("max_id", "30")
        );

        List<HttpParameter> resultParameters = paging.asPostParameterList();
        assertEquals(expectedParameters, resultParameters);
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(1, 20, 's', 123456L, 30);

        List<HttpParameter> expectedParameters = Arrays.asList(
            new HttpParameter("page", "1"),
            new HttpParameter("count", "20"),
            new HttpParameter("since_id", "123456"),
            new HttpParameter("max_id", "30")
        );

        List<HttpParameter> resultParameters = paging.asPostParameterList('s');
        assertEquals(expectedParameters, resultParameters);
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging(1, 20, -1L, 30);

        try {
            List<HttpParameter> resultParameters = paging.asPostParameterList('x');
            fail("Should have thrown an exception.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}