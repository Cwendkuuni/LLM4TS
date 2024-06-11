import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OAuthConfigTest {
    // 
    @Test
    public void testGetApiKey() {
        String apiKey = "testKey";
        OAuthConfig config = new OAuthConfig(apiKey, "testSecret");
        String returnedApiKey = config.getApiKey();
        assertEquals(apiKey, returnedApiKey);
    }
    // 
}