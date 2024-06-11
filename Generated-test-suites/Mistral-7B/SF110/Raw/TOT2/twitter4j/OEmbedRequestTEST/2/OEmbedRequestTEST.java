package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1234567890L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        Assert.assertEquals(statusId, oEmbedRequest.getStatusId());
        Assert.assertEquals(url, oEmbedRequest.getUrl());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        Assert.assertEquals(100, oEmbedRequest.getMaxWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setMaxWidth(-1);
    }

    @Test
    public void testSetNullUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setUrl(null);
        Assert.assertNull(oEmbedRequest.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setUrl("");
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        long statusId = 1234567890L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteStream);
        oos.writeObject(oEmbedRequest);
        oos.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInput);
        OEmbedRequest deserialized = (OEmbedRequest) ois.readObject();
        ois.close();

        Assert.assertEquals(statusId, deserialized.getStatusId());
        Assert.assertEquals(url, deserialized.getUrl());
    }

    // other methods testing will be added in next steps
}