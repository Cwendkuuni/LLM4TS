import org.junit.Assert;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        Assert.assertEquals(1L, request.getStatusId());
        Assert.assertEquals("https://example.com", request.getUrl());
    }

    @Test
    public void testOEmbedRequestWithArgumentsConstruction() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com")
                .setMaxWidth(100)
                .setHideMedia(false)
                .setHideThread(false)
                .setOmitScript(true)
                .setAlign(Align.LEFT)
                .setRelated(new String[]{"related1", "related2"})
                .setLang("en");

        Assert.assertEquals(100, request.getMaxWidth());
        Assert.assertFalse(request.isHideMedia());
        Assert.assertFalse(request.isHideThread());
        Assert.assertTrue(request.isOmitScript());
        Assert.assertEquals(Align.LEFT, request.getAlign());
        Assert.assertArrayEquals(new String[]{"related1", "related2"}, request.getRelated());
        Assert.assertEquals("en", request.getLang());
        Assert.assertEquals(1L, request.getStatusId());
        Assert.assertEquals("https://example.com", request.getUrl());
    }

    // ... other test cases for methods
}