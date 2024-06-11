import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        assertNotNull(oauthRequest);
        assertEquals(Verb.GET, oauthRequest.getVerb());
        assertEquals("https://example.com", oauthRequest.getUrl());
        assertEquals(new HashMap<>(), oauthRequest.getOauthParameters());
    }
}