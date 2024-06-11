import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("https://example.com", request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOMitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }
}