package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Paging paging = new Paging(1, 20);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(20);
        paging.setSinceId(1L);
        paging.setMaxId(2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 20, 1L, 2L);
        Paging paging2 = new Paging(1, 20, 1L, 2L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(1, 20, 1L, 3L);
        assertFalse(paging1.equals(paging3));

        Object obj = new Object();
        assertFalse(paging1.equals(obj));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 20, 1L, 2L);
        Paging paging2 = new Paging(1, 20, 1L, 2L);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging(1, 20, 1L, 3L);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        Object obj = new Object();
        assertNotEquals(paging1.hashCode(), obj.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        String expected = "Paging{page=1, count=20, sinceId=1, maxId=2}";
        assertEquals(expected, paging.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNegativePage() {
        new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNegativeCount() {
        new Paging(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNegativeSinceId() {
        new Paging(1, 1, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNegativeMaxId() {
        new Paging(1, 1, 1L, -1L);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> expected = Arrays.asList(
                new HttpParameter("page", String.valueOf(1)),
                new HttpParameter("count", String.valueOf(20)),
                new HttpParameter("since_id", String.valueOf(1)),
                new HttpParameter("max_id", String.valueOf(2))
        );
        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging();
        List<HttpParameter> expected = Paging.NULL_PARAMETER_LIST;
        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParamsAndPerPageParamName() {
        Paging paging = new Paging();
        String perPageParamName = "per_page";
        List<HttpParameter> expected = Paging.NULL_PARAMETER_LIST;
        List<HttpParameter> actual = paging.asPostParameterList(Paging.SMCP, perPageParamName);
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> expected = Arrays.asList(
                new HttpParameter("page", String.valueOf(1)),
                new HttpParameter("count", String.valueOf(20)),
                new HttpParameter("since_id", String.valueOf(1)),
                new HttpParameter("max_id", String.valueOf(2))
        );
        List<HttpParameter> actual = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithSupportedParamsAndPerPageParamName() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> expected = Arrays.asList(
                new HttpParameter("page", String.valueOf(1)),
                new HttpParameter("count", String.valueOf(20)),
                new HttpParameter("since_id", String.valueOf(1)),
                new HttpParameter("max_id", String.valueOf(2))
        );
        List<HttpParameter> actual = paging.asPostParameterList(Paging.SMCP, "per_page");
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] expected = new HttpParameter[]{
                new HttpParameter("page", String.valueOf(1)),
                new HttpParameter("count", String.valueOf(20)),
                new HttpParameter("since_id", String.valueOf(1)),
                new HttpParameter("max_id", String.valueOf(2))
        };
        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging();
        HttpParameter[] expected = Paging.NULL_PARAMETER_ARRAY;
        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParamsAndPerPageParamName() {
        Paging paging = new Paging();
        String perPageParamName = "per_page";
        HttpParameter[] expected = Paging.NULL_PARAMETER_ARRAY;
        HttpParameter[] actual = paging.asPostParameterArray(Paging.SMCP, perPageParamName);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] expected = new HttpParameter[]{
                new HttpParameter("page", String.valueOf(1)),
                new HttpParameter("count", String.valueOf(20)),
                new HttpParameter("since_id", String.valueOf(1)),
                new HttpParameter("max_id", String.valueOf(2))
        };
        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParamsAndPerPageParamName() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] expected = new HttpParameter[]{
                new HttpParameter("page", String.valueOf(1)),
                new HttpParameter("count", String.valueOf(20)),
                new HttpParameter("since_id", String.valueOf(1)),
                new HttpParameter("max_id", String.valueOf(2))
        };
        HttpParameter[] actual = paging.asPostParameterArray(Paging.SMCP, "per_page");
        assertArrayEquals(expected, actual);
    }
}