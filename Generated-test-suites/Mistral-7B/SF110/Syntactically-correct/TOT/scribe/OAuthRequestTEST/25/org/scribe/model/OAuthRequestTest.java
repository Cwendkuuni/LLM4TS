import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.*;

public class OAuthRequestTest {
   // Test case for constructor
   @Test
   public void testOAuthRequestConstruction() {
      OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
      assertNull(request.getOauthParameters());
   }

   // Test case for addOAuthParameter method
   @Test
   public void testAddOAuthParameter() {
      OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
      request.addOAuthParameter("oauth_token", "abc123");
      Map<String, String> oauthParameters = request.getOauthParameters();
      assertEquals("abc123", oauthParameters.get("oauth_token"));
   }

   // Test case for checkKey method
   @Test(expected = IllegalArgumentException.class)
   public void testCheckKeyIllegalArgumentException() {
      OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
      request.addOAuthParameter("invalid_key", "value");
      request.checkKey("invalid_key");
   }

   // Test case for getOauthParameters method
   @Test
   public void testGetOauthParameters() {
      OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
      request.addOAuthParameter("oauth_token", "abc123");
      request.addOAuthParameter("scope", "read write");
      Map<String, String> oauthParameters = request.getOauthParameters();
      assertEquals("read write", oauthParameters.get("scope"));
      assertEquals("abc123", oauthParameters.get("oauth_token"));
   }
}