package org.scribe; // assume ScribeTest is the package name for your test class

import static org.junit.Assert.*;

import java.util.*;
import org.scribe.*;
import org.scribe.model.*;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testOAuthRequestCreation() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameterKey() {
 oAuthRequest.addOAuthParameter("test", "testValue");
 oAuthRequest.addOAuthParameter("invalidKey", "testValue");
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_nonce", "testNonce");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("testNonce", oauthParameters.get("oauth_nonce"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 @Test
 public void testAddDuplicateOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_nonce", "testNonce");
 oAuthRequest.addOAuthParameter("oauth_nonce", "testNonce2");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("testNonce", oauthParameters.get("oauth_nonce"));
 }

 @Test
 public void testGetOAuthParameters() {
 oAuthRequest.addOAuthParameter("oauth_nonce", "testNonce");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals(2, oauthParameters.size());
 }

}