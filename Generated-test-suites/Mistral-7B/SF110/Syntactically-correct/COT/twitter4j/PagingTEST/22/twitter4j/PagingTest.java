import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

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
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithAllParameters() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1L, paging.getSinceId());
        Assert.assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging(1);
        paging.setPage(2);
        Assert.assertEquals(2, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegativeNumber() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        paging.setCount(5);
        Assert.assertEquals(5, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegativeNumber() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1L);
        Assert.assertEquals(1L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegativeNumber() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(1L);
        Assert.assertEquals(1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegativeNumber() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "per_page");
        Assert.assertEquals(4, parameters.size());
        HttpParameter sinceIdParam = null;
        HttpParameter pageParam = null;
        HttpParameter countParam = null;
        HttpParameter maxIdParam = null;
        for (HttpParameter param : parameters) {
            switch (param.getName()) {
                case "since_id":
                    sinceIdParam = param;
                    break;
                case "page":
                    pageParam = param;
                    break;
                case "count":
                    countParam = param;
                    break;
                case "max_id":
                    maxIdParam = param;
                    break;
            }
        }
        Assert.assertNotNull(sinceIdParam);
        Assert.assertEquals(1L, Long.parseLong(sinceIdParam.getValue()));
        Assert.assertNotNull(pageParam);
        Assert.assertEquals(1, Integer.parseInt(pageParam.getValue()));
        Assert.assertNotNull(countParam);
        Assert.assertEquals(10, Integer.parseInt(countParam.getValue()));
        Assert.assertNotNull(maxIdParam);
        Assert.assertEquals(100L, Long.parseLong(maxIdParam.getValue()));
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] parameters = paging.asPostParameterArray(Paging.SMCP, "per_page");
        Assert.assertArrayEquals(new HttpParameter[]{
                new HttpParameter("since_id", "1"),
                new HttpParameter("page", "1"),
                new HttpParameter("count", "10"),
                new HttpParameter("max_id", "100")}, parameters);
    }
}