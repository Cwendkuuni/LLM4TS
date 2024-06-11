package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PagingTest {

    @Test
    public void testPagingDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithAllParameters() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging(1, 1, 1L, 100L);
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testSetMaxIdNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(1, 10, 1L, 200L);
        assertFalse(paging1.equals(paging3));

        Paging paging4 = new Object();
        assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging(1, 10, 1L, 200L);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        Paging paging4 = new Object();
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("10", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("1", parameters.get(2).getValue());
        assertEquals("max_id", parameters.get(3).getName());
        assertEquals("100", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> parameters = paging.asPostParameterList();
        assertNull(parameters);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] parameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, parameters.length);
        assertEquals("page", parameters[0].getName());
        assertEquals("1", parameters[0].getValue());
        assertEquals("count", parameters[1].getName());
        assertEquals("10", parameters[1].getValue());
        assertEquals("since_id", parameters[2].getName());
        assertEquals("1", parameters[2].getValue());
        assertEquals("max_id", parameters[3].getName());
        assertEquals("100", parameters[3].getValue());
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] parameters = paging.asPostParameterArray();
        assertNull(parameters);
    }

    @Test
    public void testAddPostParameter() {
        List<HttpParameter> parameters = new ArrayList<>();
        Paging.addPostParameter(Paging.SMCP, 'p', parameters, "page", 1);
        assertEquals(1, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());

        Paging.addPostParameter(Paging.SMCP, 'p', parameters, "page", 2);
        assertEquals(2, parameters.size());
        assertEquals("page", parameters.get(1).getName());
        assertEquals("2", parameters.get(1).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterUnsupportedParam() {
        List<HttpParameter> parameters = new ArrayList<>();
        Paging.addPostParameter(Paging.SMCP, 'q', parameters, "page", 1);
    }
}