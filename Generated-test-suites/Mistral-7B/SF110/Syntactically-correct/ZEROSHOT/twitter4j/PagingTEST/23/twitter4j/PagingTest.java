import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testNewInstance() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        Paging pageOnlyPaging = new Paging(1);
        assertEquals(1, pageOnlyPaging.getPage());
        assertEquals(-1, pageOnlyPaging.getCount());
        assertEquals(-1L, pageOnlyPaging.getSinceId());
        assertEquals(-1L, pageOnlyPaging.getMaxId());

        Paging sinceIdOnlyPaging = new Paging(-1L);
        assertEquals(-1, sinceIdOnlyPaging.getPage());
        assertEquals(-1, sinceIdOnlyPaging.getCount());
        assertEquals(-1L, sinceIdOnlyPaging.getSinceId());
        assertEquals(-1L, sinceIdOnlyPaging.getMaxId());

        Paging pageCountPaging = new Paging(1, 10);
        assertEquals(1, pageCountPaging.getPage());
        assertEquals(10, pageCountPaging.getCount());
        assertEquals(-1L, pageCountPaging.getSinceId());
        assertEquals(-1L, pageCountPaging.getMaxId());

        Paging pageCountSinceIdPaging = new Paging(1, 10, 1234567890123L);
        assertEquals(1, pageCountSinceIdPaging.getPage());
        assertEquals(10, pageCountSinceIdPaging.getCount());
        assertEquals(1234567890123L, pageCountSinceIdPaging.getSinceId());
        assertEquals(-1L, pageCountSinceIdPaging.getMaxId());

        Paging pageCountMaxIdPaging = new Paging(1, 10, 1234567890123L, 9876543210987L);
        assertEquals(1, pageCountMaxIdPaging.getPage());
        assertEquals(10, pageCountMaxIdPaging.getCount());
        assertEquals(1234567890123L, pageCountMaxIdPaging.getSinceId());
        assertEquals(9876543210987L, pageCountMaxIdPaging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1234567890123L);
        paging.setMaxId(9876543210987L);

        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1234567890123L, paging.getSinceId());
        assertEquals(9876543210987L, paging.getMaxId());

        paging.setPage(-1);
        paging.setCount(-1);
        paging.setSinceId(-1L);
        paging.setMaxId(-1L);

        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging();

        assertFalse(paging1.equals(null));
        assertFalse(paging1.equals(new Object()));
        assertFalse(paging1.equals(paging2));

        paging1.setPage(1);
        paging1.setCount(10);
        paging1.setSinceId(1234567890123L);
        paging1.setMaxId(9876543210987L);

        Paging paging3 = new Paging();
        paging3.setPage(1);
        paging3.setCount(10);
        paging3.setSinceId(1234567890123L);
        paging3.setMaxId(9876543210987L);

        assertTrue(paging1.equals(paging3));

        Paging paging4 = new Paging();
        paging4.setPage(1);
        paging4.setCount(11);
        paging4.setSinceId(1234567890123L);
        paging4.setMaxId(9876543210987L);

        assertFalse(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging();

        int hashCode1 = paging1.hashCode();
        int hashCode2 = paging2.hashCode();

        paging1.setPage(1);
        paging1.setCount(10);
        paging1.setSinceId(1234567890123L);
        paging1.setMaxId(9876543210987L);

        int hashCode3 = paging1.hashCode();

        Paging paging3 = new Paging();
        paging3.setPage(1);
        paging3.setCount(10);
        paging3.setSinceId(1234567890123L);
        paging3.setMaxId(9876543210987L);

        int hashCode4 = paging3.hashCode();

        assertEquals(hashCode1, hashCode4);

        Paging paging4 = new Paging();
        paging4.setPage(1);
        paging4.setCount(11);
        paging4.setSinceId(1234567890123L);
        paging4.setMaxId(9876543210987L);

        int hashCode5 = paging4.hashCode();

        assertNotEquals(hashCode3, hashCode5);
    }

    @Test
    public void testToString() {
        Paging paging = new Paging();
        assertEquals("Paging{page=0, count=0, sinceId=-1, maxId=-1}", paging.toString());

        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1234567890123L);
        paging.setMaxId(9876543210987L);

        assertEquals("Paging{page=1, count=10, sinceId=1234567890123, maxId=9876543210987}", paging.toString());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();

        HttpParameter[] expected = new HttpParameter[0];
        HttpParameter[] result = paging.asPostParameterArray();

        assertArrayEquals(expected, result);

        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1234567890123L);
        paging.setMaxId(9876543210987L);

        HttpParameter[] supportedParams = Paging.SMCP;
        expected = paging.asPostParameterArray(supportedParams, "count");

        List<HttpParameter> pagingParams = new ArrayList<>();
        this.addPostParameter(supportedParams, 's', pagingParams, "since_id", paging.getSinceId());
        this.addPostParameter(supportedParams, 'm', pagingParams, "max_id", paging.getMaxId());
        this.addPostParameter(supportedParams, 'c', pagingParams, "count", paging.getCount());
        this.addPostParameter(supportedParams, 'p', pagingParams, "page", paging.getPage());

        HttpParameter[] result2 = paging.asPostParameterArray();

        assertArrayEquals(pagingParams.toArray(new HttpParameter[0]), result2);
        assertArrayEquals(expected, result2);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();

        List<HttpParameter> expected = new ArrayList<>();

        List<HttpParameter> result = paging.asPostParameterList();

        assertEquals(expected, result);

        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(1234567890123L);
        paging.setMaxId(9876543210987L);

        List<HttpParameter> supportedParams = Arrays.asList(new HttpParameter('s', "since_id"), new HttpParameter('m', "max_id"), new HttpParameter('c', "count"), new HttpParameter('p', "page"));
        List<HttpParameter> result2 = paging.asPostParameterList(supportedParams.toArray(new HttpParameter[0]), "count");

        assertEquals(4, result2.size());

        for (int i = 0; i < 4; i++) {
            HttpParameter expectedParam = supportedParams.get(i);
            HttpParameter resultParam = result2.get(i);

            assertEquals(expectedParam.getName(), resultParam.getName());
            assertEquals(expectedParam.getValue(), resultParam.getValue());
        }

        List<HttpParameter> pagingParams = new ArrayList<>();
        this.addPostParameter(supportedParams.toArray(new HttpParameter[0]), 's', pagingParams, "since_id", paging.getSinceId());
        this.addPostParameter(supportedParams.toArray(new HttpParameter[0]), 'm', pagingParams, "max_id", paging.getMaxId());
        this.addPostParameter(supportedParams.toArray(new HttpParameter[0]), 'c', pagingParams, "count", paging.getCount());
        this.addPostParameter(supportedParams.toArray(new HttpParameter[0]), 'p', pagingParams, "page", paging.getPage());

        List<HttpParameter> result3 = paging.asPostParameterList();

        assertEquals(pagingParams, result3);
    }

    private void addPostParameter(HttpParameter[] supportedParams, char paramKey, List<HttpParameter> pagingParams, String paramName, long paramValue) {
        for (HttpParameter param : supportedParams) {
            if (param.getName().charAt(0) == paramKey) {
                pagingParams.add(new HttpParameter(paramName, String.valueOf(paramValue)));
                return;
            }
        }
        throw new AssertionError("Param key not found in supported params: " + paramKey);
    }
}