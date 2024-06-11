import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 private static final String API_KEY = "testApiKey";
 private static final String API_SECRET = "testApiSecret";
 private static final String CALLBACK = "testCallback";
 private static final SignatureType SIGNATURE_TYPE_HEADER = SignatureType.Header;
 private static final SignatureType SIGNATURE_TYPE_QUERY = SignatureType.Query;
 private static final String SCOPE = "testScope";

 @Test
 public void testOAuthConfigConstructionWithApiKeyAndApiSecret() {
 OAuthConfig config = new OAuthConfig(API_KEY, API_SECRET);
 Assert.assertEquals(API_KEY, config.getApiKey());
 Assert.assertEquals(API_SECRET, config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigConstructionWithApiKeySecretCallbackScopeSignatureType() {
 OAuthConfig config = new OAuthConfig(API_KEY, API_SECRET, CALLBACK, SIGNATURE_TYPE_QUERY, SCOPE);
 Assert.assertEquals(API_KEY, config.getApiKey());
 Assert.assertEquals(API_SECRET, config.getApiSecret());
 Assert.assertEquals(CALLBACK, config.getCallback());
 Assert.assertEquals(SignatureType.Query, config.getSignatureType());
 Assert.assertEquals(SCOPE, config.getScope());
 }

 @Test
 public void testGetters() {
 OAuthConfig config = new OAuthConfig(API_KEY, API_SECRET, CALLBACK, SIGNATURE_TYPE_HEADER, SCOPE);
 Assert.assertEquals(API_KEY, config.getApiKey());
 Assert.assertEquals(API_SECRET, config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertEquals(SCOPE, config.getScope());
 Assert.assertTrue(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 Assert.assertThrows(NullPointerException.class, () -> new OAuthConfig(null, API_SECRET));
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 Assert.assertThrows(NullPointerException.class, () -> new OAuthConfig(API_KEY, null));
 }

 @Test
 public void testEmptyCallback() {
 OAuthConfig config = new OAuthConfig(API_KEY, API_SECRET, "", SIGNATURE_TYPE_HEADER, SCOPE);
 Assert.assertEquals("", config.getCallback());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSignatureType() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new OAuthConfig(API_KEY, API_SECRET, CALLBACK, SignatureType.Invalid, SCOPE));
 }
}