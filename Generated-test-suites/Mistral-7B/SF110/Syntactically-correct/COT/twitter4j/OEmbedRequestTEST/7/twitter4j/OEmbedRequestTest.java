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
    public void testOEmbedRequestConstructor() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        int maxWidth = 200;
        oEmbedRequest.setMaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(300);
        assertEquals(300, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);

        oEmbedRequest.HideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);

        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);

        oEmbedRequest.HideThread(true);
        assertTrue(oEmbedRequest.hideThread);

        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);

        oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.omitScript);

        Align align = Align.CENTER;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.getAlign());

        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());

        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, oEmbedRequest.getRelated());

        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());

        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testAppendParameter() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("id", 1L, params);
        assertEquals(1, params.size());
        HttpParameter param = params.get(0);
        assertEquals("id", param.getName());
        assertEquals("1", param.getValue());

        oEmbedRequest.appendParameter("url", "https://example.com/status/2", params);
        assertEquals(2, params.size());
        param = params.get(1);
        assertEquals("url", param.getName());
        assertEquals("https://example.com/status/2", param.getValue());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "https://example.com/status/2");
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "https://example.com/status/1");

        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest2.setHideMedia(false);

        oEmbedRequest1.setHideThread(false);
        oEmbedRequest2.setHideThread(false);

        oEmbedRequest1.setLang("en");
        oEmbedRequest2.setLang("en");

        oEmbedRequest1.setRelated(new String[]{"related1"});
        oEmbedRequest2.setRelated(new String[]{"related1"});

        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest4));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest5));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "https://example.com/status/2");

        int hashCode1 = oEmbedRequest1.hashCode();
        int hashCode2 = oEmbedRequest2.hashCode();
        int hashCode3 = oEmbedRequest3.hashCode();
        int hashCode4 = oEmbedRequest4.hashCode();

        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest2.setHideMedia(false);

        oEmbedRequest1.setHideThread(false);
        oEmbedRequest2.setHideThread(false);

        oEmbedRequest1.setLang("en");
        oEmbedRequest2.setLang("en");

        oEmbedRequest1.setRelated(new String[]{"related1"});
        oEmbedRequest2.setRelated(new String[]{"related1"});

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        String expected = "OEmbedRequest{statusId=1, url='https://example.com/status/1', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());

        oEmbedRequest.setMaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");

        expected = "OEmbedRequest{statusId=1, url='https://example.com/status/1', maxWidth=200, hideMedia=false, hideThread=false, omitScript=false, align=NONE, related=[related1, related2], lang='en'}";
        assertEquals(expected, oEmbedRequest.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersNegativeValues() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");

        oEmbedRequest.setMaxWidth(-1);

        oEmbedRequest.setHideMedia(-1);
        oEmbedRequest.HideMedia(-1);

        oEmbedRequest.setHideThread(-1);
        oEmbedRequest.HideThread(-1);

        oEmbedRequest.setLang(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersNullValues() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");

        oEmbedRequest.setRelated(null);
        oEmbedRequest.related(null);
    }
}