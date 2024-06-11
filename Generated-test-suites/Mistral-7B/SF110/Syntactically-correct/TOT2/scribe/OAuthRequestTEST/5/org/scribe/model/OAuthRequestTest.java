import static org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request);
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        assertEquals("value1", request.getOauthParameters().get("key1"));
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        Map<String, String> parameters = request.getOauthParameters();
        assertEquals(1, parameters.size());
        assertEquals("value1", parameters.get("key1"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameterKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOAuthParameterKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key1", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthRequestConstructorNullVerb() {
        Verb verb = null;
        OAuthRequest request = new OAuthRequest(verb, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthRequestConstructorNullUrl() {
        String url = null;
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterNullKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterNullValue() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", null);
    }
}