package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
 assertEquals("apiKey", config.getApiKey());
 assertEquals("apiSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallback() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callbackUrl");
 assertEquals("apiKey", config.getApiKey());
 assertEquals("apiSecret", config.getApiSecret());
 assertEquals("callbackUrl", config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndType() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.QueryString);
 assertEquals("apiKey", config.getApiKey());
 assertEquals("apiSecret", config.getApiSecret());
 assertEquals("callbackUrl", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndTypeAndScope() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.Header, "scopeValue");
 assertEquals("apiKey", config.getApiKey());
 assertEquals("apiSecret", config.getApiSecret());
 assertEquals("callbackUrl", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals("scopeValue", config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullKeyAndSecret() {
 assertNull(new OAuthConfig(null, null));
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 OAuthConfig config = new OAuthConfig(null, "apiSecret", null, null, null);
 assertNull(config.getApiKey());
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 OAuthConfig config = new OAuthConfig("apiKey", null, null, null, null);
 assertNull(config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, null, null);
 assertNull(config.getCallback());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, null, null);
 assertNull(config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.Header, null);
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.Header, "");
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithNonEmptyScope() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.Header, "scopeValue");
 assertEquals("scopeValue", config.getScope());
 }

 @Test
 public void testOAuthConfigWithHasScope() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.Header, "scopeValue");
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureTypeAndDefault() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, null, null);
 assertEquals(SignatureType.Header, config.getSignatureType());
 }
}