package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://localhost:8080/callback");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("http://localhost:8080/callback", config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://localhost:8080/callback", SignatureType.QueryString, "scope1 scope2");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("http://localhost:8080/callback", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", SignatureType.Header);
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "scope1 scope2");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndNullValues() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testHasScopeWithNullScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertFalse(config.hasScope());
 }

 @Test
 public void testHasScopeWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "");
 assertFalse(config.hasScope());
 }

 @Test
 public void testHasScopeWithNonEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "scope1 scope2");
 assertTrue(config.hasScope());
 }
}