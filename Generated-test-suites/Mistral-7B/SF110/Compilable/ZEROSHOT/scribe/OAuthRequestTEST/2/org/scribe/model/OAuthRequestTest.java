package org.scribe.model;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OAuthRequestTest {

    private OAuthRequest oAuthRequest;

    @Before
    public void setUp() {
        oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
    }

    @Test
    public void testCreation() {
        assertNotNull(oAuthRequest);
    }

    @Test
    public void testAddOAuthParameter() {
        oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_key");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals("test_key", oauthParameters.get("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        oAuthRequest.addOAuthParameter("invalid_key", "test_value");
    }

    @Test
    public void testGetOauthParameters() {
        oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_key");
        oAuthRequest.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals("test_key", oauthParameters.get("oauth_consumer_key"));
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, http://example.com)", oAuthRequest.toString());
    }
}