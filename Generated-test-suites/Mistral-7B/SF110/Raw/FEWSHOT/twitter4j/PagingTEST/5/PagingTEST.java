package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testPagingConstructor() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());

        Paging paging1 = new Paging(1);
        assertEquals(1, paging1.getPage());
        assertNull(paging1.getCount());
        assertNull(paging1.getSinceId());
        assertNull(paging1.getMaxId());

        Paging paging2 = new Paging(1, 5);
        assertEquals(1, paging2.getPage());
        assertEquals(5, paging2.getCount());
        assertNull(paging2.getSinceId());
        assertNull(paging2.getMaxId());

        Paging paging3 = new Paging(1, 5, 10L);
        assertEquals(1, paging3.getPage());
        assertEquals(5, paging3.getCount());
        assertEquals(10L, paging3.getSinceId());
        assertNull(paging3.getMaxId());

        Paging paging4 = new Paging(1, 5, 10L, 20L);
        assertEquals(1, paging4.getPage());
        assertEquals(5, paging4.getCount());
        assertEquals(10L, paging4.getSinceId());
        assertEquals(20L, paging4.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(1234567890L);
        assertEquals(1234567890L, paging.getSinceId());

        paging.setMaxId(9876543210L);
        assertEquals(9876543210L, paging.getMaxId());

        paging.setPage(3);
        assertEquals(3, paging.getPage());

        paging.setCount(20);
        assertEquals(20, paging.getCount());

        paging.setSinceId(9876543210L);
        assertEquals(9876543210L, paging.getSinceId());

        paging.setMaxId(1234567890L);
        assertEquals(1234567890L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSettersWithInvalidValues() {
        Paging paging = new Paging();

        try {
            paging.setPage(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            paging.setCount(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            paging.setSinceId(-1L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            paging.setMaxId(-1L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            paging.setPage(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            paging.setCount(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            paging.setSinceId(0L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            paging.setMaxId(0L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 5, 10L, 20L);
        HttpParameter[] passed = new HttpParameter[] {
            new HttpParameter("page", "1"),
            new HttpParameter("count", "5"),
            new HttpParameter("since_id", "10"),
            new HttpParameter("max_id", "20")
        };
        HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArraysEqual(passed, result);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 5, 10L, 20L);
        List<HttpParameter> passed = Arrays.asList(
            new HttpParameter("page", "1"),
            new HttpParameter("count", "5"),
            new HttpParameter("since_id", "10"),
            new HttpParameter("max_id", "20")
        );
        List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(passed, result);
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging();
        try {
            paging.asPostParameterArray(new char[] {'z'}, "page");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging();
        try {
            paging.asPostParameterList(new char[] {'z'}, "page");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 5, 10L, 20L);
        Paging paging2 = new Paging(1, 5, 10L, 20L);
        Paging paging3 = new Paging(1, 5, 10L, 21L);
        Paging paging4 = new Paging(1, 6, 10L, 20L);

        assertTrue(paging1.equals(paging1));
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 5, 10L, 20L);
        Paging paging2 = new Paging(1, 5, 10L, 20L);
        Paging paging3 = new Paging(1, 5, 10L, 21L);
        Paging paging4 = new Paging(1, 6, 10L, 20L);

        assertEquals(paging1.hashCode(), paging1.hashCode());
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 5, 10L, 20L);
        String expected = "Paging{page=1, count=5, sinceId=10, maxId=20}";
        assertEquals(expected, paging.toString());
    }

    private void assertArraysEqual(Object[] expected, Object[] actual) {
        assertArrayEquals(expected, actual, "Arrays not equal");
    }

    private void assertArrayEquals(Object[] expected, Object[] actual, String message) {
        if (expected.length != actual.length) {
            fail(message + ": different lengths. Expected: " + expected.length + ", Actual: " + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                fail(message + ": [" + i + "] Expected: " + expected[i] + ", Actual: " + actual[i]);
            }
        }
    }
}