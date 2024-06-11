import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals("value", oauthParameters.get("key"));
    }
}