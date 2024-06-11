import static org.junit.Assert.assertEquals;
import twitter4j.OEmbedRequest;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/123";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertEquals(Integer.MAX_VALUE, oEmbedRequest.getMaxWidth());
        assertEquals(true, oEmbedRequest.isHideMedia());
        assertEquals(true, oEmbedRequest.isHideThread());
        assertEquals(false, oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertEquals(new String[0], oEmbedRequest.getRelated());
        assertEquals(null, oEmbedRequest.getLang());
    }
}