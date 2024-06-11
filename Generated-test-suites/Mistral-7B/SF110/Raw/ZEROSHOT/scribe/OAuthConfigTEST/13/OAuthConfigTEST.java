package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OAuthConfigTest {

 private String apiKey;
 private String apiSecret;
 private String callback;
 private SignatureType signatureType;
 private String scope;

 @Test
 public void testDefaultConstructor() {
     OAuthConfig oauthConfig = new OAuthConfig();
     assertNull(oauthConfig.getApiKey());
     assertNull(oauthConfig.getApiSecret());
     assertNull(oauthConfig.getCallback());
     assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
     assertNull(oauthConfig.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecret() {
     apiKey = "apiKey";
     apiSecret = "apiSecret";
     OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);
     assertEquals(apiKey, oauthConfig.getApiKey());
     assertEquals(apiSecret, oauthConfig.getApiSecret());
     assertNull(oauthConfig.getCallback());
     assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
     assertNull(oauthConfig.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndCallback() {
     apiKey = "apiKey";
     apiSecret = "apiSecret";
     callback = "http://example.com/callback";
     OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback);
     assertEquals(apiKey, oauthConfig.getApiKey());
     assertEquals(apiSecret, oauthConfig.getApiSecret());
     assertEquals(callback, oauthConfig.getCallback());
     assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
     assertNull(oauthConfig.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
     apiKey = "apiKey";
     apiSecret = "apiSecret";
     callback = "http://example.com/callback";
     signatureType = SignatureType.QueryString;
     OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType);
     assertEquals(apiKey, oauthConfig.getApiKey());
     assertEquals(apiSecret, oauthConfig.getApiSecret());
     assertEquals(callback, oauthConfig.getCallback());
     assertEquals(signatureType, oauthConfig.getSignatureType());
     assertNull(oauthConfig.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
     apiKey = "apiKey";
     apiSecret = "apiSecret";
     callback = "http://example.com/callback";
     signatureType = SignatureType.QueryString;
     scope = "scope";
     OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, oauthConfig.getApiKey());
     assertEquals(apiSecret, oauthConfig.getApiSecret());
     assertEquals(callback, oauthConfig.getCallback());
     assertEquals(signatureType, oauthConfig.getSignatureType());
     assertEquals(scope, oauthConfig.getScope());
 }

 @Test
 public void testGetApiKey() {
     OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
     assertEquals("apiKey", oauthConfig.getApiKey());
 }

 @Test
 public void testGetApiSecret() {
     OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
     assertEquals("apiSecret", oauthConfig.getApiSecret());
 }

 @Test
 public void testGetCallback() {
     OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null);
     assertEquals("oob", oauthConfig.getCallback());
     OAuthConfig oauthConfig2 = new OAuthConfig("apiKey", "apiSecret", "http://example.com/callback");
     assertEquals("http://example.com/callback", oauthConfig2.getCallback());
 }

 @Test
 public void testGetSignatureType() {
     OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
     assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
     OAuthConfig oauthConfig2 = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.QueryString);
     assertEquals(SignatureType.QueryString, oauthConfig2.getSignatureType());
 }

 @Test
 public void testGetScope() {
     OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, null, "scope");
     assertEquals("scope", oauthConfig.getScope());
 }

 @Test
 public void testHasScope() {
     OAuthConfig oauthConfig1 = new OAuthConfig("apiKey", "apiSecret", null, null, null);
     assertFalse(oauthConfig1.hasScope());
     OAuthConfig oauthConfig2 = new OAuthConfig("apiKey", "apiSecret", null, null, "scope");
     assertTrue(oauthConfig2.hasScope());
 }
}