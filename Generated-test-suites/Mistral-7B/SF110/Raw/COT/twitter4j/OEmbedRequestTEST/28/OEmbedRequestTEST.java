package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

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
        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        request.HideMedia(true);
        assertEquals(true, request.isHideMedia());

        request.setHideThread(false);
        request.HideThread(true);
        assertEquals(true, request.isHideThread());

        request.setOmitScript(true);
        request.omitScript(false);
        assertEquals(false, request.isOmitScript());

        request.setAlign(Align.LEFT);
        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        request.setLang("en");
        request.lang("fr");
        assertEquals("fr", request.getLang());

        HttpParameter[] parameters = request.asHttpParameterArray();
        assertEquals(12, parameters.length);
        for (HttpParameter parameter : parameters) {
            System.out.println(parameter.getName() + "=" + parameter.getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        OEmbedRequest request = new OEmbedRequest(1L, null);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(200);
        OEmbedRequest request6 = new OEmbedRequest(1L, null);

        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request5);
        assertNotEquals(request1, request6);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(200);
        OEmbedRequest request6 = new OEmbedRequest(1L, null);

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();
        int hashCode6 = request6.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        String actual = request.toString();
        assertEquals(expected, actual);
    }
}