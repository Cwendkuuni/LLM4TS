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
        long statusId = 1L;
        String url = "http://example.com";
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
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(10);
        assertEquals(10, request.getMaxWidth());

        request.MaxWidth(20);
        assertEquals(20, request.getMaxWidth());

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
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(10);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] parameters = request.asHttpParameterArray();
        List<String> names = Collections.nCopies(parameters.length, "name");
        List<String> values = Arrays.asList("value");
        for (int i = 0; i < parameters.length; i++) {
            assertEquals(names.get(i), parameters[i].getName());
            assertEquals(values.get(i % 2), parameters[i].getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        long statusId = 1L;
        String url = null;
        try {
            new OEmbedRequest(statusId, url);
        } catch (NullPointerException e) {
            throw new AssertionError("Should not have reached here", e);
        }
        fail("Expected NullPointerException");
    }

    @Test
    public void testEquals() {
        long statusId1 = 1L;
        long statusId2 = 2L;
        String url1 = "http://example.com";
        String url2 = "http://example.com";
        String url3 = "http://example.com/different";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request2 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request3 = new OEmbedRequest(statusId2, url1);
        OEmbedRequest request4 = new OEmbedRequest(statusId1, url2);
        OEmbedRequest request5 = new OEmbedRequest(statusId1, url3);

        assertTrue(request1.equals(request2));
        assertTrue(request1.equals(request2.asHashMap().get("oEmbedRequest").get("oEmbedRequest").get(0)));
        assertTrue(request1.equals(request1.asHashMap().get("oEmbedRequest").get("oEmbedRequest").get(0).asHashMap().get("oEmbedRequest")));

        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
        assertFalse(request1.equals(""));
        assertFalse(request1.equals((Object)null));
    }

    @Test
    public void testHashCode() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        int hashCode1 = request.hashCode();
        request.setMaxWidth(10);
        int hashCode2 = request.hashCode();
        assertEquals(hashCode1, hashCode2);

        request.MaxWidth(20);
        int hashCode3 = request.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }
}