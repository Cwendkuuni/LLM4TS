package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10);
        char[] supportedParams = Paging.SMCP;
        List<HttpParameter> expected = new ArrayList<>();
        expected.add(new HttpParameter("page", "1"));
        expected.add(new HttpParameter("count", "10"));

        List<HttpParameter> actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setSinceId(1L);
        expected.add(new HttpParameter("since_id", "1"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setMaxId(2L);
        expected.add(new HttpParameter("max_id", "2"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setPage(2);
        expected = new ArrayList<>();
        expected.add(new HttpParameter("page", "2"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setCount(5);
        expected = new ArrayList<>();
        expected.add(new HttpParameter("count", "5"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setSinceId(3L);
        expected.add(new HttpParameter("since_id", "3"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setMaxId(4L);
        expected.add(new HttpParameter("max_id", "4"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setPage(2);
        expected.add(new HttpParameter("page", "2"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setCount(5);
        expected.add(new HttpParameter("count", "5"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setSinceId(3L);
        expected.add(new HttpParameter("since_id", "3"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setMaxId(4L);
        expected.add(new HttpParameter("max_id", "4"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setPage(2);
        expected.add(new HttpParameter("page", "2"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setCount(5);
        expected.add(new HttpParameter("count", "5"));
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);

        paging.setSinceId(-1L);
        paging.setMaxId(-1L);
        expected = Paging.NULL_PARAMETER_LIST;
        actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10);
        char[] supportedParams = Paging.SMCP;

        HttpParameter[] expected = new HttpParameter[] {
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10")
        };

        HttpParameter[] actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setSinceId(1L);
        expected = new HttpParameter[] {
                new HttpParameter("since_id", "1"),
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setMaxId(2L);
        expected = new HttpParameter[] {
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("max_id", "2")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setPage(2);
        expected = new HttpParameter[] {
                new HttpParameter("page", "2")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setCount(5);
        expected = new HttpParameter[] {
                new HttpParameter("count", "5")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setSinceId(3L);
        expected = new HttpParameter[] {
                new HttpParameter("since_id", "3")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setMaxId(4L);
        expected = new HttpParameter[] {
                new HttpParameter("max_id", "4")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setPage(2);
        expected = new HttpParameter[] {
                new HttpParameter("page", "2")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setCount(5);
        expected = new HttpParameter[] {
                new HttpParameter("count", "5")
        };
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(expected, actual);

        paging.setSinceId(-1L);
        paging.setMaxId(-1L);
        HttpParameter[] emptyArray = Paging.NULL_PARAMETER_ARRAY;
        actual = paging.asPostParameterArray(supportedParams);
        assertArrayEquals(emptyArray, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging(1, 10);
        char[] supportedParams = Paging.SMCP;
        paging.addPostParameter(supportedParams, 'x', new ArrayList<>(), "unsupported_param", 5L);
    }
}