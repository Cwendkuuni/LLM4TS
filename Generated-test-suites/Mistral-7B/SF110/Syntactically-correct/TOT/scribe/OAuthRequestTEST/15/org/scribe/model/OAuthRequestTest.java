// Test case for constructor and getters
import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(OAuthRequest.Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getOauthParameters());
    }

    // Test case for addOAuthParameter method
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.POST, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals("testKey", oauthParameters.get("oauth_consumer_key"));
    }
}

