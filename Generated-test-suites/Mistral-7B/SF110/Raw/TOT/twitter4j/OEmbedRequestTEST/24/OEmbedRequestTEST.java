import org.junit.Assert;
import twitter4j.OEmbedRequest;
import twitter4j.twitter4jException;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        Assert.assertEquals(statusId, oEmbedRequest.getStatusId());
        Assert.assertEquals(url, oEmbedRequest.getUrl());
        Assert.assertTrue(oEmbedRequest.isHideMedia());
        Assert.assertTrue(oEmbedRequest.isHideThread());
        Assert.assertFalse(oEmbedRequest.isOmitScript());
        Assert.assertEquals(Align.NONE, oEmbedRequest.getAlign());
        Assert.assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        Assert.assertNull(oEmbedRequest.getLang());
    }
}