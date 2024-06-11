package org.scribe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignaturType; // Note: SignatureType should be named SignaturType in the test file to match the class in the source code

public class OAuthConfigTest {

 // Test constructor with only apiKey and apiSecret
 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
     OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret");
     assertNotNull(config.getApiKey());
     assertNotNull(config.getApiSecret());
     assertNull(config.getCallback());
     assertEquals(SignaturType.Header, config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

 // Test constructor with all parameters
 @Test
 public void testOAuthConfigWithApiKeySecretCallbackSignatureTypeAndScope() {
     OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback", SignaturType.QueryString, "testScope");
     assertNotNull(config.getApiKey());
     assertNotNull(config.getApiSecret());
     assertNotNull(config.getCallback());
     assertEquals(SignaturType.QueryString, config.getSignatureType());
     assertNotNull(config.getScope());
     assertTrue(config.hasScope());
 }

 // Test null apiKey
 @Test(expected = NullPointerException.class)
 public void testOAuthConfigNullApiKey() {
     new OAuthConfig(null, "testApiSecret");
 }

 // Test null apiSecret
 @Test(expected = NullPointerException.class)
 public void testOAuthConfigNullApiSecret() {
     new OAuthConfig("testApiKey", null);
 }

 // Test empty apiKey
 @Test
 public void testOAuthConfigEmptyApiKey() {
     OAuthConfig config = new OAuthConfig("", "testApiSecret");
     assertNotNull(config.getApiKey());
     assertNotNull(config.getApiSecret());
     assertNull(config.getCallback());
     assertEquals(SignaturType.Header, config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

 // Test null callback
 @Test
 public void testOAuthConfigNullCallback() {
     OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", null);
     assertNotNull(config.getApiKey());
     assertNotNull(config.getApiSecret());
     assertNull(config.getCallback());
     assertEquals(SignaturType.Header, config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

 // Test invalid signatureType
 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigInvalidSignatureType() {
     new OAuthConfig("testApiKey", "testApiSecret", null, new SignaturType("InvalidSignatureType"));
 }

 // Test null signatureType
 @Test(expected = NullPointerException.class)
 public void testOAuthConfigNullSignatureType() {
     new OAuthConfig("testApiKey", "testApiSecret", null, null);
 }

 // Test getters
 @Test
 public void testGetters() {
     OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback", SignaturType.Header, "testScope");
     assertNotNull(config.getApiKey());
     assertNotNull(config.getApiSecret());
     assertNotNull(config.getCallback());
     assertEquals(SignaturType.Header, config.getSignatureType());
     assertNotNull(config.getScope());
     assertTrue(config.hasScope());
 }
}