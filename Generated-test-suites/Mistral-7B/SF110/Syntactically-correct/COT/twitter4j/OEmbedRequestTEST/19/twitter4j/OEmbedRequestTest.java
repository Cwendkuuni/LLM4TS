package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");

        oEmbedRequest.setMaxWidth(10);
        assertEquals(10, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(20);
        assertEquals(20, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());

        oEmbedRequest.HideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());

        oEmbedRequest.HideThread(true);
        assertTrue(oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());

        oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.isOmitScript());

        oEmbedRequest.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, oEmbedRequest.getAlign());

        oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.CENTER, oEmbedRequest.getAlign());

        oEmbedRequest.setRelated(new String[]{"rel1", "rel2"});
        assertArrayEquals(new String[]{"rel1", "rel2"}, oEmbedRequest.getRelated());

        oEmbedRequest.related(new String[0]);
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());

        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());

        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(10);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.LEFT);
        oEmbedRequest.setRelated(new String[]{"rel1", "rel2"});
        oEmbedRequest.setLang("en");

        List<HttpParameter> httpParameters = Arrays.asList(oEmbedRequest.asHttpParameterArray());

        assertEquals(12, httpParameters.size());
        assertEquals("id", httpParameters.get(0).getName());
        assertEquals("1", httpParameters.get(0).getValue());
        assertEquals("url", httpParameters.get(1).getName());
        assertEquals("https://example.com", httpParameters.get(1).getValue());
        assertEquals("maxwidth", httpParameters.get(2).getName());
        assertEquals("10", httpParameters.get(2).getValue());
        assertEquals("hide_media", httpParameters.get(3).getName());
        assertEquals("false", httpParameters.get(3).getValue());
        assertEquals("hide_thread", httpParameters.get(4).getName());
        assertEquals("true", httpParameters.get(4).getValue());
        assertEquals("omit_script", httpParameters.get(5).getName());
        assertEquals("true", httpParameters.get(5).getValue());
        assertEquals("align", httpParameters.get(6).getName());
        assertEquals("left", httpParameters.get(6).getValue());
        assertEquals("related", httpParameters.get(7).getName());
        assertEquals("rel1,rel2", httpParameters.get(7).getValue());
        assertEquals("lang", httpParameters.get(8).getName());
        assertEquals("en", httpParameters.get(8).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNegativeStatusId() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(-1L, "https://example.com");
        oEmbedRequest.appendParameter("id", -1L, Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendParameterNullUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, null);
        oEmbedRequest.appendParameter("url", null, Collections.emptyList());
    }
}