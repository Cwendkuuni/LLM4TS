package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://example.com/callback");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("http://example.com/callback", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://example.com/callback", SignatureType.QueryString, "scope1 scope2");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("http://example.com/callback", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullValues() {
 OAuthConfig config = new OAuthConfig(null, null);
 assertNull(config.getApiKey());
 assertNull(config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "scope1 scope2");
 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test
 public void testOAuthConfigHasScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "scope1 scope2");
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigNoScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null);
 assertFalse(config.hasScope());
 }
}