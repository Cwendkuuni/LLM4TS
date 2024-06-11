package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testConstructors() {
        Paging paging = new Paging(); // Default constructor
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        Paging paging1 = new Paging(1); // page constructor
        assertEquals(1, paging1.getPage());
        assertEquals(-1, paging1.getCount());
        assertEquals(-1L, paging1.getSinceId());
        assertEquals(-1L, paging1.getMaxId());

        Paging paging2 = new Paging(1, 5); // page and count constructor
        assertEquals(1, paging2.getPage());
        assertEquals(5, paging2.getCount());
        assertEquals(-1L, paging2.getSinceId());
        assertEquals(-1L, paging2.getMaxId());

        Paging paging3 = new Paging(1, 5, 60L); // page, count, and sinceId constructor
        assertEquals(1, paging3.getPage());
        assertEquals(5, paging3.getCount());
        assertEquals(60L, paging3.getSinceId());
        assertEquals(-1L, paging3.getMaxId());

        Paging paging4 = new Paging(1, 5, 60L, 70L); // page, count, sinceId, and maxId constructor
        assertEquals(1, paging4.getPage());
        assertEquals(5, paging4.getCount());
        assertEquals(60L, paging4.getSinceId());
        assertEquals(70L, paging4.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(5);
        paging.setSinceId(60L);
        paging.setMaxId(70L);

        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(60L, paging.getSinceId());
        assertEquals(70L, paging.getMaxId());

        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(50L);
        paging.setMaxId(80L);

        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(50L, paging.getSinceId());
        assertEquals(80L, paging.getMaxId());
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 5, 60L, 70L);
        Paging paging2 = new Paging(1, 5, 60L, 70L);
        Paging paging3 = new Paging(1, 5, 61L, 70L);
        Paging paging4 = new Paging(1, 5, 60L, 69L);

        assertTrue(paging1.equals(paging2));
        assertTrue(paging1.hashCode() == paging2.hashCode());

        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        assertNotEquals(paging1, paging4);
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testAsPostParameterListAndArray() {
        Paging paging = new Paging(1, 5, 60L, 70L);

        List<HttpParameter> pagingParams = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] pagingParamsArray = paging.asPostParameterArray(Paging.SMCP, "count");

        assertEquals(4, pagingParams.size());
        assertEquals("page", pagingParams.get(0).getName());
        assertEquals("1", pagingParams.get(0).getValue());

        assertEquals("count", pagingParams.get(3).getName());
        assertEquals("5", pagingParams.get(3).getValue());

        assertEquals(4, pagingParamsArray.length);
        assertEquals("page", pagingParamsArray[0].getName());
        assertEquals("1", pagingParamsArray[0].getValue());

        assertEquals("count", pagingParamsArray[3].getName());
        assertEquals("5", pagingParamsArray[3].getValue());
    }

    @Test
    public void testAddPostParameter() {
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging.addPostParameter(Paging.SMCP, 'p', pagingParams, "page", 1);
        Paging.addPostParameter(Paging.SMCP, 'c', pagingParams, "count", 5);

        assertEquals(2, pagingParams.size());

        HttpParameter pageParam = pagingParams.get(0);
        assertEquals("page", pageParam.getName());
        assertEquals("1", pageParam.getValue());

        HttpParameter countParam = pagingParams.get(1);
        assertEquals("count", countParam.getName());
        assertEquals("5", countParam.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging.addPostParameter(Paging.SMCP, 'a', pagingParams, "paramA", 1);
    }
}