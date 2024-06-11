package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging(1, 10);
    }

    @Test
    public void testAsPostParameterList() {
        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("count", "10"),
                new HttpParameter("page", "1")
        );
        List<HttpParameter> actualParams = paging.asPostParameterList();
        assertArrayEquals(expectedParams.stream().mapToInt(hp -> hp.getName().length()).toArray(), actualParams.stream().mapToInt(hp -> hp.getName().length()).toArray());
        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        char[] supportedParams = {'s', 'm', 'c'};
        assertThrows(IllegalStateException.class, () -> paging.asPostParameterList(supportedParams, "p"));
    }

    @Test
    public void testAsPostParameterListWithNoParams() {
        List<HttpParameter> expectedParams = new ArrayList<>();
        List<HttpParameter> actualParams = paging.asPostParameterList(Paging.SMCP, "");
        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsPostParameterArray() {
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("count", "10"),
                new HttpParameter("page", "1")
        };
        HttpParameter[] actualParams = paging.asPostParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAddPostParameterWithNegativeValue() {
        char[] supportedParams = {'s', 'm', 'c'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        assertThrows(IllegalStateException.class, () -> paging.addPostParameter(supportedParams, 'c', pagingParams, "count", -1));
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(2);
        paging.setCount(5);
        paging.setSinceId(1234567890L);
        paging.setMaxId(9876543210L);

        assertEquals(2, paging.getPage());
        assertEquals(5, paging.getCount());
        assertEquals(1234567890L, paging.getSinceId());
        assertEquals(9876543210L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSettersWithInvalidValues() {
        Paging paging = new Paging();

        assertThrows(IllegalArgumentException.class, () -> paging.setPage(-1));
        assertThrows(IllegalArgumentException.class, () -> paging.setCount(-1));
        assertThrows(IllegalArgumentException.class, () -> paging.setSinceId(-1L));
        assertThrows(IllegalArgumentException.class, () -> paging.setMaxId(-1L));
    }
}