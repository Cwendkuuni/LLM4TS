package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testPagingConstruction() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());

        Paging paging1 = new Paging(1);
        assertEquals(1, paging1.getPage());
        assertNull(paging1.getCount());
        assertNull(paging1.getSinceId());
        assertNull(paging1.getMaxId());

        Paging paging2 = new Paging(1, 2);
        assertEquals(1, paging2.getPage());
        assertEquals(2, paging2.getCount());
        assertNull(paging2.getSinceId());
        assertNull(paging2.getMaxId());

        Paging paging3 = new Paging(1, 2, 3);
        assertEquals(1, paging3.getPage());
        assertEquals(2, paging3.getCount());
        assertEquals(3, paging3.getSinceId());
        assertNull(paging3.getMaxId());

        Paging paging4 = new Paging(1, 2, 3, 4);
        assertEquals(1, paging4.getPage());
        assertEquals(2, paging4.getCount());
        assertEquals(3, paging4.getSinceId());
        assertEquals(4, paging4.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());

        paging.setCount(2);
        assertEquals(2, paging.getCount());

        paging.setSinceId(3);
        assertEquals(3, paging.getSinceId());

        paging.setMaxId(4);
        assertEquals(4, paging.getMaxId());

        paging.setPage(5);
        assertEquals(5, paging.getPage());

        paging.setCount(6);
        assertEquals(6, paging.getCount());

        paging.setSinceId(7);
        assertEquals(7, paging.getSinceId());

        paging.setMaxId(8);
        assertEquals(8, paging.getMaxId());

        paging.count(9);
        assertEquals(9, paging.getCount());

        paging.sinceId(10);
        assertEquals(10, paging.getSinceId());

        paging.maxId(11);
        assertEquals(11, paging.getMaxId());
    }

    @Test
    public void testEqualsHashcode() {
        Paging paging1 = new Paging(1, 2, 3, 4);
        Paging paging2 = new Paging(1, 2, 3, 4);
        assertTrue(paging1.equals(paging2));
        assertTrue(paging1.hashCode() == paging2.hashCode());

        Paging paging3 = new Paging(1, 2, 3, 5);
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.hashCode() == paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 2, 3, 4);
        String toString = paging.toString();
        assertTrue(toString.contains("Paging{"));
        assertTrue(toString.contains("page=" + 1));
        assertTrue(toString.contains("count=" + 2));
        assertTrue(toString.contains("sinceId=" + 3));
        assertTrue(toString.contains("maxId=" + 4));
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 2, 3, 4);
        List<HttpParameter> asPostParameterList = paging.asPostParameterList();
        assertNotNull(asPostParameterList);
        assertSame(asPostParameterList.getClass(), ArrayList.class);
        assertEquals(4, asPostParameterList.size());

        HttpParameter parameter = asPostParameterList.get(0);
        assertEquals("page", parameter.getName());
        assertEquals("1", parameter.getValue());

        parameter = asPostParameterList.get(1);
        assertEquals("count", parameter.getName());
        assertEquals("2", parameter.getValue());

        parameter = asPostParameterList.get(2);
        assertEquals("since_id", parameter.getName());
        assertEquals("3", parameter.getValue());

        parameter = asPostParameterList.get(3);
        assertEquals("max_id", parameter.getName());
        assertEquals("4", parameter.getValue());
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(1, 2, 3, 4);
        char[] supportedParams = Paging.SMCP;
        List<HttpParameter> asPostParameterList = paging.asPostParameterList(supportedParams, "count");
        assertNotNull(asPostParameterList);
        assertSame(asPostParameterList.getClass(), ArrayList.class);
        assertEquals(4, asPostParameterList.size());

        HttpParameter parameter = asPostParameterList.get(0);
        assertEquals("s", parameter.getName());
        assertEquals("page", parameter.getParameterName());
        assertEquals("1", parameter.getValue());

        parameter = asPostParameterList.get(1);
        assertEquals("m", parameter.getName());
        assertEquals("since_id", parameter.getParameterName());
        assertEquals("3", parameter.getValue());

        parameter = asPostParameterList.get(2);
        assertEquals("c", parameter.getName());
        assertEquals("count", parameter.getParameterName());
        assertEquals("2", parameter.getValue());

        parameter = asPostParameterList.get(3);
        assertEquals("p", parameter.getName());
        assertEquals("page", parameter.getParameterName());
        assertEquals("1", parameter.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;
        List<HttpParameter> pagingParams = new ArrayList<HttpParameter>(supportedParams.length);
        this.addPostParameter(supportedParams, 'x', pagingParams, "unsupported_param", 5L);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 2, 3, 4);
        char[] supportedParams = Paging.SMCP;
        HttpParameter[] asPostParameterArray = paging.asPostParameterArray(supportedParams, "count");
        assertNotNull(asPostParameterArray);
        assertSame(asPostParameterArray.getClass(), Paging.NULL_PARAMETER_ARRAY.getClass());
        assertEquals(4, asPostParameterArray.length);

        HttpParameter parameter = asPostParameterArray[0];
        assertEquals("s", parameter.getName());
        assertEquals("page", parameter.getParameterName());
        assertEquals("1", parameter.getValue());

        parameter = asPostParameterArray[1];
        assertEquals("m", parameter.getName());
        assertEquals("since_id", parameter.getParameterName());
        assertEquals("3", parameter.getValue());

        parameter = asPostParameterArray[2];
        assertEquals("c", parameter.getName());
        assertEquals("count", parameter.getParameterName());
        assertEquals("2", parameter.getValue());

        parameter = asPostParameterArray[3];
        assertEquals("p", parameter.getName());
        assertEquals("page", parameter.getParameterName());
        assertEquals("1", parameter.getValue());
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging(1, 2, 3, 4);
        char[] supportedParams = Paging.SMCP;
        HttpParameter[] asPostParameterArray = paging.asPostParameterArray(supportedParams, "count");
        assertNotNull(asPostParameterArray);
        assertSame(asPostParameterArray.getClass(), new HttpParameter[4].getClass());

        HttpParameter parameter = asPostParameterArray[0];
        assertEquals("s", parameter.getName());
        assertEquals("page", parameter.getParameterName());
        assertEquals("1", parameter.getValue());

        parameter = asPostParameterArray[1];
        assertEquals("m", parameter.getName());
        assertEquals("since_id", parameter.getParameterName());
        assertEquals("3", parameter.getValue());

        parameter = asPostParameterArray[2];
        assertEquals("c", parameter.getName());
        assertEquals("count", parameter.getParameterName());
        assertEquals("2", parameter.getValue());

        parameter = asPostParameterArray[3];
        assertEquals("p", parameter.getName());
        assertEquals("page", parameter.getParameterName());
        assertEquals("1", parameter.getValue());
    }

    private void addPostParameter(final char[] supportedParams, final char paramKey, final List<HttpParameter> pagingParams, final String paramName, final long paramValue) {
        Paging paging = new Paging();
        paging.addPostParameter(supportedParams, paramKey, pagingParams, paramName, paramValue);
    }
}