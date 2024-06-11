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
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(123);
        assertEquals(123, request.getMaxWidth());
        request.MaxWidth(456);
        assertEquals(456, request.getMaxWidth());
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
        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);
        assertEquals("id", params[0].getName());
        assertEquals(String.valueOf(request.getStatusId()), params[0].getValue());
        assertEquals("url", params[1].getName());
        assertEquals(request.getUrl(), params[1].getValue());
        assertTrue(params[2].getName().startsWith("maxwidth"));
        assertEquals(String.valueOf(request.getMaxWidth()), params[2].getValue());
        assertEquals("hide_media", params[3].getName());
        assertEquals(String.valueOf(request.isHideMedia()), params[3].getValue());
        assertEquals("hide_thread", params[4].getName());
        assertEquals(String.valueOf(request.isHideThread()), params[4].getValue());
        assertEquals("omit_script", params[5].getName());
        assertEquals(String.valueOf(request.isOmitScript()), params[5].getValue());
        assertNull(params[6].getName());
        assertNull(params[6].getValue());
        assertNull(params[7].getName());
        assertNull(params[7].getValue());
        assertNull(params[8].getName());
        assertNull(params[8].getValue());
        assertNull(params[9].getName());
        assertNull(params[9].getValue());
        assertNull(params[10].getName());
        assertNull(params[10].getValue());
        if (request.getRelated() != null) {
            String[] expectedRelated = request.getRelated();
            for (int i = 0; i < expectedRelated.length; i++) {
                assertEquals("related[" + i + "]", params[11].getName());
                assertEquals(expectedRelated[i], params[11].getValue());
            }
        }
        else {
            assertNull(params[11].getName());
            assertNull(params[11].getValue());
        }
        assertEquals("lang", params[12].getName());
        assertEquals(request.getLang(), params[12].getValue());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "https://example.com");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "https://example.com");

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
        assertNotSame(hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAlignEnumValues() {
        for (Align align : OEmbedRequest.Align.values()) {
            assertEquals(align.name().toLowerCase(), align.toString().toLowerCase());
        }
    }
}