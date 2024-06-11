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
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.QueryString);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.QueryString, "scopeValue");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
 assertEquals("scopeValue", oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKeyAndApiSecret() {
 assertNull(new OAuthConfig(null, null));
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", null);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertNull(oauthConfig.getApiSecret());
 assertEquals("oob", oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKeyAndNullApiSecret() {
 assertNull(new OAuthConfig(null, null));
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl");
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.Header);
 assertEquals("apiKey", oauthConfig.getApiKey());
 assertEquals("apiSecret", oauthConfig.getApiSecret());
 assertEquals("callbackUrl", oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

}