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
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
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
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        List<HttpParameter> params = Arrays.asList(request.asHttpParameterArray());

        assertEquals(13, params.size());
        assertEquals("id", params.get(0).getName());
        assertEquals("1", params.get(0).getValue());
        assertEquals("url", params.get(1).getName());
        assertEquals("http://example.com", params.get(1).getValue());
        assertEquals("maxwidth", params.get(2).getName());
        assertEquals("100", params.get(2).getValue());
        assertTrue(params.get(3).getName().equals("hide_media") || params.get(3).getName().equals("hideMedia"));
        assertTrue(params.get(3).getValue().equals("false") || params.get(3).getValue().equals("0"));
        assertTrue(params.get(4).getName().equals("hide_thread") || params.get(4).getName().equals("hideThread"));
        assertTrue(params.get(4).getValue().equals("false") || params.get(4).getValue().equals("0"));
        assertTrue(params.get(5).getName().equals("omit_script") || params.get(5).getName().equals("omitScript"));
        assertTrue(params.get(5).getValue().equals("true") || params.get(5).getValue().equals("1"));
        assertTrue(params.get(6).getName().equals("align") || params.get(6).getName().equals("Align") || params.get(6).getName().equals("align"));
        assertEquals(Align.LEFT.name(), params.get(6).getValue());
        List<HttpParameter> relatedParams = Arrays.asList(params.subList(7, 11));
        assertEquals(4, relatedParams.size());
        assertEquals("related", relatedParams.get(0).getName());
        assertEquals(StringUtil.join(related, ","), relatedParams.get(0).getValue());
        assertEquals("lang", params.get(11).getName());
        assertEquals("en", params.get(11).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(-1);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(50);

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertTrue(request2.equals(request1));
        assertTrue(request1.equals(request4));
        assertTrue(request4.equals(request1));
        assertTrue(request1.equals(request5));
        assertTrue(request5.equals(request1));
        assertFalse(request1.equals(request3));
        assertFalse(request3.equals(request1));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(50);

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode5);
        assertEquals(request1.hashCode(), request5.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang=''}";
        String result = request.toString();
        assertEquals(expected, result);
    }
}