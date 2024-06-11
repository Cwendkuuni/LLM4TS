import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 123;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertNotNull(oEmbedRequest);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
    }

    // Tests for setter methods
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testMaxWidthMethodChain() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest oEmbedRequestWithMaxWidth = oEmbedRequest.MaxWidth(100);
        assertEquals(100, oEmbedRequestWithMaxWidth.getMaxWidth());
        assertEquals(oEmbedRequest, oEmbedRequestWithMaxWidth);
    }

    // Tests for other setter methods
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());
    }

    // ... other setter method tests

    // Edge cases and exception handling tests
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxWidthNegativeValue() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setUrl(null);
    }

    private long getStatusId(OEmbedRequest oEmbedRequest) {
        return oEmbedRequest.statusId;
    }

    private String getUrl(OEmbedRequest oEmbedRequest) {
        return oEmbedRequest.url;
    }

    private int getMaxWidth(OEmbedRequest oEmbedRequest) {
        return oEmbedRequest.maxWidth;
    }

    private boolean isHideMedia(OEmbedRequest oEmbedRequest) {
        return oEmbedRequest.hideMedia;
    }

    // ... other helper methods for getting values from OEmbedRequest
}