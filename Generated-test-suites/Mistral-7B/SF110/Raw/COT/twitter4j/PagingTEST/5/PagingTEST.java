import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertNotNull(paging);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        Paging pagingWithPage = new Paging(1);
        assertEquals(1, pagingWithPage.getPage());
        assertEquals(-1, pagingWithPage.getCount());
        assertEquals(-1L, pagingWithPage.getSinceId());
        assertEquals(-1L, pagingWithPage.getMaxId());

        Paging pagingWithCount = new Paging(1, 2);
        assertEquals(1, pagingWithCount.getPage());
        assertEquals(2, pagingWithCount.getCount());
        assertEquals(-1L, pagingWithCount.getSinceId());
        assertEquals(-1L, pagingWithCount.getMaxId());

        Paging pagingWithSinceId = new Paging(1, -1L);
        assertEquals(1, pagingWithSinceId.getPage());
        assertEquals(-1, pagingWithSinceId.getCount());
        assertEquals(-1L, pagingWithSinceId.getSinceId());
        assertEquals(-1L, pagingWithSinceId.getMaxId());

        Paging pagingWithAllParams = new Paging(1, 2, -1L, -1L);
        assertEquals(1, pagingWithAllParams.getPage());
        assertEquals(2, pagingWithAllParams.getCount());
        assertEquals(-1L, pagingWithAllParams.getSinceId());
        assertEquals(-1L, pagingWithAllParams.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();

        paging.setPage(5);
        assertEquals(5, paging.getPage());

        paging.setCount(10);
        assertEquals(10, paging.getCount());

        paging.setSinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());

        paging.setMaxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = paging.asPostParameterList('s', 'c');
        paging.addPostParameter('s', 's', pagingParams, "since_id", 123L);
        assertEquals(1, pagingParams.size());
        HttpParameter sinceIdParam = pagingParams.get(0);
        assertEquals("since_id", sinceIdParam.getName());
        assertEquals("123", sinceIdParam.getValue());

        pagingParams = paging.asPostParameterList('s', 'c');
        paging.addPostParameter('s', 'm', pagingParams, "max_id", 456L);
        assertEquals(2, pagingParams.size());
        HttpParameter maxIdParam = pagingParams.get(1);
        assertEquals("max_id", maxIdParam.getName());
        assertEquals("456", maxIdParam.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddUnsupportedParameter() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = paging.asPostParameterList('s', 'c');
        paging.addPostParameter('s', 'x', pagingParams, "unsupported", 789L);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] pagingParamsArray = paging.asPostParameterArray('s', "count");
        assertEquals(0, pagingParamsArray.length);

        List<HttpParameter> pagingParamsList = paging.asPostParameterList('s', "count");
        pagingParamsList.add(new HttpParameter("since_id", "123"));
        pagingParamsArray = paging.asPostParameterArray('s', "count");
        assertEquals(1, pagingParamsArray.length);
        assertEquals("since_id", pagingParamsArray[0].getName());
        assertEquals("123", pagingParamsArray[0].getValue());

        pagingParamsList.add(new HttpParameter("max_id", "456"));
        pagingParamsArray = paging.asPostParameterArray('s', "count");
        assertEquals(2, pagingParamsArray.length);
        assertEquals("max_id", pagingParamsArray[1].getName());
        assertEquals("456", pagingParamsArray[1].getValue());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 2, 123L, 456L);
        assertEquals("Paging{page=1, count=2, sinceId=123, maxId=456}", paging.toString());
    }
}