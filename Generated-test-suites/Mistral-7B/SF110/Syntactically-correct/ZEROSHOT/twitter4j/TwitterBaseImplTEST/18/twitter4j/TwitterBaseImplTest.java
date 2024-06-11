package com.example.twitter4j.test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;

 @Before
 public void setUp() {
 Configuration conf = new Configuration();
 conf.setDebugEnabled(true);
 Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 String screenName = twitterBaseImpl.getScreenName();
 assertNotNull(screenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl.auth = null;
 try {
 twitterBaseImpl.getScreenName();
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 long id = twitterBaseImpl.getId();
 assertNotNull(id);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuth() throws TwitterException, IllegalStateException {
 twitterBaseImpl.auth = null;
 try {
 twitterBaseImpl.getId();
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 User user = twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(user);
 assertNotNull(user.getScreenName());
 assertNotNull(user.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener((Consumer<RateLimitStatusEvent>) event -> {
 // do something
 });
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
 twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true));
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(15, 30);
 twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true));
 }

 @Test
 public void testHttpResponseReceived() {
 HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(200, "OK", "{}"));
 twitterBaseImpl.httpResponseReceived(event);
 }

 @Test
 public void testGetAuthorization() {
 Authorization auth = twitterBaseImpl.getAuthorization();
 assertNotNull(auth);
 }

 @Test
 public void testGetConfiguration() {
 Configuration conf = twitterBaseImpl.getConfiguration();
 assertNotNull(conf);
 }

 @Test
 public void testEnsureAuthorizationEnabled() {
 twitterBaseImpl.auth = new NullAuthorization();
 try {
 twitterBaseImpl.ensureAuthorizationEnabled();
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testEnsureOAuthEnabled() {
 twitterBaseImpl.auth = new BasicAuthorization("consumerKey", "consumerSecret");
 try {
 twitterBaseImpl.ensureOAuthEnabled();
 } catch (IllegalStateException e) {
 fail("IllegalStateException not expected");
 }
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 assertTrue(twitterBaseImpl.getAuthorization() instanceof OAuthAuthorization);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthConsumerTwice() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 try {
 twitterBaseImpl.setOAuthConsumer("consumerKey2", "consumerSecret2");
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken);
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("123", "456");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken.getScreenName(), twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("screenName", "password");
 assertNotNull(accessToken);
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("consumerKey", "consumerSecret"));
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("consumerKey", "consumerSecret"));
 int hashCode1 = twitterBaseImpl.hashCode();
 int hashCode2 = twitterBaseImpl2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }
}