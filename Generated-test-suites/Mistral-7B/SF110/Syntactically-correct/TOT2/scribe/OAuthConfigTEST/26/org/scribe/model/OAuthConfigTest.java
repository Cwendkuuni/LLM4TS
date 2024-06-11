package org.junit.platform.commons.testing.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String apiKey = config.getApiKey();
        String apiSecret = config.getApiSecret();
        assertEquals("testKey", apiKey);
        assertEquals("testSecret", apiSecret);
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullApiKey() {
        new OAuthConfig(null, "testSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullApiSecret() {
        new OAuthConfig("testKey", null);
    }

    @Test
    public void testOAuthConfigWithEmptyCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "");
        String callback = config.getCallback();
        assertEquals("oob", callback);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullSignatureType() {
        new OAuthConfig("testKey", "testSecret", "", null, "");
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullScope() {
        new OAuthConfig("testKey", "testSecret", "", SignatureType.Header, null);
    }

    @Test
    public void testOAuthConfigWithScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "", SignatureType.Header, "testScope");
        String scope = config.getScope();
        assertEquals("testScope", scope);
    }

    @Test
    public void testOAuthConfigWithHasScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "", SignatureType.Header, "testScope");
        boolean hasScope = config.hasScope();
        assertTrue(hasScope);
    }

    @Test
    public void testOAuthConfigWithEmptyApiKey() {
        OAuthConfig config = new OAuthConfig("", "testSecret");
        String apiKey = config.getApiKey();
        assertEquals("", apiKey);
    }

    @Test
    public void testOAuthConfigWithEmptyApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "");
        String apiSecret = config.getApiSecret();
        assertEquals("", apiSecret);
    }
}