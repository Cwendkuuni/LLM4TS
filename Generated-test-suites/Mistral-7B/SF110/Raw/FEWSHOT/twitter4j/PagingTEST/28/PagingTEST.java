package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testConstructors() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging(1);
        Paging paging3 = new Paging(1, 10);
        Paging paging4 = new Paging(1, 10, 123L);
        Paging paging5 = new Paging(1, 10, 123L, 456L);

        assertEquals(-1, paging1.getPage());
        assertEquals(-1, paging1.getCount());
        assertEquals(-1L, paging1.getSinceId());
        assertEquals(-1L, paging1.getMaxId());

        assertEquals(1, paging2.getPage());
        assertEquals(-1, paging2.getCount());
        assertEquals(-1L, paging2.getSinceId());
        assertEquals(-1L, paging2.getMaxId());

        assertEquals(1, paging3.getPage());
        assertEquals(10, paging3.getCount());
        assertEquals(-1L, paging3.getSinceId());
        assertEquals(-1L, paging3.getMaxId());

        assertEquals(1, paging4.getPage());
        assertEquals(10, paging4.getCount());
        assertEquals(123L, paging4.getSinceId());
        assertEquals(-1L, paging4.getMaxId());

        assertEquals(1, paging5.getPage());
        assertEquals(10, paging5.getCount());
        assertEquals(123L, paging5.getSinceId());
        assertEquals(456L, paging5.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 10, 123L, 456L);

        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123L, paging.getSinceId());
        assertEquals(456L, paging.getMaxId());

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(20);
        assertEquals(20, paging.getCount());

        paging.setSinceId(789L);
        assertEquals(789L, paging.getSinceId());

        paging.setMaxId(1111L);
        assertEquals(1111L, paging.getMaxId());
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 10, 123L, 456L);
        Paging paging2 = new Paging(1, 10, 123L, 456L);
        Paging paging3 = new Paging(1, 10, 12345L, 456L);
        Paging paging4 = new Paging(1, 10, 123L, 456L);
        Paging paging5 = new Object();

        assertTrue(paging1.equals(paging2));
        assertTrue(paging1.hashCode() == paging2.hashCode());
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.equals(paging5));
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 123L, 456L);

        assertEquals("Paging{page=1, count=10, sinceId=123, maxId=456}", paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 123L, 456L);
        char[] supportedParams = Paging.SMCP;

        HttpParameter[] params = paging.asPostParameterArray(supportedParams, "per_page");

        assertEquals(4, params.length);
        assertEquals("page", params[0].getName());
        assertEquals("1", params[0].getValue());
        assertEquals("per_page", params[1].getName());
        assertEquals("10", params[1].getValue());
        assertEquals("since_id", params[2].getName());
        assertEquals("123", params[2].getValue());
        assertEquals("max_id", params[3].getName());
        assertEquals("456", params[3].getValue());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 123L, 456L);
        char[] supportedParams = Paging.SMCP;

        List<HttpParameter> params = paging.asPostParameterList(supportedParams, "per_page");

        assertEquals(4, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("1", params.get(0).getValue());
        assertEquals("per_page", params.get(1).getName());
        assertEquals("10", params.get(1).getValue());
        assertEquals("since_id", params.get(2).getName());
        assertEquals("123", params.get(2).getValue());
        assertEquals("max_id", params.get(3).getName());
        assertEquals("456", params.get(3).getValue());
    }

    @Test
    public void testEmptyConstructors() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging(null, -1, null, null);

        assertEquals(-1, paging1.getPage());
        assertEquals(-1, paging1.getCount());
        assertEquals(-1L, paging1.getSinceId());
        assertEquals(-1L, paging1.getMaxId());

        assertEquals(-1, paging2.getPage());
        assertEquals(-1, paging2.getCount());
        assertNull(paging2.getSinceId());
        assertNull(paging2.getMaxId());
    }

    @Test
    public void testNullParameters() {
        Paging paging = new Paging(1, 10, 123L, 456L);

        List<HttpParameter> params = paging.asPostParameterList();
        assertTrue(params.isEmpty());

        HttpParameter[] arrParams = paging.asPostParameterArray();
        assertNull(arrParams);

        params = paging.asPostParameterList(null, null);
        assertTrue(params.isEmpty());

        arrParams = paging.asPostParameterArray(null, null);
        assertNull(arrParams);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsupportedParameter() {
        Paging paging = new Paging(1, 10, 123L, 456L);
        char[] supportedParams = Paging.S;

        paging.addPostParameter(supportedParams, 'a', null, "unsupported", 123L);
    }
}