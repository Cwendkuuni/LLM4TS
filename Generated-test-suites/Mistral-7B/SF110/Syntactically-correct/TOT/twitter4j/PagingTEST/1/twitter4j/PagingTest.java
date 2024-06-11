package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpParameterHandler;
import twitter4j.Paging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testDefaultPaging() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
        
        List<HttpParameter> params = paging.asPostParameterList();
        List<HttpParameter> expectedParams = new ArrayList<>();
        assertEquals(0, params.size());
        assertEquals(0, expectedParams.size());
        
        HttpParameter[] actualParams = paging.asPostParameterArray();
        HttpParameter[] expectedParamsArray = Paging.NULL_PARAMETER_ARRAY;
        assertArrayEquals(expectedParamsArray, actualParams);
    }

    @Test
    public void testPagingWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("page", "1"));
        assertEquals(1, params.size());
        assertEquals(1, expectedParams.size());
        for (HttpParameter param : params) {
            assertEquals("page", param.getName());
            assertEquals("1", param.getValue());
        }

        HttpParameter[] actualParams = paging.asPostParameterArray();
        assertEquals(1, actualParams.length);
        HttpParameter actualParam = actualParams[0];
        assertEquals("page", actualParam.getName());
        assertEquals("1", actualParam.getValue());
    }

    @Test
    public void testPagingWithSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("since_id", "1"));
        assertEquals(1, params.size());
        assertEquals(1, expectedParams.size());
        for (HttpParameter param : params) {
            assertEquals("since_id", param.getName());
            assertEquals("1", param.getValue());
        }

        HttpParameter[] actualParams = paging.asPostParameterArray();
        assertEquals(1, actualParams.length);
        HttpParameter actualParam = actualParams[0];
        assertEquals("since_id", actualParam.getName());
        assertEquals("1", actualParam.getValue());
    }

    @Test
    public void testPagingWithPageAndSinceId() {
        Paging paging = new Paging(1, 1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("page", "1"));
        expectedParams.add(new HttpParameter("since_id", "1"));
        assertEquals(2, params.size());
        assertEquals(2, expectedParams.size());
        for (HttpParameter param : params) {
            if (param.getName().equals("page")) {
                assertEquals("page", param.getName());
                assertEquals("1", param.getValue());
            } else if (param.getName().equals("since_id")) {
                assertEquals("since_id", param.getName());
                assertEquals("1", param.getValue());
            }
        }

        HttpParameter[] actualParams = paging.asPostParameterArray();
        assertEquals(2, actualParams.length);
        List<String> actualParamNames = new ArrayList<>();
        for (HttpParameter param : Arrays.asList(actualParams)) {
            actualParamNames.add(param.getName());
        }
        List<String> expectedParamNames = new ArrayList<>();
        expectedParamNames.add("page");
        expectedParamNames.add("since_id");
        assertEquals(expectedParamNames, actualParamNames);
    }

    @Test(expected = IllegalStateException.class)
    public void testPagingWithInvalidParam() {
        Paging paging = new Paging();
        paging.addPostParameter(Paging.SMCP, 'x', new ArrayList<>(), "invalid_param", -1L);
    }
}