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
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(0, params.size());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(1, params.size());
        HttpParameter param = params.get(0);
        Assert.assertEquals("page", param.getName());
        Assert.assertEquals("1", param.getValue());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(1, params.size());
        HttpParameter param = params.get(0);
        Assert.assertEquals("since_id", param.getName());
        Assert.assertEquals("1", param.getValue());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(3, params.size());
        HttpParameter pageParam = params.get(0);
        HttpParameter countParam = params.get(1);
        HttpParameter cParam = params.get(2);

        Assert.assertEquals("page", pageParam.getName());
        Assert.assertEquals("1", pageParam.getValue());
        Assert.assertEquals("count", countParam.getName());
        Assert.assertEquals("10", countParam.getValue());
        Assert.assertEquals("c", cParam.getName()); // check for 'c' instead of 'count'
        Assert.assertNull(cParam.getValue());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(2, params.size());
        HttpParameter pageParam = params.get(0);
        HttpParameter sinceIdParam = params.get(1);

        Assert.assertEquals("page", pageParam.getName());
        Assert.assertEquals("1", pageParam.getValue());
        Assert.assertEquals("since_id", sinceIdParam.getName());
        Assert.assertEquals("1", sinceIdParam.getValue());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(3, params.size());
        HttpParameter pageParam = params.get(0);
        HttpParameter countParam = params.get(1);
        HttpParameter sinceIdParam = params.get(2);

        Assert.assertEquals("page", pageParam.getName());
        Assert.assertEquals("1", pageParam.getValue());
        Assert.assertEquals("count", countParam.getName());
        Assert.assertEquals("10", countParam.getValue());
        Assert.assertEquals("since_id", sinceIdParam.getName());
        Assert.assertEquals("1", sinceIdParam.getValue());
    }

    @Test
    public void testConstructorWithPageCountMaxId() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(2L, paging.getMaxId());

        List<HttpParameter> params = paging.asPostParameterList();
        Assert.assertEquals(4, params.size());
        HttpParameter pageParam = params.get(0);
        HttpParameter countParam = params.get(1);
        HttpParameter sinceIdParam = params.get(2);
        HttpParameter maxIdParam = params.get(3);

        Assert.assertEquals("page", pageParam.getName());
        Assert.assertEquals("1", pageParam.getValue());
        Assert.assertEquals("count", countParam.getName());
        Assert.assertEquals("10", countParam.getValue());
        Assert.assertEquals("since_id", sinceIdParam.getName());
        Assert.assertEquals("1", sinceIdParam.getValue());
        Assert.assertEquals("max_id", maxIdParam.getName());
        Assert.assertEquals("2", maxIdParam.getValue());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> params = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', params, "since_id", 1L);
        paging.addPostParameter(Paging.SMCP, 'm', params, "max_id", 2L);

        Assert.assertEquals(2, params.size());
        HttpParameter sinceIdParam = params.get(0);
        HttpParameter maxIdParam = params.get(1);

        Assert.assertEquals("since_id", sinceIdParam.getName());
        Assert.assertEquals("1", sinceIdParam.getValue());
        Assert.assertEquals("max_id", maxIdParam.getName());
        Assert.assertEquals("2", maxIdParam.getValue());

        List<HttpParameter> resultParams = paging.asPostParameterList();
        Assert.assertEquals(4, resultParams.size());
        HttpParameter resultSinceIdParam = resultParams.get(1);
        HttpParameter resultMaxIdParam = resultParams.get(2);

        Assert.assertEquals("since_id", resultSinceIdParam.getName());
        Assert.assertEquals("1", resultSinceIdParam.getValue());
        Assert.assertEquals("max_id", resultMaxIdParam.getName());
        Assert.assertEquals("2", resultMaxIdParam.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> params = new ArrayList<>();
        paging.addPostParameter(Paging.S, 'w', params, "unsupported_param", 1L);
    }

    @Test
    public void testEmptyListAndArray() {
        List<HttpParameter> emptyList = Collections.emptyList();
        HttpParameter[] emptyArray = Paging.NULL_PARAMETER_ARRAY;

        List<HttpParameter> paramsList = new ArrayList<>();
        HttpParameter[] paramsArray = new HttpParameter[0];

        Assert.assertArrayEquals(emptyArray, paramsArray);
        Assert.assertEquals(0, emptyList.size());
        Assert.assertEquals(0, paramsList.size());

        List<HttpParameter> list = Arrays.asList(new HttpParameter[] { new HttpParameter("param", "value") });
        HttpParameter[] array = new HttpParameter[] { new HttpParameter("param", "value") };

        Assert.assertArrayEquals(array, list.toArray(array));
    }
}