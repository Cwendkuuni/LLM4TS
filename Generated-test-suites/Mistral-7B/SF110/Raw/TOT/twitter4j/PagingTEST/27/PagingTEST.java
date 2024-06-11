import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;

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
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndMaxId() {
        Paging paging = new Paging(1, 10, 11L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(11L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 1L, 11L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(11L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativePage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetters() {
        Paging paging = new Paging(1, 10, 1L, 11L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(11L, paging.getMaxId());
    }

    @Test
    public void testSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1L);
        paging.setMaxId(11L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(11L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 11L);
        Paging paging2 = new Paging(1, 10, 1L, 11L);
        Paging paging3 = new Paging(1, 10, 1L, 12L);
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 11L);
        Paging paging2 = new Paging(1, 10, 1L, 11L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
}