
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://www.example.com/status/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertEquals(Align.NONE, request.getAlign());
        assertEquals(new boolean[0], request.getRelated());
        assertEquals(null, request.getLang());
        assertEquals(12, request.asHttpParameterArray().length);
    }

    @Test
    public void testSetMaxWidth() {
        long statusId = 1L;
        String url = "https://www.example.com/status/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.MaxWidth(200);

        assertEquals(200, request.getMaxWidth());
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        long statusId2 = 2L;
        String url1 = "https://www.example.com/status/1";
        String url2 = "https://www.example.com/status/2";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);

        assertEquals(request1, request3);
        assertEquals(request1.hashCode(), request3.hashCode());

        request1.setMaxWidth(100);
        request3.setMaxWidth(100);
        assertEquals(request1, request3);
        assertEquals(request1.hashCode(), request3.hashCode());

        request1.setMaxWidth(200);
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());

        request3.setMaxWidth(200);
        assertEquals(request1, request3);
        assertEquals(request1.hashCode(), request3.hashCode());
    }
}