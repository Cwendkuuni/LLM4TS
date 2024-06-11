package org.scribe.model;

import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.*;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

 @BeforeClass
 public static void setupClass() {
 System.out.println("### OAuthRequestTest.java - Before Class ###");
 }

 @AfterClass
 public static void tearDownClass() {
 System.out.println("### OAuthRequestTest.java - After Class ###");
 }

 @Before
 public void setup() {
 System.out.println("### OAuthRequestTest.java - Before Method ###");
 }

 @After
 public void tearDown() {
 System.out.println("### OAuthRequestTest.java - After Method ###");
 }

 @Test
 public void testOAuthRequestConstruction() {
 Verb verb = Verb.GET;
 String url = "http://example.com/api";
 OAuthRequest oauthRequest = new OAuthRequest(verb, url);
 assertEquals("@OAuthRequest(GET, http://example.com/api)", oauthRequest.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("invalid_key", "value");
 fail("IllegalArgumentException not thrown");
 }

 @Test
 public void testAddOAuthParameter() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("oauth_nonce", "nonce_value");
 Map<String, String> expectedOAuthParameters = new HashMap<>();
 expectedOAuthParameters.put("oauth_nonce", "nonce_value");
 assertEquals(expectedOAuthParameters, oauthRequest.getOauthParameters());
 }

 @Test
 public void testAddOAuthParameterWithDuplicateKey() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("oauth_nonce", "nonce_value_1");
 oauthRequest.addOAuthParameter("oauth_nonce", "nonce_value_2");
 Map<String, String> expectedOAuthParameters = new HashMap<>();
 expectedOauthParameters.put("oauth_nonce", "nonce_value_2");
 assertEquals(expectedOAuthParameters, oauthRequest.getOauthParameters());
 }

 @Test
 public void testAddScopeParameter() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> expectedOAuthParameters = new HashMap<>();
 expectedOauthParameters.put("scope", "read write");
 assertEquals(expectedOAuthParameters, oauthRequest.getOauthParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidScopeKey() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("invalid_scope_key", "value");
 fail("IllegalArgumentException not thrown");
 }
}