package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, oEmbedRequest.getStatusId());
        assertEquals("http://example.com", oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.getMaxWidth());
        oEmbedRequest.HideMedia();
        assertFalse(oEmbedRequest.isHideMedia());
        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());
        String[] related = new String[]{"test1", "test2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
        oEmbedRequest.lang("en");
        assertEquals("en", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(12, httpParameters.length);
        HttpParameter idParameter = findParameterByName(httpParameters, "id");
        assertEquals(1L, Long.parseLong(idParameter.getValue()));
        HttpParameter urlParameter = findParameterByName(httpParameters, "url");
        assertEquals("http://example.com", urlParameter.getValue());
        // Test other parameters
        HttpParameter maxWidthParameter = findParameterByName(httpParameters, "maxwidth");
        assertEquals("100", maxWidthParameter.getValue());
        // ... (test other parameters similarly)
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest7 = new OEmbedRequest(1L, "http://example2.com");

        // Test equality
        assertTrue(oEmbedRequest1.equals(oEmbedRequest1));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest4));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest5));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest6));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
        assertFalse(oEmbedRequest1.equals(null));
        assertFalse(oEmbedRequest1.equals(new Object()));

        // Test hashcode
        int hashcode1 = oEmbedRequest1.hashCode();
        int hashcode2 = oEmbedRequest2.hashCode();
        int hashcode3 = oEmbedRequest3.hashCode();
        int hashcode4 = oEmbedRequest4.hashCode();
        int hashcode5 = oEmbedRequest5.hashCode();
        int hashcode6 = oEmbedRequest6.hashCode();
        int hashcode7 = oEmbedRequest7.hashCode();
        assertNotSame(hashcode1, hashcode2);
        assertNotSame(hashcode1, hashcode3);
        assertNotSame(hashcode1, hashcode4);
        assertNotSame(hashcode1, hashcode5);
        assertNotSame(hashcode1, hashcode6);
        assertNotSame(hashcode1, hashcode7);
        assertTrue(hashcode1 == hashcode4);
        assertTrue(hashcode1 == hashcode5);
        assertTrue(hashcode1 == hashcode6);
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }

    private HttpParameter findParameterByName(HttpParameter[] httpParameters, String name) {
        for (HttpParameter httpParameter : httpParameters) {
            if (name.equals(httpParameter.getName())) {
                return httpParameter;
            }
        }
        return null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNegativeStatusId() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(-1L, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNullUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNegativeMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendParameterNullAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setAlign(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendParameterNullRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setRelated(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendParameterNullLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setLang(null);
    }
}