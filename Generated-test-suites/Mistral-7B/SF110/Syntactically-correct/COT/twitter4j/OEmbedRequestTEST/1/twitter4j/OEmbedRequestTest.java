package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("https://example.com", request.getUrl());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");

        request.setMaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.MaxWidth(300);
        assertEquals(300, request.getMaxWidth());

        request.setHideMedia(false);
        assertFalse(request.isHideMedia());

        request.HideMedia(true);
        assertTrue(request.isHideMedia());

        request.setHideThread(false);
        assertFalse(request.isHideThread());

        request.HideThread(true);
        assertTrue(request.isHideThread());

        request.setOmitScript(true);
        assertTrue(request.isOmitScript());

        request.omitScript(false);
        assertFalse(request.isOmitScript());

        List<Align> alignList = Arrays.asList(Align.LEFT, Align.CENTER, Align.RIGHT, Align.NONE);
        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        request.setAlign(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());

        request.align(Align.NONE);
        assertEquals(Align.NONE, request.getAlign());

        String[] relatedArray = new String[]{"related1", "related2", "related3"};
        request.setRelated(relatedArray);
        assertArrayEquals(relatedArray, request.getRelated());

        request.related(new String[]{"related4", "related5", "related6"});
        assertArrayEquals(new String[]{"related4", "related5", "related6"}, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);

        assertEquals("id", params[0].getName());
        assertEquals(Long.toString(request.getStatusId()), params[0].getValue());

        assertEquals("url", params[1].getName());
        assertEquals(request.getUrl(), params[1].getValue());

        assertEquals("maxwidth", params[2].getName());
        assertEquals(Integer.toString(request.getMaxWidth()), params[2].getValue());

        assertEquals("hide_media", params[3].getName());
        assertEquals(Boolean.toString(request.isHideMedia()), params[3].getValue());

        assertEquals("hide_thread", params[4].getName());
        assertEquals(Boolean.toString(request.isHideThread()), params[4].getValue());

        assertEquals("omit_script", params[5].getName());
        assertEquals(Boolean.toString(request.isOmitScript()), params[5].getValue());

        assertEquals("align", params[6].getName());
        assertEquals(request.getAlign().name().toLowerCase(), params[6].getValue());

        assertEquals("related", params[7].getName());
        assertEquals(StringUtil.join(request.getRelated()), params[7].getValue());

        assertEquals("lang", params[8].getName());
        assertEquals(request.getLang(), params[8].getValue());

        // Check null values
        OEmbedRequest emptyRequest = new OEmbedRequest(1L, "https://example.com");
        HttpParameter[] emptyParams = emptyRequest.asHttpParameterArray();
        assertEquals(11, emptyParams.length);
        assertNull(emptyParams[7].getValue());
        assertNull(emptyParams[8].getValue());

        // Check long value out of range
        OEmbedRequest longRequest = new OEmbedRequest(Long.MAX_VALUE, "https://example.com");
        HttpParameter[] longParams = longRequest.asHttpParameterArray();
        assertEquals(11, longParams.length);
        assertNull(longParams[2].getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNullName() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter(null, "value", params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNullValue() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("name", null, params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNegativeLongValue() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("name", -1L, params);
    }
}