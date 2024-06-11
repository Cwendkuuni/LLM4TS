package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructorAndGetters() {
        long statusId = 123L;
        String url = "http://example.com/status/123";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 123L;
        String url = "http://example.com/status/123";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(42);
        assertEquals(42, request.getMaxWidth());

        request.HideMedia();
        assertFalse(request.isHideMedia());

        request.HideThread();
        assertFalse(request.isHideThread());

        request.omitScript();
        assertTrue(request.isOmitScript());

        request.align(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        String[] related = new String[] {"related1", "related2"};
        request.related(related);
        assertArrayEquals(related, request.getRelated());

        request.lang("en");
        assertEquals("en", request.getLang());

        request.setMaxWidth(-1);
        assertFalse(request.getMaxWidth() >= 0);

        request.HideMedia();
        request.HideMedia();
        assertFalse(request.isHideMedia());

        request.HideThread();
        request.HideThread();
        assertFalse(request.isHideThread());

        request.omitScript();
        request.omitScript();
        assertTrue(request.isOmitScript());

        request.align(null);
        assertNull(request.getAlign());

        request.related(null);
        assertArrayEquals(new String[0], request.getRelated());

        request.lang(null);
        assertNull(request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 123L;
        String url = "http://example.com/status/123";
        String lang = "en";
        String[] related = {"related1", "related2"};
        Align align = Align.LEFT;
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("id", String.valueOf(statusId)),
                new HttpParameter("url", url),
                new HttpParameter("maxwidth", String.valueOf(maxWidth)),
                new HttpParameter("hide_media", String.valueOf(hideMedia)),
                new HttpParameter("hide_thread", String.valueOf(hideThread)),
                new HttpParameter("omit_script", String.valueOf(omitScript)),
                new HttpParameter("align", align.name().toLowerCase()),
                new HttpParameter("related", StringUtil.join(related)),
                new HttpParameter("lang", lang));

        HttpParameter[] params = request.asHttpParameterArray();
        List<HttpParameter> actualParams = Arrays.asList(params);

        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testEquals() {
        long statusId1 = 123L;
        long statusId2 = 456L;
        String url1 = "http://example.com/status/123";
        String url2 = "http://example.com/status/456";
        String lang1 = "en";
        String lang2 = "fr";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request3));
        assertFalse(request1.equals(request2));
        assertFalse(request1.equals(null));
        assertFalse(request1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        long statusId = 123L;
        String url = "http://example.com/status/123";
        String lang = "en";
        String[] related = {"related1", "related2"};
        Align align = Align.LEFT;
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;

        OEmbedRequest request1 = new OEmbedRequest(statusId, url);
        request1.setMaxWidth(maxWidth);
        request1.setHideMedia(hideMedia);
        request1.setHideThread(hideThread);
        request1.setOmitScript(omitScript);
        request1.setAlign(align);
        request1.setRelated(related);
        request1.setLang(lang);

        long hashCode1 = request1.hashCode();

        request1.setStatusId(statusId + 1);
        long hashCode2 = request1.hashCode();

        request1.setUrl("http://example.com/status/124");
        long hashCode3 = request1.hashCode();

        request1.setMaxWidth(maxWidth + 1);
        long hashCode4 = request1.hashCode();

        request1.HideMedia();
        long hashCode5 = request1.hashCode();

        request1.HideThread();
        long hashCode6 = request1.hashCode();

        request1.omitScript();
        long hashCode7 = request1.hashCode();

        request1.align(Align.CENTER);
        long hashCode8 = request1.hashCode();

        request1.related(new String[] {"related3", "related4"});
        long hashCode9 = request1.hashCode();

        request1.lang("fr");
        long hashCode10 = request1.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
        assertNotEquals(hashCode1, hashCode7);
        assertNotEquals(hashCode1, hashCode8);
        assertNotEquals(hashCode1, hashCode9);
        assertNotEquals(hashCode1, hashCode10);
    }

    @Test
    public void testToString() {
        long statusId = 123L;
        String url = "http://example.com/status/123";
        String lang = "en";
        String[] related = {"related1", "related2"};
        Align align = Align.LEFT;
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        String expectedString = "OEmbedRequest{statusId=" + statusId + ", url='" + url + "', maxWidth=" + maxWidth + ", hideMedia=" + hideMedia + ", hideThread=" + hideThread + ", omitScript=" + omitScript + ", align=" + align + ", related=" + Arrays.asList(related) + ", lang='" + lang + "'}";

        assertEquals(expectedString, request.toString());
    }
}