package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testPagingConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        Paging pagingWithPage = new Paging(1);
        assertEquals(1, pagingWithPage.getPage());
        assertEquals(-1, pagingWithPage.getCount());
        assertEquals(-1L, pagingWithPage.getSinceId());
        assertEquals(-1L, pagingWithPage.getMaxId());

        Paging pagingWithCount = new Paging(1, 2);
        assertEquals(1, pagingWithCount.getPage());
        assertEquals(2, pagingWithCount.getCount());
        assertEquals(-1L, pagingWithCount.getSinceId());
        assertEquals(-1L, pagingWithCount.getMaxId());

        Paging pagingWithSinceId = new Paging(1, 2, 3L);
        assertEquals(1, pagingWithSinceId.getPage());
        assertEquals(2, pagingWithSinceId.getCount());
        assertEquals(3L, pagingWithSinceId.getSinceId());
        assertEquals(-1L, pagingWithSinceId.getMaxId());

        Paging pagingWithAllParameters = new Paging(1, 2, 3L, 4L);
        assertEquals(1, pagingWithAllParameters.getPage());
        assertEquals(2, pagingWithAllParameters.getCount());
        assertEquals(3L, pagingWithAllParameters.getSinceId());
        assertEquals(4L, pagingWithAllParameters.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(2);
        paging.setSinceId(3L);
        paging.setMaxId(4L);

        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(4L, paging.getMaxId());

        paging.setPage(-1);
        paging.setCount(-1);
        paging.setSinceId(-1L);
        paging.setMaxId(-1L);

        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, params.size());
        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "2"),
                new HttpParameter("since_id", "3"),
                new HttpParameter("max_id", "4")
        );
        assertEquals(expectedParams, params);

        params = paging.asPostParameterList();
        assertEquals(4, params.size());
        expectedParams = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "2"),
                new HttpParameter("since_id", "3"),
                new HttpParameter("max_id", "4")
        );
        assertEquals(expectedParams, params);
    }

    @Test
    public void testAsPostParameterList_UnsupportedParam() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        List<HttpParameter> params = paging.asPostParameterList(new char[]{'x'}, "count");
        assertEquals(0, params.size());
        assertNull(Paging.NULL_PARAMETER_LIST);

        try {
            params = paging.asPostParameterList(new char[]{'x'}, "unsupported_param");
        } catch (IllegalStateException e) {
            assertEquals("Paging parameter [unsupported_param] is not supported with this operation.", e.getMessage());
        }
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArrayEquals(new HttpParameter[]{"page", "1"}, Arrays.copyOfRange(params, 0, 1));
        assertArrayEquals(new HttpParameter[]{"count", "2"}, Arrays.copyOfRange(params, 1, 2));
        assertArrayEquals(new HttpParameter[]{"since_id", "3"}, Arrays.copyOfRange(params, 2, 3));
        assertArrayEquals(new HttpParameter[]{"max_id", "4"}, Arrays.copyOfRange(params, 3, 4));

        params = paging.asPostParameterArray();
        assertArrayEquals(new HttpParameter[]{"page", "1"}, Arrays.copyOfRange(params, 0, 1));
        assertArrayEquals(new HttpParameter[]{"count", "2"}, Arrays.copyOfRange(params, 1, 2));
        assertArrayEquals(new HttpParameter[]{"since_id", "3"}, Arrays.copyOfRange(params, 2, 3));
        assertArrayEquals(new HttpParameter[]{"max_id", "4"}, Arrays.copyOfRange(params, 3, 4));
    }

    @Test
    public void testAsPostParameterArray_UnsupportedParam() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        HttpParameter[] params = paging.asPostParameterArray(new char[]{'x'}, "count");
        assertArrayEquals(new HttpParameter[0], params);
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, params);

        try {
            params = paging.asPostParameterArray(new char[]{'x'}, "unsupported_param");
        } catch (IllegalStateException e) {
            assertEquals("Paging parameter [unsupported_param] is not supported with this operation.", e.getMessage());
        }
    }
}