import static org.junit.Assert.assertEquals;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    @org.junit.Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.MaxWidth(100);
        int actualMaxWidth = request.getMaxWidth();
        assertEquals(100, actualMaxWidth);
    }

    // ... other test cases for other methods
}