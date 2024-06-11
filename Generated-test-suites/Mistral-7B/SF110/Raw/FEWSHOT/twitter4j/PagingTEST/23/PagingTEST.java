package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
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
    public void testNewInstanceWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testNewInstanceWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testNewInstanceWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(5);
        paging.setSinceId(1L);
        paging.setMaxId(5L);

        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(5L, paging.getMaxId());

        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(2L);
        paging.setMaxId(10L);

        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(2L, paging.getSinceId());
        assertEquals(10L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidPage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(2, 10, 1L, 100L);
        assertFalse(paging1.equals(paging3));

        Paging paging4 = new Paging();
        assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging(2, 10, 1L, 100L);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        Paging paging4 = new Paging();
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> httpParameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, httpParameters.size());
        HttpParameter[] parameters = httpParameters.toArray(new HttpParameter[httpParameters.size()]);
        assertArrayEquals(new HttpParameter[] {
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "100")
        }, parameters);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> httpParameters = paging.asPostParameterList(new char[0], "count");
        assertEquals(0, httpParameters.size());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] httpParameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, httpParameters.length);
        assertArrayEquals(new HttpParameter[] {
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "100")
        }, httpParameters);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] httpParameters = paging.asPostParameterArray(new char[0], "count");
        assertEquals(0, httpParameters.length);
    }
}