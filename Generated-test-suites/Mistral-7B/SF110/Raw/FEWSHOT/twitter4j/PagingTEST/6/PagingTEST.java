package twitter4j;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> expected = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "2")
        );

        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);

        Paging pagingEmpty = new Paging();
        List<HttpParameter> actualEmpty = pagingEmpty.asPostParameterList();
        assertEquals(Paging.NULL_PARAMETER_LIST, actualEmpty);
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> actual = paging.asPostParameterList((char) 'x');
        assertEquals(Paging.NULL_PARAMETER_LIST, actual);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] expected = new HttpParameter[] {
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "2")
        };

        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);

        Paging pagingEmpty = new Paging();
        HttpParameter[] actualEmpty = pagingEmpty.asPostParameterArray();
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, actualEmpty);
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] actual = paging.asPostParameterArray((char) 'x');
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, actual);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();

        paging.addPostParameter(Paging.SMCP, 'c', pagingParams, "count", 10);
        assertEquals(1, pagingParams.size());
        assertEquals("count", pagingParams.get(0).getName());
        assertEquals("10", pagingParams.get(0).getValue());

        paging.addPostParameter(Paging.SMCP, 'c', pagingParams, "count", 5);
        assertEquals(1, pagingParams.size());
        assertEquals("count", pagingParams.get(0).getName());
        assertEquals("5", pagingParams.get(0).getValue());

        paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "count", 3);
        assertEquals(1, pagingParams.size());
        assertEquals("count", pagingParams.get(0).getName());
        assertEquals("5", pagingParams.get(0).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParamValue() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();

        paging.addPostParameter(Paging.SMCP, 'c', pagingParams, "count", 0);
    }

    @Test
    public void testEquals() {
        Paging p1 = new Paging(1, 10, 1L, 2L);
        Paging p2 = new Paging(1, 10, 1L, 2L);
        Paging p3 = new Paging(1, 11, 1L, 2L);

        assertTrue(p1.equals(p1));
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
    }

    @Test
    public void testHashCode() {
        Paging p1 = new Paging(1, 10, 1L, 2L);
        Paging p2 = new Paging(1, 10, 1L, 2L);
        Paging p3 = new Paging(1, 11, 1L, 2L);

        int hashCode1 = p1.hashCode();
        int hashCode2 = p1.hashCode();
        int hashCode3 = p3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}