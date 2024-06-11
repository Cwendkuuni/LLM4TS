package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://example.com";
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
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");

        request.setMaxWidth(120);
        assertEquals(120, request.getMaxWidth());

        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());

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

        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        request.setAlign(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());

        request.align(Align.NONE);
        assertEquals(Align.NONE, request.getAlign());

        String[] related = new String[]{"test1", "test2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(new String[]{"test3", "test4"});
        assertArrayEquals(new String[]{"test3", "test4"}, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        int maxWidth = 120;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = new String[]{"test1", "test2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        HttpParameter[] httpParameters = request.asHttpParameterArray();
        List<String> expectedKeys = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (String key : expectedKeys) {
            assertTrue(findParameter(httpParameters, key) != null);
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        long statusId2 = 2L;
        String url1 = "https://example.com/1";
        String url2 = "https://example.com/2";
        int maxWidth1 = 120;
        int maxWidth2 = 200;
        boolean hideMedia1 = false;
        boolean hideMedia2 = true;
        boolean hideThread1 = false;
        boolean hideThread2 = false;
        boolean omitScript1 = false;
        boolean omitScript2 = true;
        Align align1 = Align.LEFT;
        Align align2 = Align.RIGHT;
        String lang1 = null;
        String lang2 = "en";
        String[] related1 = new String[0];
        String[] related2 = new String[]{"test"};

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

        assertTrue(request1.equals(request1));
        assertTrue(request2.equals(request2));
        assertFalse(request1.equals(request2));

        assertEquals(request1.hashCode(), request1.hashCode());
        assertEquals(request2.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com";
        int maxWidth = 120;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = new String[]{"test1", "test2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        String expected = "OEmbedRequest{statusId=1, url='https://example.com', maxWidth=120, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[test1, test2], lang='en'}";
        assertEquals(expected, request.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStatusId() {
        long statusId = -1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        long statusId = 1L;
        String url = null;
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        fail("NullPointerException expected");
    }

    @Test
    public void testEmptyRelatedArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setRelated(new String[0]);
        HttpParameter[] httpParameters = request.asHttpParameterArray();
        assertNull(findParameter(httpParameters, "related"));
    }

    private HttpParameter findParameter(HttpParameter[] parameters, String name) {
        for (HttpParameter param : parameters) {
            if (param.getName().equalsIgnoreCase(name)) {
                return param;
            }
        }
        return null;
    }
}