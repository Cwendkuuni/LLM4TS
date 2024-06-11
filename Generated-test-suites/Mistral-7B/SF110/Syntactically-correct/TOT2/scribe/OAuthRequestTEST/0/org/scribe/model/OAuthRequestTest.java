import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("key1", "value1");
        Map<String, String> expectedOAuthParameters = new HashMap<>();
        expectedOAuthParameters.put("key1", "value1");
        Map<String, String> actualOAuthParameters = oAuthRequest.getOauthParameters();
        assertEquals(expectedOAuthParameters, actualOAuthParameters);
    }
}