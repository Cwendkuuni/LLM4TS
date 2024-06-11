
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstructor() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        assertNotNull(request);
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key", "value");
        assertEquals("value", request.getOauthParameters().get("key"));
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key", "value1");
        request.addOAuthParameter("scope", "scope1");
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key", "value1");
        expectedMap.put("scope", "scope1");
        assertEquals(expectedMap, request.getOauthParameters());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        new OAuthRequest(null, null);
    }

    @Test
    public void testAddDuplicateKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key", "value");
        request.addOAuthParameter("key", "value2");
        assertEquals("value", request.getOauthParameters().get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("invalidKey", "value");
    }

    @Test
    public void testInvalidKeyWithMessage() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        try {
            request.addOAuthParameter("invalidKey", "value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
}