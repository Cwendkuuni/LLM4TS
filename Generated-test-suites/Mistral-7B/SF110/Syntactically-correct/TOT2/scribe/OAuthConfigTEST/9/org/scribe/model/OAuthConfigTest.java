package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.annotations.Nullable;

public class OAuthConfigTest {
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("testKey");
        String apiKey = config.getApiKey();
        assertEquals("testKey", apiKey);
    }
    
    @Test
    public void testEmptyConstructor() {
        OAuthConfig config = new OAuthConfig();
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullKeyAndSecret() {
        OAuthConfig config = new OAuthConfig(null, null);
    }
    
    @Test
    public void testLongKeyAndSecret() {
        String longKey = "1234567890123456789012345678901234567890";
        String longSecret = "12345678901234567890123456789012345678901234567890";
        OAuthConfig config = new OAuthConfig(longKey, longSecret);
        assertEquals(longKey, config.getApiKey());
        assertEquals(longSecret, config.getApiSecret());
    }
    
    @Test
    public void testEmptyCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, "testScope");
        assertEquals("oob", config.getCallback());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, "testScope");
    }
    
    @Test
    public void testEmptyScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, null);
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", (SignatureType)-1, "testScope");
    }
    
    @Test
    public void testLongSignatureType() {
        SignatureType longSignatureType = new Enum("LONG_SIGNATURE_TYPE", 99);
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", longSignatureType, "testScope");
        assertEquals(longSignatureType, config.getSignatureType());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullGetApiKey() {
        OAuthConfig config = new OAuthConfig();
        assertNull(config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String apiSecret = config.getApiSecret();
        assertEquals("testSecret", apiSecret);
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback");
        String callback = config.getCallback();
        assertEquals("testCallback", callback);
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, "testScope");
        SignatureType signatureType = config.getSignatureType();
        assertEquals(SignatureType.Header, signatureType);
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, "testScope");
        String scope = config.getScope();
        assertEquals("testScope", scope);
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, "testScope");
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testNullGetApiSecret() {
        OAuthConfig config = new OAuthConfig();
        assertNull(config.getApiSecret());
    }
    
    @Test
    public void testNullGetCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        assertNull(config.getCallback());
    }
    
    @Test
    public void testNullGetSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback");
        assertNull(config.getSignatureType());
    }
    
    @Test
    public void testNullGetScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header);
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testNullHasScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, null);
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testToString() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, "testScope");
        String expected = "OAuthConfig{apiKey='testKey', apiSecret='testSecret', callback='testCallback', signatureType=HEADER, scope='testScope'}";
        assertEquals(expected, config.toString());
    }
}