import org.junit.Test;
import org.scribe.model.OAuthRequest;
import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
        assertNotNull(request);
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.POST, "https://api.example.com");
        request.addOAuthParameter("oauth_token", "abcd1234");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("abcd1234", oauthParameters.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.POST, "https://api.example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.POST, "https://api.example.com");
        request.addOAuthParameter("oauth_token", "abcd1234");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(2, oauthParameters.size());
        assertEquals("abcd1234", oauthParameters.get("oauth_token"));
        assertEquals("read write", oauthParameters.get("scope"));
    }
}