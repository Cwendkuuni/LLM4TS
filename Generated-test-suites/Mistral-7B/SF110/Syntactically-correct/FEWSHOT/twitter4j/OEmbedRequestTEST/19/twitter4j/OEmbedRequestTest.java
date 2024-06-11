package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

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
        assert ArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(42);
        assertEquals(42, request.getMaxWidth());
        OEmbedRequest maxWidthRequest = request.MaxWidth(7);
        assertEquals(7, maxWidthRequest.getMaxWidth());
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertTrue(request.isHideMedia());
        request.setHideMedia(false);
        assertFalse(request.isHideMedia());
        OEmbedRequest hideMediaRequest = request.HideMedia(true);
        assertTrue(hideMediaRequest.isHideMedia());
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertTrue(request.isHideThread());
        request.setHideThread(false);
        assertFalse(request.isHideThread());
        OEmbedRequest hideThreadRequest = request.HideThread(true);
        assertTrue(hideThreadRequest.isHideThread());
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertFalse(request.isOmitScript());
        request.setOmitScript(true);
        assertTrue(request.isOmitScript());
        OEmbedRequest omitScriptRequest = request.omitScript(false);
        assertFalse(omitScriptRequest.isOmitScript());
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertNull(request.getAlign());
        OEmbedRequest alignRequest = request.align(Align.LEFT);
        assertEquals(Align.LEFT, alignRequest.getAlign());
        OEmbedRequest alignNoneRequest = request.align(null);
        assertNull(alignNoneRequest.getAlign());
    }

    @Test
    public void testRelated() {
        String[] related = new String[]{"related1", "related2"};
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());
        OEmbedRequest relatedRequest = request.related(new String[]{"related3"});
        assertArrayEquals(new String[]{"related3"}, relatedRequest.getRelated());
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertNull(request.getLang());
        OEmbedRequest langRequest = request.lang("en");
        assertEquals("en", langRequest.getLang());
        OEmbedRequest langNullRequest = request.lang(null);
        assertNull(langNullRequest.getLang());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        assertTrue(request1.equals(request2));
        request2.setMaxWidth(7);
        assertFalse(request1.equals(request2));
        request2.setMaxWidth(42);
        assertTrue(request1.equals(request2));
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        assertFalse(request1.equals(request3));
        OEmbedRequest request4 = new OEmbedRequest(1L, null);
        assertFalse(request1.equals(request4));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        assertEquals(request1.hashCode(), request2.hashCode());
        request2.setMaxWidth(7);
        assertNotEquals(request1.hashCode(), request2.hashCode());
        request2.setMaxWidth(42);
        assertEquals(request1.hashCode(), request2.hashCode());
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        assertNotEquals(request1.hashCode(), request3.hashCode());
        OEmbedRequest request4 = new OEmbedRequest(1L, null);
        assertNotEquals(request1.hashCode(), request4.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, request.toString());
        request.setMaxWidth(42);
        request.setHideMedia(false);
        request.setHideThread(false);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        String expectedWithRelated = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=42, hideMedia=false, hideThread=false, omitScript=false, align=NONE, related=[related1, related2], lang='null'}";
        assertEquals(expectedWithRelated, request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        List<HttpParameter> expectedParams = Arrays.asList(
            new HttpParameter("id", "1"),
            new HttpParameter("url", "http://example.com"),
            new HttpParameter("maxwidth", "0"),
            new HttpParameter("hide_media", "true"),
            new HttpParameter("hide_thread", "true"),
            new HttpParameter("omit_script", "false"),
            new HttpParameter("align", "NONE"),
            new HttpParameter("lang", "null")
        );
        assertEquals(expectedParams, Arrays.asList(params));
        request.setMaxWidth(42);
        request.setHideMedia(false);
        request.setHideThread(false);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        HttpParameter[] paramsWithRelated = request.asHttpParameterArray();
        expectedParams = Arrays.asList(
            new HttpParameter("id", "1"),
            new HttpParameter("url", "http://example.com"),
            new HttpParameter("maxwidth", "42"),
            new HttpParameter("hide_media", "false"),
            new HttpParameter("hide_thread", "false"),
            new HttpParameter("omit_script", "false"),
            new HttpParameter("align", "NONE"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        );
        assertEquals(expectedParams, Arrays.asList(paramsWithRelated));
    }
}