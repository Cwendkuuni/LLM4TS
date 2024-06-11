package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 123L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
    }

    @Test
    public void testSetAndGetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testSetAndGetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());
    }

    @Test
    public void testSetAndGetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());
    }

    @Test
    public void testSetAndGetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
    }

    @Test
    public void testSetAndGetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        Align align = Align.RIGHT;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.getAlign());
    }

    @Test
    public void testSetAndGetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
    }

    @Test
    public void testSetAndGetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.RIGHT);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        List<HttpParameter> httpParameters = Arrays.asList(oEmbedRequest.asHttpParameterArray());

        assertEquals(13, httpParameters.size());
        assertEquals("id", httpParameters.get(0).getName());
        assertEquals("1", httpParameters.get(0).getValue());
        assertEquals("url", httpParameters.get(1).getName());
        assertEquals("https://example.com", httpParameters.get(1).getValue());
        assertEquals("maxwidth", httpParameters.get(2).getName());
        assertEquals("100", httpParameters.get(2).getValue());
        assertEquals("hide_media", httpParameters.get(3).getName());
        assertEquals("false", httpParameters.get(3).getValue());
        assertEquals("hide_thread", httpParameters.get(4).getName());
        assertEquals("true", httpParameters.get(4).getValue());
        assertEquals("omit_script", httpParameters.get(5).getName());
        assertEquals("true", httpParameters.get(5).getValue());
        assertEquals("align", httpParameters.get(6).getName());
        assertEquals(Align.RIGHT.name().toLowerCase(), httpParameters.get(6).getValue());
        assertEquals("related", httpParameters.get(7).getName());
        assertEquals("related1,related2", httpParameters.get(7).getValue());
        assertEquals("lang", httpParameters.get(8).getName());
        assertEquals("en", httpParameters.get(8).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNegativeStatusId() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("id", -1L, params);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendParameterNullUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, null);
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("url", "", params);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        assertNotEquals(null, oEmbedRequest);
    }

    @Test
    public void testEqualsSameObject() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = oEmbedRequest1;
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(2L, "https://example.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testEqualsSameValuesButDifferentInstances() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com");
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com");
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        String expectedString = "OEmbedRequest{statusId=1, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang=''}";
        assertEquals(expectedString, oEmbedRequest.toString());
    }
}