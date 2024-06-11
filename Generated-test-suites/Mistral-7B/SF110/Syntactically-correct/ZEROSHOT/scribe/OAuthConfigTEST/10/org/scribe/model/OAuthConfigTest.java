package org.junit.tests;

import static org.junit.Assert.*;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @org.junit.Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @org.junit.Test
 public void testOAuthConfigWithKeyAndSecretAndCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testCallback");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertEquals("testCallback", oauthConfig.getCallback());
 assertNull(oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @org.junit.Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndType() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertEquals("testCallback", oauthConfig.getCallback());
 assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
 assertEquals("testScope", oauthConfig.getScope());
 }

 @org.junit.Test
 public void testOAuthConfigWithKeyAndSecretAndType() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", SignatureType.Header);
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @org.junit.Test
 public void testOAuthConfigWithCallbackAsNull() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret");
 assertNull(oauthConfig.getCallback());
 }

 @org.junit.Test
 public void testOAuthConfigWithScopeAsNull() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testCallback");
 assertNull(oauthConfig.getScope());
 }

 @org.junit.Test
 public void testOAuthConfigHasScope() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testScope");
 assertTrue(oauthConfig.hasScope());
 }

}