import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        assertEquals(1L, oEmbedRequest.getStatusId());
        assertEquals("https://example.com", oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }
}