package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PagingTest {

    @Test
    public void testPagingDefaultConstructor() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPage() {
        Paging paging = new Paging(1);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithAllParameters() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(100L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingNegativePage() {
        new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingNegativeCount() {
        new Paging(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingNegativeSinceId() {
        new Paging(1, 1, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingNegativeMaxId() {
        new Paging(1, 1, 1, -1L);
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "per_page");
        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "100")
        );
        Assert.assertArrayEquals(expectedParams.toArray(new HttpParameter[expectedParams.size()]), params);
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParamName() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "unsupported_param");
        Assert.assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, params);
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "per_page");
        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "100")
        );
        Assert.assertEquals(expectedParams, params);
    }

    @Test
    public void testAsPostParameterListWithEmptyList() {
        Paging paging = new Paging();
        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(Paging.NULL_PARAMETER_LIST, params);
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParamName() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> params = paging.asPostParameterList(new char[]{'a'}, "unsupported_param");
        Assert.assertEquals(Collections.emptyList(), params);
    }
}