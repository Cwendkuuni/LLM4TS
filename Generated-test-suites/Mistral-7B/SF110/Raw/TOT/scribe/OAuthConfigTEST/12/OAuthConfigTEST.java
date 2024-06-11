import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidArguments() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://localhost:8080/callback", SignatureType.Header, "scope1 scope2");

 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("http://localhost:8080/callback", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullArguments() {
 OAuthConfig config1 = new OAuthConfig("testKey", "testSecret");
 OAuthConfig config2 = new OAuthConfig("testKey", "testSecret", null, null, null);
 OAuthConfig config3 = new OAuthConfig("testKey", "testSecret", "callback", null, null);

 assertEquals("testKey", config1.getApiKey());
 assertEquals("testSecret", config1.getApiSecret());
 assertEquals("oob", config1.getCallback());
 assertEquals(SignatureType.Header, config1.getSignatureType());
 assertEquals(null, config1.getScope());
 assertFalse(config1.hasScope());

 assertEquals("testKey", config2.getApiKey());
 assertEquals("testSecret", config2.getApiSecret());
 assertEquals("callback", config2.getCallback());
 assertEquals(SignatureType.Header, config2.getSignatureType());
 assertEquals(null, config2.getScope());
 assertFalse(config2.hasScope());

 assertEquals("testKey", config3.getApiKey());
 assertEquals("testSecret", config3.getApiSecret());
 assertEquals("callback", config3.getCallback());
 assertEquals(SignatureType.Header, config3.getSignatureType());
 assertEquals("", config3.getScope());
 assertFalse(config3.hasScope());
 }

 @Test
 public void testOAuthConfigWithEmptyString() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "", SignatureType.Header, "");

 assertEquals("testKey", config.getApiKey());
 assertEquals("testSecret", config.getApiSecret());
 assertEquals("", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals("", config.getScope());
 assertFalse(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullKey() {
 OAuthConfig config = new OAuthConfig(null, "testSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSecret() {
 OAuthConfig config = new OAuthConfig("testKey", null);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "callback", null, null);
 }
}