import static org.junit.Assert.*;
import static twitter4j.OEmbedRequest.Align.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
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

        request.setAlign(LEFT);
        assertEquals(LEFT, request.getAlign());

        request.align(RIGHT);
        assertEquals(RIGHT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);
        List<String> names = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (int i = 0; i < params.length; i++) {
            assertEquals(names.get(i), params[i].getName());
        }
    }

    @Test
    public void testEquality() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setLang("fr");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertTrue(request2.equals(request1));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
        assertFalse(request5.equals(request1));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setLang("fr");

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode2, hashCode3);
        assertNotEquals(hashCode2, hashCode4);
        assertNotEquals(hashCode2, hashCode5);
        assertNotEquals(hashCode3, hashCode4);
        assertNotEquals(hashCode3, hashCode5);
        assertNotEquals(hashCode4, hashCode5);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());

        request.setMaxWidth(100);
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());

        request.HideMedia(false);
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());

        request.HideThread(false);
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());

        request.omitScript(true);
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=true, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());

        request.setAlign(LEFT);
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[], lang='null'}";
        assertEquals(expected, request.toString());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='null'}";
        assertEquals(expected, request.toString());

        request.setLang("en");
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expected, request.toString());
    }
}