package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testAsPostParameterListEmpty() {
        Paging paging = new Paging();
        List<HttpParameter> result = paging.asPostParameterList();
        assertNull(result);
    }

    @Test
    public void testAsPostParameterListNullSupportedParams() {
        Paging paging = new Paging(1, 10);
        assertThrows(IllegalArgumentException.class, () -> paging.asPostParameterList((char[]) null));
    }

    @Test
    public void testAsPostParameterListNullPerPageParamName() {
        Paging paging = new Paging(1, 10);
        assertThrows(NullPointerException.class, () -> paging.asPostParameterList(Paging.SMCP, null));
    }

    @Test
    public void testAsPostParameterListWithInvalidParameters() {
        Paging paging = new Paging(1, 10);
        assertThrows(IllegalArgumentException.class, () -> paging.asPostParameterList(Paging.SMCP, "invalidParamName", -1));
        assertThrows(IllegalArgumentException.class, () -> paging.asPostParameterList(Paging.SMCP, "page", "invalidValue"));
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> result = paging.asPostParameterList();
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterArrayEmpty() {
        Paging paging = new Paging();
        HttpParameter[] result = paging.asPostParameterArray();
        assertNull(result);
    }

    @Test
    public void testAsPostParameterArrayNullSupportedParams() {
        Paging paging = new Paging(1, 10);
        assertThrows(IllegalArgumentException.class, () -> paging.asPostParameterArray((char[]) null));
    }

    @Test
    public void testAsPostParameterArrayNullPerPageParamName() {
        Paging paging = new Paging(1, 10);
        assertThrows(NullPointerException.class, () -> paging.asPostParameterArray(null, "perPageParamName"));
    }

    @Test
    public void testAsPostParameterArrayWithInvalidParameters() {
        Paging paging = new Paging(1, 10);
        assertThrows(IllegalArgumentException.class, () -> paging.asPostParameterArray(Paging.SMCP, "invalidParamName", -1));
        assertThrows(IllegalArgumentException.class, () -> paging.asPostParameterArray(Paging.SMCP, "page", "invalidValue"));
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10);
        HttpParameter[] result = paging.asPostParameterArray();
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterListWithCustomPerPageParamName() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "customPerPageParamName");
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> result = paging.asPostParameterList(new char[0], "count");
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10);
        HttpParameter[] result = paging.asPostParameterArray(new char[0], "count");
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterListWithCustomSupportedParams() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> result = paging.asPostParameterList(new char[]{'s', 'm', 'c'}, "count");
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterArrayWithCustomSupportedParams() {
        Paging paging = new Paging(1, 10);
        HttpParameter[] result = paging.asPostParameterArray(new char[]{'s', 'm', 'c'}, "count");
        assertNotNull(result);
    }
}