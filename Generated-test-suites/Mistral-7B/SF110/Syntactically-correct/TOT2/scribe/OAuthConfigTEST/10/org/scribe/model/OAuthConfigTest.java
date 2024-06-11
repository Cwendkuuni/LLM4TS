package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String actualApiKey = config.getApiKey();
        String expectedApiKey = "testKey";
        assertEquals(expectedApiKey, actualApiKey);
    }

    @Test
    public void testGetApiKeyWithNullObject() {
        OAuthConfig config = null;
        String actualApiKey = config.getApiKey();
        assertNull(actualApiKey);
    }

    @Test
    public void testGetApiSecretWithNullObject() {
        OAuthConfig config = null;
        String actualApiSecret = config.getApiSecret();
        assertNull(actualApiSecret);
    }

    @Test
    public void testGetCallbackWithEmptyString() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "", SignatureType.Header, "testScope");
        String actualCallback = config.getCallback();
        assertEquals("", actualCallback);
    }

    @Test
    public void testGetCallbackWithNullObject() {
        OAuthConfig config = null;
        String actualCallback = config.getCallback();
        assertNull(actualCallback);
    }

    @Test
    public void testGetCallbackWithOobValue() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "oob", SignatureType.Header, "testScope");
        String actualCallback = config.getCallback();
        assertEquals("oob", actualCallback);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSignatureTypeWithInvalidSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.INVALID, "testScope");
        config.getSignatureType();
    }

    @Test
    public void testHasScopeWithNullObject() {
        OAuthConfig config = null;
        boolean actualHasScope = config.hasScope();
        assertFalse(actualHasScope);
    }

    @Test
    public void testGetScopeWithNullObject() {
        OAuthConfig config = null;
        String actualScope = config.getScope();
        assertNull(actualScope);
    }

    @Test
    public void testGetScopeWithEmptyString() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, "");
        String actualScope = config.getScope();
        assertEquals("", actualScope);
    }
}