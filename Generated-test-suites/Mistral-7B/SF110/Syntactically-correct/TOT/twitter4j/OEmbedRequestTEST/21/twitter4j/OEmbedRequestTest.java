import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/123";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertEquals(0, oEmbedRequest.getMaxWidth());
        assertEquals(true, oEmbedRequest.isHideMedia());
        assertEquals(true, oEmbedRequest.isHideThread());
        assertEquals(false, oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertEquals(new String[0], oEmbedRequest.getRelated());
        assertEquals(null, oEmbedRequest.getLang());
    }
}