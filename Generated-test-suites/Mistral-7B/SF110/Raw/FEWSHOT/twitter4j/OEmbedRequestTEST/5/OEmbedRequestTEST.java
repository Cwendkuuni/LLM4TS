package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(1234567890L, request.getStatusId());
        assertEquals("https://example.com/status/1234567890", request.getUrl());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        int maxWidth = 42;
        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.MaxWidth(56);
        assertEquals(56, request.getMaxWidth());

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

        String[] related = {"related1", "related2"};
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
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.RIGHT;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url)
                .MaxWidth(maxWidth)
                .HideMedia(hideMedia)
                .HideThread(hideThread)
                .omitScript(omitScript)
                .align(align)
                .related(related)
                .lang(lang);

        HttpParameter[] httpParams = request.asHttpParameterArray();
        assertEquals(12, httpParams.length);

        assertEquals("id", httpParams[0].getName());
        assertEquals(Long.toString(statusId), httpParams[0].getValue());

        assertEquals("url", httpParams[1].getName());
        assertEquals(url, httpParams[1].getValue());

        assertEquals("maxwidth", httpParams[2].getName());
        assertEquals(Integer.toString(maxWidth), httpParams[2].getValue());

        assertEquals("hide_media", httpParams[3].getName());
        assertEquals(Boolean.toString(hideMedia), httpParams[3].getValue());

        assertEquals("hide_thread", httpParams[4].getName());
        assertEquals(Boolean.toString(hideThread), httpParams[4].getValue());

        assertEquals("omit_script", httpParams[5].getName());
        assertEquals(Boolean.toString(omitScript), httpParams[5].getValue());

        assertEquals("align", httpParams[6].getName());
        assertEquals(align.name().toLowerCase(), httpParams[6].getValue());

        assertEquals("related", httpParams[7].getName());
        assertEquals(StringUtil.join(related), httpParams[7].getValue());

        assertEquals("lang", httpParams[8].getName());
        assertEquals(lang, httpParams[8].getValue());

        assertNull(httpParams[9]);
        assertNull(httpParams[10]);
        assertNull(httpParams[11]);
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1234567890L;
        long statusId2 = 9876543210L;
        String url1 = "https://example.com/status/1234567890";
        String url2 = "https://example.com/status/9876543210";
        int maxWidth1 = 42;
        int maxWidth2 = 56;
        boolean hideMedia1 = false;
        boolean hideMedia2 = false;
        boolean hideThread1 = false;
        boolean hideThread2 = false;
        boolean omitScript1 = true;
        boolean omitScript2 = false;
        Align align1 = Align.RIGHT;
        Align align2 = Align.LEFT;
        String[] related1 = {"related1", "related2"};
        String[] related2 = {"related1", "related3"};
        String lang1 = "en";
        String lang2 = "fr";

        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1)
                .MaxWidth(maxWidth1)
                .HideMedia(hideMedia1)
                .HideThread(hideThread1)
                .omitScript(omitScript1)
                .align(align1)
                .related(related1)
                .lang(lang1);

        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2)
                .MaxWidth(maxWidth2)
                .HideMedia(hideMedia2)
                .HideThread(hideThread2)
                .omitScript(omitScript2)
                .align(align2)
                .related(related2)
                .lang(lang2);

        assertTrue(request1.equals(request1));
        assertTrue(request2.equals(request2));

        assertFalse(request1.equals(request2));

        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1)
                .MaxWidth(maxWidth1)
                .HideMedia(hideMedia1)
                .HideThread(hideThread1)
                .omitScript(omitScript1)
                .align(align1)
                .related(related1)
                .lang(lang1);

        assertTrue(request1.equals(request3));

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode2, hashCode3);
    }

    @Test
    public void testToString() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        int maxWidth = 42;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.RIGHT;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url)
                .MaxWidth(maxWidth)
                .HideMedia(hideMedia)
                .HideThread(hideThread)
                .omitScript(omitScript)
                .align(align)
                .related(related)
                .lang(lang);

        String expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=%d, hideMedia=%b, hideThread=%b, omitScript=%b, align=%s, related=%s, lang='%s'}",
                statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, Arrays.toString(related), lang);

        assertEquals(expected, request.toString());
    }
}