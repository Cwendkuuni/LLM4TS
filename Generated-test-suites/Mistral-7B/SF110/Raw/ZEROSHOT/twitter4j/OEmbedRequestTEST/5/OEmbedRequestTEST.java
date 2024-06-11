package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
        long statusId = 1234567890L;
        String url = "https://example.com/tweet";
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
        long statusId = 1234567890L;
        String url = "https://example.com/tweet";
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.CENTER;
        String[] related = {"related1", "related2"};
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

        HttpParameter[] httpParams = request.asHttpParameterArray();
        List<HttpParameter> paramsList = Arrays.asList(httpParams);

        assertEquals(12, paramsList.size());
        assertEquals("1234567890", paramsList.get(0).getName());
        assertEquals("id", paramsList.get(0).getName());
        assertEquals("https://example.com/tweet", paramsList.get(1).getValue());
        assertEquals("url", paramsList.get(1).getName());
        assertEquals("300", paramsList.get(2).getValue());
        assertEquals("maxwidth", paramsList.get(2).getName());
        assertEquals("false", paramsList.get(3).getValue());
        assertEquals("hide_media", paramsList.get(3).getName());
        assertEquals("false", paramsList.get(4).getValue());
        assertEquals("hide_thread", paramsList.get(4).getName());
        assertEquals("true", paramsList.get(5).getValue());
        assertEquals("omit_script", paramsList.get(5).getName());
        assertEquals("center", paramsList.get(6).getValue());
        assertEquals("align", paramsList.get(6).getName());
        assertArrayEquals(new String[]{"related1", "related2"}, paramsList.get(7).getValue().split(","));
        assertEquals("related", paramsList.get(7).getName());
        assertEquals("en", paramsList.get(8).getValue());
        assertEquals("lang", paramsList.get(8).getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1234567890L;
        long statusId2 = 1234567891L;
        String url = "https://example.com/tweet";
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.CENTER;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest request1 = new OEmbedRequest(statusId1, url);
        request1.setMaxWidth(maxWidth);
        request1.setHideMedia(hideMedia);
        request1.setHideThread(hideThread);
        request1.setOmitScript(omitScript);
        request1.setAlign(align);
        request1.setRelated(related);
        request1.setLang(lang);

        OEmbedRequest request2 = new OEmbedRequest(statusId2, url);
        request2.setMaxWidth(maxWidth);
        request2.setHideMedia(hideMedia);
        request2.setHideThread(hideThread);
        request2.setOmitScript(omitScript);
        request2.setAlign(align);
        request2.setRelated(related);
        request2.setLang(lang);

        OEmbedRequest request3 = new OEmbedRequest(statusId1, url);
        request3.setMaxWidth(maxWidth + 1);
        request3.setHideMedia(hideMedia);
        request3.setHideThread(hideThread);
        request3.setOmitScript(omitScript);
        request3.setAlign(align);
        request3.setRelated(related);
        request3.setLang(lang);

        OEmbedRequest request4 = new OEmbedRequest(statusId1, url);
        request4.setMaxWidth(maxWidth);
        request4.setHideMedia(false);
        request4.setHideThread(hideThread);
        request4.setOmitScript(omitScript);
        request4.setAlign(align);
        request4.setRelated(related);
        request4.setLang(lang);

        OEmbedRequest request5 = new OEmbedRequest(statusId1, url);
        request5.setMaxWidth(maxWidth);
        request5.setHideMedia(hideMedia);
        request5.setHideThread(false);
        request5.setOmitScript(omitScript);
        request5.setAlign(align);
        request5.setRelated(related);
        request5.setLang(lang);

        OEmbedRequest request6 = new OEmbedRequest(statusId1, url);
        request6.setMaxWidth(maxWidth);
        request6.setHideMedia(hideMedia);
        request6.setHideThread(hideThread);
        request6.setOmitScript(false);
        request6.setAlign(align);
        request6.setRelated(related);
        request6.setLang(lang);

        OEmbedRequest request7 = new OEmbedRequest(statusId1, url);
        request7.setMaxWidth(maxWidth);
        request7.setHideMedia(hideMedia);
        request7.setHideThread(hideThread);
        request7.setOmitScript(omitScript);
        request7.setAlign(Align.LEFT);
        request7.setRelated(related);
        request7.setLang(lang);

        OEmbedRequest request8 = new OEmbedRequest(statusId1, url);
        request8.setMaxWidth(maxWidth);
        request8.setHideMedia(hideMedia);
        request8.setHideThread(hideThread);
        request8.setOmitScript(omitScript);
        request8.setAlign(align);
        request8.setRelated(Arrays.copyOf(related, related.length + 1));
        request8.setLang(lang);

        OEmbedRequest request9 = new OEmbedRequest(statusId1, url);
        request9.setMaxWidth(maxWidth);
        request9.setHideMedia(hideMedia);
        request9.setHideThread(hideThread);
        request9.setOmitScript(omitScript);
        request9.setAlign(align);
        request9.setRelated(related);
        request9.setLang("fr");

        OEmbedRequest request10 = new OEmbedRequest(statusId1, null);
        request10.setMaxWidth(maxWidth);
        request10.setHideMedia(hideMedia);
        request10.setHideThread(hideThread);
        request10.setOmitScript(omitScript);
        request10.setAlign(align);
        request10.setRelated(related);
        request10.setLang(lang);

        OEmbedRequest request11 = null;

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
        assertFalse(request1.equals(request8));
        assertFalse(request1.equals(request9));
        assertFalse(request1.equals(request10));
        assertFalse(request1.equals(request11));

        assertTrue(request1.hashCode() == request2.hashCode());
        assertFalse(request1.hashCode() == request3.hashCode());
        assertFalse(request1.hashCode() == request4.hashCode());
        assertFalse(request1.hashCode() == request5.hashCode());
        assertFalse(request1.hashCode() == request6.hashCode());
        assertFalse(request1.hashCode() == request7.hashCode());
        assertFalse(request1.hashCode() == request8.hashCode());
        assertFalse(request1.hashCode() == request9.hashCode());
        assertFalse(request1.hashCode() == request10.hashCode());
        assertNotNull(request11.hashCode());

        assertEquals(request1, request1);
        assertEquals(request1, request1);
        assertNotEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request5);
        assertNotEquals(request1, request6);
        assertNotEquals(request1, request7);
        assertNotEquals(request1, request8);
        assertNotEquals(request1, request9);
        assertNotEquals(request1, request10);
        assertNotEquals(request1, request11);
    }

    @Test
    public void testToString() {
        long statusId = 1234567890L;
        String url = "https://example.com/tweet";
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.CENTER;
        String[] related = {"related1", "related2"};
        String lang = "en";

        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        String expectedResult = "OEmbedRequest{statusId=1234567890, url='https://example.com/tweet', maxWidth=300, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[related1, related2], lang='en'}";
        assertEquals(expectedResult, request.toString());
    }
}