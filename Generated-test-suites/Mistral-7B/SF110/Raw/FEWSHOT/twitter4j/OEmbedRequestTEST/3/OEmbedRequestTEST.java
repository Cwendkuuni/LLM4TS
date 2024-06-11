package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest request = new OEmbedRequest(12345L, "http://example.com");
        assertEquals(12345L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(12345L, "http://example.com");

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
        OEmbedRequest request = new OEmbedRequest(12345L, "http://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);

        assertTrue(Arrays.asList(params).contains(new HttpParameter("id", "12345")));
        assertTrue(Arrays.asList(params).contains(new HttpParameter("url", "http://example.com")));

        assertNull(Arrays.asList(params).find(param -> param.getName().equals("maxwidth")));

        assertTrue(Arrays.asList(params).contains(new HttpParameter("hide_media", "true")));
        assertTrue(Arrays.asList(params).contains(new HttpParameter("hide_thread", "true")));
        assertNull(Arrays.asList(params).find(param -> param.getName().equals("omit_script")));

        assertTrue(Arrays.asList(params).contains(new HttpParameter("align", "none")));

        assertNull(Arrays.asList(params).find(param -> param.getName().equals("related")));

        assertNull(Arrays.asList(params).find(param -> param.getName().equals("lang")));
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "http://example.com");

        OEmbedRequest request3 = new OEmbedRequest(67890L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(12345L, "http://example.com/other");

        OEmbedRequest request5 = new OEmbedRequest(12345L, "http://example.com");
        request5.setMaxWidth(100);
        OEmbedRequest request6 = new OEmbedRequest(12345L, "http://example.com");
        request6.setMaxWidth(100);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());

        assertNotEquals(request1, request4);
        assertNotEquals(request1.hashCode(), request4.hashCode());

        assertEquals(request1, request5);
        assertEquals(request1.hashCode(), request5.hashCode());
        assertEquals(request1, request6);
        assertEquals(request1.hashCode(), request6.hashCode());
    }
}