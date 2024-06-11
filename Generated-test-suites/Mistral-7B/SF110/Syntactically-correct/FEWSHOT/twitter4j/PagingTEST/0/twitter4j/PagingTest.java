package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testEmptyPagingObject() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testInitializationWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testInitializationWithSinceId() {
        Paging paging = new Paging(1L);
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testInitializationWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testInitializationWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertNull(paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testInitializationWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testInitializationWithPageAndCountAndSinceIdAndMaxId() {
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
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] postParameters = paging.asPostParameterArray(Paging.SMCP, "count");
        List<HttpParameter> expectedParameters = new ArrayList<HttpParameter>(4);
        expectedParameters.add(new HttpParameter("page", "1"));
        expectedParameters.add(new HttpParameter("count", "10"));
        expectedParameters.add(new HttpParameter("since_id", "1"));
        expectedParameters.add(new HttpParameter("max_id", "2"));

        assertEquals(expectedParameters.size(), postParameters.length);
        for (int i = 0; i < postParameters.length; i++) {
            assertEquals(expectedParameters.get(i).getName(), postParameters[i].getName());
            assertEquals(expectedParameters.get(i).getValue(), postParameters[i].getValue());
        }
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> postParameters = paging.asPostParameterList(Paging.SMCP, "count");
        List<HttpParameter> expectedParameters = new ArrayList<HttpParameter>(4);
        expectedParameters.add(new HttpParameter("page", "1"));
        expectedParameters.add(new HttpParameter("count", "10"));
        expectedParameters.add(new HttpParameter("since_id", "1"));
        expectedParameters.add(new HttpParameter("max_id", "2"));

        assertEquals(expectedParameters, postParameters);
    }

}