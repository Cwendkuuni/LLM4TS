package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
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
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());

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

        Align align = Align.CENTER;
        request.setAlign(align);
        assertEquals(align, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        List<String> names = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (int i = 0; i < params.length; i++) {
            String name = names.get(i);
            String value = null;
            if (i < 8) {
                value = String.valueOf(request.getClass().getDeclaredField(name).get(request));
            } else {
                if (i == 7) {
                    value = request.getAlign().name().toLowerCase();
                } else {
                    value = Arrays.toString(request.getRelated());
                }
            }
            assertEquals(name, params[i].getName());
            assertEquals(value, params[i].getValue());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "https://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);

        long statusId2 = 2L;
        String url2 = "https://example.com";
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);

        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);

        OEmbedRequest request4 = new OEmbedRequest(statusId1, url1);
        request4.setMaxWidth(100);
        request4.setHideMedia(false);
        request4.setHideThread(true);
        request4.setOmitScript(true);
        request4.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request4.setRelated(related);
        request4.setLang("en");

        OEmbedRequest request5 = new OEmbedRequest(statusId1, url1);
        request5.setMaxWidth(200);

        OEmbedRequest request6 = new OEmbedRequest(statusId2, url1);

        List<OEmbedRequest> requests = Arrays.asList(request1, request2, request3, request4, request5, request6);
        for (OEmbedRequest request : requests) {
            for (OEmbedRequest otherRequest : requests) {
                if (request == otherRequest) {
                    assertTrue(request.equals(otherRequest));
                    assertEquals(request.hashCode(), otherRequest.hashCode());
                } else {
                    assertFalse(request.equals(otherRequest));
                    assertNotSame(request.hashCode(), otherRequest.hashCode());
                }
            }
        }
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        String expected = "OEmbedRequest{statusId=1, url='" + url + "', maxWidth=100, hideMedia=false, hideThread=true, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expected, request.toString());
    }

}