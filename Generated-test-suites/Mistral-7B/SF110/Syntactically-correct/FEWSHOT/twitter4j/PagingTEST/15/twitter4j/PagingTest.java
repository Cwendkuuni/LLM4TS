package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertNotNull(paging);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        assertNotNull(paging);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 2);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 2, 3L);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithAllParameters() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(4L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(2);
        paging.setSinceId(3L);
        paging.setMaxId(4L);

        assertEquals(1, paging.getPage());
        assertEquals(2, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(4L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithInvalidPage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithInvalidCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithInvalidSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithInvalidMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 2, 3L, 4L);
        Paging paging2 = new Paging(1, 2, 3L, 4L);
        Paging paging3 = new Paging(1, 2, 3L, 5L);

        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.equals(null));
        assertFalse(paging1.equals(""));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 2, 3L, 4L);
        Paging paging2 = new Paging(1, 2, 3L, 4L);
        Paging paging3 = new Paging(1, 2, 3L, 5L);

        assertEquals(paging1.hashCode(), paging1.hashCode());
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging1.hashCode(), null.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        assertEquals("Paging{page=1, count=2, sinceId=3, maxId=4}", paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        HttpParameter[] expected = new HttpParameter[] { new HttpParameter("page", "1"), new HttpParameter("count", "2"), new HttpParameter("since_id", "3"), new HttpParameter("max_id", "4") };
        HttpParameter[] actual = paging.asPostParameterArray(Paging.SMCP.clone(), "count");

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        List<HttpParameter> expected = Arrays.asList(new HttpParameter("page", "1"), new HttpParameter("count", "2"), new HttpParameter("since_id", "3"), new HttpParameter("max_id", "4") );
        List<HttpParameter> actual = paging.asPostParameterList(Paging.SMCP.clone(), "count");

        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayWithEmptyList() {
        Paging paging = new Paging();
        HttpParameter[] expected = Paging.NULL_PARAMETER_ARRAY;
        HttpParameter[] actual = paging.asPostParameterArray(Paging.SMCP.clone(), "count");

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListWithEmptyList() {
        Paging paging = new Paging();
        List<HttpParameter> expected = Paging.NULL_PARAMETER_LIST;
        List<HttpParameter> actual = paging.asPostParameterList(Paging.SMCP.clone(), "count");

        assertEquals(expected, actual);
    }
}