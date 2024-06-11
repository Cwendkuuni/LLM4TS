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
        Paging paging = new Paging(1, 10, 1L, 2L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterArrayWhenNoParameters() {
        Paging paging = new Paging();
        HttpParameter[] parameters = paging.asPostParameterArray();
        Assert.assertArrayEquals(new HttpParameter[0], parameters);
    }

    @Test
    public void testAsPostParameterArrayWhenSomeParameters() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] parameters = paging.asPostParameterArray();
        List<HttpParameter> expectedParameters = Arrays.asList(
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("since_id", "1"),
                new HttpParameter("max_id", "2")
        );
        Assert.assertArrayEquals(expectedParameters.toArray(new HttpParameter[expectedParameters.size()]), parameters);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParameter() {
        char[] supportedParams = Paging.SMCP;
        Paging paging = new Paging();
        paging.addPostParameter(supportedParams, 'a', new ArrayList<HttpParameter>(), "unsupportedParam", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativePage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 2L);
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        Assert.assertTrue(paging1.equals(paging2));
        Paging paging3 = new Paging(1, 10, 1L, 3L);
        Assert.assertFalse(paging1.equals(paging3));
        Paging paging4 = new Object();
        Assert.assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashcode() {
        Paging paging1 = new Paging(1, 10, 1L, 2L);
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        String expectedString = "Paging{page=1, count=10, sinceId=1, maxId=2}";
        Assert.assertEquals(expectedString, paging.toString());
    }
}