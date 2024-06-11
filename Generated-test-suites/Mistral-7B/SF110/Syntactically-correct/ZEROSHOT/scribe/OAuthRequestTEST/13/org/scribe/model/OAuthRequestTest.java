package org.scribe.model;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

 @BeforeClass
 static void setUpBeforeClass() throws Exception {
 // Set up class level variables or initialize static resources here
 }

 @AfterClass
 static void tearDownAfterClass() throws Exception {
 // Release shared resources or clean up after the tests here
 }

 @Before
 static void setUp() {
 // Set up test environment or initialize test objects here
 }

 @After
 static void tearDown() {
 // Clean up test environment or release test objects here
 }

 @Test
 public void testOAuthRequestWithValidVerbAndUrl() {
 OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
 assertEquals("@OAuthRequest(GET, https://api.example.com)", request.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthRequestWithInvalidVerb() {
 try {
 OAuthRequest request = new OAuthRequest(new Verb("INVALID"), "https://api.example.com");
 fail("Expected an IllegalArgumentException, but no exception was thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception, do nothing
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthRequestWithInvalidUrl() {
 try {
 OAuthRequest request = new OAuthRequest(Request.Verb.GET, null);
 fail("Expected an IllegalArgumentException, but no exception was thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception, do nothing
 }
 }

 @Test
 public void testAddOAuthParameter() {
 OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
 request.addOAuthParameter("oauth_token", "abc123");
 Map<String, String> params = request.getOauthParameters();
 assertEquals("abc123", params.get("oauth_token"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
 try {
 request.addOAuthParameter("invalid_key", "value");
 fail("Expected an IllegalArgumentException, but no exception was thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception, do nothing
 }
 }

 @Test
 public void testGetOAuthParameters() {
 OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
 request.addOAuthParameter("oauth_token", "abc123");
 request.addOAuthParameter("oauth_token_secret", "def456");
 request.addOAuthParameter("scope", "read write");
 Map<String, String> params = request.getOauthParameters();
 assertEquals(3, params.size());
 assertEquals("abc123", params.get("oauth_token"));
 assertEquals("def456", params.get("oauth_token_secret"));
 assertEquals("read write", params.get("scope"));
 }

 @Test
 public void testToStringWithValidParameters() {
 OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
 request.addOAuthParameter("oauth_token", "abc123");
 request.addOAuthParameter("oauth_token_secret", "def456");
 request.addOAuthParameter("scope", "read write");
 assertEquals("@OAuthRequest(GET, https://api.example.com, [oauth_token=abc123, oauth_token_secret=def456, scope=read write])", request.toString());
 }
}