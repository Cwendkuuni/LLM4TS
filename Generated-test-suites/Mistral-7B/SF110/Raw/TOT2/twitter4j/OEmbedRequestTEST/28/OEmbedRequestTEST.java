import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 123L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        // Additional assertions for other fields if needed
    }
}