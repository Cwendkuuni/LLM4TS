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
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullValues() {
 OAuthConfig config = new OAuthConfig(null, null, null, null, null);
 Assert.assertNull(config.getApiKey());
 Assert.assertNull(config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithOnlySignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, null);
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithOnlyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 }

 @Test
 public void testOAuthConfigHasScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertTrue(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiKey() {
 OAuthConfig config = new OAuthConfig(null, "testSecret", null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiSecret() {
 OAuthConfig config = new OAuthConfig("testKey", null, null, null, null);
 }
}