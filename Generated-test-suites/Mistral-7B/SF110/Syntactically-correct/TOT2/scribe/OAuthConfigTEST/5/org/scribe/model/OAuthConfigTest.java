import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Assert.*;
import org.junit.experimental.annotations.ExpectedException;

public class OAuthConfigTest {

    @Test
    public void testConstructorNullArguments() {
        try {
            new OAuthConfig(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorEmptyStrings() {
        OAuthConfig oAuthConfig = new OAuthConfig("", "");
        assertEquals("", oAuthConfig.getApiKey());
        assertEquals("", oAuthConfig.getApiSecret());
    }

    @Test
    public void testGetApiKey() {
        String apiKey = "testApiKey";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, "");
        assertEquals(apiKey, oAuthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig("", apiSecret);
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String callback = "testCallback";
        OAuthConfig oAuthConfig = new OAuthConfig("", "", callback, null, null);
        assertEquals(callback, oAuthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig oAuthConfig = new OAuthConfig("", "", null, signatureType, null);
        assertEquals(signatureType, oAuthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String scope = "testScope";
        OAuthConfig oAuthConfig = new OAuthConfig("", "", null, null, scope);
        assertEquals(scope, oAuthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        String scope = "testScope";
        OAuthConfig oAuthConfig = new OAuthConfig("", "", null, null, scope);
        assertTrue(oAuthConfig.hasScope());
    }

    @Test
    public void testGettersWithNullObjects() {
        OAuthConfig oAuthConfig = new OAuthConfig("", "", null, null, null);
        assertNull(oAuthConfig.getApiKey());
        assertNull(oAuthConfig.getApiSecret());
        assertNull(oAuthConfig.getCallback());
        assertNull(oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
    }

    @Test(expected = NullPointerException.class)
    public void testGettersWithNullValues() {
        String apiKey = null;
        String apiSecret = null;
        String callback = null;
        SignatureType signatureType = null;
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
        assertEquals(apiKey, oAuthConfig.getApiKey()); // Expects NullPointerException
    }

    @Test
    public void testGettersWithEmptyStrings() {
        OAuthConfig oAuthConfig = new OAuthConfig("", "", "", null, "");
        assertEquals("", oAuthConfig.getApiKey());
        assertEquals("", oAuthConfig.getApiSecret());
        assertEquals("oob", oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertEquals("", oAuthConfig.getScope());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettersWithInvalidSignatureType() {
        OAuthConfig oAuthConfig = new OAuthConfig("", "", null, null, "");
        try {
            oAuthConfig.setSignatureType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

//