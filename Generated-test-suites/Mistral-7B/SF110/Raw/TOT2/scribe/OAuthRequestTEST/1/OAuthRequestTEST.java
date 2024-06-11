// Your comprehensive JUnit 4 test file for the OAuthRequest class
package org.junit.platform.commons.annotation.Test;
import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestConstruction() {
        // Given
        String url = "https://api.example.com";
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, url);

        // Then
        Assert.assertEquals("GET", request.getVerb().name());
        Assert.assertNotNull(request.getOauthParameters());
        Assert.assertEquals(0, request.getOauthParameters().size());
    }

    @Test
    public void testAddOAuthParameter() {
        // Given
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");

        // When
        request.addOAuthParameter("foo", "bar");

        // Then
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals("bar", oauthParameters.get("foo"));
        Assert.assertEquals(1, oauthParameters.size());
    }

    // Test for null or empty constructor arguments
    @Test(expected = NullPointerException.class)
    public void testOAuthRequestNullUrl() {
        new OAuthRequest(Request.Verb.GET, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthRequestEmptyUrl() {
        new OAuthRequest(Request.Verb.GET, "");
    }

    // Test for adding the same key twice
    @Test
    public void testAddOAuthParameterDuplicateKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
        request.addOAuthParameter("foo", "bar");
        request.addOAuthParameter("foo", "baz");

        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals("bar", oauthParameters.get("foo"));
        Assert.assertEquals(1, oauthParameters.size());
    }

    // Test for invalid key in addOAuthParameter method
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
        request.addOAuthParameter("invalid", "value");
    }

    // Test for concurrent modification of oauthParameters map
    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModification() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(() -> request.addOAuthParameter("key1", "value1"));
        Future<?> future2 = executor.submit(() -> request.addOAuthParameter("key2", "value2"));

        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals(2, oauthParameters.size());
    }
}

//