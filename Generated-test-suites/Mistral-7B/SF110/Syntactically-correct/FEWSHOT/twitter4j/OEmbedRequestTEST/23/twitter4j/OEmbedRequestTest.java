package twitter4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
        long statusId = 123;
        String url = "https://example.com/statuses/123";
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
    public void testSettersAndGetters() {
        long statusId = 123;
        String url = "https://example.com/statuses/123";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(hideMedia);
        assertEquals(hideMedia, oEmbedRequest.isHideMedia());

        oEmbedRequest.HideMedia(hideMedia);
        assertEquals(hideMedia, oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(hideThread);
        assertEquals(hideThread, oEmbedRequest.isHideThread());

        oEmbedRequest.HideThread(hideThread);
        assertEquals(hideThread, oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(omitScript);
        assertEquals(omitScript, oEmbedRequest.isOmitScript());

        oEmbedRequest.omitScript(omitScript);
        assertEquals(omitScript, oEmbedRequest.isOmitScript());

        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.getAlign());

        oEmbedRequest.align(align);
        assertEquals(align, oEmbedRequest.getAlign());

        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.related(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());

        oEmbedRequest.lang(lang);
        assertEquals(lang, oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 123;
        String url = "https://example.com/statuses/123";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(maxWidth);
        oEmbedRequest.setHideMedia(hideMedia);
        oEmbedRequest.setHideThread(hideThread);
        oEmbedRequest.setOmitScript(omitScript);
        oEmbedRequest.setAlign(align);
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang(lang);

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();

        assertEquals(13, httpParameters.length);
        assertEquals("id", httpParameters[0].getName());
        assertEquals(String.valueOf(statusId), httpParameters[0].getValue());
        assertEquals("url", httpParameters[1].getName());
        assertEquals(url, httpParameters[1].getValue());
        assertEquals("maxwidth", httpParameters[2].getName());
        assertEquals(String.valueOf(maxWidth), httpParameters[2].getValue());
        assertEquals("hide_media", httpParameters[3].getName());
        assertEquals(String.valueOf(hideMedia), httpParameters[3].getValue());
        assertEquals("hide_thread", httpParameters[4].getName());
        assertEquals(String.valueOf(hideThread), httpParameters[4].getValue());
        assertEquals("omit_script", httpParameters[5].getName());
        assertEquals(String.valueOf(omitScript), httpParameters[5].getValue());
        assertEquals("align", httpParameters[6].getName());
        assertEquals(align.name().toLowerCase(), httpParameters[6].getValue());
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
        long statusId1 = 123;
        long statusId2 = 456;
        String url1 = "https://example.com/statuses/123";
        String url2 = "https://example.com/statuses/456";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related1 = {"related1", "related2"};
        String lang1 = "en";
        String[] related2 = {"related3", "related4"};
        String lang2 = "fr";

        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId2, url1);
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId1, url2);
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest7 = new OEmbedRequest(statusId1, url1);

        oEmbedRequest1.setMaxWidth(maxWidth);
        oEmbedRequest1.setHideMedia(hideMedia);
        oEmbedRequest1.setHideThread(hideThread);
        oEmbedRequest1.setOmitScript(omitScript);
        oEmbedRequest1.setAlign(align);
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang(lang1);

        oEmbedRequest2.setMaxWidth(maxWidth);
        oEmbedRequest2.setHideMedia(hideMedia);
        oEmbedRequest2.setHideThread(hideThread);
        oEmbedRequest2.setOmitScript(omitScript);
        oEmbedRequest2.setAlign(align);
        oEmbedRequest2.setRelated(related1);
        oEmbedRequest2.setLang(lang1);

        assertEquals(oEmbedRequest1, oEmbedRequest2);
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());

        oEmbedRequest3.setStatusId(statusId2);
        assertNotEquals(oEmbedRequest1, oEmbedRequest3);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest3.hashCode());

        oEmbedRequest4.setUrl(url2);
        assertNotEquals(oEmbedRequest1, oEmbedRequest4);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest4.hashCode());

        oEmbedRequest5.setMaxWidth(maxWidth + 1);
        assertNotEquals(oEmbedRequest1, oEmbedRequest5);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest5.hashCode());

        oEmbedRequest6.setHideMedia(true);
        assertNotEquals(oEmbedRequest1, oEmbedRequest6);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest6.hashCode());

        oEmbedRequest7.setRelated(related2);
        assertNotEquals(oEmbedRequest1, oEmbedRequest7);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest7.hashCode());

        oEmbedRequest7.setLang(lang2);
        assertNotEquals(oEmbedRequest1, oEmbedRequest7);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest7.hashCode());
    }

    @Test
    public void testToString() {
        long statusId = 123;
        String url = "https://example.com/statuses/123";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(maxWidth);
        oEmbedRequest.setHideMedia(hideMedia);
        oEmbedRequest.setHideThread(hideThread);
        oEmbedRequest.setOmitScript(omitScript);
        oEmbedRequest.setAlign(align);
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang(lang);

        String expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=%d, hideMedia=%b, hideThread=%b, omitScript=%b, align=%s, related=[%s, %s], lang='%s'}", statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related[0], related[1], lang);
        assertEquals(expected, oEmbedRequest.toString());
    }

    @Test
    public void testAlignEnum() {
        Align alignLeft = Align.LEFT;
        Align alignCenter = Align.CENTER;
        Align alignRight = Align.RIGHT;
        Align alignNone = Align.NONE;

        assertEquals(Align.LEFT, alignLeft);
        assertEquals(Align.LEFT.name(), alignLeft.name());
        assertEquals("LEFT", alignLeft.toString());

        assertEquals(Align.CENTER, alignCenter);
        assertEquals(Align.CENTER.name(), alignCenter.name());
        assertEquals("CENTER", alignCenter.toString());

        assertEquals(Align.RIGHT, alignRight);
        assertEquals(Align.RIGHT.name(), alignRight.name());
        assertEquals("RIGHT", alignRight.toString());

        assertEquals(Align.NONE, alignNone);
        assertEquals(Align.NONE.name(), alignNone.name());
        assertEquals("NONE", alignNone.toString());
    }
}