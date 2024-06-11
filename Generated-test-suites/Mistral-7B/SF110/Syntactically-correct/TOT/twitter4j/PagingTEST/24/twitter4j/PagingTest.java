package twitter4j.test;

import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testEmptyPaging() {
        Paging paging = new Paging();
        List<HttpParameter> expectedParameters = Arrays.asList(new HttpParameter("count", ""));
        List<HttpParameter> actualParameters = paging.asPostParameterList();
        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testValidPaging() {
        Paging paging = new Paging(2, 10, 1, 2);
        List<HttpParameter> expectedParameters = Arrays.asList(
                new HttpParameter("page", "2"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "2")
        );
        List<HttpParameter> actualParameters = paging.asPostParameterList();
        assertEquals(expectedParameters, actualParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPage() {
        new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCount() {
        new Paging(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceId() {
        new Paging(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxId() {
        new Paging(1, 1, -1);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidParam() {
        char[] supportedParams = new char[] {'s', 'm'};
        Paging paging = new Paging();
        paging.addPostParameter(supportedParams, 'n', new ArrayList<>(), "param", 1);
    }
}