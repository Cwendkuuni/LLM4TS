import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertEquals(new String[0], oEmbedRequest.getRelated());
        assertEquals(false, oEmbedRequest.isHideMedia());
        assertEquals(false, oEmbedRequest.isHideThread());
        assertEquals(false, oEmbedRequest.isOmitScript());
        assertEquals(0, oEmbedRequest.getMaxWidth());
        assertEquals(null, oEmbedRequest.getLang());
    }
}