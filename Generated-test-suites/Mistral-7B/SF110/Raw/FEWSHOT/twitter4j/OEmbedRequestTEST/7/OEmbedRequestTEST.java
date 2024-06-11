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
    public void testOEmbedRequestConstruction() {
        long statusId = 12345L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertEquals(Align.NONE, request.getAlign());
        assertEquals(new String[0], request.getRelated());
        assertEquals(false, request.isHideMedia());
        assertEquals(false, request.isHideThread());
        assertEquals(false, request.isOmitScript());
        assertEquals(0, request.getMaxWidth());
        assertEquals(null, request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 12345L;
        String url = "http://example.com";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = false;
        Align align = Align.LEFT;
        String[] related = new String[]{"rel1", "rel2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.MaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.setHideMedia(hideMedia);
        request.HideMedia(hideMedia);
        assertEquals(hideMedia, request.isHideMedia());

        request.setHideThread(hideThread);
        request.HideThread(hideThread);
        assertEquals(hideThread, request.isHideThread());

        request.setOmitScript(omitScript);
        request.omitScript(omitScript);
        assertEquals(omitScript, request.isOmitScript());

        request.setAlign(align);
        request.align(align);
        assertEquals(align, request.getAlign());

        request.setRelated(related);
        request.related(related);
        assertArrayEquals(related, request.getRelated());

        request.setLang(lang);
        request.lang(lang);
        assertEquals(lang, request.getLang());
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 12345L;
        String url1 = "http://example.com";
        long statusId2 = 67890L;
        String url2 = "http://example.org";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = false;
        Align align = Align.LEFT;
        String[] related = new String[]{"rel1", "rel2"};
        String lang = "en";

        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request2 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request3 = new OEmbedRequest(statusId2, url2);

        OEmbedRequest request4 = new OEmbedRequest(statusId1, url1);
        request4.setMaxWidth(maxWidth);
        request4.setHideMedia(hideMedia);
        request4.setHideThread(hideThread);
        request4.setAlign(align);
        request4.setRelated(related);
        request4.setLang(lang);

        OEmbedRequest request5 = new OEmbedRequest(statusId1, url1);
        request5.setMaxWidth(maxWidth + 1);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertSame(request1, request4);
        assertSame(request1, request4);
        assertNotSame(request1, request3);
        assertNotSame(request1, request5);
    }

    @Test
    public void testToString() {
        long statusId = 12345L;
        String url = "http://example.com";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = false;
        Align align = Align.LEFT;
        String[] related = new String[]{"rel1", "rel2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        String expected = "OEmbedRequest{statusId=" + statusId + ", url='" + url + "', maxWidth=" + maxWidth + ", hideMedia=" + hideMedia + ", hideThread=" + hideThread + ", omitScript=" + omitScript + ", align=" + align + ", related=" + Arrays.asList(related) + ", lang='" + lang + "'}";
        assertEquals(expected, request.toString());

        request.setMaxWidth(maxWidth + 1);
        expected = "OEmbedRequest{statusId=" + statusId + ", url='" + url + "', maxWidth=" + (maxWidth + 1) + ", hideMedia=" + hideMedia + ", hideThread=" + hideThread + ", omitScript=" + omitScript + ", align=" + align + ", related=" + Arrays.asList(related) + ", lang='" + lang + "'}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 12345L;
        String url = "http://example.com";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = false;
        Align align = Align.LEFT;
        String[] related = new String[]{"rel1", "rel2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        HttpParameter[] expected = new HttpParameter[] {
                new HttpParameter("id", statusId),
                new HttpParameter("url", url),
                new HttpParameter("maxwidth", String.valueOf(maxWidth)),
                new HttpParameter("hide_media", String.valueOf(hideMedia)),
                new HttpParameter("hide_thread", String.valueOf(hideThread)),
                new HttpParameter("omit_script", String.valueOf(omitScript)),
                new HttpParameter("align", align.name().toLowerCase()),
                new HttpParameter("related", StringUtil.join(related)),
                new HttpParameter("lang", lang)
        };

        HttpParameter[] params = request.asHttpParameterArray();
        assertArrayEquals(expected, params);
    }

    @Test
    public void testAppendParameter() {
        List<HttpParameter> params = new ArrayList<HttpParameter>(4);

        OEmbedRequest request = new OEmbedRequest(12345L, "http://example.com");
        request.appendParameter("id", 12345L, params);
        request.appendParameter("url", "http://example.com", params);
        assertEquals(2, params.size());
        assertEquals(new HttpParameter("id", 12345L), params.get(0));
        assertEquals(new HttpParameter("url", "http://example.com"), params.get(1));

        params.clear();
        request.appendParameter("id", null, params);
        assertEquals(0, params.size());

        params.clear();
        request.appendParameter("id", "12345", params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("id", "12345"), params.get(0));

        params.clear();
        request.appendParameter("id", -12345L, params);
        assertEquals(0, params.size());

        params.clear();
        request.appendParameter("id", "", params);
        assertEquals(0, params.size());

        params.clear();
        request.appendParameter("id", " ", params);
        assertEquals(0, params.size());

        params.clear();
        request.appendParameter("id", null, params);
        assertEquals(0, params.size());

        params.clear();
        request.appendParameter("id", "", params);
        assertEquals(0, params.size());
    }

    @Test
    public void testEmptyConstructor() {
        OEmbedRequest request = new OEmbedRequest();
        assertEquals(0L, request.getStatusId());
        assertEquals(null, request.getUrl());
        assertEquals(Align.NONE, request.getAlign());
        assertEquals(new String[0], request.getRelated());
        assertEquals(false, request.isHideMedia());
        assertEquals(false, request.isHideThread());
        assertEquals(false, request.isOmitScript());
        assertEquals(0, request.getMaxWidth());
        assertEquals(null, request.getLang());
    }
}