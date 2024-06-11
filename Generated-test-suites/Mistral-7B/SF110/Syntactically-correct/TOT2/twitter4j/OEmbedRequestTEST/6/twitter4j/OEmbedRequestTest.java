package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Align;
import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;

public class OEmbedRequestTest {
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        oEmbedRequest.setMaxWidth(50);
        int maxWidth = oEmbedRequest.getMaxWidth();
        assertEquals(50, maxWidth);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        oEmbedRequest.setMaxWidth(-5);
    }

    @Test
    public void testSetNullRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        oEmbedRequest.setRelated(null);
        assertNull(oEmbedRequest.getRelated());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        oEmbedRequest.setLang(null);
    }

    @Test
    public void testEqualsDifferentObjects() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "www.example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(2L, "www.example.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testEqualsSameObject() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        assertTrue(oEmbedRequest.equals(oEmbedRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "www.example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "www.example.com");
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        String toString = oEmbedRequest.toString();
        String expected = "OEmbedRequest{statusId=1, url='www.example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, toString);
    }

    @Test
    public void testAppedParameter() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("id", "1", params);
        HttpParameter[] paramArray = oEmbedRequest.asHttpParameterArray();
        assertEquals(1, paramArray.length);
        assertEquals("id", paramArray[0].getName());
        assertEquals("1", paramArray[0].getValue());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "www.example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("id", "1", params);
        HttpParameter[] paramArray = oEmbedRequest.asHttpParameterArray();
        assertEquals(1, paramArray.length);
        assertEquals("id", paramArray[0].getName());
        assertEquals("1", paramArray[0].getValue());
    }
}