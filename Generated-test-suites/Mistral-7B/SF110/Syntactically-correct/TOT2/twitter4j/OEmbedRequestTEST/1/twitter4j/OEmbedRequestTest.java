import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.Align;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://testurl.com", request.getUrl());
    }

    @Test
    public void testSetAndGetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());
    }

    @Test
    public void testSetAndGetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        request.setHideMedia(false);
        assertFalse(request.isHideMedia());
    }

    @Test
    public void testSetAndGetHideThread() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        request.setHideThread(false);
        assertFalse(request.isHideThread());
    }

    @Test
    public void testSetAndGetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        request.setOmitScript(true);
        assertTrue(request.isOmitScript());
    }

    @Test
    public void testSetAndGetAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        Align align = Align.LEFT;
        request.setAlign(align);
        assertEquals(align, request.getAlign());
    }

    @Test
    public void testSetAndGetRelated() {
        String[] related = {"http://relatedURL1.com", "http://relatedURL2.com"};
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        request.setRelated(related);
        assertEquals(related, request.getRelated());
    }

    // ... Test cases for edge cases and exception handling scenarios ...

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStatusId() {
        new OEmbedRequest(-1L, "http://testurl.com");
    }

    @Test
    public void testNullUrl() {
        OEmbedRequest request = new OEmbedRequest(1L, null);
        assertNull(request.getUrl());
    }

    // ... More test cases for edge cases and exception handling scenarios ...

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://testurl.com");
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = {"http://relatedURL1.com", "http://relatedURL2.com"};
        request.setRelated(related);
        String expected = "OEmbedRequest{statusId=1, url='http://testurl.com', maxWidth=100, hideMedia=false, hideThread=true, omitScript=true, align=LEFT, related=[http://relatedURL1.com, http://relatedURL2.com], lang=''}";
        assertEquals(expected, request.toString());
    }
}