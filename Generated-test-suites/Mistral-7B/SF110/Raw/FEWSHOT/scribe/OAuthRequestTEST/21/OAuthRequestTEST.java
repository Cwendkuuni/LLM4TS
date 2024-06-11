package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest request = new OAuthRequest(verb, url);
        assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", request.toString());
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest request = new OAuthRequest(verb, url);
        Map<String, String> oauthParameters = new HashMap<>();
        oauthParameters.put("oauth_consumer_key", "consumerKey");
        oauthParameters.put("oauth_token", "token");
        oauthParameters.put("scope", "scope1 scope2");
        request.addOAuthParameter("oauth_consumer_key", "consumerKey");
        request.addOAuthParameter("oauth_token", "token");
        request.addOAuthParameter("scope", "scope1 scope2");
        Map<String, String> actualOAuthParameters = request.getOauthParameters();
        assertEquals(oauthParameters.size(), actualOAuthParameters.size());
        for (Map.Entry<String, String> entry : oauthParameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String actualKey = request.checkKey(key);
            assertEquals(key, actualKey);
            assertEquals(value, actualOAuthParameters.get(actualKey));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIllegalOAuthParameter() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest request = new OAuthRequest(verb, url);
        request.addOAuthParameter("illegalParameter", "value");
    }

    @Test
    public void testToStringWithOAuthParameters() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest request = new OAuthRequest(verb, url);
        Map<String, String> oauthParameters = new HashMap<>();
        oauthParameters.put("oauth_consumer_key", "consumerKey");
        oauthParameters.put("oauth_token", "token");
        oauthParameters.put("scope", "scope1 scope2");
        request.addOAuthParameter("oauth_consumer_key", "consumerKey");
        request.addOAuthParameter("oauth_token", "token");
        request.addOAuthParameter("scope", "scope1 scope2");
        StringBuilder expected = new StringBuilder("@OAuthRequest(GET, https://api.example.com/resource, {");
        expected.append("\"oauth_consumer_key\"=\"consumerKey\",");
        expected.append("\"oauth_token\"=\"token\",");
        expected.append("\"scope\"=\"scope1 scope2\"});");
        assertEquals(expected.toString(), request.toString());
    }

    @Test
    public void testGetEmptyOAuthParametersMap() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest request = new OAuthRequest(verb, url);
        Map<String, String> actualOAuthParameters = request.getOauthParameters();
        assertTrue(Collections.isEmpty(actualOAuthParameters));
    }
}