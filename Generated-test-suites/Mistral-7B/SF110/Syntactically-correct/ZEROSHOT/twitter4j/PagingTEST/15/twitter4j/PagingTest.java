import org.junit.Test;
import org.junit.Assert;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testPagingConstructor() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());

        Paging paging1 = new Paging(1);
        Assert.assertEquals(1, paging1.getPage());
        Assert.assertEquals(-1, paging1.getCount());
        Assert.assertEquals(-1L, paging1.getSinceId());
        Assert.assertEquals(-1L, paging1.getMaxId());

        Paging paging2 = new Paging(1, 10);
        Assert.assertEquals(1, paging2.getPage());
        Assert.assertEquals(10, paging2.getCount());
        Assert.assertEquals(-1L, paging2.getSinceId());
        Assert.assertEquals(-1L, paging2.getMaxId());

        Paging paging3 = new Paging(1, 10, 1L);
        Assert.assertEquals(1, paging3.getPage());
        Assert.assertEquals(10, paging3.getCount());
        Assert.assertEquals(1L, paging3.getSinceId());
        Assert.assertEquals(-1L, paging3.getMaxId());

        Paging paging4 = new Paging(1, 10, 1L, 100L);
        Assert.assertEquals(1, paging4.getPage());
        Assert.assertEquals(10, paging4.getCount());
        Assert.assertEquals(1L, paging4.getSinceId());
        Assert.assertEquals(100L, paging4.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(2);
        Assert.assertEquals(2, paging.getPage());

        paging.setCount(5);
        Assert.assertEquals(5, paging.getCount());

        paging.setSinceId(1L);
        Assert.assertEquals(1L, paging.getSinceId());

        paging.setMaxId(100L);
        Assert.assertEquals(100L, paging.getMaxId());

        paging.setPage(3);
        Assert.assertEquals(3, paging.getPage());

        paging.setCount(15);
        Assert.assertEquals(15, paging.getCount());

        paging.setSinceId(2L);
        Assert.assertEquals(2L, paging.getSinceId());

        paging.setMaxId(200L);
        Assert.assertEquals(200L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] postParameters = paging.asPostParameterArray();

        Assert.assertArrayEquals(new HttpParameter[] { new HttpParameter("page", "1"), new HttpParameter("count", "10"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "100") }, postParameters);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> postParameters = paging.asPostParameterList();

        Assert.assertEquals(4, postParameters.size());
        Assert.assertEquals(new HttpParameter("page", "1"), postParameters.get(0));
        Assert.assertEquals(new HttpParameter("count", "10"), postParameters.get(1));
        Assert.assertEquals(new HttpParameter("since_id", "1"), postParameters.get(2));
        Assert.assertEquals(new HttpParameter("max_id", "100"), postParameters.get(3));
    }

    @Test
    public void testAddPostParameter() {
        List<HttpParameter> postParameters = new ArrayList<>();
        Paging.addPostParameter(Paging.SMCP, 'p', postParameters, "page", 1);
        Assert.assertEquals(1, postParameters.size());
        Assert.assertEquals(new HttpParameter("page", "1"), postParameters.get(0));

        Paging.addPostParameter(Paging.SMCP, 'c', postParameters, "count", 10);
        Assert.assertEquals(2, postParameters.size());
        Assert.assertEquals(new HttpParameter("count", "10"), postParameters.get(1));
    }
}