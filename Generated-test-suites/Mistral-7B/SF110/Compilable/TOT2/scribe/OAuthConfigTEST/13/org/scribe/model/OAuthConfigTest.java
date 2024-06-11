package org.scribe.model;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {
    @Test
    public void testGetApiKey() {
        String key = "testKey";
        OAuthConfig config = new OAuthConfig(key, "");
        String actualApiKey = config.getApiKey();
        String expectedApiKey = key;
        assertEquals(expectedApiKey, actualApiKey);
    }
}