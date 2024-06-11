package test;

import static org.junit.Assert.*;

import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setup() {
 conf = new Configuration();
 auth = new BasicAuthorization("consumerKey", "consumerSecret");
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.init();
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertZero(twitterBaseImpl.getId());
 twitterBaseImpl.init();
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotZero(twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getScreenName();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(new TestRateLimitStatusListener()));
 twitterBaseImpl.removeRateLimitStatusListener(new TestRateLimitStatusListener());
 assertFalse(twitterBaseImpl.rateLimitStatusListeners.contains(new TestRateLimitStatusListener()));
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(1000, 1000), true);
 Consumer<RateLimitStatusEvent> action = new TestConsumer<>();
 twitterBaseImpl.onRateLimitStatus(action);
 action.getOccurrences().size(); // should be > 0
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(1000, 1000), true);
 Consumer<RateLimitStatusEvent> action = new TestConsumer<>();
 twitterBaseImpl.onRateLimitReached(action);
 action.getOccurrences().size(); // should be > 0
 }

 @Test
 public void testHttpResponseReceived() {
 HttpResponseEvent event = new HttpResponseEvent(null, null, null, null, false);
 twitterBaseImpl.httpResponseReceived(event);
 assertTrue(event.getRateLimitStatus() != null);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 assertNotNull(twitterBaseImpl.getAuthorization().getConsumerKey());
 assertNotNull(twitterBaseImpl.getAuthorization().getConsumerSecret());
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerNullConsumerKey() {
 twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerNullConsumerSecret() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", null);
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken.getToken());
 assertNotNull(requestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken.getScreenName());
 assertNotZero(accessToken.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetOAuthAccessTokenWithoutAuthorization() throws TwitterException {
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
 assertNotNull(accessToken.getScreenName());
 assertNotZero(accessToken.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauthVerifier");
 assertNotNull(accessToken.getScreenName());
 assertNotZero(accessToken.getId());
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("screenName", 1L, "token", "tokenSecret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals("screenName", twitterBaseImpl.getScreenName());
 assertEquals(1L, twitterBaseImpl.getId());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutOAuthRequestToken() throws TwitterException {
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutOAuthVerifier() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 twitterBaseImpl.getOAuthAccessToken(requestToken);
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));
 twitterBaseImpl2.setOAuthAccessToken(new AccessToken("screenName", 1L, "token", "tokenSecret"));
 assertFalse(twitterBaseImpl.equals(twitterBaseImpl2));
 twitterBaseImpl.setOAuthAccessToken(new AccessToken("screenName", 1L, "token", "tokenSecret"));
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));
 }

 @Test
 public void testHashCode() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 int hashCode1 = twitterBaseImpl.hashCode();
 twitterBaseImpl2.setOAuthAccessToken(new AccessToken("screenName", 1L, "token", "tokenSecret"));
 int hashCode2 = twitterBaseImpl2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

}

class TestRateLimitStatusListener implements RateLimitStatusListener {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 getClass().getSimpleName() + ": onRateLimitStatus";
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 getClass().getSimpleName() + ": onRateLimitReached";
 }
}

class TestConsumer<T> implements Consumer<T> {
 private List<T> occurrences = new ArrayList<>();

 @Override
 public void accept(T t) {
 occurrences.add(t);
 }

 public List<T> getOccurrences() {
 return occurrences;
 }
}