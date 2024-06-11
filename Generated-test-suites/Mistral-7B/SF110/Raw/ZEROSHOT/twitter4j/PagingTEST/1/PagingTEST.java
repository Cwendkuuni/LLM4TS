import org.junit.Test;
import org.junit.Assert;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

import java.util.ArrayList;
import java.util.List;

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

        Paging paging2 = new Paging(1, 2);
        Assert.assertEquals(1, paging2.getPage());
        Assert.assertEquals(2, paging2.getCount());
        Assert.assertEquals(-1L, paging2.getSinceId());
        Assert.assertEquals(-1L, paging2.getMaxId());

        Paging paging3 = new Paging(1, 2, 3L);
        Assert.assertEquals(1, paging3.getPage());
        Assert.assertEquals(2, paging3.getCount());
        Assert.assertEquals(3L, paging3.getSinceId());
        Assert.assertEquals(-1L, paging3.getMaxId());

        Paging paging4 = new Paging(1, 2, 3L, 4L);
        Assert.assertEquals(1, paging4.getPage());
        Assert.assertEquals(2, paging4.getCount());
        Assert.assertEquals(3L, paging4.getSinceId());
        Assert.assertEquals(4L, paging4.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(1);
        Assert.assertEquals(1, paging.getPage());

        paging.setCount(2);
        Assert.assertEquals(2, paging.getCount());

        paging.setSinceId(3L);
        Assert.assertEquals(3L, paging.getSinceId());

        paging.setMaxId(4L);
        Assert.assertEquals(4L, paging.getMaxId());

        paging.setPage(5);
        Assert.assertEquals(5, paging.getPage());

        paging.setCount(6);
        Assert.assertEquals(6, paging.getCount());

        paging.setSinceId(7L);
        Assert.assertEquals(7L, paging.getSinceId());

        paging.setMaxId(8L);
        Assert.assertEquals(8L, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 2, 3L, 4L);

        HttpParameter[] expectedParameters = new HttpParameter[] {
                new HttpParameter("page", "1"),
                new HttpParameter("count", "2"),
                new HttpParameter("since_id", "3"),
                new HttpParameter("max_id", "4")
        };

        HttpParameter[] postParameters = paging.asPostParameterArray(Paging.SMCP, "count");
        Assert.assertArrayEquals(expectedParameters, postParameters);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 2, 3L, 4L);

        List<HttpParameter> expectedParameters = new ArrayList<HttpParameter>() {{
            add(new HttpParameter("page", "1"));
            add(new HttpParameter("count", "2"));
            add(new HttpParameter("since_id", "3"));
            add(new HttpParameter("max_id", "4"));
        }};

        List<HttpParameter> postParameters = paging.asPostParameterList(Paging.SMCP, "count");
        Assert.assertEquals(expectedParameters, postParameters);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> postParameters = new ArrayList<HttpParameter>();
        paging.addPostParameter(Paging.SMCP, 'x', postParameters, "unsupportedParam", 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithNegativeValues() {
        Paging paging = new Paging();

        paging.setPage(-1);
        paging.setCount(-1);
        paging.setSinceId(-1L);
        paging.setMaxId(-1L);
    }
}