package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInitialization() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.setMaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        assertFalse(request.isHideMedia());

        request.setHideThread(false);
        assertFalse(request.isHideThread());

        request.setOmitScript(true);
        assertTrue(request.isOmitScript());

        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());
    }

    @Test
    public void testToHttpParameters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com")
                .setMaxWidth(200)
                .setHideMedia(false)
                .setHideThread(false)
                .setOmitScript(true)
                .setAlign(Align.LEFT)
                .setRelated(new String[]{"related1", "related2"})
                .setLang("en");

        HttpParameter[] httpParameters = request.asHttpParameterArray();
        List<String> expectedParameterNames = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");

        for (HttpParameter httpParameter : httpParameters) {
            assertTrue(expectedParameterNames.contains(httpParameter.getName()));
            if (httpParameter.getValue() != null) {
                assertEquals(httpParameter.getValue(), httpParameter.getName().equals("related") ? Arrays.toString(request.getRelated()) : httpParameter.getValue());
            } else {
                assertTrue(httpParameter.getValue() == null || (httpParameter.getName().equals("related") && request.getRelated() == null));
            }
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com")
                .setMaxWidth(200)
                .setHideMedia(false)
                .setHideThread(false)
                .setOmitScript(true)
                .setAlign(Align.LEFT)
                .setRelated(new String[]{"related1", "related2"})
                .setLang("en");

        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com")
                .setMaxWidth(200)
                .setHideMedia(false)
                .setHideThread(false)
                .setOmitScript(true)
                .setAlign(Align.LEFT)
                .setRelated(new String[]{"related1", "related2"})
                .setLang("en");

        OEmbedRequest request3 = new OEmbedRequest(1L, "http://example.com")
                .setMaxWidth(201)
                .setHideMedia(false)
                .setHideThread(false)
                .setOmitScript(true)
                .setAlign(Align.LEFT)
                .setRelated(new String[]{"related1", "related2"})
                .setLang("en");

        OEmbedRequest request4 = new OEmbedRequest(2L, "http://example.com")
                .setMaxWidth(200)
                .setHideMedia(false)
                .setHideThread(false)
                .setOmitScript(true)
                .setAlign(Align.LEFT)
                .setRelated(new String[]{"related1", "related2"})
                .setLang("en");

        assertTrue(request1.equals(request2));
        assertTrue(request1.hashCode() == request2.hashCode());

        assertFalse(request1.equals(request3));
        assertFalse(request1.hashCode() == request3.hashCode());

        assertFalse(request1.equals(request4));
        assertFalse(request1.hashCode() == request4.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com")
                .setMaxWidth(200)
                .setHideMedia(false)
                .setHideThread(false)
                .setOmitScript(true)
                .setAlign(Align.LEFT)
                .setRelated(new String[]{"related1", "related2"})
                .setLang("en");

        String expectedOutput = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";

        assertEquals(expectedOutput, request.toString());
    }
}