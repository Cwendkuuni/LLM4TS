package twitter4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertEquals(new String[0], oEmbedRequest.getRelated());
        assertEquals(null, oEmbedRequest.getLang());
        assertEquals(true, oEmbedRequest.isHideMedia());
        assertEquals(true, oEmbedRequest.isHideThread());
        assertEquals(false, oEmbedRequest.isOmitScript());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.getMaxWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOrEmptyUrl() {
        long statusId = 1L;
        String url = null; // or an empty string
        try {
            new OEmbedRequest(statusId, url);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Do nothing
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMaxWidth() {
        long statusId = 1L;
        String url = "https://example.com";
        try {
            new OEmbedRequest(statusId, url).setMaxWidth(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Do nothing
        }
    }
}