package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JUnit4Mockito;

@RunWith(JUnit4Mockito.class)
public class OAuthRequestTest {

 @Mock
 Request request;

 @Test
 public void testOAuthRequestCreation() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
  assertEquals(new HashMap<>(), oauthRequest.getOauthParameters());
 }

 @Test
 public void testAddOAuthParameterValidKey() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  String parameterKey = "oauth_token";
  String parameterValue = "abc123";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  oauthRequest.addOAuthParameter(parameterKey, parameterValue);
  Map<String, String> expectedParameters = new HashMap<>();
  expectedParameters.put(parameterKey, parameterValue);
  assertEquals(expectedParameters, oauthRequest.getOauthParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterInvalidKey() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  String invalidParameterKey = "invalid_key";
  String parameterValue = "abc123";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  oauthRequest.addOAuthParameter(invalidParameterKey, parameterValue);
 }

 @Test
 public void testAddOAuthParameterDuplicateKey() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  String parameterKey = "oauth_token";
  String parameterValue1 = "abc123";
  String parameterValue2 = "def456";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  oauthRequest.addOAuthParameter(parameterKey, parameterValue1);
  oauthRequest.addOAuthParameter(parameterKey, parameterValue2);
  Map<String, String> expectedParameters = new HashMap<>();
  expectedParameters.put(parameterKey, parameterValue1);
  assertEquals(expectedParameters, oauthRequest.getOauthParameters());
 }

 @Test
 public void testToStringWithOAuthParameters() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  String parameterKey = "oauth_token";
  String parameterValue = "abc123";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  oauthRequest.addOAuthParameter(parameterKey, parameterValue);
  String expectedToString = String.format("@OAuthRequest(GET, http://example.com) %s", oauthRequest.getOauthParameters().toString());
  assertEquals(expectedToString, oauthRequest.toString());
 }

 @Test
 public void testGetOAuthParameterValue() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  String parameterKey = "oauth_token";
  String parameterValue = "abc123";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  oauthRequest.addOAuthParameter(parameterKey, parameterValue);
  String expectedValue = "abc123";
  assertEquals(expectedValue, oauthRequest.getOauthParameters().get(parameterKey));
 }

 @Test
 public void testGetNonOAuthParameterValue() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  String nonOauthParameterKey = "non_oauth_parameter";
  String parameterValue = "abc123";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  oauthRequest.setParameter(nonOauthParameterKey, parameterValue);
  String expectedValue = "abc123";
  assertEquals(expectedValue, oauthRequest.getParameter(nonOauthParameterKey));
  assertNull(oauthRequest.getOauthParameters().get(nonOauthParameterKey));
 }

 @Test
 public void testGetNullOAuthParameterValue() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  String parameterKey = "oauth_token";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
  assertNull(oauthParameters.get(parameterKey));
 }

 @Test
 public void testGetEmptyOAuthParameterMap() {
  Verb verb = Verb.GET;
  String url = "http://example.com";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
  assertEquals(0, oauthParameters.size());
 }

 @Test
 public void testGetOAuthParametersWithMockRequest() {
  Verb verb = Mockito.mock(Verb.class);
  String url = "http://example.com";
  String parameterKey = "oauth_token";
  String parameterValue = "abc123";
  OAuthRequest oauthRequest = new OAuthRequest(verb, url);
  Mockito.when(verb.name()).thenReturn("GET");
  oauthRequest.addOAuthParameter(parameterKey, parameterValue);
  Map<String, String> expectedParameters = new HashMap<>();
  expectedParameters.put(parameterKey, parameterValue);
  Mockito.verify(request, Mockito.atLeastOnce()).getOauthParameters();
  assertEquals(expectedParameters, oauthRequest.getOauthParameters());
 }
}