
package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.Verb;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        // Test case for constructing an OAuthRequest with valid parameters
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        // add some valid OAuth parameters
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        request.addOAuthParameter("oauth_signature", "signed_value");
        Map<String, String> actualParameters = request.getOauthParameters();
        assertFalse(actualParameters.isEmpty());
        assertTrue(actualParameters.containsKey("oauth_consumer_key"));
        assertTrue(actualParameters.containsKey("oauth_token"));
        assertTrue(actualParameters.containsKey("oauth_signature"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameter() {
        // Test case for adding an invalid OAuth parameter
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testCheckKeyValid() {
        // Test case for checking valid OAuth parameter keys
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String validKey = "oauth_signature";
        String expected = validKey;
        String actual = request.checkKey(validKey);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        // Test case for checking invalid OAuth parameter keys
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String invalidKey = "invalid_key";
        request.checkKey(invalidKey);
    }

    @Test
    public void testToString() {
        // Test case for toString() method
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        request.addOAuthParameter("oauth_signature", "signed_value");
        String expected = "@OAuthRequest(GET, https://example.com)";
        String actual = request.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOauthParametersWhenNoOauthParamsAdded() {
        // Test case for getting OAuth parameters when no OAuth parameters have been added
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        Map<String, String> actualParameters = request.getOauthParameters();
        assertNotNull(actualParameters);
        assertTrue(actualParameters.isEmpty());
    }

    @Test
    public void testGetOauthParameters() {
        // Test case for getting OAuth parameters
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        request.addOAuthParameter("oauth_signature", "signed_value");
        Map<String, String> actualParameters = request.getOauthParameters();
        assertFalse(actualParameters.isEmpty());
        assertEquals("abc123", actualParameters.get("oauth_consumer_key"));
        assertEquals("def456", actualParameters.get("oauth_token"));
        assertEquals("signed_value", actualParameters.get("oauth_signature"));
    }

    @Test
    public void testGetOauthParameterValueWhenKeyIsNull() {
        // Test case for getting OAuth parameter value when key is null
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        request.addOAuthParameter("oauth_signature", "signed_value");
        String key = null;
        String expected = null;
        String actual = request.getOauthParameterValue(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOauthParameterValue() {
        // Test case for getting OAuth parameter value
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        request.addOAuthParameter("oauth_signature", "signed_value");
        String key = "oauth_consumer_key";
        String expected = "abc123";
        String actual = request.getOauthParameterValue(key);
        assertEquals(expected, actual);
    }
}