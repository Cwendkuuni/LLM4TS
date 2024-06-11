package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.Align;
import twitter4j.OEmbedRequest;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
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
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(120);
        assertEquals(120, oEmbedRequest.getMaxWidth());
        OEmbedRequest maxWidthOEmbedRequest = oEmbedRequest.MaxWidth(200);
        assertEquals(200, maxWidthOEmbedRequest.getMaxWidth());
    }

    @Test
    public void testSetAndGetHideMedia() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());
        OEmbedRequest hideMediaOEmbedRequest = oEmbedRequest.HideMedia(true);
        assertTrue(hideMediaOEmbedRequest.isHideMedia());
    }

    @Test
    public void testSetAndGetHideThread() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());
        OEmbedRequest hideThreadOEmbedRequest = oEmbedRequest.HideThread(true);
        assertTrue(hideThreadOEmbedRequest.isHideThread());
    }

    @Test
    public void testSetAndGetOmitScript() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
        OEmbedRequest omitScriptOEmbedRequest = oEmbedRequest.omitScript(false);
        assertFalse(omitScriptOEmbedRequest.isOmitScript());
    }

    @Test
    public void testSetAndGetAlign() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.getAlign());
        Align leftAlignOEmbedRequest = oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.CENTER, leftAlignOEmbedRequest.getAlign());
    }

    @Test
    public void testSetAndGetRelated() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
        OEmbedRequest relatedOEmbedRequest = oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, relatedOEmbedRequest.getRelated());
    }

    @Test
    public void testSetAndGetLang() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());
        OEmbedRequest langOEmbedRequest = oEmbedRequest.lang("fr");
        assertEquals("fr", langOEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "http://example.com";        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(120);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.RIGHT);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        String lang = "en";
        oEmbedRequest.setLang(lang);
        HttpParameter[] parameters = oEmbedRequest.asHttpParameterArray();
        List<HttpParameter> expectedParameters = Arrays.asList(
            new HttpParameter("id", Long.toString(statusId)),
            new HttpParameter("url", url),
            new HttpParameter("maxwidth", Integer.toString(120)),
            new HttpParameter("hide_media", Boolean.toString(false)),
            new HttpParameter("hide_thread", Boolean.toString(false)),
            new HttpParameter("omit_script", Boolean.toString(true)),
            new HttpParameter("align", Align.RIGHT.name().toLowerCase()),
            new HttpParameter("related", StringUtil.join(related, ",")),
            new HttpParameter("lang", lang)
        );
        assertEquals(expectedParameters.size(), parameters.length);
        for (int i = 0; i < expectedParameters.size(); i++) {
            assertEquals(expectedParameters.get(i).getName(), parameters[i].getName());
            assertEquals(expectedParameters.get(i).getValue(), parameters[i].getValue());
        }
    }
}