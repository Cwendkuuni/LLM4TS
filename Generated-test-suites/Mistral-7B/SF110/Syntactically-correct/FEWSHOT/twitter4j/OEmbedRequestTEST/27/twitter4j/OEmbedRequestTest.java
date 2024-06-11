package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://example.com/tweet/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1234567890L;
        String url = "https://example.com/tweet/1234567890";
        int maxWidth = 400;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = new String[]{"related1", "related2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.MaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.setHideMedia(hideMedia);
        assertEquals(hideMedia, request.isHideMedia());

        request.HideMedia(hideMedia);
        assertEquals(hideMedia, request.isHideMedia());

        request.setHideThread(hideThread);
        assertEquals(hideThread, request.isHideThread());

        request.HideThread(hideThread);
        assertEquals(hideThread, request.isHideThread());

        request.setOmitScript(omitScript);
        assertEquals(omitScript, request.isOmitScript());

        request.omitScript(omitScript);
        assertEquals(omitScript, request.isOmitScript());

        request.setAlign(align);
        assertEquals(align, request.getAlign());

        request.align(align);
        assertEquals(align, request.getAlign());

        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(related);
        assertArrayEquals(related, request.getRelated());

        request.setLang(lang);
        assertEquals(lang, request.getLang());

        request.lang(lang);
        assertEquals(lang, request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1234567890L;
        String url = "https://example.com/tweet/1234567890";
        int maxWidth = 400;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
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

        HttpParameter[] httpParamArray = request.asHttpParameterArray();
        assertEquals(13, httpParamArray.length);
        assertEquals("id", httpParamArray[0].getName());
        assertEquals(String.valueOf(statusId), httpParamArray[0].getValue());
        assertEquals("url", httpParamArray[1].getName());
        assertEquals(url, httpParamArray[1].getValue());
        assertEquals("maxwidth", httpParamArray[2].getName());
        assertEquals(String.valueOf(maxWidth), httpParamArray[2].getValue());
        assertEquals("hide_media", httpParamArray[3].getName());
        assertEquals(String.valueOf(hideMedia), httpParamArray[3].getValue());
        assertEquals("hide_thread", httpParamArray[4].getName());
        assertEquals(String.valueOf(hideThread), httpParamArray[4].getValue());
        assertEquals("omit_script", httpParamArray[5].getName());
        assertEquals(String.valueOf(omitScript), httpParamArray[5].getValue());
        assertEquals("align", httpParamArray[6].getName());
        assertEquals(align.name().toLowerCase(), httpParamArray[6].getValue());
        if (related != null && related.length > 0) {
            assertEquals("related", httpParamArray[7].getName());
            assertEquals(StringUtil.join(related), httpParamArray[7].getValue());
        } else {
            assertNull(httpParamArray[7]);
        }
        assertEquals("lang", httpParamArray[8].getName());
        assertEquals(lang, httpParamArray[8].getValue());
    }

    @Test
    public void testEquals() {
        long statusId1 = 1234567890L;
        String url1 = "https://example.com/tweet/1234567890";
        int maxWidth1 = 400;
        boolean hideMedia1 = false;
        boolean hideThread1 = false;
        boolean omitScript1 = true;
        Align align1 = Align.LEFT;
        String[] related1 = new String[]{"related1", "related2"};
        String lang1 = "en";

        long statusId2 = 9876543210L;
        String url2 = "https://example.com/tweet/9876543210";
        int maxWidth2 = 600;
        boolean hideMedia2 = false;
        boolean hideThread2 = false;
        boolean omitScript2 = true;
        Align align2 = Align.LEFT;
        String[] related2 = new String[]{"related1", "related2"};
        String lang2 = "en";

        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        request1.setMaxWidth(maxWidth1);
        request1.setHideMedia(hideMedia1);
        request1.setHideThread(hideThread1);
        request1.setOmitScript(omitScript1);
        request1.setAlign(align1);
        request1.setRelated(related1);
        request1.setLang(lang1);

        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        request2.setMaxWidth(maxWidth2);
        request2.setHideMedia(hideMedia2);
        request2.setHideThread(hideThread2);
        request2.setOmitScript(omitScript2);
        request2.setAlign(align2);
        request2.setRelated(related2);
        request2.setLang(lang2);

        assertFalse(request1.equals(null));
        assertFalse(request1.equals(request2));
        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);
        request3.setMaxWidth(maxWidth1);
        request3.setHideMedia(hideMedia1);
        request3.setHideThread(hideThread1);
        request3.setOmitScript(omitScript1);
        request3.setAlign(align1);
        request3.setRelated(related1);
        request3.setLang(lang1);
        assertTrue(request1.equals(request3));
    }

    @Test
    public void testHashCode() {
        long statusId = 1234567890L;
        String url = "https://example.com/tweet/1234567890";
        int maxWidth = 400;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
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

        long hashCode = request.hashCode();
        long statusId2 = 9876543210L;
        String url2 = "https://example.com/tweet/9876543210";
        int maxWidth2 = 600;
        boolean hideMedia2 = false;
        boolean hideThread2 = false;
        boolean omitScript2 = true;
        Align align2 = Align.LEFT;
        String[] related2 = new String[]{"related1", "related2"};
        String lang2 = "en";

        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        request2.setMaxWidth(maxWidth2);
        request2.setHideMedia(hideMedia2);
        request2.setHideThread(hideThread2);
        request2.setOmitScript(omitScript2);
        request2.setAlign(align2);
        request2.setRelated(related2);
        request2.setLang(lang2);

        assertEquals(hashCode, request.hashCode());

        int maxWidth3 = 800;
        request.setMaxWidth(maxWidth3);
        int newHashCode = request.hashCode();
        assertNotEquals(hashCode, newHashCode);

        request2.setMaxWidth(maxWidth3);
        assertEquals(newHashCode, request2.hashCode());
    }

    @Test
    public void testToString() {
        long statusId = 1234567890L;
        String url = "https://example.com/tweet/1234567890";
        int maxWidth = 400;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
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

        String expectedString = "OEmbedRequest{statusId=" + statusId + ", url='" + url + '\'' + ", maxWidth=" + maxWidth + ", hideMedia=" + hideMedia + ", hideThread=" + hideThread + ", omitScript=" + omitScript + ", align=" + align + ", related=" + Arrays.asList(related) + ", lang='" + lang + '\'' + '}';
        assertEquals(expectedString, request.toString());
    }
}