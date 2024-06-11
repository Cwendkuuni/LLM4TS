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
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.setMaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.HideMedia();
        assertEquals(false, request.isHideMedia());

        request.HideThread();
        assertEquals(false, request.isHideThread());

        request.omitScript();
        assertEquals(true, request.isOmitScript());

        request.align(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.related(related);
        assertArrayEquals(related, request.getRelated());

        request.lang("en");
        assertEquals("en", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        HttpParameter[] httpParams = request.asHttpParameterArray();
        assertEquals(12, httpParams.length);

        assertEquals("id", httpParams[0].getName());
        assertEquals(String.valueOf(request.getStatusId()), httpParams[0].getValue());

        assertEquals("url", httpParams[1].getName());
        assertEquals(request.getUrl(), httpParams[1].getValue());

        assertEquals("maxwidth", httpParams[2].getName());
        assertEquals(String.valueOf(request.getMaxWidth()), httpParams[2].getValue());

        assertEquals("hide_media", httpParams[3].getName());
        assertEquals(String.valueOf(request.isHideMedia()), httpParams[3].getValue());

        assertEquals("hide_thread", httpParams[4].getName());
        assertEquals(String.valueOf(request.isHideThread()), httpParams[4].getValue());

        assertEquals("omit_script", httpParams[5].getName());
        assertEquals(String.valueOf(request.isOmitScript()), httpParams[5].getValue());

        assertEquals("align", httpParams[6].getName());
        assertEquals(request.getAlign().name().toLowerCase(), httpParams[6].getValue());

        if (request.getRelated().length > 0) {
            assertEquals("related", httpParams[7].getName());
            assertEquals(StringUtil.join(request.getRelated()), httpParams[7].getValue());
        } else {
            assertNull(httpParams[7]);
        }

        assertEquals("lang", httpParams[8].getName());
        assertEquals(request.getLang(), httpParams[8].getValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");

        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        request4.setMaxWidth(200);
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(201);
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setUrl("http://different.com");
        OEmbedRequest request7 = new OEmbedRequest(1L, null);

        OEmbedRequest request8 = request1;

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
        assertTrue(request1.equals(request8));

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();
        int hashCode6 = request6.hashCode();
        int hashCode7 = request7.hashCode();
        int hashCode8 = request8.hashCode();

        assertEquals(hashCode1, hashCode8);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
        assertNotEquals(hashCode1, hashCode7);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang=''}";
        assertEquals(expected, request.toString());
    }
}