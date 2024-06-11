package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertNull(oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertNull(oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.HMAC_SHA1, "scope");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.HMAC_SHA1, oauthConfig.getSignatureType());
 assertEquals("scope", oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndEmptyScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.HMAC_SHA1, "");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.HMAC_SHA1, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndNonEmptyScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.HMAC_SHA1, "scope1 scope2");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.HMAC_SHA1, oauthConfig.getSignatureType());
 assertEquals("scope1 scope2", oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndNullCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndNullSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, null);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertNull(oauthConfig.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndNullScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.HMAC_SHA1, null);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKeyAndApiSecret() {
 assertNull(new OAuthConfig(null, null));
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 assertNull(new OAuthConfig("", "apiSecret"));
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 assertNull(new OAuthConfig("apiKey", ""));
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, null);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertNull(oauthConfig.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.HMAC_SHA1, null);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithEmptyApiKey() {
 assertNull(new OAuthConfig("", "apiSecret"));
 }

 @Test
 public void testOAuthConfigWithEmptyApiSecret() {
 assertNull(new OAuthConfig("apiKey", ""));
 }

 @Test
 public void testOAuthConfigWithEmptyApiKeyAndApiSecret() {
 assertNull(new OAuthConfig("", ""));
 }

 @Test
 public void testOAuthConfigWithEmptyCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("", oauthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithEmptySignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, "", "");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertNull(oauthConfig.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.HMAC_SHA1, "");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertNull(oathConfig.getScope());
 }
}