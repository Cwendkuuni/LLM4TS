package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testPagingConstructorWithNoArguments() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageArgument() {
        Paging paging = new Paging(5);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithSinceIdArgument() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndCountArguments() {
        Paging paging = new Paging(5, 10);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndSinceIdArguments() {
        Paging paging = new Paging(5, 1L);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithAllArguments() {
        Paging paging = new Paging(5, 10, 1L, 2L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeInteger() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeInteger() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeLong() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetAndSetPage() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test
    public void testGetAndSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testGetAndSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test
    public void testGetAndSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(2L);
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(5, 10, 1L, 2L);
        Paging paging2 = new Paging(5, 10, 1L, 2L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(5, 10, 1L, 3L);
        assertFalse(paging1.equals(paging3));

        Object obj = new Object();
        assertFalse(paging1.equals(obj));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(5, 10, 1L, 2L);
        Paging paging2 = new Paging(5, 10, 1L, 2L);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging(5, 10, 1L, 3L);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(5, 10, 1L, 2L);
        assertEquals("Paging{page=5, count=10, sinceId=1, maxId=2}", paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(5, 10, 1L, 2L);
        List<HttpParameter> httpParameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, httpParameters.size());
        assertNotNull(httpParameters.get(0));
        assertEquals("count", httpParameters.get(0).getName());
        assertEquals("10", httpParameters.get(0).getValue());

        HttpParameter maxIdParam = new HttpParameter("max_id", "2");
        httpParameters.add(maxIdParam);
        assertEquals(5, httpParameters.size());
        assertEquals(maxIdParam, httpParameters.get(4));
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(5, 10, 1L, 2L);
        HttpParameter[] postParameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, postParameters.length);
        assertNotNull(postParameters[0]);
        assertEquals("count", postParameters[0].getName());
        assertEquals("10", postParameters[0].getValue());

        HttpParameter maxIdParam = new HttpParameter("max_id", "2");
        List<HttpParameter> postParameterList = new ArrayList<>();
        postParameterList.addAll(Arrays.asList(postParameters));
        postParameterList.add(maxIdParam);
        HttpParameter[] newPostParameters = postParameterList.toArray(new HttpParameter[postParameterList.size()]);
        assertEquals(5, newPostParameters.length);
        assertEquals(maxIdParam, newPostParameters[4]);
    }
}