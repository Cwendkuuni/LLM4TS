package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignaturaType;

public class OAuthConfigTest {

 private String key = "testKey";
 private String secret = "testSecret";
 private String callback = "http://test.com";
 private String scope = "testScope";
 private SignaturaType signatureType = SignaturaType.Header;

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret);
 assertEquals(key, oAuthConfig.getApiKey());
 assertEquals(secret, oAuthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret, callback);
 assertEquals(key, oAuthConfig.getApiKey());
 assertEquals(secret, oAuthConfig.getApiSecret());
 assertEquals(callback, oAuthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithAllParameters() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret, callback, signatureType, scope);
 assertEquals(key, oAuthConfig.getApiKey());
 assertEquals(secret, oAuthConfig.getApiSecret());
 assertEquals(callback, oAuthConfig.getCallback());
 assertEquals(signatureType, oAuthConfig.getSignatureType());
 assertEquals(scope, oAuthConfig.getScope());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullApiKey() {
 assertNull(new OAuthConfig(null, secret));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullApiSecret() {
 assertNull(new OAuthConfig(key, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret);
 assertNull(oAuthConfig.getCallback());
 assertNotNull(oAuthConfig.getApiKey());
 assertNotNull(oAuthConfig.getApiSecret());
 new OAuthConfig(key, secret, null);
 }

 @Test
 public void testDefaultCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret);
 assertEquals("oob", oAuthConfig.getCallback());
 }

 @Test
 public void testDefaultSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret);
 assertEquals(SignaturaType.Header, oAuthConfig.getSignatureType());
 }

 @Test
 public void testNullScope() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret, callback, signatureType);
 assertNull(oAuthConfig.getScope());
 }

 @Test
 public void testHasScope() {
 OAuthConfig oAuthConfig = new OAuthConfig(key, secret, callback, signatureType, scope);
 assertTrue(oAuthConfig.hasScope());
 }
}