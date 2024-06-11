package org.scribe.model.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.rules.TestNameRule;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Rule
 public TestNameRule name = new TestNameRule();

 @Before
 public void setUp() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
 }

 // Test constructor
 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "https://api.example.com");
 assertNotNull(oauthRequest);
 }

 // Test addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oauthRequest.addOAuthParameter("key1", "value1");
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertEquals("value1", oauthParameters.get("key1"));
 }

 // Test checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalidParameter() {
 oauthRequest.addOAuthParameter("invalidKey", "value");
 oauthRequest.checkKey("invalidKey");
 }

 @Test
 public void testCheckKeyValidParameter() {
 oauthRequest.addOAuthParameter("oauth_key", "value");
 oauthRequest.checkKey("oauth_key");
 }

 // Test getOauthParameters method
 @Test
 public void testGetOauthParameters() {
 oauthRequest.addOAuthParameter("key1", "value1");
 oauthRequest.addOAuthParameter("key2", "value2");
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertEquals(2, oauthParameters.size());
 }

 // Test toString method
 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, https://api.example.com)", oauthRequest.toString());
 }

 // Test exception when adding non-valid OAuth parameter
 @Test(expected = IllegalArgumentException.class)
 public void testAddNonValidOAuthParameter() {
 oauthRequest.addOAuthParameter("nonValidKey", "value");
 }

 // Test exceptions when adding duplicate OAuth parameters
 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOAuthParameter() {
 oauthRequest.addOAuthParameter("key1", "value1");
 oauthRequest.addOAuthParameter("key1", "value2");
 }

 // Test exception when adding null value for OAuth parameter
 @Test(expected = IllegalArgumentException.class)
 public void testAddNullValueForOAuthParameter() {
 oauthRequest.addOAuthParameter("key1", null);
 }
}