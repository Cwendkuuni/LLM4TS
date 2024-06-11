import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequest() {
        // Test case for constructor and getters (all verbs)
        for (Verb verb : Verb.values()) {
            OAuthRequest request = new OAuthRequest(verb, "http://example.com");
            assertNull(request.getOauthParameters());

            request.addOAuthParameter("oauth_consumer_key", "test_key");
            Map<String, String> parameters = request.getOauthParameters();
            assertEquals("test_key", parameters.get("oauth_consumer_key"));
        }
    }

    // Test cases for checkKey method
    @Test
    public void testCheckKey() {
        Expert1TestCases.testCheckKey();
        Expert2TestCases.testCheckKeyEmptyKey();
        Expert2TestCases.testCheckKeyInvalidKey();
        Expert3TestCases.testCheckKeyScopeKey();
    }

    // Test cases for addOAuthParameter method and getOauthParameters method
    @Test
    public void testAddOAuthParameter() {
        Expert1TestCases.testAddOAuthParameter();
        Expert1TestCases.testAddOAuthParameterDuplicateKey();
        Expert2TestCases.testAddOAuthParameterDuplicateKey();
        Expert3TestCases.testAddOAuthParameterEmptyValue();
        Expert3TestCases.testAddOAuthParameterNullValue();
        Expert3TestCases.testAddOAuthParameterLargeNumberKeys();
    }

    // Test cases provided by the experts
    static class Expert1TestCases {
        @Test
        public void testCheckKey() {
            testCheckKeyValidKey();
            testCheckKeyInvalidKey();
        }

        @Test
        public void testAddOAuthParameter() {
            testAddOAuthParameterValidKey();
            testAddOAuthParameterDuplicateKey();
        }

        private void testCheckKeyValidKey() {
            String validKey = "oauth_consumer_key";
            new OAuthRequest(Verb.GET, "").checkKey(validKey);
            assertEquals(validKey, new OAuthRequest(Verb.GET, "").getOauthParameters().get(validKey));
        }

        private void testCheckKeyInvalidKey() {
            String invalidKey = "invalid_key";
            new OAuthRequest(Verb.GET, "").checkKey(invalidKey);
        }

        private void testAddOAuthParameterValidKey() {
            OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

            request.addOAuthParameter("oauth_consumer_key", "test_key");
            Map<String, String> parameters = request.getOauthParameters();
            assertEquals("test_key", parameters.get("oauth_consumer_key"));
        }
    }

    static class Expert2TestCases {
        @Test
        public void testCheckKeyEmptyKey() {
            String emptyKey = "";
            new OAuthRequest(Verb.GET, "").checkKey(emptyKey);
        }

        @Test
        public void testCheckKeyInvalidKey() {
            String invalidKey = "invalid_key";
            new OAuthRequest(Verb.GET, "").checkKey(invalidKey);
        }

        @Test
        public void testAddOAuthParameterDuplicateKey() {
            OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

            request.addOAuthParameter("oauth_consumer_key", "test_key");
            request.addOAuthParameter("oauth_consumer_key", "test_key_duplicate");
        }
    }

    static class Expert3TestCases {
        @Test
        public void testAddOAuthParameterEmptyValue() {
            OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

            request.addOAuthParameter("oauth_consumer_key", "");
            Map<String, String> parameters = request.getOauthParameters();
            assertNull(parameters.get("oauth_consumer_key"));
        }

        @Test
        public void testAddOAuthParameterNullValue() {
            OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

            request.addOAuthParameter("oauth_consumer_key", null);
            Map<String, String> parameters = request.getOauthParameters();
            assertNull(parameters.get("oauth_consumer_key"));
        }

        @Test
        public void testAddOAuthParameterLargeNumberKeys() {
            final int NUM_KEYS = 100;
            OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

            for (int i = 0; i < NUM_KEYS; i++) {
                request.addOAuthParameter(String.format("oauth_key_%d", i), String.valueOf(i));
            }

            Map<String, String> parameters = request.getOauthParameters();
            assertEquals(NUM_KEYS, parameters.size());
        }
    }
}