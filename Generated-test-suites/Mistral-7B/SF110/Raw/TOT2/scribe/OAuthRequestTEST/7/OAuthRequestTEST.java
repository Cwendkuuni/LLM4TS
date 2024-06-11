import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestInstanceCreation() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        assertNotNull(oAuthRequest);
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oAuthRequest.addOAuthParameter("testKey", "testValue");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals("testValue", oauthParameters.get("testKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oAuthRequest.addOAuthParameter("test", "testValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithNullValue() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oAuthRequest.addOAuthParameter("testKey", null);
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oAuthRequest.addOAuthParameter("testKey1", "testValue1");
        oAuthRequest.addOAuthParameter("testKey2", "testValue2");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals(2, oauthParameters.size());
        assertNull(oauthParameters.get("nonExistentKey"));
    }

    @Test
    public void testToString() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", oAuthRequest.toString());
    }

    @Test
    public void testGetOauthParametersWithEmptyMap() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(0, oauthParameters.size());
    }
}