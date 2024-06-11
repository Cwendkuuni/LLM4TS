package org.scribe.model;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

 @BeforeClass
 static void setUpBeforeClass() throws Exception {
 // Set up class level variables or initialize static resources here if needed
 }

 @AfterClass
 static void tearDownAfterClass() throws Exception {
 // Release shared resources or clean up after test here if needed
 }

 @Before
 static void setUp() throws Exception {
 // Set up test variables or initialize instance variables here if needed
 }

 @After
 static void tearDown() throws Exception {
 // Clean up after test here if needed
 }

 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
 assertNotNull(request.getOauthParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 request.addOAuthParameter("invalidKey", "value");
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testAddOAuthParameter() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "consumerKey");
 request.addOAuthParameter("oauth_nonce", "nonce");
 request.addOAuthParameter("oauth_signature", "signature");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "timestamp");
 request.addOAuthParameter("oauth_token", "token");
 request.addOAuthParameter("oauth_version", "1.0");
 assertFalse(request.getOauthParameters().isEmpty());
 }

 @Test
 public void testGetOAuthParameters() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "consumerKey");
 request.addOAuthParameter("oauth_nonce", "nonce");
 request.addOAuthParameter("oauth_signature", "signature");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "timestamp");
 request.addOAuthParameter("oauth_token", "token");
 request.addOAuthParameter("oauth_version", "1.0");

 Map<String, String> oauthParameters = request.getOauthParameters();
 List<String> keys = new ArrayList<>(oauthParameters.keySet());
 List<String> expectedKeys = Arrays.asList("oauth_consumer_key", "oauth_nonce", "oauth_signature", "oauth_signature_method", "oauth_timestamp", "oauth_token", "oauth_version");

 assertTrue(keys.containsAll(expectedKeys));
 }

}