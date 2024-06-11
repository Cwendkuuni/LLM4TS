import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.Align;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        assertInstanceOf(OEmbedRequest.class, request);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());
    }

    @Test
    public void testMaxWidthNegative() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        assertThrows(IllegalArgumentException.class, () -> request.setMaxWidth(-1));
    }

    @Test
    public void testMaxWidthZero() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setMaxWidth(0);
        assertEquals(0, request.getMaxWidth());
    }

    @Test
    public void testMaxWidthIntMax() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setMaxWidth(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, request.getMaxWidth());
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.isHideMedia());
    }

    @Test
    public void testSetHideMediaFalse() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.isHideMedia());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "https://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "https://example.com");
        request4.setMaxWidth(100);
        request4.setHideMedia(false);
        request4.setHideThread(false);
        request4.setOmitScript(true);
        request4.setAlign(Align.LEFT);
        request4.setRelated(new String[]{"test1", "test2"});
        request4.setLang("en");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
    }
}