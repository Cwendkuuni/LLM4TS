import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        String key = "testApiKey";
        OAuthConfig oAuthConfig = new OAuthConfig(key, "testApiSecret");
        String result = oAuthConfig.getApiKey();
        assertEquals(key, result);
    }
}