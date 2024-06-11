package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testConstructorWithDefaultValues() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(5);
        Assert.assertEquals(5, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(100L);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(100L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(2, 15);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(15, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(3, 101L);
        Assert.assertEquals(3, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(101L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(4, 20, 50L);
        Assert.assertEquals(4, paging.getPage());
        Assert.assertEquals(20, paging.getCount());
        Assert.assertEquals(50L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCountAndSinceIdAndMaxId() {
        Paging paging = new Paging(5, 30, 100L, 200L);
        Assert.assertEquals(5, paging.getPage());
        Assert.assertEquals(30, paging.getCount());
        Assert.assertEquals(100L, paging.getSinceId());
        Assert.assertEquals(200L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSetMaxIdWithValidValue() {
        Paging paging = new Paging();
        paging.setMaxId(100L);
        Assert.assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testSetMaxIdWithZeroValue() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
        Assert.assertEquals(0L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testAddPostParameterWithSupportedParam() {
        char[] supportedParams = Paging.S;
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = paging.asPostParameterList(supportedParams, "page");
        paging.addPostParameter(supportedParams, 'p', pagingParams, "page", 5);
        HttpParameter[] expectedPostParameters = new HttpParameter[]{new HttpParameter("page", "5")};
        Assert.assertArrayEquals(expectedPostParameters, pagingParams.toArray(new HttpParameter[pagingParams.size()]));
    }

    @Test
    public void testAddPostParameterWithUnsupportedParam() {
        char[] supportedParams = Paging.S;
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = paging.asPostParameterList(supportedParams, "unsupportedParam");
        paging.addPostParameter(supportedParams, 'u', pagingParams, "unsupportedParam", 5);
        List<HttpParameter> actualPostParameters = pagingParams;
        List<HttpParameter> expectedPostParameters = new ArrayList<>();
        Assert.assertEquals(actualPostParameters, expectedPostParameters);
    }

    @Test
    public void testAddPostParameterWithValidValue() {
        char[] supportedParams = Paging.S;
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = paging.asPostParameterList(supportedParams, "since_id");
        paging.addPostParameter(supportedParams, 's', pagingParams, "since_id", 100L);
        HttpParameter expectedPostParameter = new HttpParameter("since_id", "100");
        Assert.assertEquals(expectedPostParameter, pagingParams.get(0));
    }

    @Test
    public void testAddPostParameterWithEmptyList() {
        char[] supportedParams = Paging.S;
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = paging.asPostParameterList(supportedParams, "page");
        paging.addPostParameter(supportedParams, 'p', pagingParams, "page", 5);
        HttpParameter[] expectedPostParameters = new HttpParameter[]{new HttpParameter("page", "5")};
        Assert.assertArrayEquals(expectedPostParameters, pagingParams.toArray(new HttpParameter[pagingParams.size()]));
        List<HttpParameter> actualPostParameters = new ArrayList<>();
        pagingParams = paging.asPostParameterList(supportedParams, "page");
        paging.addPostParameter(supportedParams, 'p', pagingParams, "page", 6);
        actualPostParameters.add(new HttpParameter("page", "6"));
        Assert.assertEquals(actualPostParameters, pagingParams);
    }

    @Test
    public void testAsPostParameterListWithEmptyList() {
        char[] supportedParams = Paging.S;
        Paging paging = new Paging();
        List<HttpParameter> actualPostParameters = paging.asPostParameterList(supportedParams, "page");
        List<HttpParameter> expectedPostParameters = new ArrayList<>();
        Assert.assertEquals(actualPostParameters, expectedPostParameters);
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        char[] supportedParams = Paging.SMCP;
        Paging paging = new Paging(1, 10, 50L, 100L);
        List<HttpParameter> actualPostParameters = paging.asPostParameterList(supportedParams, "page");
        List<HttpParameter> expectedPostParameters = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "50"),
                new HttpParameter("max_id", "100")
        );
        Assert.assertEquals(expectedPostParameters, actualPostParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsPostParameterListWithUnsupportedParam() {
        char[] supportedParams = Paging.SMCP;
        Paging paging = new Paging(1, 10, 50L, 100L);
        List<HttpParameter> actualPostParameters = paging.asPostParameterList(supportedParams, "unsupportedParam");
    }

    @Test
    public void testAsPostParameterArrayWithEmptyList() {
        char[] supportedParams = Paging.S;
        Paging paging = new Paging();
        List<HttpParameter> actualPostParameters = paging.asPostParameterList(supportedParams, "page");
        HttpParameter[] expectedPostParameters = Paging.NULL_PARAMETER_ARRAY;
        Assert.assertArrayEquals(expectedPostParameters, actualPostParameters.toArray(new HttpParameter[actualPostParameters.size()]));
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        char[] supportedParams = Paging.SMCP;
        Paging paging = new Paging(1, 10, 50L, 100L);
        List<HttpParameter> actualPostParameters = paging.asPostParameterList(supportedParams, "page");
        HttpParameter[] expectedPostParameters = new HttpParameter[]{
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "50"),
                new HttpParameter("max_id", "100")
        };
        Assert.assertArrayEquals(expectedPostParameters, actualPostParameters.toArray(new HttpParameter[actualPostParameters.size()]));
        HttpParameter[] actualPostParametersArray = paging.asPostParameterArray(supportedParams, "page");
        Assert.assertArrayEquals(expectedPostParameters, actualPostParametersArray);
    }
}