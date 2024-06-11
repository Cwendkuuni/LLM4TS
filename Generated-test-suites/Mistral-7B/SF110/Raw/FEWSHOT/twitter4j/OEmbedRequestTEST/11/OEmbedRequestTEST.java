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
        long statusId = 123L;
        String url = "https://example.com/status/123";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertNull(oEmbedRequest.getMaxWidth());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertNull(oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 123L;
        String url = "https://example.com/status/123";
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String lang = "en";
        String[] related = new String[]{"rel1", "rel2"};

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
        long statusId = 123L;
        String url = "https://example.com/status/123";
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String lang = "en";
        String[] related = new String[]{"rel1", "rel2"};

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(maxWidth);
        oEmbedRequest.MaxWidth(maxWidth);

        oEmbedRequest.setHideMedia(hideMedia);
        oEmbedRequest.HideMedia(hideMedia);

        oEmbedRequest.setHideThread(hideThread);
        oEmbedRequest.HideThread(hideThread);

        oEmbedRequest.setOmitScript(omitScript);
        oEmbedRequest.omitScript(omitScript);

        oEmbedRequest.setAlign(align);
        oEmbedRequest.align(align);

        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(related);

        oEmbedRequest.setLang(lang);
        oEmbedRequest.lang(lang);

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();

        assertEquals(12, httpParameters.length);
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

        List<String> relatedList = Arrays.asList(related);
        assertEquals("related", httpParameters[7].getName());
        assertEquals(StringUtil.join(related), httpParameters[7].getValue());

        assertEquals("lang", httpParameters[8].getName());
        assertEquals(lang, httpParameters[8].getValue());
    }

    @Test
    public void testEquals() {
        long statusId1 = 123L;
        long statusId2 = 456L;
        String url1 = "https://example.com/status/123";
        String url2 = "https://example.com/status/456";
        int maxWidth1 = 300;
        int maxWidth2 = 400;
        boolean hideMedia1 = true;
        boolean hideMedia2 = false;
        boolean hideThread1 = false;
        boolean hideThread2 = true;
        boolean omitScript1 = false;
        boolean omitScript2 = true;
        Align align1 = Align.LEFT;
        Align align2 = Align.RIGHT;
        String lang1 = "en";
        String lang2 = "fr";
        String[] related1 = new String[]{"rel1"};
        String[] related2 = new String[0];

        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);

        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId1, url1);

        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId1, url1);
        oEmbedRequest4.setMaxWidth(maxWidth1);
        oEmbedRequest4.MaxWidth(maxWidth1);
        oEmbedRequest4.setHideMedia(hideMedia1);
        oEmbedRequest4.HideMedia(hideMedia1);
        oEmbedRequest4.setHideThread(hideThread1);
        oEmbedRequest4.HideThread(hideThread1);
        oEmbedRequest4.setOmitScript(omitScript1);
        oEmbedRequest4.omitScript(omitScript1);
        oEmbedRequest4.setAlign(align1);
        oEmbedRequest4.align(align1);
        oEmbedRequest4.setRelated(related1);
        oEmbedRequest4.related(related1);
        oEmbedRequest4.setLang(lang1);
        oEmbedRequest4.lang(lang1);

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(statusId1, url1);
        oEmbedRequest5.setMaxWidth(maxWidth2);
        oEmbedRequest5.MaxWidth(maxWidth2);
        oEmbedRequest5.setHideMedia(hideMedia1);
        oEmbedRequest5.HideMedia(hideMedia1);
        oEmbedRequest5.setHideThread(hideThread1);
        oEmbedRequest5.HideThread(hideThread1);
        oEmbedRequest5.setOmitScript(omitScript1);
        oEmbedRequest5.omitScript(omitScript1);
        oEmbedRequest5.setAlign(align2);
        oEmbedRequest5.align(align2);
        oEmbedRequest5.setRelated(related2);
        oEmbedRequest5.related(related2);
        oEmbedRequest5.setLang(lang2);
        oEmbedRequest5.lang(lang2);

        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(statusId1, url1);
        oEmbedRequest6.setLang(null);

        OEmbedRequest oEmbedRequest7 = new OEmbedRequest(statusId2, url2);

        OEmbedRequest oEmbedRequest8 = new OEmbedRequest(statusId1, url1);
        oEmbedRequest8.setMaxWidth(maxWidth1);
        oEmbedRequest8.MaxWidth(maxWidth1);
        oEmbedRequest8.setHideMedia(hideMedia1);
        oEmbedRequest8.HideMedia(hideMedia2);
        oEmbedRequest8.setHideThread(hideThread1);
        oEmbedRequest8.HideThread(hideThread2);
        oEmbedRequest8.setOmitScript(omitScript1);
        oEmbedRequest8.omitScript(omitScript2);
        oEmbedRequest8.setAlign(align1);
        oEmbedRequest8.align(align2);
        oEmbedRequest8.setRelated(related1);
        oEmbedRequest8.related(related2);
        oEmbedRequest8.setLang(lang1);
        oEmbedRequest8.lang(lang2);

        assertTrue(oEmbedRequest1.equals(oEmbedRequest3));
        assertTrue(oEmbedRequest3.equals(oEmbedRequest4));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest5));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest6));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest7));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest8));
    }

    @Test
    public void testHashCode() {
        long statusId = 123L;
        String url = "https://example.com/status/123";
        int maxWidth = 300;
        boolean hideMedia = true;
        boolean hideThread = false;
        boolean omitScript = false;
        Align align = Align.LEFT;
        String lang = "en";
        String[] related = new String[]{"rel1"};

        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId, url);
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId, url);

        oEmbedRequest1.setMaxWidth(maxWidth);
        oEmbedRequest1.MaxWidth(maxWidth);
        oEmbedRequest1.setHideMedia(hideMedia);
        oEmbedRequest1.HideMedia(hideMedia);
        oEmbedRequest1.setHideThread(hideThread);
        oEmbedRequest1.HideThread(hideThread);
        oEmbedRequest1.setOmitScript(omitScript);
        oEmbedRequest1.omitScript(omitScript);
        oEmbedRequest1.setAlign(align);
        oEmbedRequest1.align(align);
        oEmbedRequest1.setRelated(related);
        oEmbedRequest1.related(related);
        oEmbedRequest1.setLang(lang);
        oEmbedRequest1.lang(lang);

        int hashCode1 = oEmbedRequest1.hashCode();

        oEmbedRequest2.setMaxWidth(maxWidth + 1);
        oEmbedRequest2.MaxWidth(maxWidth + 1);

        int hashCode2 = oEmbedRequest2.hashCode();

        assertEquals(hashCode1, oEmbedRequest3.hashCode());
        assertEquals(hashCode1, oEmbedRequest4.hashCode());
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        long statusId = 123L;
        String url = "https://example.com/status/123";
        int maxWidth = 300;
        boolean hideMedia = true;
        boolean hideThread = false;
        boolean omitScript = false;
        Align align = Align.LEFT;
        String lang = "en";
        String[] related = new String[]{"rel1", "rel2"};

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(maxWidth);
        oEmbedRequest.MaxWidth(maxWidth);
        oEmbedRequest.setHideMedia(hideMedia);
        oEmbedRequest.HideMedia(hideMedia);
        oEmbedRequest.setHideThread(hideThread);
        oEmbedRequest.HideThread(hideThread);
        oEmbedRequest.setOmitScript(omitScript);
        oEmbedRequest.omitScript(omitScript);
        oEmbedRequest.setAlign(align);
        oEmbedRequest.align(align);
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(related);
        oEmbedRequest.setLang(lang);
        oEmbedRequest.lang(lang);

        String expected = "OEmbedRequest{statusId=" + statusId + ", url='" + url + "', maxWidth=" + maxWidth + ", hideMedia=" + hideMedia + ", hideThread=" + hideThread + ", omitScript=" + omitScript + ", align=" + align + ", related=[rel1, rel2], lang='" + lang + "'}";

        assertEquals(expected, oEmbedRequest.toString());
    }

    @Test
    public void testAlignEnum() {
        Align align = Align.LEFT;
        String expected = "left";

        assertEquals(expected, align.name().toLowerCase());

        Align align2 = Align.RIGHT;
        expected = "right";

        assertEquals(expected, align2.name().toLowerCase());

        Align align3 = Align.NONE;
        expected = "none";

        assertEquals(expected, align3.name().toLowerCase());
    }
}