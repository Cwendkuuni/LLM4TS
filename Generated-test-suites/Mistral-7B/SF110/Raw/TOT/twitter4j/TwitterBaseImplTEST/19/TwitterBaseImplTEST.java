package twitter4j.test;

import static org.junit.Assert.*;
import static twitter4j.conf.Configuration.Builder.*;
import static twitter4j.conf.Configuration.Property.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBase;

 @Before
 public void setup() {
 Configuration conf = new Configuration.Builder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret")
 .build();
 twitterBase = new TwitterBaseImpl(conf, new NullAuthorization());
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertNull(twitterBase.getScreenName());
 twitterBase.setOAuthConsumer("testUser@test.com", "testPassword");
 User user = twitterBase.fillInIDAndScreenName();
 assertEquals("testUser", user.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithNoAuth() throws TwitterException, IllegalStateException {
 twitterBase.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertNull(twitterBase.getId());
 twitterBase.setOAuthConsumer("testUser@test.com", "testPassword");
 User user = twitterBase.fillInIDAndScreenName();
 assertEquals(1L, user.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithNoAuth() throws TwitterException, IllegalStateException {
 twitterBase.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBase.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 assertEquals(RATE_LIMIT_STATUS.RATE_LIMITED, event.getRateLimitStatus().getLimit());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Rate limit reached should not be called");
 }
 });

 twitterBase.onRateLimitReached((Consumer<RateLimitStatusEvent>)event -> {
 fail("Rate limit reached listener should not be called");
 });
 }

 @Test
 public void testRateLimitStatusListeners() {
 twitterBase.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 assertEquals(RATE_LIMIT_STATUS.RATE_LIMITED, event.getRateLimitStatus().getLimit());
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Rate limit reached listener should not be called");
 }
 });

 twitterBase.onRateLimitReached((Consumer<RateLimitStatusEvent>)event -> {
 fail("Rate limit reached listener should not be called");
 });

 HttpResponseEvent responseEvent = new HttpResponseEvent(null, null, null, null, null, null);
 twitterBase.httpResponseReceived(responseEvent);

 List<RateLimitStatusListener> listeners = twitterBase.getRateLimitStatusListeners();
 assertEquals(2, listeners.size());
 }
}