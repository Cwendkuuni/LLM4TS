package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testNewInstance() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testNewInstanceWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testNewInstanceWithSinceId() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testNewInstanceWithPageCount() {
        Paging paging = new Paging(1, 2);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testNewInstanceWithPageSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testNewInstanceWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(4L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativePage() {
        new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeCount() {
        new Paging(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeSinceId() {
        new Paging(1, 1, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeMaxId() {
        new Paging(1, 1, 1L, -1L);
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        assertEquals(1, paging.getPage());
        paging.setPage(2);
        assertEquals(2, paging.getPage());
        assertEquals(2, paging.count().getPage());
        paging.setCount(3);
        assertEquals(3, paging.getCount());
        paging.setSinceId(5L);
        assertEquals(5L, paging.getSinceId());
        assertEquals(5L, paging.sinceId(5L).getSinceId());
        paging.setMaxId(7L);
        assertEquals(7L, paging.getMaxId());
        assertEquals(7L, paging.maxId(7L).getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, parameters.size());
        HttpParameter param1 = parameters.get(0);
        assertEquals("count", param1.getName());
        assertEquals("2", param1.getValue());
        HttpParameter param2 = parameters.get(1);
        assertEquals("page", param2.getName());
        assertEquals("1", param2.getValue());
        HttpParameter param3 = parameters.get(2);
        assertEquals("since_id", param3.getName());
        assertEquals("3", param3.getValue());
        HttpParameter param4 = parameters.get(3);
        assertEquals("max_id", param4.getName());
        assertEquals("4", param4.getValue());

        List<HttpParameter> emptyParameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(0, emptyParameters.size());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        HttpParameter[] parameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, parameters.length);
        HttpParameter param1 = parameters[0];
        assertEquals("count", param1.getName());
        assertEquals("2", param1.getValue());
        HttpParameter param2 = parameters[1];
        assertEquals("page", param2.getName());
        assertEquals("1", param2.getValue());
        HttpParameter param3 = parameters[2];
        assertEquals("since_id", param3.getName());
        assertEquals("3", param3.getValue());
        HttpParameter param4 = parameters[3];
        assertEquals("max_id", param4.getName());
        assertEquals("4", param4.getValue());

        HttpParameter[] emptyParameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(0, emptyParameters.length);
    }

    @Test
    public void testAddPostParameter() {
        List<HttpParameter> parameters = new ArrayList<HttpParameter>();
        Paging paging = new Paging(1, 2, 3L, 4L);
        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 5L);
        assertEquals(1, parameters.size());
        HttpParameter param = parameters.get(0);
        assertEquals("since_id", param.getName());
        assertEquals("5", param.getValue());

        paging.addPostParameter(Paging.SMCP, 'm', parameters, "max_id", 6L);
        assertEquals(2, parameters.size());
        param = parameters.get(1);
        assertEquals("max_id", param.getName());
        assertEquals("6", param.getValue());

        List<HttpParameter> emptyParameters = new ArrayList<HttpParameter>();
        paging.addPostParameter(Paging.SMCP, 'x', emptyParameters, "paramName", 7L);
        assertEquals(0, emptyParameters.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterUnsupportedParam() {
        List<HttpParameter> parameters = new ArrayList<HttpParameter>();
        Paging paging = new Paging(1, 2, 3L, 4L);
        paging.addPostParameter(Paging.SMCP, 'x', parameters, "paramName", 5L);
    }
}