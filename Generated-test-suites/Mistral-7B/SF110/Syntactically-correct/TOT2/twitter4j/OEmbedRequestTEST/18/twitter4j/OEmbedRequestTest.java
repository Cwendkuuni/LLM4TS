package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://test.com");
        oEmbedRequest.setMaxWidth(120);
        int expectedMaxWidth = 120;
        int actualMaxWidth = oEmbedRequest.getMaxWidth();
        assertEquals(expectedMaxWidth, actualMaxWidth);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxWidthNegative() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://test.com");
        oEmbedRequest.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testSetMaxWidthNull() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, null);
        oEmbedRequest.setMaxWidth(120);
    }

    @Test
    public void testSetMaxWidthEmptyURL() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "");
        oEmbedRequest.setMaxWidth(120);
        int expectedMaxWidth = 120;
        int actualMaxWidth = oEmbedRequest.getMaxWidth();
        assertEquals(expectedMaxWidth, actualMaxWidth);
    }

    @Test
    public void testSetMaxWidthTwice() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://test.com");
        oEmbedRequest.setMaxWidth(120);
        int expectedMaxWidth = 120;
        int actualMaxWidth = oEmbedRequest.getMaxWidth();
        assertEquals(expectedMaxWidth, actualMaxWidth);

        oEmbedRequest.setMaxWidth(150);
        int actualMaxWidthAfterSetAgain = oEmbedRequest.getMaxWidth();
        assertEquals(expectedMaxWidth, actualMaxWidthAfterSetAgain);
    }

    // Add test cases for other methods here
}