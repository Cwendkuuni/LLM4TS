import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        assertNotNull(oauthRequest);
        assertEquals(Request.Verb.GET, oauthRequest.getVerb());
        assertEquals("https://example.com", oauthRequest.getUrl());
        assertNull(oauthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oauthRequest.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("value", oauthParameters.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIllegalOAuthParameterKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oauthRequest.addOAuthParameter("invalid", "value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oauthRequest.addOAuthParameter("key", "value1");
        oauthRequest.addOAuthParameter("scope", "value2");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(2, oauthParameters.size());
        assertEquals("value1", oauthParameters.get("key"));
        assertEquals("value2", oauthParameters.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "https://example.com");
        oauthRequest.addOAuthParameter("key", "value1");
        oauthRequest.addOAuthParameter("scope", "value2");
        String expected = String.format("@OAuthRequest(GET, https://example.com)%n%n%s%n%s%n", "[key=value1, scope=value2]", "");
        assertEquals(expected, oauthRequest.toString());
    }
}