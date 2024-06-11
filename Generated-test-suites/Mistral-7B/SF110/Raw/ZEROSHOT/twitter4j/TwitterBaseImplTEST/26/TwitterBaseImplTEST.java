package test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

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
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 assertEquals("userName", twitterBaseImpl.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthentication() throws TwitterException, IllegalStateException {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getScreenName();
 fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 User user = twitterBaseImpl.fillInIDAndScreenName();
 assertEquals(user.getId(), twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuthentication() throws TwitterException, IllegalStateException {
 twitterBaseImpl.setAuthorization(null);
 try {
 twitterBaseImpl.getId();
 fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(final RateLimitStatusEvent event) {
 // implementation
 }

 @Override
 public void onRateLimitReached(final RateLimitStatusEvent event) {
 // implementation
 }
 };
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
 }

 @Test
 public void testHttpResponseReceived() {
 RateLimitStatusListener listener = new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(final RateLimitStatusEvent event) {
 // implementation
 }

 @Override
 public void onRateLimitReached(final RateLimitStatusEvent event) {
 // implementation
 }
 };
 twitterBaseImpl.addRateLimitStatusListener(listener);
 HttpResponseEvent event = new HttpResponseEvent(null, new HttpResponse(200, "OK"));
 twitterBaseImpl.httpResponseReceived(event);
 assertTrue(listener.onRateLimitStatusCalled);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
 assertNotSame("consumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
 assertNotSame("consumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
 }

 @After
 public void tearDown() {
 twitterBaseImpl = null;
 conf = null;
 auth = null;
 }
}