import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {
    
    @Test
    public void testOAuthRequestConstructor() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        OAuthRequest request = new OAuthRequest(verb, url);
        assertNotNull(request);
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertEquals(0, request.getOauthParameters().size());
    }
    
}