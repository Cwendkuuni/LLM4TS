package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

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
        Paging paging = new Paging(1, 2);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
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
        Paging paging = new Paging(1, 2, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 2, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(2);
        paging.setSinceId(1L);
        paging.setMaxId(2L);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 2, 1L, 2L);
        Paging paging2 = new Paging(1, 2, 1L, 2L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging();
        assertFalse(paging1.equals(paging3));

        Paging paging4 = new Paging(1, 2, 1L, 3L);
        assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 2, 1L, 2L);
        Paging paging2 = new Paging(1, 2, 1L, 2L);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging();
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        Paging paging4 = new Paging(1, 2, 1L, 3L);
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 2, 1L, 2L);
        assertEquals("Paging{page=1, count=2, sinceId=1, maxId=2}", paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 2, 1L, 2L);
        List<HttpParameter> httpParameters = paging.asPostParameterList();
        assertEquals(4, httpParameters.size());
        assertEquals("page", httpParameters.get(0).getName());
        assertEquals("1", httpParameters.get(0).getValue());
        assertEquals("count", httpParameters.get(1).getName());
        assertEquals("2", httpParameters.get(1).getValue());
        assertEquals("since_id", httpParameters.get(2).getName());
        assertEquals("1", httpParameters.get(2).getValue());
        assertEquals("max_id", httpParameters.get(3).getName());
        assertEquals("2", httpParameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 2, 1L, 2L);
        HttpParameter[] httpParameters = paging.asPostParameterArray();
        assertEquals(4, httpParameters.length);
        assertEquals("page", httpParameters[0].getName());
        assertEquals("1", httpParameters[0].getValue());
        assertEquals("count", httpParameters[1].getName());
        assertEquals("2", httpParameters[1].getValue());
        assertEquals("since_id", httpParameters[2].getName());
        assertEquals("1", httpParameters[2].getValue());
        assertEquals("max_id", httpParameters[3].getName());
        assertEquals("2", httpParameters[3].getValue());
    }

    @Test
    public void testAsPostParameterListWithNullSupportedParams() {
        Paging paging = new Paging();
        List<HttpParameter> httpParameters = paging.asPostParameterList();
        assertEquals(0, httpParameters.size());
    }

    @Test
    public void testAsPostParameterArrayWithNullSupportedParams() {
        Paging paging = new Paging();
        HttpParameter[] httpParameters = paging.asPostParameterArray();
        assertEquals(0, httpParameters.length);
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(1, 2, 1L, 2L);
        List<HttpParameter> httpParameters = paging.asPostParameterList('s', 'm');
        assertEquals(3, httpParameters.size());
        assertEquals("page", httpParameters.get(0).getName());
        assertEquals("1", httpParameters.get(0).getValue());
        assertEquals("count", httpParameters.get(1).getName());
        assertEquals("2", httpParameters.get(1).getValue());
        assertEquals("since_id", httpParameters.get(2).getName());
        assertEquals("1", httpParameters.get(2).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> httpParameters = new ArrayList<>();
        paging.addPostParameter('x', "paramName", httpParameters, "value");
    }

    @Test
    public void testAddPostParameterWithSupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> httpParameters = new ArrayList<>();
        paging.addPostParameter('s', "page", httpParameters, "1");
        assertEquals(1, httpParameters.size());
        assertEquals("page", httpParameters.get(0).getName());
        assertEquals("1", httpParameters.get(0).getValue());
    }
}