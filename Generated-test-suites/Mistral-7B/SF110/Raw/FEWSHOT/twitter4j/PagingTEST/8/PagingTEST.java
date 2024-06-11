package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 20);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertNull(paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 20, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertNull(paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 2, 3, 4);
        Paging paging2 = new Paging(1, 2, 3, 4);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(1, 2, 3, 5);
        assertFalse(paging1.equals(paging3));

        Paging paging4 = new Paging(2, 2, 3, 4);
        assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 2, 3, 4);
        int hashCode1 = paging1.hashCode();

        Paging paging2 = new Paging(1, 2, 3, 4);
        int hashCode2 = paging2.hashCode();

        assertEquals(hashCode1, hashCode2);

        Paging paging3 = new Paging(1, 2, 3, 5);
        assertNotEquals(hashCode1, paging3.hashCode());

        Paging paging4 = new Paging(2, 2, 3, 4);
        assertNotEquals(hashCode1, paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        String toString = paging.toString();
        assertTrue(toString.contains("Paging{page=" + 1 + ", count=" + 20 + ", sinceId=" + 1 + ", maxId=" + 2 + '}'));
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> asPostParameterList = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, asPostParameterList.size());
        assertNotNull(asPostParameterList.get(0));
        assertEquals("page", asPostParameterList.get(0).getName());
        assertEquals("1", asPostParameterList.get(0).getValue());

        assertNotNull(asPostParameterList.get(1));
        assertEquals("count", asPostParameterList.get(1).getName());
        assertEquals("20", asPostParameterList.get(1).getValue());

        assertNotNull(asPostParameterList.get(2));
        assertEquals("since_id", asPostParameterList.get(2).getName());
        assertEquals("1", asPostParameterList.get(2).getValue());

        assertNotNull(asPostParameterList.get(3));
        assertEquals("max_id", asPostParameterList.get(3).getName());
        assertEquals("2", asPostParameterList.get(3).getValue());
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> asPostParameterList = paging.asPostParameterList(new char[0], "count");
        assertEquals(0, asPostParameterList.size());
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> asPostParameterList = paging.asPostParameterList(new char[] { 'x' }, "count");
        assertEquals(0, asPostParameterList.size());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] asPostParameterArray = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, asPostParameterArray.length);
        assertNotNull(asPostParameterArray[0]);
        assertEquals("page", asPostParameterArray[0].getName());
        assertEquals("1", asPostParameterArray[0].getValue());

        assertNotNull(asPostParameterArray[1]);
        assertEquals("count", asPostParameterArray[1].getName());
        assertEquals("20", asPostParameterArray[1].getValue());

        assertNotNull(asPostParameterArray[2]);
        assertEquals("since_id", asPostParameterArray[2].getName());
        assertEquals("1", asPostParameterArray[2].getValue());

        assertNotNull(asPostParameterArray[3]);
        assertEquals("max_id", asPostParameterArray[3].getName());
        assertEquals("2", asPostParameterArray[3].getValue());
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] asPostParameterArray = paging.asPostParameterArray(new char[0], "count");
        assertEquals(0, asPostParameterArray.length);
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] asPostParameterArray = paging.asPostParameterArray(new char[] { 'x' }, "count");
        assertEquals(0, asPostParameterArray.length);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>(Arrays.asList(new HttpParameter[] { new HttpParameter("paramName", "1") }));
        paging.addPostParameter(Paging.SMCP, 'p', pagingParams, "paramName", 2);
        assertEquals(1, pagingParams.size());
        assertEquals("paramName=1&paramName=2", pagingParams.get(0).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>(Arrays.asList(new HttpParameter[] { new HttpParameter("paramName", "1") }));
        paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "paramName", 2);
    }

    @Test
    public void testAddPostParameterWithEmptySupportedParams() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(new char[0], 'p', pagingParams, "paramName", 2);
        assertEquals(0, pagingParams.size());
    }

    @Test
    public void testAddPostParameterWithEmptySupportedParamsAndValidParam() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'p', pagingParams, "paramName", 2);
        assertEquals(1, pagingParams.size());
        assertEquals("paramName=2", pagingParams.get(0).getValue());
    }
}