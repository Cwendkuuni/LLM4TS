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
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1L);
        paging.setMaxId(2L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());

        paging.setPage(-1);
        try {
            paging.setCount(-1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        paging.setCount(0);
        try {
            paging.setSinceId(-1L);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertNotNull(parameters);
        assertEquals(4, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("10", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("1", parameters.get(2).getValue());
        assertEquals("max_id", parameters.get(3).getName());
        assertEquals("2", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = paging.asPostParameterList(new char[0], "count");
        assertSame(Paging.NULL_PARAMETER_LIST, parameters);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] parameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertNotNull(parameters);
        assertEquals(4, parameters.length);
        assertEquals("page", parameters[0].getName());
        assertEquals("1", parameters[0].getValue());
        assertEquals("count", parameters[1].getName());
        assertEquals("10", parameters[1].getValue());
        assertEquals("since_id", parameters[2].getName());
        assertEquals("1", parameters[2].getValue());
        assertEquals("max_id", parameters[3].getName());
        assertEquals("2", parameters[3].getValue());
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging();
        HttpParameter[] parameters = paging.asPostParameterArray(new char[0], "count");
        assertSame(Paging.NULL_PARAMETER_ARRAY, parameters);
    }
}