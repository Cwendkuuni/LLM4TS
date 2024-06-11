import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    // Test case for constructor
    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        assertEquals(OAuthRequest.Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }

    // Test case for addOAuthParameter method with invalid key
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "testValue");
    }

    // Test case for addOAuthParameter method with existing key
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithExistingKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey1");
        request.addOAuthParameter("oauth_consumer_key", "testKey2");
    }

    // Test case for getOauthParameters method
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey1");
        request.addOAuthParameter("oauth_nonce", "testNonce1");
        request.addOAuthParameter("scope", "testScope");
        Map<String, String> parameters = request.getOauthParameters();
        assertEquals("testKey1", parameters.get("oauth_consumer_key"));
        assertEquals("testNonce1", parameters.get("oauth_nonce"));
        assertEquals("testScope", parameters.get("scope"));
        assertEquals(3, parameters.size());
    }
}