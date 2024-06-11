
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        Map<String, String> expectedOAuthParameters = new HashMap<>();
        expectedOAuthParameters.put("key1", "value1");
        Map<String, String> actualOAuthParameters = request.getOauthParameters();
        assertEquals(expectedOAuthParameters, actualOAuthParameters);
    }

    @Test
    public void testAddOAuthParameterWithExistingKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key1", "value2");
        Map<String, String> expectedOAuthParameters = new HashMap<>();
        expectedOAuthParameters.put("key1", "value2");
        Map<String, String> actualOAuthParameters = request.getOauthParameters();
        assertEquals(expectedOAuthParameters, actualOAuthParameters);
    }

    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        try {
            request.checkKey("invalidKey");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCheckKeyWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        try {
            request.checkKey("invalidKey");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        Map<String, String> expectedOAuthParameters = new HashMap<>();
        expectedOAuthParameters.put("key1", "value1");
        expectedOAuthParameters.put("key2", "value2");
        Map<String, String> actualOAuthParameters = request.getOauthParameters();
        assertEquals(expectedOAuthParameters, actualOAuthParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithNullValue() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithEmptyKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithEmptyValue() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key1", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKeyForSetParameter() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        try {
            request.addOAuthParameter("invalidKey", "value");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOauthParametersAfterSetRequestBody() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        request.setBody("test body");
        Map<String, String> oauthParameters = request.getOauthParameters();
    }

    @Test(expected = IllegalStateException.class)
    public void testToStringAfterSetRequestBody() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        request.setBody("test body");
        String toString = request.toString();
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://example.com");
        String expectedToString = "@OAuthRequest(GET, https://example.com)";
        String actualToString = request.toString();
        assertEquals(expectedToString, actualToString);
    }
}