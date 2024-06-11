package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        int maxWidth = 123;
        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.MaxWidth(567);
        assertEquals(567, request.getMaxWidth());

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

        Align align = Align.RIGHT;
        request.setAlign(align);
        assertEquals(align, request.getAlign());

        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
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
        String url = "http://example.com";
        int maxWidth = 123;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.RIGHT;
        String[] related = new String[]{"related1", "related2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("id", String.valueOf(statusId)));
        expectedParams.add(new HttpParameter("url", url));
        if (maxWidth > 0) {
            expectedParams.add(new HttpParameter("maxwidth", String.valueOf(maxWidth)));
        }
        expectedParams.add(new HttpParameter("hide_media", String.valueOf(hideMedia)));
        expectedParams.add(new HttpParameter("hide_thread", String.valueOf(hideThread)));
        expectedParams.add(new HttpParameter("omit_script", String.valueOf(omitScript)));
        expectedParams.add(new HttpParameter("align", align.name().toLowerCase()));
        if (related.length > 0) {
            expectedParams.add(new HttpParameter("related", StringUtil.join(related)));
        }
        expectedParams.add(new HttpParameter("lang", lang));

        HttpParameter[] actualParams = request.asHttpParameterArray();
        assertArrayEquals(expectedParams.toArray(new HttpParameter[expectedParams.size()]), actualParams);
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "http://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);

        long statusId2 = 2L;
        String url2 = "http://example.com";
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);

        long statusId3 = 1L;
        String url3 = "http://example.com";
        OEmbedRequest request3 = new OEmbedRequest(statusId3, url1);

        long statusId4 = 1L;
        String url4 = "http://example.org";
        OEmbedRequest request4 = new OEmbedRequest(statusId4, url1);

        assertEquals(request1, request1);
        assertEquals(request1, request1);
        assertEquals(request1, request3);

        assertNotEquals(request1, request2);
        assertNotEquals(request1, request4);

        request1.setMaxWidth(123);
        request2.setMaxWidth(123);
        assertEquals(request1, request2);

        request1.setHideMedia(false);
        assertNotEquals(request1, request2);

        request2.setHideMedia(false);
        assertEquals(request1, request2);

        request1.setRelated(new String[]{"related1"});
        request2.setRelated(new String[]{"related2"});
        assertNotEquals(request1, request2);

        request2.setRelated(new String[]{"related1"});
        assertEquals(request1, request2);

        request1.setLang("en");
        request2.setLang("fr");
        assertNotEquals(request1, request2);

        request2.setLang("en");
        assertEquals(request1, request2);
    }
}