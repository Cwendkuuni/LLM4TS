import org.junit.Assert;
import org.junit.Test;
import twitter4j.Align;
import twitter4j.OEmbedRequest;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        Assert.assertEquals(statusId, oEmbedRequest.getStatusId());
        Assert.assertEquals(url, oEmbedRequest.getUrl());
        Assert.assertTrue(oEmbedRequest.isHideMedia());
        Assert.assertTrue(oEmbedRequest.isHideThread());
        Assert.assertFalse(oEmbedRequest.isOmitScript());
        Assert.assertEquals(Align.NONE, oEmbedRequest.getAlign());
        Assert.assertNull(oEmbedRequest.getRelated());
        Assert.assertNull(oEmbedRequest.getLang());
    }
}