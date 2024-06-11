package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertNull(oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        int maxWidth = 100;
        oEmbedRequest.setMaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

        boolean hideMedia = false;
        oEmbedRequest.setHideMedia(hideMedia);
        assertEquals(hideMedia, oEmbedRequest.isHideMedia());

        boolean hideThread = false;
        oEmbedRequest.setHideThread(hideThread);
        assertEquals(hideThread, oEmbedRequest.isHideThread());

        boolean omitScript = true;
        oEmbedRequest.setOmitScript(omitScript);
        assertEquals(omitScript, oEmbedRequest.isOmitScript());

        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.CENTER);
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        String lang = "en";
        oEmbedRequest.setLang(lang);

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(13, httpParameters.length);
        assertEquals("id", httpParameters[0].getName());
        assertEquals(String.valueOf(statusId), httpParameters[0].getValue());
        assertEquals("url", httpParameters[1].getName());
        assertEquals(url, httpParameters[1].getValue());
        assertEquals("maxwidth", httpParameters[2].getName());
        assertEquals(String.valueOf(100), httpParameters[2].getValue());
        assertEquals("hide_media", httpParameters[3].getName());
        assertEquals(String.valueOf(false), httpParameters[3].getValue());
        assertEquals("hide_thread", httpParameters[4].getName());
        assertEquals(String.valueOf(false), httpParameters[4].getValue());
        assertEquals("omit_script", httpParameters[5].getName());
        assertEquals(String.valueOf(true), httpParameters[5].getValue());
        assertEquals("align", httpParameters[6].getName());
        assertEquals(Align.CENTER.name().toLowerCase(), httpParameters[6].getValue());
        if (related != null) {
            assertEquals("related", httpParameters[7].getName());
            assertEquals(StringUtil.join(related), httpParameters[7].getValue());
        } else {
            assertNull(httpParameters[7]);
        }
        assertEquals("lang", httpParameters[8].getName());
        assertEquals(lang, httpParameters[8].getValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "https://example.com";
        long statusId2 = 2L;
        String url2 = "https://example.com";
        long statusId3 = 1L;
        String url3 = "https://example.com";
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId3, url3);

        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId1, url1);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest4));
        assertTrue(oEmbedRequest4.hashCode() == oEmbedRequest1.hashCode());

        assertFalse(oEmbedRequest1.equals(null));
        assertFalse(oEmbedRequest1.equals(new Object()));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(statusId1, url1);
        oEmbedRequest5.setMaxWidth(100);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest5));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest5.hashCode());

        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(statusId1, url1);
        oEmbedRequest6.setRelated(new String[]{"related1"});
        assertFalse(oEmbedRequest1.equals(oEmbedRequest6));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest6.hashCode());

        OEmbedRequest oEmbedRequest7 = new OEmbedRequest(statusId1, url1);
        oEmbedRequest7.setLang("fr");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest7));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest7.hashCode());
    }
}