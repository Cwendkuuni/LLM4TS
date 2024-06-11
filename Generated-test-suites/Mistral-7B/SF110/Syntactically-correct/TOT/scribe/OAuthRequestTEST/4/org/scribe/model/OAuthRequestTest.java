import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        Assert.assertNotNull(request);
        Assert.assertNotNull(request.getOauthParameters());
        Assert.assertEquals(0, request.getOauthParameters().size());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey");
        request.addOAuthParameter("oauth_nonce", "testNonce");
        request.addOAuthParameter("oauth_signature", "testSignature");
        request.addOAuthParameter("oauth_signature_method", "testMethod");
        request.addOAuthParameter("oauth_timestamp", "testTime");
        request.addOAuthParameter("oauth_token", "testToken");
        request.addOAuthParameter("oauth_version", "testVersion");
        Map<String, String> parameters = request.getOauthParameters();

        Assert.assertEquals(7, parameters.size());
        Assert.assertEquals("testKey", parameters.get("oauth_consumer_key"));
        Assert.assertEquals("testNonce", parameters.get("oauth_nonce"));
        Assert.assertEquals("testSignature", parameters.get("oauth_signature"));
        Assert.assertEquals("testMethod", parameters.get("oauth_signature_method"));
        Assert.assertEquals("testTime", parameters.get("oauth_timestamp"));
        Assert.assertEquals("testToken", parameters.get("oauth_token"));
        Assert.assertEquals("testVersion", parameters.get("oauth_version"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "testValue");
    }
}