package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

    @org.junit.Test
    public void testOAuthRequestWithValidArguments() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_consumer_key", "CK-abc123");
        request.addOAuthParameter("oauth_token", "TOK-def456");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        request.addOAuthParameter("oauth_nonce", "nonce12345");
        request.addOAuthParameter("oauth_version", "1.0");

        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("CK-abc123", oauthParameters.get("oauth_consumer_key"));
        assertEquals("TOK-def456", oauthParameters.get("oauth_token"));
        assertEquals("HMAC-SHA1", oauthParameters.get("oauth_signature_method"));
        // Add other expected valid values and checks here
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("invalid_key", "value");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithEmptyKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("", "value");
    }

    @org.junit.Test
    public void testGetOauthParametersWithEmptyMap() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }

    @org.junit.Test
    public void testToStringWithValidArguments() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_consumer_key", "CK-abc123");
        request.addOAuthParameter("oauth_token", "TOK-def456");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        request.addOAuthParameter("oauth_nonce", "nonce12345");
        request.addOAuthParameter("oauth_version", "1.0");

        String expected = String.format("@OAuthRequest(GET, %s)", "https://api.example.com/resource");
        String actual = request.toString();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testToStringWithNoOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        String actual = request.toString();
        assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", actual);
    }
}