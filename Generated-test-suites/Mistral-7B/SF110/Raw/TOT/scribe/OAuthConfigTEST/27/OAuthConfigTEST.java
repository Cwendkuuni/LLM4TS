import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidArguments() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 OAuthConfig configWithCallback = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.Header, "testScope");

 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("oob", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals("testScope", config.getScope());
 assertEquals(true, config.hasScope());

 assertEquals("testKey", configWithCallback.getApiKey());
 assertEquals("testSecret", configWithCallback.getApiSecret());
 assertEquals("http://test.com/callback", configWithCallback.getCallback());
 assertEquals(SignatureType.Header, configWithCallback.getSignatureType());
 assertEquals("testScope", configWithCallback.getScope());
 assertEquals(true, configWithCallback.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 try {
 new OAuthConfig(null, "testSecret");
 fail("Should have thrown a NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 try {
 new OAuthConfig("testKey", null);
 fail("Should have thrown a NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals(config.getCallback(), "oob");

 OAuthConfig configWithCallback = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, "testScope");
 assertEquals(configWithCallback.getCallback(), "oob");
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals(config.getSignatureType(), SignatureType.Header);

 OAuthConfig configWithSignatureType = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", null, "testScope");
 assertEquals(configWithSignatureType.getSignatureType(), SignatureType.Header);
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.Header, "");
 assertEquals(config.getScope(), "");
 assertEquals(false, config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.Header, null);
 assertEquals(config.getScope(), null);
 assertEquals(false, config.hasScope());
 }
}