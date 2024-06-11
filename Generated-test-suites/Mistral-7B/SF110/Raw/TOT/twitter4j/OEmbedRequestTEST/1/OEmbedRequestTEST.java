package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class OEmbedRequestTest {
    
    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 123456L;
        String url = "https://example.com/status/123456";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456L, "https://example.com/status/123456");
        request.setMaxWidth(100);
        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456L, "https://example.com/status/123456");
        OEmbedRequest request2 = new OEmbedRequest(123456L, "https://example.com/status/123456");
        OEmbedRequest request3 = new OEmbedRequest(123457L, "https://example.com/status/123456");
        OEmbedRequest request4 = new OEmbedRequest(123456L, "https://example.com/status/123457");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
    }
}