package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

        paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        paging = new Paging(1, 2);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        paging = new Paging(1, 2, 3L);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        paging = new Paging(1, 2, 3L, 4L);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(4L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());

        paging.setMaxId(2L);
        assertEquals(2L, paging.getMaxId());

        paging.setPage(1);
        paging.setCount(5);
        paging.setSinceId(3L);
        paging.setMaxId(4L);

        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(4L, paging.getMaxId());

        paging.setPage(-1);
        assertFalse(paging.isInitialized());
    }

    @Test
    public void testEqualsAndHashcode() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging();
        assertFalse(paging1.equals(paging2));
        assertEquals(paging1.hashCode(), paging2.hashCode());

        paging1.setPage(1);
        paging2.setPage(1);
        assertTrue(paging1.equals(paging2));
        assertEquals(paging1.hashCode(), paging2.hashCode());

        paging1.setPage(1);
        paging2.setPage(2);
        assertFalse(paging1.equals(paging2));
        assertFalse(paging1.equals(null));
        assertNull(paging1.hashCode());

        paging1.setPage(1);
        paging1.setCount(5);
        paging2.setPage(1);
        paging2.setCount(5);
        assertTrue(paging1.equals(paging2));
        assertEquals(paging1.hashCode(), paging2.hashCode());

        paging1.setPage(1);
        paging1.setCount(5);
        paging2.setPage(2);
        paging2.setCount(5);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testToString() {
        Paging paging = new Paging();
        assertEquals("Paging{}", paging.toString());

        paging.setPage(1);
        paging.setCount(5);
        paging.setSinceId(3L);
        paging.setMaxId(4L);
        assertEquals("Paging{page=1, count=5, sinceId=3, maxId=4}", paging.toString());
    }

    @Test
    public void testAsPostParameterListAndArray() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] parameterArray = paging.asPostParameterArray(Paging.SMCP, "count");

        assertEquals(4, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("2", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("3", parameters.get(2).getValue());
        assertEquals("max_id", parameters.get(3).getName());
        assertEquals("4", parameters.get(3).getValue());

        assertEquals(4, parameterArray.length);
        assertEquals("page", parameterArray[0].getName());
        assertEquals("1", parameterArray[0].getValue());
        assertEquals("count", parameterArray[1].getName());
        assertEquals("2", parameterArray[1].getValue());
        assertEquals("since_id", parameterArray[2].getName());
        assertEquals("3", parameterArray[2].getValue());
        assertEquals("max_id", parameterArray[3].getName());
        assertEquals("4", parameterArray[3].getValue());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = new ArrayList<HttpParameter>();
        paging.addPostParameter(Paging.SMCP, 'p', parameters, "page", 1);
        HttpParameter parameter = parameters.get(0);
        assertEquals("page", parameter.getName());
        assertEquals("1", parameter.getValue());

        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 3L);
        parameter = parameters.get(1);
        assertEquals("since_id", parameter.getName());
        assertEquals("3", parameter.getValue());

        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 4L);
        assertEquals(2, parameters.size());
        assertFalse(parameters.get(1).equals(parameter));
        assertEquals("since_id", parameters.get(1).getName());
        assertEquals("4", parameters.get(1).getValue());

        paging.addPostParameter(Paging.SMCP, 'p', parameters, "page", 2);
        assertEquals(3, parameters.size());
        assertFalse(parameters.get(0).equals(parameter));
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("page", parameters.get(2).getName());
        assertEquals("2", parameters.get(2).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = new ArrayList<HttpParameter>();
        paging.addPostParameter(Paging.SMCP, 'x', parameters, "paramName", 1);
    }
}