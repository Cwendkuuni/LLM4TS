package org.scribe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 // Testing constructor with two arguments
 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
     String apiKey = "test_api_key";
     String apiSecret = "test_api_secret";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertNull(config.getCallback());
     assertEquals(SignatureType.Header, config.getSignatureType());
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }

 // Testing constructor with all arguments
 @Test
 public void testOAuthConfigWithAllArguments() {
     String apiKey = "test_api_key";
     String apiSecret = "test_api_secret";
     String callback = "test_callback";
     SignatureType signatureType = SignatureType.PostParam;
     String scope = "test_scope";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
     assertTrue(config.hasScope());
 }

 // Testing getter methods for API key, API secret, callback, signature type, and scope
 @Test
 public void testGetterMethods() {
     String apiKey = "test_api_key";
     String apiSecret = "test_api_secret";
     String callback = "test_callback";
     SignatureType signatureType = SignatureType.Header;
     String scope = "test_scope";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
 }

 // Edge cases and exception handling testing

 // Testing constructor with null apiKey
 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
     String apiSecret = "test_api_secret";
     String callback = "test_callback";
     SignatureType signatureType = SignatureType.Header;
     String scope = "test_scope";
     new OAuthConfig(null, apiSecret, callback, signatureType, scope);
 }

 // Testing constructor with null apiSecret
 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
     String apiKey = "test_api_key";
     String callback = "test_callback";
     SignatureType signatureType = SignatureType.Header;
     String scope = "test_scope";
     new OAuthConfig(apiKey, null, callback, signatureType, scope);
 }

 // Testing constructor with empty string as apiKey or apiSecret
 @Test
 public void testEmptyStringsAsApiKeyOrApiSecret() {
     String apiKey = "";
     String apiSecret = "";
     String callback = "test_callback";
     SignatureType signatureType = SignatureType.Header;
     String scope = "test_scope";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals("", config.getApiKey());
     assertEquals("", config.getApiSecret());
 }

 // Testing constructor with null callback
 @Test
 public void testNullCallback() {
     String apiKey = "test_api_key";
     String apiSecret = "test_api_secret";
     SignatureType signatureType = SignatureType.Header;
     String scope = "test_scope";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
     assertNull(config.getCallback());
 }

 // Testing constructor with null signatureType
 @Test
 public void testNullSignatureType() {
     String apiKey = "test_api_key";
     String apiSecret = "test_api_secret";
     String callback = "test_callback";
     String scope = "test_scope";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
     assertEquals(SignatureType.Header, config.getSignatureType());
 }

 // Testing constructor with empty string as scope
 @Test
 public void testEmptyStringAsScope() {
     String apiKey = "test_api_key";
     String apiSecret = "test_api_secret";
     String callback = "test_callback";
     SignatureType signatureType = SignatureType.Header;
     String scope = "";
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertNull(config.getScope());
     assertFalse(config.hasScope());
 }
}