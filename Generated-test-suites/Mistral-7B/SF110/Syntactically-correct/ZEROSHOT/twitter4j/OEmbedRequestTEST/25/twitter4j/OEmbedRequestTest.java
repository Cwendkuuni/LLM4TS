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
        assertFalse(request.isHideMedia());
        assertFalse(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.setMaxWidth(300);
        assertEquals(300, request.getMaxWidth());
        request.MaxWidth(400);
        assertEquals(400, request.getMaxWidth());

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

        request.setAlign(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());
        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());
        request.related(new String[]{"related3"});
        assertArrayEquals(new String[]{"related3"}, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());
        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] httpParameters = request.asHttpParameterArray();

        assertEquals(12, httpParameters.length);
        List<String> expectedParameterNames = Collections.nCopies(12, "name");
        List<Object> expectedParameterValues = Arrays.asList(1L, "http://example.com", 300, false, false, false, Align.NONE, new String[]{"related1", "related2"}, "en");

        for (int i = 0; i < httpParameters.length; i++) {
            HttpParameter httpParameter = httpParameters[i];
            String name = httpParameter.getName();
            Object value = httpParameter.getValue();
            int index = expectedParameterNames.indexOf(name);
            assertEquals(expectedParameterNames.get(index), name);
            assertEquals(expectedParameterValues.get(index), value);
        }
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://different-example.com");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertTrue(request1.equals(request4));
        assertFalse(request1.equals(request5));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://different-example.com");

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expectedString = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expectedString, request.toString());
    }
}