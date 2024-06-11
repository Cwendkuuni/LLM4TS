import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        String actualKey = oauthParameters.keySet().iterator().next();
        String actualValue = oauthParameters.get(actualKey);

        String expectedKey = "key";
        String expectedValue = "value";

        assertEquals(expectedKey, actualKey);
        assertEquals(expectedValue, actualValue);
    }
}