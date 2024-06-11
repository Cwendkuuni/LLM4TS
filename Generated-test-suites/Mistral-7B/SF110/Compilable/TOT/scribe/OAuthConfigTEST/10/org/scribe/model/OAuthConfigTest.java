package org.scribe.model;
import org.junit.Test;import static org.junit.Assert.*;import org.scribe.model.OAuthConfig;import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidArguments() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 assertEquals(apiKey, oAuthConfig.getApiKey());
 assertEquals(apiSecret, oAuthConfig.getApiSecret());
 assertEquals(callback, oAuthConfig.getCallback());
 assertEquals(signatureType, oAuthConfig.getSignatureType());
 assertEquals(scope, oAuthConfig.getScope());
 assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 String apiKey = "testApiKey";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, null, callback, signatureType, scope);

 assertNotNull(oAuthConfig.getApiKey());
 assertNull(oAuthConfig.getApiSecret());
 assertEquals(callback, oAuthConfig.getCallback());
 assertEquals(signatureType, oAuthConfig.getSignatureType());
 assertEquals(scope, oAuthConfig.getScope());
 assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(null, apiSecret, callback, signatureType, scope);

 assertNull(oAuthConfig.getApiKey());
 assertNotNull(oAuthConfig.getApiSecret());
 assertEquals(callback, oAuthConfig.getCallback());
 assertEquals(signatureType, oAuthConfig.getSignatureType());
 assertEquals(scope, oAuthConfig.getScope());
 assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);

 assertNotNull(oAuthConfig.getApiKey());
 assertNotNull(oAuthConfig.getApiSecret());
 assertEquals("oob", oAuthConfig.getCallback());
 assertEquals(signatureType, oAuthConfig.getSignatureType());
 assertEquals(scope, oAuthConfig.getScope());
 assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, null, scope);

 assertNotNull(oAuthConfig.getApiKey());
 assertNotNull(oAuthConfig.getApiSecret());
 assertEquals(callback, oAuthConfig.getCallback());
 assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 assertEquals(scope, oAuthConfig.getScope());
 assertTrue(oAuthConfig.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullScope() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
 }

}