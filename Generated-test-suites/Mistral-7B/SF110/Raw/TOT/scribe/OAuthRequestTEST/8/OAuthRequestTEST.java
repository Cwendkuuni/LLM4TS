import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    // Test case for constructor
    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "https://example.com");
        assertNotNull(request);
        assertEquals(OAuthRequest.Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }

    // Test case for addOAuthParameter method
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.POST, "https://example.com");
        request.addOAuthParameter("oauth_nonce", "abc123");
        Map<String, String> parameters = request.getOauthParameters();
        assertEquals("abc123", parameters.get("oauth_nonce"));
        assertEquals(1, parameters.size());
    }

    // Test case for checkKey method
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyIllegalArgumentException() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "https://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    // Test case for getOauthParameters method
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_nonce", "abc123");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> parameters = request.getOauthParameters();
        assertEquals(2, parameters.size());
        assertTrue(parameters.containsKey("oauth_nonce"));
        assertTrue(parameters.containsKey("scope"));
    }
}