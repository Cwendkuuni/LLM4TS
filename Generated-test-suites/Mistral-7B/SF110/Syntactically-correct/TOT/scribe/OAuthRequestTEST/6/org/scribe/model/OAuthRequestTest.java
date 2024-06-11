package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read");
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals("abc123", oauthParameters.get("oauth_token"));
        Assert.assertEquals("read", oauthParameters.get("scope"));
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read");
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertNotNull(oauthParameters);
        Assert.assertEquals(2, oauthParameters.size());
        Assert.assertTrue(oauthParameters.containsKey("oauth_token"));
        Assert.assertTrue(oauthParameters.containsKey("scope"));
    }
    
    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://example.com");
        Assert.assertEquals("oauth_token", request.checkKey("oauth_token"));
        Assert.assertEquals("scope", request.checkKey("scope"));
        try {
            request.checkKey("invalid_key");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }
}