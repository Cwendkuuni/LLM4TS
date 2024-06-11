import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 private String key = "testKey";
 private String secret = "testSecret";
 private String callback = "http://localhost:8080/callback";
 private SignatureType signatureType = SignatureType.Header;
 private String scope = "testScope";

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig(key, secret);
 assertEquals(key, config.getApiKey());
 assertEquals(secret, config.getApiSecret());
 assertNull(config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig config = new OAuthConfig(key, secret, callback, signatureType, scope);
 assertEquals(key, config.getApiKey());
 assertEquals(secret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertEquals(scope, config.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 assertNull(new OAuthConfig(null, secret));
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 assertNull(new OAuthConfig(key, null));
 }

 @Test
 public void testEmptyCallback() {
 OAuthConfig config = new OAuthConfig(key, secret, "", signatureType, scope);
 assertEquals(key, config.getApiKey());
 assertEquals(secret, config.getApiSecret());
 assertEquals("", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSignatureType() {
 assertNull(new OAuthConfig(key, secret, callback, null, scope));
 }

 @Test
 public void testDefaultCallback() {
 OAuthConfig config = new OAuthConfig(key, secret);
 assertEquals("oob", config.getCallback());
 }

 @Test
 public void testHasScope() {
 OAuthConfig config = new OAuthConfig(key, secret, callback, signatureType, scope);
 assertTrue(config.hasScope());
 }

 @Test
 public void testNoScope() {
 OAuthConfig config = new OAuthConfig(key, secret, callback, signatureType, null);
 assertFalse(config.hasScope());
 }
}