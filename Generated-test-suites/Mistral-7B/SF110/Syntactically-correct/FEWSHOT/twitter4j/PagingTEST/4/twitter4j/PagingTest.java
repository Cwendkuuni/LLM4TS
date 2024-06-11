package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class PagingTest {

    @Test
    public void testPagingObjectCreation() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging(1);
        Paging paging3 = new Paging(1, 10);
        Paging paging4 = new Paging(1, 10, 100L);
        Paging paging5 = new Paging(1, 10, 100L, 200L);

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
        assertEquals(100L, paging4.getSinceId());
        assertEquals(-1L, paging4.getMaxId());

        assertEquals(1, paging5.getPage());
        assertEquals(10, paging5.getCount());
        assertEquals(100L, paging5.getSinceId());
        assertEquals(200L, paging5.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(5);
        assertEquals(5, paging.getCount());

        paging.setSinceId(123L);
        assertEquals(123L, paging.getSinceId());

        paging.setMaxId(321L);
        assertEquals(321L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithInvalidValues() {
        Paging paging = new Paging();

        paging.setPage(-1);
        paging.setCount(-1);
        paging.setSinceId(-1L);
        paging.setMaxId(-1L);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 100L, 200L);

        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, params.size());

        String paramName = params.get(0).getName();
        assertEquals("page", paramName);
        assertEquals("1", params.get(0).getValue());

        paramName = params.get(1).getName();
        assertEquals("count", paramName);
        assertEquals("10", params.get(1).getValue());

        paramName = params.get(2).getName();
        assertEquals("since_id", paramName);
        assertEquals("100", params.get(2).getValue());

        paramName = params.get(3).getName();
        assertEquals("max_id", paramName);
        assertEquals("200", params.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 100L, 200L);

        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, params.length);

        String paramName = params[0].getName();
        assertEquals("page", paramName);
        assertEquals("1", params[0].getValue());

        paramName = params[1].getName();
        assertEquals("count", paramName);
        assertEquals("10", params[1].getValue());

        paramName = params[2].getName();
        assertEquals("since_id", paramName);
        assertEquals("100", params[2].getValue());

        paramName = params[3].getName();
        assertEquals("max_id", paramName);
        assertEquals("200", params[3].getValue());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> params = new ArrayList<>();

        paging.addPostParameter(Paging.SMCP, 'p', params, "page", 1);
        assertEquals(1, params.size());
        assertEquals("page", params.get(0).getName());
        assertEquals("1", params.get(0).getValue());

        paging.addPostParameter(Paging.SMCP, 's', params, "since_id", 100L);
        assertEquals(2, params.size());
        assertEquals("since_id", params.get(1).getName());
        assertEquals("100", params.get(1).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> params = new ArrayList<>();

        paging.addPostParameter(Paging.SMCP, 'x', params, "unsupported", 1);
    }
}