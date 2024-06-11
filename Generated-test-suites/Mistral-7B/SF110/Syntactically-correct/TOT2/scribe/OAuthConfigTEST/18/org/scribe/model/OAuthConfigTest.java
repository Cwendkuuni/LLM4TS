import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig oAuthConfig = new OAuthConfig("testKey");
        String apiKey = oAuthConfig.getApiKey();
        Assert.assertEquals("testKey", apiKey);
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
        String apiSecret = oAuthConfig.getApiSecret();
        Assert.assertEquals("testSecret", apiSecret);
    }

    @Test
    public void testGetCallback() {
        OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback");
        String callback = oAuthConfig.getCallback();
        Assert.assertEquals("testCallback", callback);
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
        SignatureType signatureType = oAuthConfig.getSignatureType();
        Assert.assertEquals(SignatureType.Header, signatureType);
    }

    @Test
    public void testGetScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
        String scope = oAuthConfig.getScope();
        Assert.assertEquals("testScope", scope);
    }

    @Test
    public void testHasScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
        boolean hasScope = oAuthConfig.hasScope();
        Assert.assertTrue(hasScope);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionHandling() {
        new OAuthConfig(null, null); // constructor with null arguments

        OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
        String nullApiKey = null;
        String nullApiSecret = null;

        nullApiKey = oAuthConfig.getApiKey(); // getApiKey() returns null
        Assert.assertNull(nullApiKey);

        nullApiSecret = oAuthConfig.getApiSecret(); // getApiSecret() returns null
        Assert.assertNull(nullApiSecret);
    }

}