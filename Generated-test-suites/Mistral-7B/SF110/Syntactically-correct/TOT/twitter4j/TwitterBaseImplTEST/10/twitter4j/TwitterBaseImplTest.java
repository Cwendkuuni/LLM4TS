package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret")
 .build();
 auth = new BasicAuthorization("userName", "password");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() {
 String screenName = twitterBaseImpl.getScreenName();
 assertNotNull(screenName);
 }

 @Test
 public void testGetScreenName_NoAuth() {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getScreenName();
 fail();
 } catch (IllegalStateException e) {
 assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }
 }

 @Test
 public void testGetId() {
 long id = twitterBaseImpl.getId();
 assertNotNull(id);
 }

 @Test
 public void testGetId_NoAuth() {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getId();
 fail();
 } catch (IllegalStateException e) {
 assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // handle rate limit status
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // handle rate limit reached
 }
 });
 }

 @Test
 public void testOnRateLimitStatus() {
 twitterBaseImpl.onRateLimitStatus(new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 // handle rate limit status
 }
 });
 }

 @Test
 public void testOnRateLimitReached() {
 twitterBaseImpl.onRateLimitReached(new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 // handle rate limit reached
 }
 });
 }

 @Test
 public void testHttpResponseReceived() {
 // simulate HttpResponseEvent
 HttpResponseEvent httpResponseEvent = new HttpResponseEvent(new HttpResponse(200, ""), new TwitterException(420, "Rate limit exceeded"), true);
 twitterBaseImpl.httpResponseReceived(httpResponseEvent);
 // handle rate limit exceeded
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 assertNotNull(twitterBaseImpl.getAuthorization().getConsumerKey());
 assertNotNull(twitterBaseImpl.getAuthorization().getConsumerSecret());
 }

 @Test
 public void testGetOAuthRequestToken() {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken.getScreenName());
 assertNotNull(accessToken.getUserId());
 }

 @Test
 public void testGetOAuthAccessToken_InvalidVerifier() throws TwitterException {
 twitterBaseImpl.getOAuth().setOAuthAccessToken(new AccessToken("accessToken", "accessTokenSecret"));
 try {
 twitterBaseImpl.getOAuthAccessToken(new String[]{"invalidVerifier"});
 fail();
 } catch (TwitterException e) {
 assertEquals("Invalid verification code.", e.getMessage());
 }
 }

 @Test
 public void testGetOAuthAccessToken_NullVerifier() throws TwitterException {
 twitterBaseImpl.getOAuth().setOAuthAccessToken(new AccessToken("accessToken", "accessTokenSecret"));
 try {
 twitterBaseImpl.getOAuthAccessToken(null);
 fail();
 } catch (TwitterException e) {
 assertEquals("Verification code is required.", e.getMessage());
 }
 }
}