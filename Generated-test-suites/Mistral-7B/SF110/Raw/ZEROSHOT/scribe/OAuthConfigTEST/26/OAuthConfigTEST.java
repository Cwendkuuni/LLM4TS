package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

 private String apiKey;
 private String apiSecret;
 private String callback;
 private SignatureType signatureType;
 private String scope;

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret");
 assertEquals("testApiKey", config.getApiKey());
 assertEquals("testApiSecret", config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback", SignatureType.QueryString, "testScope");
 assertEquals("testApiKey", config.getApiKey());
 assertEquals("testApiSecret", config.getApiSecret());
 assertEquals("testCallback", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertEquals("testScope", config.getScope());
 }

 @Test
 public void testOAuthConfigWithOnlyKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret");
 assertFalse(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndNoSignatureType() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback");
 assertFalse(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretAndNullCallback() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret");
 assertEquals("oob", config.getCallback());
 }

 @Test
 public void testOAuthConfigWithKeySecretAndDefaultSignatureType() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret");
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndDefaultSignatureType() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback");
 assertEquals("testCallback", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

}