import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

    Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        paging = new Paging(1, 5);
        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        paging = new Paging(1, 5, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndMaxId() {
        paging = new Paging(1, 5, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(3L);
        paging.setMaxId(4L);

        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(3L, paging.getSinceId());
        assertEquals(4L, paging.getMaxId());
    }

    @Test
    public void testIllegalArgumentExceptionWhenCountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            paging.setCount(-1);
        });
    }

    @Test
    public void testIllegalArgumentExceptionWhenPageIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            paging.setPage(-1);
        });
    }

    @Test
    public void testIllegalArgumentExceptionWhenSinceIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            paging.setSinceId(-1L);
        });
    }

    @Test
    public void testIllegalArgumentExceptionWhenMaxIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            paging.setMaxId(-1L);
        });
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 5, 1L, 2L);
        Paging paging2 = new Paging(1, 5, 1L, 2L);
        Paging paging3 = new Paging(1, 5, 1L, 3L);

        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertFalse(paging1.equals(null));
        assertFalse(paging1.equals("test"));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 5, 1L, 2L);
        Paging paging2 = new Paging(1, 5, 1L, 2L);
        Paging paging3 = new Paging(1, 5, 1L, 3L);

        int hashCode1 = paging1.hashCode();
        int hashCode2 = paging2.hashCode();
        int hashCode3 = paging3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 5, 1L, 2L);
        String expected = "Paging{page=1, count=5, sinceId=1, maxId=2}";
        assertEquals(expected, paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        paging = new Paging(1, 5, 1L, 2L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] expectedParamsArray = {new HttpParameter("count", "5")};
        HttpParameter[] actualParamsArray = parameters.toArray(new HttpParameter[0]);

        assertArrayEquals(expectedParamsArray, actualParamsArray);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        paging = new Paging(1, 5, 1L, 2L);
        List<HttpParameter> parameters = paging.asPostParameterList(new char[0], "count");
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParamsAndParamName() {
        paging = new Paging(1, 5, 1L, 2L);
        List<HttpParameter> parameters = paging.asPostParameterList(new char[0], "per_page");
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testAsPostParameterArray() {
        paging = new Paging(1, 5, 1L, 2L);
        HttpParameter[] expectedParamsArray = {new HttpParameter("count", "5")};
        HttpParameter[] actualParamsArray = paging.asPostParameterArray(Paging.SMCP, "count");

        assertArrayEquals(expectedParamsArray, actualParamsArray);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        paging = new Paging(1, 5, 1L, 2L);
        HttpParameter[] actualParamsArray = paging.asPostParameterArray(new char[0], "count");
        assertTrue(actualParamsArray.length == 0);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParamsAndParamName() {
        paging = new Paging(1, 5, 1L, 2L);
        HttpParameter[] actualParamsArray = paging.asPostParameterArray(new char[0], "per_page");
        assertTrue(actualParamsArray.length == 0);
    }
}