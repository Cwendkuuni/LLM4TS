package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testEmptyPaging() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testBasicPaging() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());

        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging2.getPage());
        assertEquals(10, paging2.getCount());
        assertEquals(1L, paging2.getSinceId());
        assertEquals(100L, paging2.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPage() {
        Paging paging = new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCount() {
        Paging paging = new Paging(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceId() {
        Paging paging = new Paging(1, 10, -1L);
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        paging.setPage(2);
        assertEquals(2, paging.getPage());
        paging.setPage(-1);
        assertEquals(2, paging.getPage()); // should not change
        paging.setCount(3);
        assertEquals(3, paging.getCount());
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
        paging.setMaxId(100L);
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10);
        Paging paging2 = new Paging(1, 10);
        Paging paging3 = new Paging(2, 10);
        assertTrue(paging1.equals(paging1));
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.equals(null));
        assertFalse(paging1.equals(""));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10);
        Paging paging2 = new Paging(1, 10);
        Paging paging3 = new Paging(2, 10);
        assertEquals(paging1.hashCode(), paging1.hashCode());
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging1.hashCode(), 1L);
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10);
        assertEquals("Paging{page=1, count=10, sinceId=null, maxId=null}", paging.toString());

        Paging pagingWithSinceId = new Paging(1, 10, 1L, 100L);
        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", pagingWithSinceId.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("page", "1"));
        expectedParameters.add(new HttpParameter("count", "10"));

        Paging paging = new Paging();
        HttpParameter[] actualParameters = paging.asPostParameterArray();
        assertEquals(0, actualParameters.length);

        paging = new Paging(1, 10);
        actualParameters = paging.asPostParameterArray();
        assertArrayEquals(expectedParameters.toArray(new HttpParameter[expectedParameters.size()]), actualParameters);

        paging = new Paging(1, 10, 1L, 100L);
        expectedParameters.add(new HttpParameter("since_id", "1"));
        expectedParameters.add(new HttpParameter("max_id", "100"));
        actualParameters = paging.asPostParameterArray();
        assertArrayEquals(expectedParameters.toArray(new HttpParameter[expectedParameters.size()]), actualParameters);
    }

    @Test
    public void testAsPostParameterList() {
        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("page", "1"));
        expectedParameters.add(new HttpParameter("count", "10"));

        Paging paging = new Paging();
        List<HttpParameter> actualParameters = paging.asPostParameterList();
        assertEquals(0, actualParameters.size());

        paging = new Paging(1, 10);
        actualParameters = paging.asPostParameterList();
        assertEquals(expectedParameters, actualParameters);

        paging = new Paging(1, 10, 1L, 100L);
        expectedParameters.add(new HttpParameter("since_id", "1"));
        expectedParameters.add(new HttpParameter("max_id", "100"));
        actualParameters = paging.asPostParameterList();
        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testAddPostParameter() {
        List<HttpParameter> parameters = new ArrayList<>();
        Paging paging = new Paging();
        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 1L);
        assertEquals(1, parameters.size());
        assertEquals("since_id", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());

        paging = new Paging(1, 10);
        parameters = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'p', parameters, "page", 1);
        assertEquals(1, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());

        paging = new Paging(1, 10, 1L, 100L);
        parameters = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 1L);
        paging.addPostParameter(Paging.SMCP, 'm', parameters, "max_id", 100L);
        assertEquals(2, parameters.size());
        assertEquals("since_id", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("max_id", parameters.get(1).getName());
        assertEquals("100", parameters.get(1).getValue());

        paging = new Paging(1, 10, 1L, 100L, 2L);
        parameters = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 1L);
        paging.addPostParameter(Paging.SMCP, 'm', parameters, "max_id", 100L);
        paging.addPostParameter(Paging.SMCP, 'p', parameters, "page", 2);
        assertEquals(3, parameters.size());
        assertEquals("since_id", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("max_id", parameters.get(1).getName());
        assertEquals("100", parameters.get(1).getValue());
        assertEquals("page", parameters.get(2).getName());
        assertEquals("2", parameters.get(2).getValue());
    }
}