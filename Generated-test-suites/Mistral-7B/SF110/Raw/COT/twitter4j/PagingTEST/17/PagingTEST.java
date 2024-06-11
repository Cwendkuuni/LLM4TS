import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertNull(paging.getPage());
        assertNull(paging.getCount());
        assertNull(paging.getSinceId());
        assertNull(paging.getMaxId());

        Paging pagingWithPage = new Paging(1);
        assertEquals(1, pagingWithPage.getPage());
        assertNull(pagingWithPage.getCount());
        assertNull(pagingWithPage.getSinceId());
        assertNull(pagingWithPage.getMaxId());

        Paging pagingWithSinceId = new Paging(1L);
        assertEquals(1, pagingWithSinceId.getPage());
        assertNull(pagingWithSinceId.getCount());
        assertEquals(1L, pagingWithSinceId.getSinceId());
        assertNull(pagingWithSinceId.getMaxId());

        Paging pagingWithPageAndCount = new Paging(1, 5);
        assertEquals(1, pagingWithPageAndCount.getPage());
        assertEquals(5, pagingWithPageAndCount.getCount());
        assertNull(pagingWithPageAndCount.getSinceId());
        assertNull(pagingWithPageAndCount.getMaxId());

        Paging pagingWithPageSinceId = new Paging(1, 1L);
        assertEquals(1, pagingWithPageSinceId.getPage());
        assertNull(pagingWithPageSinceId.getCount());
        assertEquals(1L, pagingWithPageSinceId.getSinceId());
        assertNull(pagingWithPageSinceId.getMaxId());

        Paging pagingWithAllParams = new Paging(1, 5, 1L, 5L);
        assertEquals(1, pagingWithAllParams.getPage());
        assertEquals(5, pagingWithAllParams.getCount());
        assertEquals(1L, pagingWithAllParams.getSinceId());
        assertEquals(5L, pagingWithAllParams.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(1);
        assertEquals(1, paging.getPage());

        paging.setCount(5);
        assertEquals(5, paging.getCount());

        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());

        paging.setMaxId(5L);
        assertEquals(5L, paging.getMaxId());

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(2L);
        assertEquals(2L, paging.getSinceId());

        paging.setMaxId(10L);
        assertEquals(10L, paging.getMaxId());
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 5, 1L, 5L);
        Paging paging2 = new Paging(1, 5, 1L, 5L);
        Paging paging3 = new Paging(1, 5, 1L, 6L);

        assertEquals(paging1, paging2);
        assertEquals(paging1.hashCode(), paging2.hashCode());

        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());

        Paging paging4 = new Paging();
        assertNotEquals(paging1, paging4);
        assertNotEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 5, 1L, 5L);
        String expected = "Paging{page=1, count=5, sinceId=1, maxId=5}";
        assertEquals(expected, paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 5, 1L, 5L);
        HttpParameter[] postParams = paging.asPostParameterArray();
        assertEquals(4, postParams.length);

        for (HttpParameter param : postParams) {
            String name = param.getName();
            String value = param.getValue();

            switch (name.charAt(0)) {
                case 'p':
                    assertEquals("page", name);
                    assertEquals("1", value);
                    break;
                case 'c':
                    assertEquals("count", name);
                    assertEquals("5", value);
                    break;
                case 's':
                    assertEquals("since_id", name);
                    assertEquals("1", value);
                    break;
                case 'm':
                    assertEquals("max_id", name);
                    assertEquals("5", value);
                    break;
                default:
                    fail("Unexpected parameter name: " + name);
            }
        }
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 5, 1L, 5L);
        List<HttpParameter> postParams = paging.asPostParameterList();
        assertEquals(4, postParams.size());

        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("page", "1"));
        expectedParams.add(new HttpParameter("count", "5"));
        expectedParams.add(new HttpParameter("since_id", "1"));
        expectedParams.add(new HttpParameter("max_id", "5"));

        for (int i = 0; i < postParams.size(); i++) {
            HttpParameter actualParam = postParams.get(i);
            HttpParameter expectedParam = expectedParams.get(i);

            assertEquals(expectedParam.getName(), actualParam.getName());
            assertEquals(expectedParam.getValue(), actualParam.getValue());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        char[] supportedParams = new char[]{'p', 'c', 's'};
        List<HttpParameter> postParams = new ArrayList<>();

        paging.addPostParameter(supportedParams, 'l', postParams, "unsupported_param", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithNegativeValue() {
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
    }
}