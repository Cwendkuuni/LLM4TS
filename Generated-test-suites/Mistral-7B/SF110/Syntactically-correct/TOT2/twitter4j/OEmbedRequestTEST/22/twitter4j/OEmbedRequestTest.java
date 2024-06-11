package twitter4j.test;

import org.junit.Test;
import org.junit.Assert;
import org.junit.experimental.categories.Category;
import twitter4j.OEmbedRequest;
import twitter4j.Align;
import twitter4j.HttpParameter;

@Category(IntegrationTests.class)
public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertNotNull(oEmbedRequest);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.getMaxWidth());
    }

    // Test other methods similarly

    @Test(expected = IllegalArgumentException.class)
    public void testOEmbedRequestNullStatusId() {
        try {
            new OEmbedRequest(0L, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    // Add test cases for other edge cases and exceptions

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        // Test the length and contents of the httpParameters array
    }
}