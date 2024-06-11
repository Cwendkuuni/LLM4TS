package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

public class PagingTest {

    @Test
    public void testConstructors() {
        // Test default constructor
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        // Test constructor with page only
        Paging paging1 = new Paging(1);
        assertEquals(1, paging1.getPage());
        assertEquals(-1, paging1.getCount());
        assertEquals(-1L, paging1.getSinceId());
        assertEquals(-1L, paging1.getMaxId());

        // Test constructor with sinceId only
        Paging paging2 = new Paging(1, 1L);
        assertEquals(1, paging2.getPage());
        assertEquals(-1, paging2.getCount());
        assertEquals(1L, paging2.getSinceId());
        assertEquals(-1L, paging2.getMaxId());

        // Test constructor with page and count
        Paging paging3 = new Paging(1, 10);
        assertEquals(1, paging3.getPage());
        assertEquals(10, paging3.getCount());
        assertEquals(-1L, paging3.getSinceId());
        assertEquals(-1L, paging3.getMaxId());

        // Test constructor with page, count, sinceId
        Paging paging4 = new Paging(1, 10, 1L);
        assertEquals(1, paging4.getPage());
        assertEquals(10, paging4.getCount());
        assertEquals(1L, paging4.getSinceId());
        assertEquals(-1L, paging4.getMaxId());

        // Test constructor with page, count, sinceId, maxId
        Paging paging5 = new Paging(1, 10, 1L, 2L);
        assertEquals(1, paging5.getPage());
        assertEquals(10, paging5.getCount());
        assertEquals(1L, paging5.getSinceId());
        assertEquals(2L, paging5.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        // Test setters
        paging.setPage(1);
        assertEquals(1, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());

        paging.setMaxId(2L);
        assertEquals(2L, paging.getMaxId());

        // Test count method
        Paging paging1 = new Paging(1);
        assertEquals(-1, paging1.count(5).getCount());
        assertEquals(5, paging1.getCount());

        // Test sinceId method
        Paging paging2 = new Paging(1, 10);
        assertEquals(-1L, paging2.sinceId(1L).getSinceId());
        assertEquals(1L, paging2.getSinceId());

        // Test maxId method
        Paging paging3 = new Paging(1, 10, 1L);
        assertEquals(-1L, paging3.maxId(2L).getMaxId());
        assertEquals(2L, paging3.getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);

        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("page", "1"));
        expectedParams.add(new HttpParameter("count", "10"));
        expectedParams.add(new HttpParameter("since_id", "1"));
        expectedParams.add(new HttpParameter("max_id", "2"));

        List<HttpParameter> actualParams1 = paging.asPostParameterList();
        assertEquals(expectedParams, actualParams1);

        List<HttpParameter> actualParams2 = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(expectedParams, actualParams2);

        List<HttpParameter> actualParams3 = paging.asPostParameterList(Paging.SMCP, "per_page");
        List<HttpParameter> expectedParams3 = new ArrayList<>();
        expectedParams3.add(new HttpParameter("page", "1"));
        expectedParams3.add(new HttpParameter("per_page", "10"));
        assertEquals(expectedParams3, actualParams3);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);

        HttpParameter[] expectedParamsArray1 = new HttpParameter[4];
        expectedParamsArray1[0] = new HttpParameter("page", "1");
        expectedParamsArray1[1] = new HttpParameter("count", "10");
        expectedParamsArray1[2] = new HttpParameter("since_id", "1");
        expectedParamsArray1[3] = new HttpParameter("max_id", "2");

        HttpParameter[] actualParams1 = paging.asPostParameterArray();
        assertArrayEquals(expectedParamsArray1, actualParams1);

        HttpParameter[] actualParams2 = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArrayEquals(expectedParamsArray1, actualParams2);

        HttpParameter[] actualParams3 = paging.asPostParameterArray(Paging.SMCP, "per_page");
        HttpParameter[] expectedParamsArray3 = new HttpParameter[3];
        expectedParamsArray3[0] = new HttpParameter("page", "1");
        expectedParamsArray3[1] = new HttpParameter("per_page", "10");
        expectedParamsArray3[2] = new HttpParameter("since_id", "1");
        assertArrayEquals(expectedParamsArray3, actualParams3);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();

        List<HttpParameter> paramsList = new ArrayList<>();
        HttpParameter[] paramsArray = new HttpParameter[10];
        char[] supportedParams = Paging.SMCP;

        paging.addPostParameter(supportedParams, 's', paramsList, "since_id", 1L);
        assertEquals(1, paramsList.size());
        assertEquals("since_id", paramsList.get(0).getName());
        assertEquals("1", paramsList.get(0).getValue());

        paging.addPostParameter(supportedParams, 's', paramsList, "since_id", 2L);
        assertEquals(2, paramsList.size());
        assertEquals("since_id", paramsList.get(1).getName());
        assertEquals("2", paramsList.get(1).getValue());

        paging.addPostParameter(supportedParams, 's', paramsArray, "since_id", 3L);
        assertEquals(10, paramsArray.length);
        assertEquals("since_id", paramsArray[0].getName());
        assertEquals("3", paramsArray[0].getValue());

        paging.addPostParameter(supportedParams, 's', paramsArray, "since_id", 4L);
        assertEquals(11, paramsArray.length);
        assertEquals("since_id", paramsArray[10].getName());
        assertEquals("4", paramsArray[10].getValue());

        try {
            paging.addPostParameter(supportedParams, 'x', paramsArray, "since_id", 5L);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            paging.addPostParameter(supportedParams, 'p', paramsArray, "page", 5L);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 2L);
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        Paging paging3 = new Paging(1, 10, 1L, 3L);

        assertTrue(paging1.equals(paging2));
        assertTrue(paging1.hashCode() == paging2.hashCode());

        assertFalse(paging1.equals(paging3));
        assertNotSame(paging1.hashCode(), paging3.hashCode());
    }
}