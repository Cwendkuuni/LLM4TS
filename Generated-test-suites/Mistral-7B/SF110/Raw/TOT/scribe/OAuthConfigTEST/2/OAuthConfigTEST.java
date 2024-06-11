import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidArguments() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 OAuthConfig configWithCallback = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.QueryString, "scope1 scope2");

 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("oob", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals(null, config.getScope());

 assertEquals("testKey", configWithCallback.getApiKey());
 assertEquals("testSecret", configWithCallback.getApiSecret());
 assertEquals("http://test.com/callback", configWithCallback.getCallback());
 assertEquals(SignatureType.QueryString, configWithCallback.getSignatureType());
 assertEquals("scope1 scope2", configWithCallback.getScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 assertFalse(new OAuthConfig(null, "testSecret") instanceof OAuthConfig);
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 assertFalse(new OAuthConfig("testKey", null) instanceof OAuthConfig);
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals("oob", config.getCallback());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "");
 assertFalse(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNonEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "scope1 scope2");
 assertTrue(config.hasScope());
 }
}