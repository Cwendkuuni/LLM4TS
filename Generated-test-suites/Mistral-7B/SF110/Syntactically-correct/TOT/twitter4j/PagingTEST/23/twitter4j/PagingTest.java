import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

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

        paging.setCount(15);
        assertEquals(15, paging.getCount());

        paging.setSinceId(-1L);
        try {
            paging.setSinceId(-2L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        paging.setPage(-1);
        try {
            paging.setPage(-2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAsPostParameterArrayAndList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] postParameters = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, postParameters.length);
        for (HttpParameter param : postParameters) {
            assertNotNull(param);
            assertEquals("page", param.getName());
            assertEquals("1", param.getValue());
            assertEquals("count", param.getName());
            assertEquals("10", param.getValue());
            assertEquals("since_id", param.getName());
            assertEquals("1", param.getValue());
            assertEquals("max_id", param.getName());
            assertEquals("100", param.getValue());
        }

        List<HttpParameter> postParameterList = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, postParameterList.size());
        for (HttpParameter param : postParameterList) {
            assertNotNull(param);
            assertEquals("page", param.getName());
            assertEquals("1", param.getValue());
            assertEquals("count", param.getName());
            assertEquals("10", param.getValue());
            assertEquals("since_id", param.getName());
            assertEquals("1", param.getValue());
            assertEquals("max_id", param.getName());
            assertEquals("100", param.getValue());
        }

        // empty test
        Paging emptyPaging = new Paging();
        HttpParameter[] emptyPostParameters = emptyPaging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(0, emptyPostParameters.length);
        List<HttpParameter> emptyPostParameterList = emptyPaging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(0, emptyPostParameterList.size());
    }

    @Test
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        char[] supportedParams = new char[]{'s', 'm', 'c', 'p'};
        List<HttpParameter> postParameterList = paging.asPostParameterList(supportedParams, "unsupported_param");
        assertFalse(postParameterList.isEmpty());
        HttpParameter param = postParameterList.get(0);
        assertEquals("unsupported_param", param.getName());
        assertEquals("IllegalStateException: Paging parameter [unsupported_param] is not supported with this operation.", param.getValue());

        HttpParameter[] postParameters = paging.asPostParameterArray(supportedParams, "unsupported_param");
        assertFalse(Arrays.equals(Paging.NULL_PARAMETER_ARRAY, postParameters));
        assertEquals(1, postParameters.length);
        HttpParameter paramArray = postParameters[0];
        assertEquals("unsupported_param", paramArray.getName());
        assertEquals("IllegalStateException: Paging parameter [unsupported_param] is not supported with this operation.", paramArray.getValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertEquals(paging1, paging2);

        Paging paging3 = new Paging(1, 10, 2L, 101L);
        assertNotEquals(paging1, paging3);

        Paging paging4 = new Paging(1, 11, 1L, 100L);
        assertNotEquals(paging1, paging4);

        Paging paging5 = new Paging(2, 10, 1L, 100L);
        assertNotEquals(paging1, paging5);

        Paging paging6 = new Paging(1, 10, 1L, 99L);
        assertNotEquals(paging1, paging6);

        Paging paging7 = new Paging(1, 10, 1L, 100L);
        paging7.setSinceId(99L);
        assertNotEquals(paging1, paging7);

        Paging paging8 = new Paging(1, 10, 1L, 100L);
        paging8.setMaxId(99L);
        assertNotEquals(paging1, paging8);

        Paging paging9 = new Paging(1, 11, 1L, 100L);
        assertNotEquals(paging1, paging9);

        Paging paging10 = new Paging(1, 10, 1L, 100L);
        paging10.setCount(11);
        assertNotEquals(paging1, paging10);
    }
}