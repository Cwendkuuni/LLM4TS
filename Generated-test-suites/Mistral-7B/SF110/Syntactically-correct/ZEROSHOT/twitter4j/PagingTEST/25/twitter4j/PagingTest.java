import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        Paging pageOnly = new Paging(5);
        assertEquals(5, pageOnly.getPage());
        assertEquals(-1, pageOnly.getCount());
        assertEquals(-1L, pageOnly.getSinceId());
        assertEquals(-1L, pageOnly.getMaxId());

        Paging pageAndCount = new Paging(5, 10);
        assertEquals(5, pageAndCount.getPage());
        assertEquals(10, pageAndCount.getCount());
        assertEquals(-1L, pageAndCount.getSinceId());
        assertEquals(-1L, pageAndCount.getMaxId());

        Paging sinceIdOnly = new Paging(5, -1L);
        assertEquals(5, sinceIdOnly.getPage());
        assertEquals(-1, sinceIdOnly.getCount());
        assertEquals(-1L, sinceIdOnly.getSinceId());
        assertEquals(-1L, sinceIdOnly.getMaxId());

        Paging pageAndCountAndSinceId = new Paging(5, 10, -1L);
        assertEquals(5, pageAndCountAndSinceId.getPage());
        assertEquals(10, pageAndCountAndSinceId.getCount());
        assertEquals(-1L, pageAndCountAndSinceId.getSinceId());
        assertEquals(-1L, pageAndCountAndSinceId.getMaxId());

        Paging pageAndCountAndMaxId = new Paging(5, 10, -1L, 100L);
        assertEquals(5, pageAndCountAndMaxId.getPage());
        assertEquals(10, pageAndCountAndMaxId.getCount());
        assertEquals(-1L, pageAndCountAndMaxId.getSinceId());
        assertEquals(100L, pageAndCountAndMaxId.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(5);
        assertEquals(5, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());

        paging.setMaxId(100L);
        assertEquals(100L, paging.getMaxId());

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(20);
        assertEquals(20, paging.getCount());

        paging.setSinceId(2L);
        assertEquals(2L, paging.getSinceId());

        paging.setMaxId(200L);
        assertEquals(200L, paging.getMaxId());

        paging.count(5);
        assertEquals(5, paging.getCount());

        paging.sinceId(2L);
        assertEquals(2L, paging.getSinceId());

        paging.maxId(300L);
        assertEquals(300L, paging.getMaxId());
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 1, 1, 1);
        Paging paging2 = new Paging(1, 1, 1, 1);
        Paging paging3 = new Paging(1, 1, 2, 1);

        assertEquals(paging1, paging1);
        assertEquals(paging1, paging2);
        assertNotEquals(paging1, paging3);

        int hashCode1 = paging1.hashCode();
        int hashCode2 = paging2.hashCode();
        int hashCode3 = paging3.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertEquals(paging1.hashCode(), paging1.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 1, 1, 1);
        String expected = "Paging{page=1, count=1, sinceId=1, maxId=1}";
        assertEquals(expected, paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1, 100);
        HttpParameter[] expected = new HttpParameter[]{
            new HttpParameter("page", "1"),
            new HttpParameter("count", "10"),
            new HttpParameter("since_id", "1"),
            new HttpParameter("max_id", "100")
        };
        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1, 100);
        List<HttpParameter> expected = new ArrayList<HttpParameter>() {{
            add(new HttpParameter("page", "1"));
            add(new HttpParameter("count", "10"));
            add(new HttpParameter("since_id", "1"));
            add(new HttpParameter("max_id", "100"));
        }};
        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }
}