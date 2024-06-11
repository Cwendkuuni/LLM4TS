package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
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
    public void testMethodsSettersAndGetters() {
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
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");

        HttpParameter[] httpParameters = request.asHttpParameterArray();
        assertEquals(12, httpParameters.length);

        assertEquals("id", httpParameters[0].getName());
        assertEquals("1", httpParameters[0].getValue());

        assertEquals("url", httpParameters[1].getName());
        assertEquals("http://example.com", httpParameters[1].getValue());

        assertEquals("maxwidth", httpParameters[2].getName());
        assertEquals("100", httpParameters[2].getValue());

        assertEquals("hide_media", httpParameters[3].getName());
        assertEquals("false", httpParameters[3].getValue());

        assertEquals("hide_thread", httpParameters[4].getName());
        assertEquals("true", httpParameters[4].getValue());

        assertEquals("omit_script", httpParameters[5].getName());
        assertEquals("true", httpParameters[5].getValue());

        assertEquals("align", httpParameters[6].getName());
        assertEquals("left", httpParameters[6].getValue());

        assertEquals("related", httpParameters[7].getName());
        assertEquals("related1,related2", httpParameters[7].getValue());

        assertEquals("lang", httpParameters[8].getName());
        assertEquals("en", httpParameters[8].getValue());

        assertNull(httpParameters[9]);
        assertNull(httpParameters[10]);
        assertNull(httpParameters[11]);
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");

        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertEquals(request2, request1);

        assertNotEquals(request1, request3);
        assertNotEquals(request3, request1);
        assertNotEquals(request3, request2);

        request1.setMaxWidth(100);
        request2.setMaxWidth(200);
        assertNotEquals(request1, request2);

        request1.setHideMedia(false);
        request2.setHideMedia(true);
        assertNotEquals(request1, request2);

        request1.setAlign(Align.LEFT);
        request2.setAlign(Align.CENTER);
        assertNotEquals(request1, request2);

        request1.setLang("en");
        request2.setLang("fr");
        assertNotEquals(request1, request2);

        request1.setRelated(new String[]{"related1", "related2"});
        request2.setRelated(new String[]{"related1", "related3"});
        assertNotEquals(request1, request2);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");

        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=true, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAlignEnum() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        assertEquals(Align.NONE, request.getAlign());
        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());
        request.setAlign(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());
        request.setAlign(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());
    }
}