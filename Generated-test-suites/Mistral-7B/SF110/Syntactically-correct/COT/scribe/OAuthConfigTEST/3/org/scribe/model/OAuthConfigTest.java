package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

 private static final String API_KEY = "apiKey";
 private static final String API_SECRET = "apiSecret";
 private static final String CALLBACK = "callback";
 private static final SignatureType SIGNATURE_TYPE_HEADER = SignatureType.Header;
 private static final SignatureType SIGNATURE_TYPE_QUERY = SignatureType.Query;
 private static final String SCOPE = "scope";

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET);
 assertEquals(API_KEY, oauthConfig.getApiKey());
 assertEquals(API_SECRET, oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertEquals(SIGNATURE_TYPE_HEADER, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET, CALLBACK);
 assertEquals(API_KEY, oauthConfig.getApiKey());
 assertEquals(API_SECRET, oauthConfig.getApiSecret());
 assertEquals(CALLBACK, oauthConfig.getCallback());
 assertNull(oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithAllParams() {
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET, CALLBACK, SIGNATURE_TYPE_QUERY, SCOPE);
 assertEquals(API_KEY, oauthConfig.getApiKey());
 assertEquals(API_SECRET, oauthConfig.getApiSecret());
 assertEquals(CALLBACK, oauthConfig.getCallback());
 assertEquals(SIGNATURE_TYPE_QUERY, oauthConfig.getSignatureType());
 assertEquals(SCOPE, oauthConfig.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 assertNull(new OAuthConfig(null, API_SECRET));
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 assertNull(new OAuthConfig(API_KEY, null));
 }

 @Test
 public void testEmptyApiKey() {
 String emptyApiKey = "";
 OAuthConfig oauthConfig = new OAuthConfig(emptyApiKey, API_SECRET);
 assertEquals(emptyApiKey, oauthConfig.getApiKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullCallback() {
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET, null);
 assertNull(oauthConfig.getCallback());
 }

 @Test(expected = NullPointerException.class)
 public void testNullSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET, CALLBACK, null);
 assertNull(oauthConfig.getSignatureType());
 }

 @Test
 public void testEmptyScope() {
 String emptyScope = "";
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET, CALLBACK, SIGNATURE_TYPE_QUERY, emptyScope);
 assertEquals(emptyScope, oauthConfig.getScope());
 }

 @Test
 public void testHasScopeReturnsTrueWithScope() {
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET, CALLBACK, SIGNATURE_TYPE_QUERY, SCOPE);
 assertTrue(oauthConfig.hasScope());
 }

 @Test
 public void testHasScopeReturnsFalseWithoutScope() {
 OAuthConfig oauthConfig = new OAuthConfig(API_KEY, API_SECRET, CALLBACK, SIGNATURE_TYPE_QUERY, null);
 assertFalse(oauthConfig.hasScope());
 }
}