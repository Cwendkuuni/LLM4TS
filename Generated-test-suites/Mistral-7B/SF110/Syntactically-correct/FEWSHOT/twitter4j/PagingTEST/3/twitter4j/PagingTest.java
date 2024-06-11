package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
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
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
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
        paging.setCount(10);
        paging.setSinceId(1L);
        paging.setMaxId(100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());

        paging.setPage(-1);
        paging.setCount(-1);
        paging.setSinceId(-1L);
        paging.setMaxId(-1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        Paging paging3 = new Paging(2, 10, 1L, 100L);
        Paging paging4 = new Paging(1, 10, 1L, 101L);

        assertEquals(paging1, paging2);
        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1, paging4);
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        Paging paging3 = new Paging(2, 10, 1L, 100L);
        Paging paging4 = new Paging(1, 10, 1L, 101L);

        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] expected = { new HttpParameter("page", "1"), new HttpParameter("count", "10"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "100") };
        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging();
        assertThrows(IllegalStateException.class, () -> paging.asPostParameterArray('x', "unsupported_param"));
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> expected = Arrays.asList(new HttpParameter("page", "1"), new HttpParameter("count", "10"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "100"));
        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging();
        assertThrows(IllegalStateException.class, () -> paging.asPostParameterList('x', "unsupported_param"));
    }
}