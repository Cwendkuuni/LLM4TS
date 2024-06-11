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
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.setMaxWidth(50);
        assertEquals(50, request.getMaxWidth());

        request.MaxWidth(100);
        assertEquals(100, request.getMaxWidth());

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

        request.setRelated(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, request.getRelated());

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

        HttpParameter[] httpParameters = request.asHttpParameterArray();
        assertEquals(12, httpParameters.length);
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("id", "1")));
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("url", "http://example.com")));
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("maxwidth", "50")));
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("hide_media", "true")));
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("hide_thread", "true")));
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("omit_script", "false")));
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("align", "left".toLowerCase())));
        if (request.getRelated() != null) {
            assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("related", "[\"related1\",\"related2\"]")));
        }
        assertTrue(Arrays.asList(httpParameters).contains(new HttpParameter("lang", "en")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(-1);
    }

    @Test
    public void testHashCodeAndEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example2.com");

        request5.setRelated(new String[]{"related1", "related2"});
        request6.setRelated(new String[]{"related1", "related2"});
        request7.setRelated(new String[]{"related3", "related4"});

        assertEquals(request1, request1);
        assertEquals(request1, request4);
        assertEquals(request1, request5);
        assertEquals(request1, request6);

        assertNotEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request7);

        assertNotEquals(request1, new Object());

        List<HttpParameter> list1 = Arrays.asList(request1.asHttpParameterArray());
        List<HttpParameter> list2 = Arrays.asList(request2.asHttpParameterArray());
        List<HttpParameter> list3 = Arrays.asList(request3.asHttpParameterArray());
        List<HttpParameter> list4 = Arrays.asList(request4.asHttpParameterArray());
        List<HttpParameter> list5 = Arrays.asList(request5.asHttpParameterArray());
        List<HttpParameter> list6 = Arrays.asList(request6.asHttpParameterArray());
        List<HttpParameter> list7 = Arrays.asList(request7.asHttpParameterArray());

        assertArrayEquals(list1.toArray(), list1.toArray());
        assertArrayEquals(list1.toArray(), list4.toArray());
        assertArrayEquals(list1.toArray(), list5.toArray());
        assertArrayEquals(list1.toArray(), list6.toArray());

        assertNotEquals(list1.toArray(), list2.toArray());
        assertNotEquals(list1.toArray(), list3.toArray());
        assertNotEquals(list1.toArray(), list7.toArray());

        assertNotEquals(list1.toArray(), new Object[0]);

        assertFalse(request1.equals(null));
        assertFalse(request1.equals(new Object()));
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=50, hideMedia=true, hideThread=true, omitScript=false, align=LEFT, related=null, lang='null'}";
        assertEquals(expected, request.toString());
    }
}