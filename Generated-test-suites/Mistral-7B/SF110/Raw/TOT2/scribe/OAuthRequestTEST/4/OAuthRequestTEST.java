import static org.junit.Assert.*;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key", "value");
        Map<String, String> actualMap = request.getOauthParameters();
        assertEquals(expectedMap, actualMap);
    }
}