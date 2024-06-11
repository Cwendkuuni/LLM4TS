package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

public class PagingTest {

    @Test
    public void testEmptyConstructor() {
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
        Paging paging = new Paging(1, 20, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging(1);
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging(1);
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging(1);
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetAndSetPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    @Test
    public void testGetAndSetCount() {
        Paging paging = new Paging(1);
        assertEquals(-1, paging.getCount());
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }

    @Test
    public void testGetAndSetSinceId() {
        Paging paging = new Paging(1);
        assertEquals(-1L, paging.getSinceId());
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test
    public void testGetAndSetMaxId() {
        Paging paging = new Paging(1);
        assertEquals(-1L, paging.getMaxId());
        paging.setMaxId(1L);
        assertEquals(1L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 20, 1L, 2L);
        Paging paging2 = new Paging(1, 20, 1L, 2L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(1, 20, 1L, 3L);
        assertFalse(paging1.equals(paging3));

        Paging paging4 = new Paging(2, 20, 1L, 2L);
        assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 20, 1L, 2L);
        int hashcode1 = paging1.hashCode();

        Paging paging2 = new Paging(1, 20, 1L, 2L);
        int hashcode2 = paging2.hashCode();

        assertEquals(hashcode1, hashcode2);

        Paging paging3 = new Paging(1, 20, 1L, 3L);
        int hashcode3 = paging3.hashCode();

        assertNotEquals(hashcode1, hashcode3);

        Paging paging4 = new Paging(2, 20, 1L, 2L);
        int hashcode4 = paging4.hashCode();

        assertNotEquals(hashcode1, hashcode4);
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        String str = "Paging{page=1, count=20, sinceId=1, maxId=2}";
        assertEquals(str, paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> parameters = paging.asPostParameterList();
        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("page", "1"));
        expectedParameters.add(new HttpParameter("count", "20"));
        expectedParameters.add(new HttpParameter("since_id", "1"));
        expectedParameters.add(new HttpParameter("max_id", "2"));
        assertEquals(expectedParameters, parameters);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        HttpParameter[] parameters = paging.asPostParameterArray();
        HttpParameter[] expectedParameters = new HttpParameter[4];
        expectedParameters[0] = new HttpParameter("page", "1");
        expectedParameters[1] = new HttpParameter("count", "20");
        expectedParameters[2] = new HttpParameter("since_id", "1");
        expectedParameters[3] = new HttpParameter("max_id", "2");
        assertEquals(expectedParameters, parameters);
    }

    @Test
    public void testAsPostParameterListWithNullSupportedParams() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = paging.asPostParameterList(null);
        assertEquals(0, parameters.size());
    }

    @Test
    public void testAsPostParameterArrayWithNullSupportedParams() {
        Paging paging = new Paging();
        HttpParameter[] parameters = paging.asPostParameterArray(null);
        assertEquals(0, parameters.length);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = paging.asPostParameterList(new char[0]);
        assertEquals(0, parameters.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsPostParameterListWithInvalidParamKey() {
        Paging paging = new Paging();
        paging.asPostParameterList(new char[]{'x'});
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParamValue() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = new ArrayList<>();
        paging.addPostParameter(new char[]{'s'}, 'x', parameters, "since_id", 0L);
    }
}