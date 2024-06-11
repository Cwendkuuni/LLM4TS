package twitter4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
        long statusId = 1L;
        String url = "https://example.com/status/123";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "https://example.com/status/123";
        int maxWidth = 200;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(maxWidth);
        oEmbedRequest.MaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(hideMedia);
        oEmbedRequest.HideMedia(hideMedia);
        assertEquals(hideMedia, oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(hideThread);
        oEmbedRequest.HideThread(hideThread);
        assertEquals(hideThread, oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(omitScript);
        oEmbedRequest.omitScript(omitScript);
        assertEquals(omitScript, oEmbedRequest.isOmitScript());

        oEmbedRequest.setAlign(align);
        oEmbedRequest.align(align);
        assertEquals(align, oEmbedRequest.getAlign());

        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.setLang(lang);
        oEmbedRequest.lang(lang);
        assertEquals(lang, oEmbedRequest.getLang());

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        List<HttpParameter> httpParameterList = Arrays.asList(httpParameters);

        assertEquals(12, httpParameterList.size());
        List<String> expectedParameterNames = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (int i = 0; i < httpParameterList.size(); i++) {
            assertEquals(expectedParameterNames.get(i), httpParameterList.get(i).getName());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "https://example.com/status/123";
        long statusId2 = 2L;
        String url2 = "https://example.com/status/456";

        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);

        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest2.hashCode());

        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId1, url1);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest3));
        assertTrue(oEmbedRequest1.hashCode() == oEmbedRequest3.hashCode());
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com/status/123";
        int maxWidth = 200;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = {"related1", "related2"};
        String lang = "en";

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

        String expectedToString = "OEmbedRequest{statusId=1, url='https://example.com/status/123', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expectedToString, oEmbedRequest.toString());
    }

    @Test
    public void testAlignEnumValue() {
        Align align = Align.LEFT;

        assertEquals("left", align.name().toLowerCase());
    }
}