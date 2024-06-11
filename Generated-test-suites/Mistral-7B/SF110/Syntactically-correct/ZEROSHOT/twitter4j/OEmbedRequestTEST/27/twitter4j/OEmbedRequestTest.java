package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://example.com/statuses/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertFalse(request.isHideMedia());
        assertFalse(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertEquals(new String[0], request.getRelated());
        assertEquals(null, request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "https://example.com/statuses/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        request.HideMedia(true);
        assertTrue(request.isHideMedia());

        request.setHideThread(false);
        request.HideThread(true);
        assertTrue(request.isHideThread());

        request.setOmitScript(true);
        request.omitScript(false);
        assertFalse(request.isOmitScript());

        Align align = Align.LEFT;
        request.setAlign(align);
        request.align(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        request.setLang("en");
        request.lang("fr");
        assertEquals("fr", request.getLang());

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);
        List<String> names = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (int i = 0; i < params.length; i++) {
            assertEquals(names.get(i), params[i].getName());
        }
    }

    @Test
    public void testEqualsAndHashcode() {
        long statusId1 = 1L;
        String url1 = "https://example.com/statuses/1";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);

        long statusId2 = 2L;
        String url2 = "https://example.com/statuses/2";
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);

        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request4 = new OEmbedRequest(statusId1, url1);

        OEmbedRequest request5 = new OEmbedRequest(statusId1, url1);
        request5.setMaxWidth(100);
        assertNotEquals(request5, request1);

        OEmbedRequest request6 = new OEmbedRequest(statusId1, url1);
        request6.setUrl("https://example.com/statuses/3");
        assertNotEquals(request6, request1);

        OEmbedRequest request7 = new OEmbedRequest(statusId2, url1);
        assertNotEquals(request7, request1);

        OEmbedRequest request8 = new OEmbedRequest(statusId1, url1);
        request8.setStatusId(2L);
        assertNotEquals(request8, request1);

        OEmbedRequest request9 = new OEmbedRequest(statusId1, url1);
        request9.setUrl(null);
        assertNotEquals(request9, request1);

        OEmbedRequest request10 = new OEmbedRequest(statusId1, url1);
        request10.setHideMedia(false);
        assertNotEquals(request10, request1);

        OEmbedRequest request11 = new OEmbedRequest(statusId1, url1);
        request11.setHideThread(false);
        assertNotEquals(request11, request1);

        OEmbedRequest request12 = new OEmbedRequest(statusId1, url1);
        request12.setOmitScript(false);
        assertNotEquals(request12, request1);

        OEmbedRequest request13 = new OEmbedRequest(statusId1, url1);
        Align align = Align.LEFT;
        request13.setAlign(align);
        assertNotEquals(request13, request1);

        String[] related = new String[]{"related1", "related2"};
        OEmbedRequest request14 = new OEmbedRequest(statusId1, url1);
        request14.setRelated(related);
        assertNotEquals(request14, request1);

        String lang = "en";
        OEmbedRequest request15 = new OEmbedRequest(statusId1, url1);
        request15.setLang(lang);
        assertNotEquals(request15, request1);

        assertEquals(request1, request4);
        assertEquals(request1, request3);
        assertEquals(request1, request1);

        int hashcode1 = request1.hashCode();
        OEmbedRequest request16 = new OEmbedRequest(statusId1, url1);
        int hashcode2 = request16.hashCode();
        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com/statuses/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        String expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());

        request.setMaxWidth(100);
        request.MaxWidth(200);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());

        request.setHideMedia(false);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=false, hideThread=true, omitScript=false, align=NONE, related=null, lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());

        request.setHideThread(false);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=false, hideThread=false, omitScript=false, align=NONE, related=null, lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());

        request.setOmitScript(true);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=NONE, related=null, lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());

        Align align = Align.LEFT;
        request.setAlign(align);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=false, hideThread=false, omitScript=false, align=LEFT, related=null, lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=false, hideThread=false, omitScript=false, align=LEFT, related=[related1, related2], lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());

        request.setLang("en");
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=false, hideThread=false, omitScript=false, align=LEFT, related=[related1, related2], lang='en'}", statusId, url, null);
        assertEquals(expected, request.toString());

        request.setRelated(null);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=200, hideMedia=false, hideThread=false, omitScript=false, align=LEFT, related=null, lang='%s'}", statusId, url, null);
        assertEquals(expected, request.toString());
    }

}