package twitter4j.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_COUNT = 20;
    private static final long DEFAULT_SINCE_ID = 0L;
    private static final long DEFAULT_MAX_ID = 1L;

    @Before
    public void setUp() {
        // Initialize the objects under test
        paging = new Paging();
    }

    @After
    public void tearDown() {
        // Release resources if needed
        paging = null;
    }

    private Paging paging;

    @Test
    public void testDefaultConstructor() {
        // Test default constructor
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        // Test constructor with page argument
        Paging paging = new Paging(DEFAULT_PAGE);
        assertEquals(DEFAULT_PAGE, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        // Test constructor with sinceId argument
        Paging paging = new Paging(DEFAULT_SINCE_ID);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(DEFAULT_SINCE_ID, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        // Test constructor with page and count arguments
        Paging paging = new Paging(DEFAULT_PAGE, DEFAULT_COUNT);
        assertEquals(DEFAULT_PAGE, paging.getPage());
        assertEquals(DEFAULT_COUNT, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        // Test constructor with page and sinceId arguments
        Paging paging = new Paging(DEFAULT_PAGE, DEFAULT_SINCE_ID);
        assertEquals(DEFAULT_PAGE, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(DEFAULT_SINCE_ID, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithAllArguments() {
        // Test constructor with all arguments
        Paging paging = new Paging(DEFAULT_PAGE, DEFAULT_COUNT, DEFAULT_SINCE_ID, DEFAULT_MAX_ID);
        assertEquals(DEFAULT_PAGE, paging.getPage());
        assertEquals(DEFAULT_COUNT, paging.getCount());
        assertEquals(DEFAULT_SINCE_ID, paging.getSinceId());
        assertEquals(DEFAULT_MAX_ID, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        // Test getters and setters
        Paging paging = new Paging();

        // Set page
        paging.setPage(DEFAULT_PAGE);
        assertEquals(DEFAULT_PAGE, paging.getPage());

        // Set count
        paging.setCount(DEFAULT_COUNT);
        assertEquals(DEFAULT_COUNT, paging.getCount());

        // Set sinceId
        paging.setSinceId(DEFAULT_SINCE_ID);
        assertEquals(DEFAULT_SINCE_ID, paging.getSinceId());

        // Set maxId
        paging.setMaxId(DEFAULT_MAX_ID);
        assertEquals(DEFAULT_MAX_ID, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersInvalidArguments() {
        // Test invalid arguments in setters
        Paging paging = new Paging();

        // Set page with negative value
        paging.setPage(-1);

        // Set count with negative value
        paging.setCount(-1);

        // Set sinceId with negative value
        paging.setSinceId(-1L);

        // Set maxId with negative value
        paging.setMaxId(-1L);
    }

    @Test
    public void testEqualsObject() {
        // Test equals() method
        Paging paging1 = new Paging();
        Paging paging2 = new Paging();

        // Check that paging1 is equal to itself
        assertTrue(paging1.equals(paging1));

        // Check that paging1 is not equal to paging2
        assertFalse(paging1.equals(paging2));

        // Set equal fields in paging2
        paging2.setPage(paging1.getPage());
        paging2.setCount(paging1.getCount());
        paging2.setSinceId(paging1.getSinceId());
        paging2.setMaxId(paging1.getMaxId());

        // Check that paging1 is equal to paging2
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        // Test hashCode() method
        Paging paging1 = new Paging();
        Paging paging2 = new Paging();

        // Check that hashcode is equal for equal objects
        Paging paging3 = new Paging();
        paging3.setPage(paging1.getPage());
        paging3.setCount(paging1.getCount());
        paging3.setSinceId(paging1.getSinceId());
        paging3.setMaxId(paging1.getMaxId());
        assertEquals(paging1.hashCode(), paging3.hashCode());

        // Check that hashcode is different for unequal objects
        paging2.setPage(paging1.getPage() + 1);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        // Test toString() method
        Paging paging = new Paging();
        paging.setPage(DEFAULT_PAGE);
        paging.setCount(DEFAULT_COUNT);
        paging.setSinceId(DEFAULT_SINCE_ID);
        paging.setMaxId(DEFAULT_MAX_ID);

        String expected = String.format("Paging{page=%d, count=%d, sinceId=%d, maxId=%d}",
                DEFAULT_PAGE, DEFAULT_COUNT, DEFAULT_SINCE_ID, DEFAULT_MAX_ID);

        String actual = paging.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArray() {
        // Test asPostParameterArray() method
        Paging paging = new Paging();
        paging.setPage(DEFAULT_PAGE);
        paging.setCount(DEFAULT_COUNT);
        paging.setSinceId(DEFAULT_SINCE_ID);
        paging.setMaxId(DEFAULT_MAX_ID);

        HttpParameter[] expected = new HttpParameter[]{
                new HttpParameter("page", String.valueOf(DEFAULT_PAGE)),
                new HttpParameter("count", String.valueOf(DEFAULT_COUNT)),
                new HttpParameter("since_id", String.valueOf(DEFAULT_SINCE_ID)),
                new HttpParameter("max_id", String.valueOf(DEFAULT_MAX_ID))
        };

        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterList() {
        // Test asPostParameterList() method
        Paging paging = new Paging();
        paging.setPage(DEFAULT_PAGE);
        paging.setCount(DEFAULT_COUNT);
        paging.setSinceId(DEFAULT_SINCE_ID);
        paging.setMaxId(DEFAULT_MAX_ID);

        List<HttpParameter> expected = new ArrayList<>(Arrays.asList(
                new HttpParameter("page", String.valueOf(DEFAULT_PAGE)),
                new HttpParameter("count", String.valueOf(DEFAULT_COUNT)),
                new HttpParameter("since_id", String.valueOf(DEFAULT_SINCE_ID)),
                new HttpParameter("max_id", String.valueOf(DEFAULT_MAX_ID))
        ));

        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }
}