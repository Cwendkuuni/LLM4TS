import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidArguments() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 OAuthConfig configWithCallback = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.Query, "scope1 scope2");

 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("oob", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals(null, config.getScope());
 assertEquals(false, config.hasScope());

 assertEquals("testKey", configWithCallback.getApiKey());
 assertEquals("testSecret", configWithCallback.getApiSecret());
 assertEquals("http://test.com/callback", configWithCallback.getCallback());
 assertEquals(SignatureType.Query, configWithCallback.getSignatureType());
 assertEquals("scope1 scope2", configWithCallback.getScope());
 assertEquals(true, configWithCallback.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 try {
 new OAuthConfig(null, "testSecret");
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 try {
 new OAuthConfig("testKey", null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "");
 assertEquals(null, config.getScope());
 assertEquals(false, config.hasScope());
 }

 @Test
 public void testOAuthConfigWithInvalidSignatureType() {
 try {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.INVALID, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}