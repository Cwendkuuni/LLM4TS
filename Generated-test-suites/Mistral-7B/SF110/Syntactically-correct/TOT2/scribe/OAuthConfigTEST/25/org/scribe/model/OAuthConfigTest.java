package org.junit.test;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
        String apiKey = oauthConfig.getApiKey();
        assertNotNull(apiKey);
        assertEquals("apiKey", apiKey);
    }

    @Test
    public void testOAuthConfigNullValues() {
        OAuthConfig oauthConfig = new OAuthConfig(null, null);
        assertNull(oauthConfig.getApiKey());
        assertNull(oauthConfig.getApiSecret());
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testOAuthConfigEmptyStrings() {
        OAuthConfig oauthConfig = new OAuthConfig("", "");
        assertNotNull(oauthConfig.getApiKey());
        assertNotNull(oauthConfig.getApiSecret());
        assertEquals("", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigInvalidSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.INVALID, null);
        // The test case should throw an IllegalArgumentException as SignatureType.INVALID is an invalid value
    }

}