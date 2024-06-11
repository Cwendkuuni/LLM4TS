package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestWithEmptyParameters() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://api.example.com/");
        Map<String, String> actual = request.getOauthParameters();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://api.example.com/");
        request.addOAuthParameter("oauth_token", "token123");
        Map<String, String> actual = request.getOauthParameters();
        assertEquals("token123", actual.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://api.example.com/");
        request.addOAuthParameter("invalid", "value");
    }

    @Test
    public void testGetOAuthParametersWithMultipleParameters() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://api.example.com/");
        request.addOAuthParameter("oauth_token", "token123");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> actual = request.getOauthParameters();
        Map<String, String> expected = new HashMap<>();
        expected.put("oauth_token", "token123");
        expected.put("scope", "read write");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(HttpMethod.GET, "https://api.example.com/");
        request.addOAuthParameter("oauth_token", "token123");
        String actual = request.toString();
        String expected = "@OAuthRequest(GET, https://api.example.com/)";
        assertNotEquals(expected, actual);
        request.addOAuthParameter("scope", "read write");
        expected += " [oauth_token=token123, scope=read write]";
        assertEquals(expected, actual);
    }
}