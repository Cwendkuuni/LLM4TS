package org.scribe.model;
import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertNull(config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://callback.com", SignatureType.QueryString, "scope1 scope2");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("http://callback.com", config.getCallback());
 Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
 Assert.assertEquals("scope1 scope2", config.getScope());
 Assert.assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithOnlyKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertFalse(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 OAuthConfig config = new OAuthConfig(null, "testSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 OAuthConfig config = new OAuthConfig("testKey", null);
 }

 @Test
 public void testEmptyApiKey() {
 OAuthConfig config = new OAuthConfig("", "testSecret");
 Assert.assertEquals("", config.getApiKey());
 }

 @Test
 public void testEmptyApiSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("", config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://callback.com", null, "scope1 scope2");
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, "scope1 scope2");
 Assert.assertEquals("oob", config.getCallback());
 }
}