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
    public void testOEmbedRequestInstance() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertNotNull(request);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        int maxWidth = 200;
        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

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

        Align align = Align.LEFT;
        request.setAlign(align);
        assertEquals(align, request.getAlign());

        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        String lang = "en";
        request.setLang(lang);
        assertEquals(lang, request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(13, params.length);
        List<String> expectedParams = Arrays.asList("id", String.valueOf(statusId),
                "url", url, "maxwidth", "200",
                "hide_media", "false",
                "hide_thread", "false",
                "omit_script", "true",
                "align", Align.LEFT.name().toLowerCase(),
                "related", Arrays.toString(related),
                "lang", "en");

        for (int i = 0; i < params.length; i++) {
            assertEquals(expectedParams.get(i), params[i].getName() + "=" + params[i].getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        long statusId = 1L;
        OEmbedRequest request = new OEmbedRequest(statusId, null);
    }

}