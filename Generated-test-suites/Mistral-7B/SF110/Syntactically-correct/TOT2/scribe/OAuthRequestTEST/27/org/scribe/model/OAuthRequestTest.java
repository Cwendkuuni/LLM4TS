import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("value", oauthParameters.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameterKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOAuthParameterKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value1");
        request.addOAuthParameter("key", "value2");
    }

    @Test
    public void testAddEmptyOAuthParameterValue() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("", oauthParameters.get("key"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(2, oauthParameters.size());
        assertEquals("value1", oauthParameters.get("key1"));
        assertEquals("value2", oauthParameters.get("key2"));
    }
}