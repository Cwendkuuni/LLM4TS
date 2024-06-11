import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {
    @Test
    public void testGetApiKey() {
        String expectedApiKey = "testApiKey";
        OAuthConfig config = new OAuthConfig(expectedApiKey, "testApiSecret");
        String actualApiKey = config.getApiKey();

        assertEquals(expectedApiKey, actualApiKey);
    }
}