import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidArguments() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 OAuthConfig configWithCallback = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.Header, "testScope");
 OAuthConfig configWithDefaults = new OAuthConfig("testKey", "testSecret");

 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("oob", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals(null, config.getScope());
 assertEquals(false, config.hasScope());

 assertEquals("testKey", configWithCallback.getApiKey());
 assertEquals("testSecret", configWithCallback.getApiSecret());
 assertEquals("http://test.com/callback", configWithCallback.getCallback());
 assertEquals(SignatureType.Header, configWithCallback.getSignatureType());
 assertEquals("testScope", configWithCallback.getScope());
 assertEquals(true, configWithCallback.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 try {
 new OAuthConfig(null, "testApiKey");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testOAuthConfigWithEmptyApiKey() {
 try {
 new OAuthConfig("", "testApiSecret");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testOAuthConfigWithNullApiKeyAndApiSecret() {
 try {
 new OAuthConfig(null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", null, "testScope");
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.Header, "");
 assertEquals(null, config.getScope());
 assertEquals(false, config.hasScope());
 }
}