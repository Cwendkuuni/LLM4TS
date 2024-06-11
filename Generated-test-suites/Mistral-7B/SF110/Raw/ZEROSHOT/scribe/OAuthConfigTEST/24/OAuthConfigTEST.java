package org.scribe.model;
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertTrue;

 import org.junit.Test;
 import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

 @RunWith(JUnit4.class)
 public class OAuthConfigTest {

 private OAuthConfig config1;
 private OAuthConfig config2;

 @Test
 public void testOAuthConfigWithNullArguments() {
 config1 = new OAuthConfig(null, null);

 assertNull(config1.getApiKey());
 assertNull(config1.getApiSecret());
 assertNull(config1.getCallback());
 assertNull(config1.getSignatureType());
 assertNull(config1.getScope());
 }

 @Test
 public void testOAuthConfigWithValidArguments() {
 config2 = new OAuthConfig("apiKey1", "apiSecret1", "callbackUrl", SignatureType.QueryString, "scope1 scope2");

 assertEquals("apiKey1", config2.getApiKey());
 assertEquals("apiSecret1", config2.getApiSecret());
 assertEquals("callbackUrl", config2.getCallback());
 assertEquals(SignatureType.QueryString, config2.getSignatureType());
 assertEquals("scope1 scope2", config2.getScope());
 assertTrue(config2.hasScope());
 }

 @Test
 public void testOAuthConfigWithValidArgumentsWithoutScope() {
 OAuthConfig config3 = new OAuthConfig("apiKey2", "apiSecret2", "callbackUrl2", SignatureType.Header);

 assertEquals("apiKey2", config3.getApiKey());
 assertEquals("apiSecret2", config3.getApiSecret());
 assertEquals("callbackUrl2", config3.getCallback());
 assertEquals(SignatureType.Header, config3.getSignatureType());
 assertNull(config3.getScope());
 assertFalse(config3.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config4 = new OAuthConfig("apiKey3", "apiSecret3", "callbackUrl3", null, "scope3 scope4");
 }
 }