package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "http://example.com";
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
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        int maxWidth = 10;
        oEmbedRequest.setMaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

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

        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, oEmbedRequest.getRelated());

        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());

        oEmbedRequest.lang("es");
        assertEquals("es", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        List<HttpParameter> httpParameters = Arrays.asList(oEmbedRequest.asHttpParameterArray());

        assertEquals(12, httpParameters.size());
        assertEquals("id", httpParameters.get(0).getName());
        assertEquals(Long.toString(statusId), httpParameters.get(0).getValue());

        assertEquals("url", httpParameters.get(1).getName());
        assertEquals(url, httpParameters.get(1).getValue());

        assertEquals("maxwidth", httpParameters.get(2).getName());
        assertEquals("0", httpParameters.get(2).getValue()); // default value

        HttpParameter hideMediaHttpParameter = httpParameters.get(3);
        assertTrue(hideMediaHttpParameter.getName().equals("hide_media") || hideMediaHttpParameter.getName().equals("hide_media"));
        assertEquals("true", hideMediaHttpParameter.getValue()); // default value

        HttpParameter hideThreadHttpParameter = httpParameters.get(4);
        assertTrue(hideThreadHttpParameter.getName().equals("hide_thread") || hideThreadHttpParameter.getName().equals("hide_thread"));
        assertEquals("true", hideThreadHttpParameter.getValue()); // default value

        HttpParameter omitScriptHttpParameter = httpParameters.get(5);
        assertTrue(omitScriptHttpParameter.getName().equals("omit_script") || omitScriptHttpParameter.getName().equals("omit_script"));
        assertEquals("false", omitScriptHttpParameter.getValue()); // default value

        HttpParameter alignHttpParameter = httpParameters.get(6);
        assertEquals("align", alignHttpParameter.getName());
        assertEquals(Align.NONE.name().toLowerCase(), alignHttpParameter.getValue()); // default value

        if (oEmbedRequest.related.length > 0) {
            HttpParameter relatedHttpParameter = httpParameters.get(7);
            assertEquals("related", relatedHttpParameter.getName());
            assertEquals(StringUtil.join(oEmbedRequest.related), relatedHttpParameter.getValue());
        } else {
            assertEquals(0, httpParameters.size());
        }

        HttpParameter langHttpParameter = httpParameters.get(8);
        assertEquals("lang", langHttpParameter.getName());
        assertNull(langHttpParameter.getValue()); // default value

        assertEquals(9, httpParameters.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxWidth() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullUrl() {
        long statusId = 1L;
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, null);
    }
}