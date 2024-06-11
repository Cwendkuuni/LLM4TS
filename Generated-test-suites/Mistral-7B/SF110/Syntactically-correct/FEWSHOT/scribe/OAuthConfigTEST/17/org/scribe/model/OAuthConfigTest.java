package org.scribe.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithNullValues() {
 OAuthConfig config = new OAuthConfig(null, null);
 assertNull(config.getApiKey());
 assertNull(config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithValidValues() {
 String apiKey = "myapikey";
 String apiSecret = "myapisecret";
 String callback = "http://mycallback.com";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read write";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 assertEquals(apiKey, config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertEquals(scope, config.getScope());
 }

 @Test
 public void testOAuthConfigWithDefaultValues() {
 String apiKey = "defaultapikey";
 String apiSecret = "defaultapisecret";
 String callback = "oob";
 SignatureType signatureType = SignatureType.Header;
 String scope = null;

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
 assertEquals(apiKey, config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 String apiKey = "myapikey";
 String apiSecret = "myapisecret";
 String callback = "http://mycallback.com";
 SignatureType signatureType = SignatureType.Header;
 String scope = "";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 assertNull(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithValidScope() {
 String apiKey = "myapikey";
 String apiSecret = "myapisecret";
 String callback = "http://mycallback.com";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read write";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 assertTrue(config.hasScope());
 }
}