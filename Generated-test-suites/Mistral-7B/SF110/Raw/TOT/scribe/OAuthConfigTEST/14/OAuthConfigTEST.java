import org.junit.Test;
import org.junit.Assert.*;

public class OAuthConfigTest {
    @Test
    public void testGetApiKey() {
        String expectedApiKey = "testKey";
        String apiKey = new OAuthConfig("testKey", "testSecret").getApiKey();
        String actualApiKey = new OAuthConfig("testKey2", "testSecret").getApiKey();

        // Test with valid input
        assertEquals(expectedApiKey, apiKey);

        // Test with different instance, same key
        assertNotSame(apiKey, actualApiKey);
    }
}