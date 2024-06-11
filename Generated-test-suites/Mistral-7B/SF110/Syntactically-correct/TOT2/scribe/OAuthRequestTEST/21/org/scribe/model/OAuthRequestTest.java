import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        Map<String, String> expectedOAuthParameters = new HashMap<>();
        expectedOAuthParameters.put("key1", "value1");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertMapEquals(expectedOAuthParameters, oauthParameters);
    }
}