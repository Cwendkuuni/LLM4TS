package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testConstructors() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        Paging page1 = new Paging(1);
        assertEquals(1, page1.getPage());
        assertEquals(-1, page1.getCount());
        assertEquals(-1L, page1.getSinceId());
        assertEquals(-1L, page1.getMaxId());

        Paging sinceId1 = new Paging(-1L);
        assertEquals(-1, sinceId1.getPage());
        assertEquals(-1, sinceId1.getCount());
        assertEquals(-1L, sinceId1.getSinceId());
        assertEquals(-1L, sinceId1.getMaxId());

        Paging page1Count1 = new Paging(1, 1);
        assertEquals(1, page1Count1.getPage());
        assertEquals(1, page1Count1.getCount());
        assertEquals(-1L, page1Count1.getSinceId());
        assertEquals(-1L, page1Count1.getMaxId());

        Paging page1Count1SinceId1 = new Paging(1, 1, -1L);
        assertEquals(1, page1Count1SinceId1.getPage());
        assertEquals(1, page1Count1SinceId1.getCount());
        assertEquals(-1L, page1Count1SinceId1.getSinceId());
        assertEquals(-1L, page1Count1SinceId1.getMaxId());

        Paging page1Count1SinceIdMaxId = new Paging(1, 1, -1L, 1L);
        assertEquals(1, page1Count1SinceIdMaxId.getPage());
        assertEquals(1, page1Count1SinceIdMaxId.getCount());
        assertEquals(-1L, page1Count1SinceIdMaxId.getSinceId());
        assertEquals(1L, page1Count1SinceIdMaxId.getMaxId());
    }

    @Test
    public void testSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());

        paging.setCount(1);
        assertEquals(1, paging.getCount());

        paging.setSinceId(-1L);
        assertEquals(-1L, paging.getSinceId());

        paging.setMaxId(-1L);
        assertEquals(-1L, paging.getMaxId());

        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(3);
        assertEquals(3, paging.getCount());

        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());

        paging.setMaxId(2L);
        assertEquals(2L, paging.getMaxId());

        Paging paging2 = new Paging(1, 1, 1L, 2L);
        paging.setPage(3);
        assertEquals(3, paging.getPage());
        assertNotSame(paging2, paging);

        paging2.setCount(3);
        assertEquals(3, paging2.getCount());
        assertEquals(3, paging.getCount());

        paging2.setSinceId(2L);
        assertEquals(2L, paging2.getSinceId());
        assertEquals(2L, paging.getSinceId());

        paging2.setMaxId(3L);
        assertEquals(3L, paging2.getMaxId());
        assertEquals(3L, paging.getMaxId());
    }

    @Test
    public void testEqualsHashCodeToString() {
        Paging paging1 = new Paging(1, 1, 1L, 2L);
        Paging paging2 = new Paging(1, 1, 1L, 2L);
        assertEquals(paging1, paging2);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertEquals(paging1.toString(), paging2.toString());

        Paging paging3 = new Paging(1, 1, 1L, 3L);
        assertNotEquals(paging1, paging3);
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging1.toString(), paging3.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 1, 1L, 2L);
        List<HttpParameter> pagingParams = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(4, pagingParams.size());
        assertEquals("count", pagingParams.get(2).getName());
        assertEquals("1", pagingParams.get(2).getValue());

        pagingParams = paging.asPostParameterList(new char[0], "count");
        assertEquals(0, pagingParams.size());

        pagingParams = paging.asPostParameterList(Paging.SMCP, "per_page");
        assertEquals(0, pagingParams.size());

        pagingParams = paging.asPostParameterList(new char[]{'s'}, "since_id");
        assertEquals(1, pagingParams.size());
        assertEquals("since_id", pagingParams.get(0).getName());
        assertEquals("1", pagingParams.get(0).getValue());

        pagingParams = paging.asPostParameterList(new char[]{'s', 'm'}, "since_id", "max_id");
        assertEquals(3, pagingParams.size());
        assertEquals("since_id", pagingParams.get(0).getName());
        assertEquals("1", pagingParams.get(0).getValue());
        assertEquals("max_id", pagingParams.get(1).getName());
        assertEquals("2", pagingParams.get(1).getValue());
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging(1, 1, 1L, 2L);
        List<HttpParameter> pagingParams = null;
        try {
            pagingParams = paging.asPostParameterList(new char[]{'x'}, "x");
        } catch (IllegalStateException e) {
            pagingParams = Arrays.asList(e.getCause().getCause());
        }
        assertEquals(1, pagingParams.size());
        assertEquals("Paging parameter [x] is not supported with this operation.", pagingParams.get(0).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 1, 1L, 2L);
        HttpParameter[] pagingParams = paging.asPostParameterArray(Paging.SMCP, "count");
        assertEquals(4, pagingParams.length);
        assertEquals("count", pagingParams[2].getName());
        assertEquals("1", pagingParams[2].getValue());

        pagingParams = paging.asPostParameterArray(new char[0], "count");
        assertEquals(0, pagingParams.length);

        pagingParams = paging.asPostParameterArray(Paging.SMCP, "per_page");
        assertEquals(0, pagingParams.length);

        pagingParams = paging.asPostParameterArray(new char[]{'s'}, "since_id");
        assertEquals(1, pagingParams.length);
        assertEquals("since_id", pagingParams[0].getName());
        assertEquals("1", pagingParams[0].getValue());

        pagingParams = paging.asPostParameterArray(new char[]{'s', 'm'}, "since_id", "max_id");
        assertEquals(3, pagingParams.length);
        assertEquals("since_id", pagingParams[0].getName());
        assertEquals("1", pagingParams[0].getValue());
        assertEquals("max_id", pagingParams[1].getName());
        assertEquals("2", pagingParams[1].getValue());
    }

    @Test
    public void testAsPostParameterArrayWithUnsupportedParam() {
        Paging paging = new Paging(1, 1, 1L, 2L);
        HttpParameter[] pagingParams = null;
        try {
            pagingParams = paging.asPostParameterArray(new char[]{'x'}, "x");
        } catch (IllegalStateException e) {
            pagingParams = paging.asPostParameterArray(Paging.SMCP, "x");
        }
        assertEquals(1, pagingParams.length);
        assertEquals("Paging parameter [x] is not supported with this operation.", pagingParams[0].getValue());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 1L);
        assertEquals(1, pagingParams.size());
        assertEquals("since_id", pagingParams.get(0).getName());
        assertEquals("1", pagingParams.get(0).getValue());

        paging = new Paging(1);
        pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'm', pagingParams, "max_id", 2L);
        assertEquals(1, pagingParams.size());
        assertEquals("max_id", pagingParams.get(0).getName());
        assertEquals("2", pagingParams.get(0).getValue());

        paging = new Paging(1, 1);
        pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'c', pagingParams, "count", 3);
        assertEquals(1, pagingParams.size());
        assertEquals("count", pagingParams.get(0).getName());
        assertEquals("3", pagingParams.get(0).getValue());

        paging = new Paging(1, 1, 1L, 2L);
        pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'p', pagingParams, "page", 4);
        assertEquals(1, pagingParams.size());
        assertEquals("page", pagingParams.get(0).getName());
        assertEquals("4", pagingParams.get(0).getValue());

        paging = new Paging(1, 1, 1L, 2L);
        pagingParams = new ArrayList<>();
        assertThrows(IllegalStateException.class, () -> {
            paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "x", 5L);
        });

        paging = new Paging(1, 1, 1L, 2L);
        pagingParams = Arrays.asList(new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2"));
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 3L);
        assertEquals(3, pagingParams.size());
        assertEquals("since_id", pagingParams.get(2).getName());
        assertEquals("3", pagingParams.get(2).getValue());
    }

    @Test
    public void testGetters() {
        Paging paging = new Paging(1, 1, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());

        Paging paging2 = new Paging(2, 2, 2L, 3L);
        assertEquals(2, paging2.getPage());
        assertEquals(2, paging2.getCount());
        assertEquals(2L, paging2.getSinceId());
        assertEquals(3L, paging2.getMaxId());
    }

    @Test
    public void testSettersAndGetters() {
        Paging paging = new Paging(1, 1, 1L, 2L);
        paging.setPage(2);
        assertEquals(2, paging.getPage());

        paging.setCount(2);
        assertEquals(2, paging.getCount());

        paging.setSinceId(2L);
        assertEquals(2L, paging.getSinceId());

        paging.setMaxId(3L);
        assertEquals(3L, paging.getMaxId());

        paging = new Paging(1, 1, 1L, 2L);
        paging.count(2);
        assertEquals(2, paging.getCount());

        paging = new Paging(1, 1, 1L, 2L);
        paging.sinceId(2L);
        assertEquals(2L, paging.getSinceId());

        paging = new Paging(1, 1, 1L, 2L);
        paging.maxId(3L);
        assertEquals(3L, paging.getMaxId());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 1, 1L, 2L);
        assertEquals("Paging{page=1, count=1, sinceId=1, maxId=2}", paging.toString());
    }
}