import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals("value1", oauthParameters.get("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithNullKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "http://example.com");
        request.addOAuthParameter(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithNullValue() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "http://example.com");
        request.addOAuthParameter("key", null);
    }

    // Add test cases for other methods

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals(1, oauthParameters.size());
        Assert.assertEquals("value1", oauthParameters.get("key1"));
    }

    // Add test cases for other methods

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "http://example.com");
        Assert.assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    // Add test cases for other methods

}