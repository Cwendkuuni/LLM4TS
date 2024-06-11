import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testDefaultConstruction() {
 OAuthConfig config = new OAuthConfig("apiKey1", "apiSecret1");
 assertEquals("oob", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testCustomConstruction() {
 OAuthConfig config = new OAuthConfig("apiKey1", "apiSecret1", "http://callback.com", SignatureType.QueryParam, "scope1 scope2");
 assertEquals("http://callback.com", config.getCallback());
 assertEquals(SignatureType.QueryParam, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test
 public void testGetters() {
 OAuthConfig config = new OAuthConfig("apiKey1", "apiSecret1", "http://callback.com", SignatureType.QueryParam, "scope1 scope2");
 assertEquals("apiKey1", config.getApiKey());
 assertEquals("apiSecret1", config.getApiSecret());
 assertEquals("http://callback.com", config.getCallback());
 assertEquals(SignatureType.QueryParam, config.getSignatureType());
 assertEquals("scope1 scope2", config.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 Assert.assertThrows(NullPointerException.class, () -> new OAuthConfig(null, "apiSecret1"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 Assert.assertThrows(NullPointerException.class, () -> new OAuthConfig("apiKey1", null));
 }

 @Test
 public void testHasScope() {
 OAuthConfig config1 = new OAuthConfig("apiKey1", "apiSecret1");
 assertFalse(config1.hasScope());
 
 OAuthConfig config2 = new OAuthConfig("apiKey1", "apiSecret1", null, null, "scope1 scope2");
 assertTrue(config2.hasScope());
}

}