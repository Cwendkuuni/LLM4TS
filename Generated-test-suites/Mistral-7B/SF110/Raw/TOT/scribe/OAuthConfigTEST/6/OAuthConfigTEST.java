import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testDefaultConstruction() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 assertEquals("oob", config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testCustomConstruction() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://example.com/callback", SignatureType.QueryString, "read_scope,write_scope");
 assertEquals("http://example.com/callback", config.getCallback());
 assertEquals(SignatureType.QueryString, config.getSignatureType());
 assertEquals("read_scope,write_scope", config.getScope());
 }

 @Test
 public void testNullArguments() {
 try {
 new OAuthConfig(null, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }

 try {
 new OAuthConfig("", "");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "");
 assertFalse(config.hasScope());
 }

 @Test
 public void testNullScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 assertFalse(config.hasScope());
 }
}