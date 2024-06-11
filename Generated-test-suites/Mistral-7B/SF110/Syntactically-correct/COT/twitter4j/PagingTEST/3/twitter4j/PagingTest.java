import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        Paging paging1 = new Paging(1);
        assertEquals(1, paging1.getPage());
        assertEquals(-1, paging1.getCount());
        assertEquals(-1L, paging1.getSinceId());
        assertEquals(-1L, paging1.getMaxId());

        Paging paging2 = new Paging(1, 2);
        assertEquals(1, paging2.getPage());
        assertEquals(2, paging2.getCount());
        assertEquals(-1L, paging2.getSinceId());
        assertEquals(-1L, paging2.getMaxId());

        Paging paging3 = new Paging(1, 2, 3L);
        assertEquals(1, paging3.getPage());
        assertEquals(2, paging3.getCount());
        assertEquals(3L, paging3.getSinceId());
        assertEquals(-1L, paging3.getMaxId());

        Paging paging4 = new Paging(1, 2, 3L, 4L);
        assertEquals(1, paging4.getPage());
        assertEquals(2, paging4.getCount());
        assertEquals(3L, paging4.getSinceId());
        assertEquals(4L, paging4.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(5);
        assertEquals(5, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(11L);
        assertEquals(11L, paging.getSinceId());

        paging.setMaxId(12L);
        assertEquals(12L, paging.getMaxId());

        paging.setPage(0);
        try {
            paging.setPage(-1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("page should be positive integer.", e.getMessage());
        }

        paging.setCount(0);
        try {
            paging.setCount(-1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("count should be positive integer.", e.getMessage());
        }

        paging.setSinceId(0L);
        try {
            paging.setSinceId(-1L);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("since_id should be positive integer.", e.getMessage());
        }

        paging.setMaxId(0L);
        try {
            paging.setMaxId(-1L);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("max_id should be positive integer.", e.getMessage());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 2, 3L, 4L);
        Paging paging2 = new Paging(1, 2, 3L, 4L);
        assertEquals(paging1, paging2);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        Paging paging3 = new Paging(1, 2, 3L, 5L);
        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        Paging paging4 = new Paging(2, 2, 3L, 4L);
        assertNotEquals(paging1, paging4);
        assertNotEquals(paging1.hashCode(), paging4.hashCode());

        Paging paging5 = new Paging(1, 2, 4L, 4L);
        assertNotEquals(paging1, paging5);
        assertNotEquals(paging1.hashCode(), paging5.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        assertEquals("Paging{page=1, count=2, sinceId=3, maxId=4}", paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        List<HttpParameter> parameters = paging.asPostParameterList();
        assertEquals(4, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("2", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("3", parameters.get(2).getValue());
        assertEquals("max_id", parameters.get(3).getName());
        assertEquals("4", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterListWithNullParameters() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = paging.asPostParameterList();
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 2, 3L, 4L);
        HttpParameter[] parameters = paging.asPostParameterArray();
        assertEquals(4, parameters.length);
        assertEquals("page", parameters[0].getName());
        assertEquals("1", parameters[0].getValue());
        assertEquals("count", parameters[1].getName());
        assertEquals("2", parameters[1].getValue());
        assertEquals("since_id", parameters[2].getName());
        assertEquals("3", parameters[2].getValue());
        assertEquals("max_id", parameters[3].getName());
        assertEquals("4", parameters[3].getValue());
    }

    @Test
    public void testAsPostParameterArrayWithNullParameters() {
        Paging paging = new Paging();
        HttpParameter[] parameters = paging.asPostParameterArray();
        assertArrayEquals(new HttpParameter[0], parameters);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = new ArrayList<>();
        paging.addPostParameter(new char[]{'s'}, 'x', parameters, "unsupported_param", 1L);
        fail();
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = new ArrayList<>();
        paging.addPostParameter(Paging.S, 's', parameters, "since_id", 1L);
        assertEquals(1, parameters.size());
        assertEquals("since_id", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());

        paging.addPostParameter(Paging.S, 's', parameters, "since_id", 2L);
        assertEquals(2, parameters.size());
        assertEquals("since_id", parameters.get(1).getName());
        assertEquals("2", parameters.get(1).getValue());
    }
}