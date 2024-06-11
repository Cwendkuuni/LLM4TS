import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigDefaultConstructor() {
 OAuthConfig oAuthConfig = new OAuthConfig("");
 OAuthConfig expectedOAuthConfig = new OAuthConfig("", "", null, null, null);

 assertEquals(expectedOAuthConfig.getApiKey(), oAuthConfig.getApiKey());
 assertEquals(expectedOAuthConfig.getApiSecret(), oAuthConfig.getApiSecret());
 assertEquals(expectedOAuthConfig.getCallback(), oAuthConfig.getCallback());
 assertEquals(expectedOAuthConfig.getSignatureType(), oAuthConfig.getSignatureType());
 assertEquals(expectedOAuthConfig.getScope(), oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigConstructorWithOnlyApiKeyAndApiSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");

 assertEquals("testKey", oAuthConfig.getApiKey());
 assertEquals("testSecret", oAuthConfig.getApiSecret());
 assertEquals("oob", oAuthConfig.getCallback());
 assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 assertEquals(null, oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigConstructorWithAllParameters() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryStringParameter, "testScope");

 assertEquals("testKey", oAuthConfig.getApiKey());
 assertEquals("testSecret", oAuthConfig.getApiSecret());
 assertEquals("testCallback", oAuthConfig.getCallback());
 assertEquals(SignatureType.QueryStringParameter, oAuthConfig.getSignatureType());
 assertEquals("testScope", oAuthConfig.getScope());
 }

 @Test
 public void testHasScope() {
 OAuthConfig oAuthConfigWithScope = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 OAuthConfig oAuthConfigWithoutScope = new OAuthConfig("testKey", "testSecret");

 assertTrue(oAuthConfigWithScope.hasScope());
 assertFalse(oAuthConfigWithoutScope.hasScope());
 }
}