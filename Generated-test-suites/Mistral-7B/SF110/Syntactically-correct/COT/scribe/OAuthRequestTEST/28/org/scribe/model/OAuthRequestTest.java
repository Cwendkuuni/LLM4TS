import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstructor() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        Assert.assertNotNull(oAuthRequest);
        Assert.assertEquals(Verb.GET, oAuthRequest.getVerb());
        Assert.assertNotNull(oAuthRequest.getOauthParameters());
        Assert.assertTrue(oAuthRequest.getOauthParameters() instanceof Map);
        Assert.assertTrue(oAuthRequest.getOauthParameters().isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "token123");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        Assert.assertEquals("token123", oauthParameters.get("oauth_token"));
        oAuthRequest.addOAuthParameter("scope", "read write");
        Assert.assertEquals("read write", oAuthRequest.getOauthParameters().get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testToString() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "token123");
        oAuthRequest.addOAuthParameter("scope", "read write");
        String expected = String.format("@OAuthRequest(GET, %s)", "http://example.com");
        String actual = oAuthRequest.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "token123");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        Assert.assertEquals("token123", oauthParameters.get("oauth_token"));
        oAuthRequest.addOAuthParameter("scope", "read write");
        Assert.assertEquals("read write", oAuthRequest.getOauthParameters().get("scope"));
        Assert.assertEquals(2, oAuthRequest.getOauthParameters().size());
    }

    @Test
    public void testCheckKey() {
        String validKey = "oauth_token";
        String invalidKey1 = "invalid_key1";
        String invalidKey2 = "scope_mismatch";
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        String expected = validKey;
        String result = oAuthRequest.checkKey(validKey);
        Assert.assertEquals(expected, result);

        try {
            String result = oAuthRequest.checkKey(invalidKey1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            oAuthRequest.checkKey(invalidKey2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}