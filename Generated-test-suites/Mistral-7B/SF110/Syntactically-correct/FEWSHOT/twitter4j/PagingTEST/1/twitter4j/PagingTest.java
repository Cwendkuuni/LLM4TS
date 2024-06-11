package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class PagingTest {

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

        Paging paging1 = new Paging(1, 10, 1L, 100L);
        assertEquals(paging, paging1);
        assertEquals(paging.hashCode(), paging1.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersNegativeValues() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersNegativeLongValues() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testConstructors() {
        Paging emptyPaging = new Paging();
        Paging pagePaging = new Paging(1);
        Paging sinceIdPaging = new Paging(1L);
        Paging countPaging = new Paging(1, 10);
        Paging pageSinceIdPaging = new Paging(1, 1L);
        Paging pageCountSinceIdPaging = new Paging(1, 10, 1L, 100L);

        assertEquals(0, emptyPaging.getPage());
        assertEquals(0, emptyPaging.getCount());
        assertEquals(-1L, emptyPaging.getSinceId());
        assertEquals(-1L, emptyPaging.getMaxId());

        assertEquals(1, pagePaging.getPage());
        assertEquals(0, pagePaging.getCount());
        assertEquals(-1L, pagePaging.getSinceId());
        assertEquals(-1L, pagePaging.getMaxId());

        assertEquals(1, sinceIdPaging.getPage());
        assertEquals(0, sinceIdPaging.getCount());
        assertEquals(1L, sinceIdPaging.getSinceId());
        assertEquals(-1L, sinceIdPaging.getMaxId());

        assertEquals(1, countPaging.getPage());
        assertEquals(10, countPaging.getCount());
        assertEquals(-1L, countPaging.getSinceId());
        assertEquals(-1L, countPaging.getMaxId());

        assertEquals(1, pageSinceIdPaging.getPage());
        assertEquals(0, pageSinceIdPaging.getCount());
        assertEquals(1L, pageSinceIdPaging.getSinceId());
        assertEquals(-1L, pageSinceIdPaging.getMaxId());

        assertEquals(1, pageCountSinceIdPaging.getPage());
        assertEquals(10, pageCountSinceIdPaging.getCount());
        assertEquals(1L, pageCountSinceIdPaging.getSinceId());
        assertEquals(100L, pageCountSinceIdPaging.getMaxId());

        assertEquals(pageCountSinceIdPaging, pageCountSinceIdPaging.count(5));
        assertEquals(pageCountSinceIdPaging, pageCountSinceIdPaging.sinceId(2L));
        assertEquals(pageCountSinceIdPaging, pageCountSinceIdPaging.maxId(200L));
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> pagingParams = paging.asPostParameterList(Paging.SMCP, "count");
        assertNotNull(pagingParams);
        assertEquals(4, pagingParams.size());
        assertEquals("page", pagingParams.get(0).getName());
        assertEquals("1", pagingParams.get(0).getValue());
        assertEquals("count", pagingParams.get(1).getName());
        assertEquals("10", pagingParams.get(1).getValue());
        assertEquals("since_id", pagingParams.get(2).getName());
        assertEquals("1", pagingParams.get(2).getValue());
        assertEquals("max_id", pagingParams.get(3).getName());
        assertEquals("100", pagingParams.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] pagingParams = paging.asPostParameterArray(Paging.SMCP, "count");
        assertNotNull(pagingParams);
        assertEquals(4, pagingParams.length);
        assertEquals("page", pagingParams[0].getName());
        assertEquals("1", pagingParams[0].getValue());
        assertEquals("count", pagingParams[1].getName());
        assertEquals("10", pagingParams[1].getValue());
        assertEquals("since_id", pagingParams[2].getName());
        assertEquals("1", pagingParams[2].getValue());
        assertEquals("max_id", pagingParams[3].getName());
        assertEquals("100", pagingParams[3].getValue());
    }

    @Test
    public void testAddPostParameter() {
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 1L);
        assertEquals(1, pagingParams.size());
        assertEquals("since_id", pagingParams.get(0).getName());
        assertEquals("1", pagingParams.get(0).getValue());

        Paging.addPostParameter(Paging.SMCP, 'm', pagingParams, "max_id", 100L);
        assertEquals(2, pagingParams.size());
        assertEquals("max_id", pagingParams.get(1).getName());
        assertEquals("100", pagingParams.get(1).getValue());

        Paging.addPostParameter(Paging.SMCP, 'c', pagingParams, "count", 10);
        assertEquals(3, pagingParams.size());
        assertEquals("count", pagingParams.get(2).getName());
        assertEquals("10", pagingParams.get(2).getValue());

        Paging.addPostParameter(Paging.SMCP, 'p', pagingParams, "page", 1);
        assertEquals(4, pagingParams.size());
        assertEquals("page", pagingParams.get(3).getName());
        assertEquals("1", pagingParams.get(3).getValue());

        Paging.addPostParameter(Paging.SMCP, 'q', pagingParams, "query", "test");
        assertEquals(5, pagingParams.size());
        assertEquals("query", pagingParams.get(4).getName());
        assertEquals("test", pagingParams.get(4).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddUnsupportedPostParameter() {
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging.addPostParameter(Paging.SMCP, 'u', pagingParams, "unsupported_param", 100L);
    }
}