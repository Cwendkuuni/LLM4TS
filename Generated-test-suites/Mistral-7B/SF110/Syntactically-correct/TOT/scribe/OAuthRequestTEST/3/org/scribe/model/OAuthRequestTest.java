import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        assertNull(request.getOauthParameters());
        assertFalse(request.hasOAuthParameters());

        OAuthRequest requestWithOAuthParameters = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        requestWithOAuthParameters.addOAuthParameter("oauth_consumer_key", "testKey");
        requestWithOAuthParameters.addOAuthParameter("oauth_token", "testToken");
        requestWithOAuthParameters.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        requestWithOauthParameters.addOAuthParameter("oauth_version", "1.0");
        requestWithOAuthParameters.addOAuthParameter("scope", "testScope");
        Map<String, String> oauthParameters = requestWithOAuthParameters.getOauthParameters();
        assertEquals(5, oauthParameters.size());
        assertTrue(requestWithOAuthParameters.hasOAuthParameters());
        assertEquals("oauth_consumer_key", oauthParameters.keySet().iterator().next());
    }

    @Test
    public void testIllegalOAuthParameterKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetOAuthParametersWithEmptyMap() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey");
        request.addOAuthParameter("oauth_token", "testToken");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_version", "1.0");
        request.addOAuthParameter("scope", "testScope");
        String toString = request.toString();
        assertEquals("@OAuthRequest(GET, http://example.com)", toString);
    }
}