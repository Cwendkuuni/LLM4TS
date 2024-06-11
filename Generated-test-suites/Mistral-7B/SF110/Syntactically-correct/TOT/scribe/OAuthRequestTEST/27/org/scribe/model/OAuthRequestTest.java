import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestConstructor() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        assertNotNull(oAuthRequest);
        assertEquals(verb, oAuthRequest.getVerb());
        assertEquals(url, oAuthRequest.getUrl());
        assertNotNull(oAuthRequest.getOauthParameters());
        assertTrue(oAuthRequest.getOauthParameters().isEmpty());
    }
}