import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithAllParameters() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1L);
        paging.setMaxId(100L);

        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
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

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, result.length);
        HttpParameter param = result[0];
        assertEquals("page", param.getName());
        assertEquals("1", param.getValue());

        param = result[1];
        assertEquals("count", param.getName());
        assertEquals("10", param.getValue());

        param = result[2];
        assertEquals("since_id", param.getName());
        assertEquals("1", param.getValue());

        param = result[3];
        assertEquals("max_id", param.getName());
        assertEquals("100", param.getValue());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, result.size());
        HttpParameter param = result.get(0);
        assertEquals("page", param.getName());
        assertEquals("1", param.getValue());

        param = result.get(1);
        assertEquals("count", param.getName());
        assertEquals("10", param.getValue());

        param = result.get(2);
        assertEquals("since_id", param.getName());
        assertEquals("1", param.getValue());

        param = result.get(3);
        assertEquals("max_id", param.getName());
        assertEquals("100", param.getValue());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', parameters, "since_id", 1L);
        assertEquals(1, parameters.size());
        HttpParameter param = parameters.get(0);
        assertEquals("since_id", param.getName());
        assertEquals("1", param.getValue());
    }
}