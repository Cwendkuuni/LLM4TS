import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testConstructors() {
        // Test empty constructor
        Paging paging1 = new Paging();
        assertEquals(-1, paging1.getPage());
        assertEquals(-1, paging1.getCount());
        assertEquals(-1L, paging1.getSinceId());
        assertEquals(-1L, paging1.getMaxId());

        // Test constructor with page
        Paging paging2 = new Paging(5);
        assertEquals(5, paging2.getPage());
        assertEquals(-1, paging2.getCount());
        assertEquals(-1L, paging2.getSinceId());
        assertEquals(-1L, paging2.getMaxId());

        // Test constructor with sinceId
        Paging paging3 = new Paging(10L);
        assertEquals(-1, paging3.getPage());
        assertEquals(-1, paging3.getCount());
        assertEquals(10L, paging3.getSinceId());
        assertEquals(-1L, paging3.getMaxId());

        // Test constructor with page and count
        Paging paging4 = new Paging(2, 10);
        assertEquals(2, paging4.getPage());
        assertEquals(10, paging4.getCount());
        assertEquals(-1L, paging4.getSinceId());
        assertEquals(-1L, paging4.getMaxId());

        // Test constructor with page, count, sinceId
        Paging paging5 = new Paging(3, 5, 15L);
        assertEquals(3, paging5.getPage());
        assertEquals(5, paging5.getCount());
        assertEquals(15L, paging5.getSinceId());
        assertEquals(-1L, paging5.getMaxId());

        // Test constructor with page, count, sinceId, maxId
        Paging paging6 = new Paging(4, 7, 20L, 30L);
        assertEquals(4, paging6.getPage());
        assertEquals(7, paging6.getCount());
        assertEquals(20L, paging6.getSinceId());
        assertEquals(30L, paging6.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        // Test getters and setters for page
        paging.setPage(5);
        assertEquals(5, paging.getPage());

        try {
            paging.setPage(-1);
            fail("Expected IllegalArgumentException for negative page number.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getters and setters for count
        paging.setCount(10);
        assertEquals(10, paging.getCount());

        try {
            paging.setCount(-1);
            fail("Expected IllegalArgumentException for negative count number.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getters and setters for sinceId
        paging.setSinceId(15L);
        assertEquals(15L, paging.getSinceId());

        try {
            paging.setSinceId(-1L);
            fail("Expected IllegalArgumentException for negative sinceId number.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getters and setters for maxId
        paging.setMaxId(30L);
        assertEquals(30L, paging.getMaxId());

        try {
            paging.setMaxId(-1L);
            fail("Expected IllegalArgumentException for negative maxId number.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(2, 10, 15L, 30L);

        HttpParameter[] expectedParameters = new HttpParameter[] {
                new HttpParameter("page", "2"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "15"),
                new HttpParameter("max_id", "30")
        };

        HttpParameter[] actualParameters = paging.asPostParameterArray();

        assertArrayEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(2, 10, 15L, 30L);

        List<HttpParameter> expectedParameters = Arrays.asList(
                new HttpParameter("page", "2"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "15"),
                new HttpParameter("max_id", "30")
        );

        List<HttpParameter> actualParameters = paging.asPostParameterList();

        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");

        try {
            paging.addPostParameter(Paging.SMCP, 'x', parameters, "invalid_param", 10);
            fail("Expected IllegalStateException for unsupported parameter.");
        } catch (IllegalStateException e) {
            // Expected
        }

        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 15L);
        assertEquals(1, parameters.size());
        assertEquals("since_id", parameters.get(0).getName());
        assertEquals("15", parameters.get(0).getValue());

        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 20L);
        assertEquals(2, parameters.size());
        assertEquals("since_id", parameters.get(1).getName());
        assertEquals("20", parameters.get(1).getValue());
    }
}