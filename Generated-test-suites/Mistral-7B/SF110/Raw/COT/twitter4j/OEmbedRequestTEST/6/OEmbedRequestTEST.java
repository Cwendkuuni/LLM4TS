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
    public void testOEmbedRequestConstructor() {
        long statusId = 1L;
        String url = "https://www.example.com";
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
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());
        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        request.setHideMedia(false);
        assertFalse(request.isHideMedia());
        request.HideMedia(true);
        assertTrue(request.isHideMedia());
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        request.setHideThread(false);
        assertFalse(request.isHideThread());
        request.HideThread(true);
        assertTrue(request.isHideThread());
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        request.setOmitScript(true);
        assertTrue(request.isOmitScript());
        request.omitScript(false);
        assertFalse(request.isOmitScript());
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());
        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());
        request.align(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());
        request.align(Align.NONE);
        assertEquals(Align.NONE, request.getAlign());
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());
        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        request.setLang("en");
        assertEquals("en", request.getLang());
        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        OEmbedRequest request = new OEmbedRequest(1L, null);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "https://www.example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "https://www.example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "https://www.example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "https://www.example.com");
        request5.setMaxWidth(100);
        OEmbedRequest request6 = new OEmbedRequest(1L, "https://www.example.com");
        request6.setRelated(new String[]{"related"});
        OEmbedRequest request7 = new OEmbedRequest(1L, "https://www.example.com");
        request7.setLang("fr");
        assertTrue(request1.equals(request2));
        assertTrue(request1.equals(request4));
        assertTrue(request1.equals(request5));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "https://www.example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "https://www.example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "https://www.example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "https://www.example.com");
        request5.setMaxWidth(100);
        OEmbedRequest request6 = new OEmbedRequest(1L, "https://www.example.com");
        request6.setRelated(new String[]{"related"});
        OEmbedRequest request7 = new OEmbedRequest(1L, "https://www.example.com");
        request7.setLang("fr");
        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();
        int hashCode6 = request6.hashCode();
        int hashCode7 = request7.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode4);
        assertEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode6);
        assertNotEquals(hashCode1, hashCode7);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        String expected = "OEmbedRequest{statusId=1, url='https://www.example.com/', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://www.example.com");
        List<HttpParameter> params = Arrays.asList(request.asHttpParameterArray());
        List<String> names = Collections.singletonList("id");
        List<String> values = Arrays.asList("1");
        assertEquals(names, Arrays.asList(params.get(0).getName()));
        assertEquals(values, Arrays.asList(params.get(0).getValue()));
        names = Arrays.asList("url");
        values = Arrays.asList("https://www.example.com");
        assertEquals(names, Arrays.asList(params.get(1).getName()));
        assertEquals(values, Arrays.asList(params.get(1).getValue()));
    }
}