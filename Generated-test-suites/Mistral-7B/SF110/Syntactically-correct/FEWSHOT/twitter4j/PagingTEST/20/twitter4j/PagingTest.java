package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 10, 1L, 100L);

        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(5);
        assertEquals(5, paging.getCount());

        paging.setSinceId(2L);
        assertEquals(2L, paging.getSinceId());

        paging.setMaxId(200L);
        assertEquals(200L, paging.getMaxId());
    }

    @Test
    public void testAddPostParameterArray() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;
        HttpParameter[] postParams = paging.asPostParameterArray(supportedParams, "count");

        assertNull(postParams);

        paging.setCount(1);
        postParams = paging.asPostParameterArray(supportedParams, "count");

        assertEquals(1, postParams.length);
        assertEquals("count", postParams[0].getName());
        assertEquals("1", postParams[0].getValue());

        paging.setPage(2);
        postParams = paging.asPostParameterArray(supportedParams, "page");

        assertEquals(1, postParams.length);
        assertEquals("page", postParams[0].getName());
        assertEquals("2", postParams[0].getValue());

        paging.setSinceId(3L);
        postParams = paging.asPostParameterArray(supportedParams, "since_id");

        assertEquals(1, postParams.length);
        assertEquals("since_id", postParams[0].getName());
        assertEquals("3", postParams[0].getValue());

        paging.setCount(5);
        postParams = paging.asPostParameterArray(supportedParams, "count");

        assertEquals(1, postParams.length);
        assertEquals("count", postParams[0].getName());
        assertEquals("5", postParams[0].getValue());

        paging.setPage(4);
        postParams = paging.asPostParameterArray(supportedParams, "page");

        assertEquals(1, postParams.length);
        assertEquals("page", postParams[0].getName());
        assertEquals("4", postParams[0].getValue());

        paging.setSinceId(7L);
        postParams = paging.asPostParameterArray(supportedParams, "since_id");

        assertEquals(1, postParams.length);
        assertEquals("since_id", postParams[0].getName());
        assertEquals("7", postParams[0].getValue());

        paging.setMaxId(11L);
        postParams = paging.asPostParameterArray(supportedParams, "max_id");

        assertEquals(1, postParams.length);
        assertEquals("max_id", postParams[0].getName());
        assertEquals("11", postParams[0].getValue());

        paging.setCount(2);
        postParams = paging.asPostParameterArray(supportedParams, "count");

        assertEquals(1, postParams.length);
        assertEquals("count", postParams[0].getName());
        assertEquals("2", postParams[0].getValue());

        paging.setPage(5);
        postParams = paging.asPostParameterArray(supportedParams, "page");

        assertEquals(1, postParams.length);
        assertEquals("page", postParams[0].getName());
        assertEquals("5", postParams[0].getValue());
    }

    @Test
    public void testAddPostParameterList() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;

        List<HttpParameter> postParams = paging.asPostParameterList(supportedParams, "count");

        assertNull(postParams);

        paging.setCount(1);
        postParams = paging.asPostParameterList(supportedParams, "count");

        assertEquals(1, postParams.size());
        assertEquals("count", postParams.get(0).getName());
        assertEquals("1", postParams.get(0).getValue());

        paging.setPage(2);
        postParams = paging.asPostParameterList(supportedParams, "page");

        assertEquals(1, postParams.size());
        assertEquals("page", postParams.get(0).getName());
        assertEquals("2", postParams.get(0).getValue());

        paging.setSinceId(3L);
        postParams = paging.asPostParameterList(supportedParams, "since_id");

        assertEquals(1, postParams.size());
        assertEquals("since_id", postParams.get(0).getName());
        assertEquals("3", postParams.get(0).getValue());

        paging.setCount(5);
        postParams = paging.asPostParameterList(supportedParams, "count");

        assertEquals(1, postParams.size());
        assertEquals("count", postParams.get(0).getName());
        assertEquals("5", postParams.get(0).getValue());

        paging.setPage(4);
        postParams = paging.asPostParameterList(supportedParams, "page");

        assertEquals(1, postParams.size());
        assertEquals("page", postParams.get(0).getName());
        assertEquals("4", postParams.get(0).getValue());

        paging.setSinceId(7L);
        postParams = paging.asPostParameterList(supportedParams, "since_id");

        assertEquals(1, postParams.size());
        assertEquals("since_id", postParams.get(0).getName());
        assertEquals("7", postParams.get(0).getValue());

        paging.setMaxId(11L);
        postParams = paging.asPostParameterList(supportedParams, "max_id");

        assertEquals(1, postParams.size());
        assertEquals("max_id", postParams.get(0).getName());
        assertEquals("11", postParams.get(0).getValue());

        paging.setCount(2);
        postParams = paging.asPostParameterList(supportedParams, "count");

        assertEquals(1, postParams.size());
        assertEquals("count", postParams.get(0).getName());
        assertEquals("2", postParams.get(0).getValue());

        paging.setPage(5);
        postParams = paging.asPostParameterList(supportedParams, "page");

        assertEquals(1, postParams.size());
        assertEquals("page", postParams.get(0).getName());
        assertEquals("5", postParams.get(0).getValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterUnsupportedParam() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;

        paging.addPostParameter(supportedParams, 'x', new ArrayList<HttpParameter>(), "param", 1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterNegativeParamValue() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;

        paging.addPostParameter(supportedParams, 'c', new ArrayList<HttpParameter>(), "count", -1);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterNullParamValue() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;

        paging.addPostParameter(supportedParams, 'c', new ArrayList<HttpParameter>(), "count", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterNullSupportedParams() {
        Paging paging = new Paging();

        try {
            paging.asPostParameterList(null, "count");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterNullPerPageParamName() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;

        try {
            paging.asPostParameterList(supportedParams, null);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 100L);

        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", paging.toString());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        Paging paging3 = new Paging(1, 10, 1L, 99L);
        Paging paging4 = new Paging(2, 10, 1L, 100L);

        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.equals(paging4));
        assertFalse(paging1.equals(null));
        assertFalse(paging1.equals(""));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        Paging paging3 = new Paging(1, 10, 1L, 99L);
        Paging paging4 = new Paging(2, 10, 1L, 100L);

        int hashcode1 = paging1.hashCode();
        int hashcode2 = paging2.hashCode();
        int hashcode3 = paging3.hashCode();
        int hashcode4 = paging4.hashCode();

        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode1, hashcode4);
    }
}