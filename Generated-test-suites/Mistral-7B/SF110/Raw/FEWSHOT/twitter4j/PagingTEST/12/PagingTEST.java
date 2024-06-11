package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testEmptyPaging() {
        Paging paging = new Paging();
        List<HttpParameter> params = paging.asPostParameterList();
        assertNotNull(params);
        assertEquals(0, params.size());
    }

    @Test
    public void testPagingWithPage() {
        Paging paging = new Paging(1);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(1, params.size());
        HttpParameter param = params.get(0);
        assertEquals("page", param.getName());
        assertEquals("1", param.getValue());
    }

    @Test
    public void testPagingWithPageAndCount() {
        Paging paging = new Paging(1, 20);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(4, params.size());
        assertParameterPresent(params, "page", "1");
        assertParameterPresent(params, "count", "20");
    }

    @Test
    public void testPagingWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(3, params.size());
        assertParameterPresent(params, "page", "1");
        assertParameterPresent(params, "since_id", "1");
    }

    @Test
    public void testPagingWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 20, 1L);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(5, params.size());
        assertParameterPresent(params, "page", "1");
        assertParameterPresent(params, "count", "20");
        assertParameterPresent(params, "since_id", "1");
    }

    @Test
    public void testPagingWithPageAndCountAndMaxId() {
        Paging paging = new Paging(1, 20, -1L, 1L);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(5, params.size());
        assertParameterPresent(params, "page", "1");
        assertParameterPresent(params, "count", "20");
        assertParameterPresent(params, "since_id", "-1");
        assertParameterPresent(params, "max_id", "1");
    }

    @Test
    public void testPagingWithPageAndSinceIdAndMaxId() {
        Paging paging = new Paging(1, 1L, 1L);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(3, params.size());
        assertParameterPresent(params, "page", "1");
        assertParameterPresent(params, "since_id", "1");
        assertParameterPresent(params, "max_id", "1");
    }

    @Test
    public void testParsingNullParameters() {
        List<HttpParameter> params = Paging.NULL_PARAMETER_LIST;
        assertNotNull(params);
        assertEquals(0, params.size());

        HttpParameter[] arrayParams = Paging.NULL_PARAMETER_ARRAY;
        assertNotNull(arrayParams);
        assertEquals(0, arrayParams.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddingInvalidParameter() {
        Paging paging = new Paging('a', 'a', 1, 1, 1);
    }

    private void assertParameterPresent(List<HttpParameter> params, String name, String value) {
        for (HttpParameter param : params) {
            if (param.getName().equals(name) && param.getValue().equals(value)) {
                return;
            }
        }
        fail("Expected parameter with name " + name + " and value " + value + " not present in the list.");
    }
}