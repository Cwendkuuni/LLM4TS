package twitter4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

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
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(200);
        assertEquals(200, request.getMaxWidth());
        request.HideMedia();
        assertFalse(request.isHideMedia());
        request.HideMedia();
        assertTrue(request.isHideMedia());
        request.align(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());
        String[] related = {"related1", "related2"};
        request.related(related);
        assertArrayEquals(related, request.getRelated());
        request.lang("en");
        assertEquals("en", request.getLang());
    }

    @Test
    public void testAppendParameter() {
        List<HttpParameter> params = new ArrayList<>();
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.appendParameter("id", "123", params);
        request.appendParameter("url", "http://example.com/embed", params);
        HttpParameter[] httpParams = request.asHttpParameterArray();
        assertEquals(2, httpParams.length);
        assertEquals("id", httpParams[0].getName());
        assertEquals("123", httpParams[0].getValue());
        assertEquals("url", httpParams[1].getName());
        assertEquals("http://example.com/embed", httpParams[1].getValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, null);

        assertTrue(request1.equals(request2));
        assertTrue(request1.hashCode() == request2.hashCode());
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(null));
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang=''}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAlignEnum() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        Align align = Align.LEFT;
        request.align(align);
        assertEquals(Align.LEFT, request.getAlign());
        assertEquals("left", request.getAlign().name().toLowerCase());
    }
}