import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        // Given
        long statusId = 1234567890L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        // Then
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        // other assertions as needed for other instance variables
    }
}