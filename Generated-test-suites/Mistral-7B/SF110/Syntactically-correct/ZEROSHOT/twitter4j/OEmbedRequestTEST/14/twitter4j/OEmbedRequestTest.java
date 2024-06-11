package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstuction() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertNull(oEmbedRequest.getMaxWidth());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSetAndGetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setMaxWidth(120);
        assertEquals(120, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(200);
        assertEquals(200, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testSetAndGetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertTrue(oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());

        oEmbedRequest.HideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());
    }

    @Test
    public void testSetAndGetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertTrue(oEmbedRequest.isHideThread());

        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());

        oEmbedRequest.HideThread(true);
        assertTrue(oEmbedRequest.isHideThread());
    }

    @Test
    public void testSetAndGetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertFalse(oEmbedRequest.isOmitScript());

        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());

        oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.isOmitScript());
    }

    @Test
    public void testSetAndGetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertNull(oEmbedRequest.getAlign());

        oEmbedRequest.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, oEmbedRequest.getAlign());

        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());

        oEmbedRequest.align(Align.NONE);
        assertNull(oEmbedRequest.getAlign());
    }

    @Test
    public void testSetAndGetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());

        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, oEmbedRequest.getRelated());
    }

    @Test
    public void testSetAndGetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertNull(oEmbedRequest.getLang());

        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());

        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] parameters = oEmbedRequest.asHttpParameterArray();

        assertEquals(12, parameters.length);

        String[] expectedNames = new String[]{"id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang"};
        String[] actualNames = new String[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            actualNames[i] = parameters[i].getName();
        }

        assertArrayEquals(expectedNames, actualNames);

        String expectedValues = new String[]{"1", "http://example.com", "0", "true", "true", "false", "NONE", null, null};
        String[] actualValues = new String[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            actualValues[i] = parameters[i].getValue();
        }

        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example2.com");

        List<OEmbedRequest> list = Arrays.asList(oEmbedRequest1, oEmbedRequest2, oEmbedRequest3, oEmbedRequest4, oEmbedRequest5);

        for (OEmbedRequest request : list) {
            for (OEmbedRequest otherRequest : list) {
                if (request != otherRequest) {
                    assertFalse(request.equals(otherRequest));
                } else {
                    assertTrue(request.equals(otherRequest));
                }
            }
        }

        assertFalse(oEmbedRequest1.equals(null));
        assertFalse(oEmbedRequest1.equals(new Object()));
        assertFalse(oEmbedRequest1.equals(new int[0]));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example2.com");

        List<OEmbedRequest> list = Arrays.asList(oEmbedRequest1, oEmbedRequest2, oEmbedRequest3, oEmbedRequest4, oEmbedRequest5);

        for (OEmbedRequest request : list) {
            for (OEmbedRequest otherRequest : list) {
                assertEquals(request.hashCode(), otherRequest.hashCode());
            }
        }

        assertNotSame(oEmbedRequest1.hashCode(), oEmbedRequest3.hashCode());
        assertNotSame(oEmbedRequest1.hashCode(), oEmbedRequest5.hashCode());
        assertNotSame(oEmbedRequest3.hashCode(), oEmbedRequest5.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");

        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        String actual = oEmbedRequest.toString();
        assertEquals(expected, actual);
    }
}