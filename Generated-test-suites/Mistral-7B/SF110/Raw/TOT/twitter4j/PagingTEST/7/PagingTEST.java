package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndMaxId() {
        Paging paging = new Paging(1, 10, 1L, 11L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(11L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterListEmpty() {
        Paging paging = new Paging();
        List<HttpParameter> postParams = paging.asPostParameterList();
        Assert.assertEquals(0, postParams.size());
    }

    @Test
    public void testAsPostParameterListWithAllParams() {
        Paging paging = new Paging(1, 10, 1L, 11L);
        List<HttpParameter> postParams = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("page", String.valueOf(paging.getPage())),
                new HttpParameter("count", String.valueOf(paging.getCount())),
                new HttpParameter("since_id", String.valueOf(paging.getSinceId())),
                new HttpParameter("max_id", String.valueOf(paging.getMaxId()))
        };
        Assert.assertArrayEquals(expectedParams, postParams.toArray(new HttpParameter[0]));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> postParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'x', postParams, "unsupportedParam", 1L);
    }

    @Test
    public void testAsPostParameterArrayEmpty() {
        Paging paging = new Paging();
        HttpParameter[] postParams = paging.asPostParameterArray();
        Assert.assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, postParams);
    }

    @Test
    public void testAsPostParameterArrayWithAllParams() {
        Paging paging = new Paging(1, 10, 1L, 11L);
        List<HttpParameter> postParams = paging.asPostParameterList(Paging.SMCP, "count");
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("page", String.valueOf(paging.getPage())),
                new HttpParameter("count", String.valueOf(paging.getCount())),
                new HttpParameter("since_id", String.valueOf(paging.getSinceId())),
                new HttpParameter("max_id", String.valueOf(paging.getMaxId()))
        };
        HttpParameter[] actualParams = paging.asPostParameterArray(Paging.SMCP, "count");
        Assert.assertArrayEquals(expectedParams, actualParams);
    }
}