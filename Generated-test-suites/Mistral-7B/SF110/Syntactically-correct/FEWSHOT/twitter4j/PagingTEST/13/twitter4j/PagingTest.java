package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testInstance() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());

        Paging pagingWithPage = new Paging(5);
        assertEquals(5, pagingWithPage.getPage());
        assertNull(pagingWithPage.getCount());
        assertNull(pagingWithPage.getSinceId());
        assertNull(pagingWithPage.getMaxId());

        Paging pagingWithCount = new Paging(5, 10);
        assertEquals(5, pagingWithCount.getPage());
        assertEquals(10, pagingWithCount.getCount());
        assertNull(pagingWithCount.getSinceId());
        assertNull(pagingWithCount.getMaxId());

        Paging pagingWithSinceId = new Paging(5, 10, 12345L);
        assertEquals(5, pagingWithSinceId.getPage());
        assertEquals(10, pagingWithSinceId.getCount());
        assertEquals(12345L, pagingWithSinceId.getSinceId());
        assertNull(pagingWithSinceId.getMaxId());

        Paging pagingWithAllParameters = new Paging(5, 10, 12345L, 67890L);
        assertEquals(5, pagingWithAllParameters.getPage());
        assertEquals(10, pagingWithAllParameters.getCount());
        assertEquals(12345L, pagingWithAllParameters.getSinceId());
        assertEquals(67890L, pagingWithAllParameters.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging(5);
        assertEquals(5, paging.getPage());
        paging.setPage(10);
        assertEquals(10, paging.getPage());
        paging.setCount(20);
        assertEquals(20, paging.getCount());
        paging.setSinceId(12345L);
        assertEquals(12345L, paging.getSinceId());
        paging.setMaxId(67890L);
        assertEquals(67890L, paging.getMaxId());

        try {
            paging.setPage(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            paging.setCount(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            paging.setSinceId(-1L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            paging.setMaxId(-1L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(5, 10, 12345L, 67890L);
        Paging paging2 = new Paging(5, 10, 12345L, 67890L);
        assertTrue(paging1.equals(paging2));
        assertTrue(paging1.hashCode() == paging2.hashCode());

        Paging paging3 = new Paging(5, 10, 12345L, 67891L);
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.hashCode() == paging3.hashCode());

        Paging paging4 = new Paging(10, 10, 12345L, 67890L);
        assertFalse(paging1.equals(paging4));
        assertFalse(paging1.hashCode() == paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        String expected = "Paging{page=5, count=10, sinceId=12345, maxId=67890}";
        assertEquals(expected, paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        List<HttpParameter> expected = Arrays.asList(
                new HttpParameter("page", "5"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "12345"),
                new HttpParameter("max_id", "67890")
        );
        List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(expected, result);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        List<HttpParameter> result = paging.asPostParameterList(new char[0], "count");
        assertSame(Collections.emptyList(), result);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        HttpParameter[] expected = new HttpParameter[] {
                new HttpParameter("page", "5"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "12345"),
                new HttpParameter("max_id", "67890")
        };
        HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        HttpParameter[] result = paging.asPostParameterArray(new char[0], "count");
        assertSame(Paging.NULL_PARAMETER_ARRAY, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "unsupported", 12345L);
    }

    @Test
    public void testAddPostParameterWithSupportedParamAndValue() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 12345L);
        List<HttpParameter> expected = Arrays.asList(new HttpParameter("since_id", "12345"));
        assertEquals(expected, pagingParams);
    }

    @Test
    public void testAddPostParameterWithSupportedParamAndNullValue() {
        Paging paging = new Paging(5, 10, 12345L, 67890L);
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", null);
        List<HttpParameter> expected = Collections.emptyList();
        assertEquals(expected, pagingParams);
    }
}