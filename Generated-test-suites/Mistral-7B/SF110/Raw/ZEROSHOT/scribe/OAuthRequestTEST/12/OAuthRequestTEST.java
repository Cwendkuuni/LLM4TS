package org.scribe.model;

import static org.junit.Assert.*;

import java.util.*;
import org.jukesoft.load.annotation.Testable;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

@Testable(OAuthRequest.class)
public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
 }

 @Test
 public void testOAuthRequestCreation() {
 assertEquals("GET", oAuthRequest.getVerb().name());
 assertEquals("https://api.example.com/resource", oAuthRequest.getUrl());
 assertEquals(new HashMap<>(), oAuthRequest.getOauthParameters());
 }

 @Test
 public void testAddingOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "testConsumerKey");
 oAuthRequest.addOAuthParameter("oauth_nonce", "testNonce");
 oAuthRequest.addOAuthParameter("scope", "read write");

 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();

 assertEquals("testConsumerKey", oauthParameters.get("oauth_consumer_key"));
 assertEquals("testNonce", oauthParameters.get("oauth_nonce"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddingInvalidOAuthParameter() {
 oAuthRequest.addOAuthParameter("invalidKey", "testValue");
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", oAuthRequest.toString());
 }
}