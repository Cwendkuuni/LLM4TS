package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Signatures.SignatureType;

public class OAuthConfigTest {

 // Test constructor with key and secret
 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
 assertEquals("testKey", oAuthConfig.getApiKey());
 assertEquals("testSecret", oAuthConfig.getApiSecret());
 }

 // Test constructor with all parameters
 @Test
 public void testOAuthConfigWithAllParameters() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.HMAC_SHA1, "testScope");
 assertEquals("testKey", oAuthConfig.getApiKey());
 assertEquals("testSecret", oAuthConfig.getApiSecret());
 assertEquals("testCallback", oAuthConfig.getCallback());
 assertEquals(SignatureType.HMAC_SHA1, oAuthConfig.getSignatureType());
 assertEquals("testScope", oAuthKey.getScope());
 }

 // Test getter methods for apiKey, apiSecret, callback, signatureType, and scope
 @Test
 public void testGetters() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "testCallback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 assertEquals(apiKey, oAuthConfig.getApiKey());
 assertEquals(apiSecret, oAuthConfig.getApiSecret());
 assertEquals(callback, oAuthConfig.getCallback());
 assertEquals(signatureType, oAuthConfig.getSignatureType());
 assertEquals(scope, oAuthConfig.getScope());
 }

 // Test hasScope method
 @Test
 public void testHasScope() {
 OAuthConfig oAuthConfigWithScope = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 OAuthConfig oAuthConfigWithoutScope = new OAuthConfig("testKey", "testSecret", null, null, null);

 assertTrue(oAuthConfigWithScope.hasScope());
 assertFalse(oAuthConfigWithoutScope.hasScope());
 }

 // Edge cases and exception handling tests

 // Test null key and secret
 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, null);
 }

 // Test empty key and secret
 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("", "");
 }

 // Test null callback
 @Test
 public void testNullCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, null);
 assertEquals(OAuthConfig.Callback.oob, oAuthConfig.getCallback());
 }

 // Test invalid signature type
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSignatureType() {
 SignatureType invalidSignatureType = new SignatureType("InvalidSignatureType");
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, invalidSignatureType, null);
 }

 // Test null scope
 @Test
 public void testNullScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, null);
 }

 // Test empty scope
 @Test
 public void testEmptyScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, "");
 }
}