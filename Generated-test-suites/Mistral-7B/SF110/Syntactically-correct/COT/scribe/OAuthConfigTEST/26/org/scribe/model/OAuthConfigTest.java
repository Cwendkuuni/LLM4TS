package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignaturTypeEnum; // Assuming SignatureType is an Enum

public class OAuthConfigTest {

 private final String apiKey = "testKey";
 private final String apiSecret = "testSecret";
 private final String callback = "testCallback";
 private final SignaturTypeEnum signatureType = SignaturTypeEnum.Header;
 private final String scope = "testScope";

 @Test
 public void testOAuthConfigWithNullKeyAndSecret() {
     OAuthConfig config = new OAuthConfig(null, null);
     assertNull(config.getApiKey());
     assertNull(config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithValidParameters() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
     assertEquals(apiKey, config.getApiKey());
     assertEquals(apiSecret, config.getApiSecret());
     assertEquals(callback, config.getCallback());
     assertEquals(signatureType, config.getSignatureType());
     assertEquals(scope, config.getScope());
     assertEquals(true, config.hasScope());
 }

 @Test
 public void testOAuthConfigWithEmptyCallback() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, "", signatureType, scope);
     assertEquals("", config.getCallback());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
     OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
     assertEquals("oob", config.getCallback());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiKey() {
     new OAuthConfig(null, apiSecret);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiSecret() {
     new OAuthConfig(apiKey, null);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
     new OAuthConfig(apiKey, apiSecret, callback, null, scope);
 }
}