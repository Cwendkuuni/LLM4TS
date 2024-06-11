package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;

import twitter4j.OEmbedRequest;
import twitter4j.Align;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/123";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com/status/123");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(200);
        assertEquals(200, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1234567890L;
        long statusId2 = 9876543210L;
        String url1 = "https://example.com/status/123";
        String url2 = "https://example.com/status/456";
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId1, url1);

        // Test equality with same object
        assertEquals(oEmbedRequest1, oEmbedRequest1);

        // Test equality with equal values
        assertEquals(oEmbedRequest1, oEmbedRequest3);

        // Test inequality with different objects
        assertNotSame(oEmbedRequest1, oEmbedRequest2);

        // Test hash code consistency
        int hashCode1 = oEmbedRequest1.hashCode();
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId1, url1);
        int hashCode2 = oEmbedRequest4.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}