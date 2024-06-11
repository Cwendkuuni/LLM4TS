import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testCallback");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertEquals("testCallback", oauthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertEquals("testCallback", oauthConfig.getCallback());
 assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
 assertEquals("testScope", oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", SignatureType.Header);
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertEquals("oob", oauthConfig.getCallback());
 assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret");
 assertTrue(oauthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndNoScope() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret");
 assertFalse(oauthConfig.hasScope());
 }
}