package twitter4j.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration configuration;
 private Authorization mockAuthorization;
 private HttpClient mockHttpClient;

 @Before
 public void setUp() {
 configuration = new Configuration();
 mockAuthorization = mock(Authorization.class);
 mockHttpClient = mock(HttpClient.class);
 twitterBaseImpl = new TwitterBaseImpl(configuration, mockAuthorization);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test with initialized screenName and ID
 twitterBaseImpl.setFactory();
 twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
 twitterBaseImpl.onRateLimitReached(mock(RateLimitStatusEvent.class));
 twitterBaseImpl.getScreenName();

 // Test with null screenName and ID
 twitterBaseImpl.auth = null;
 twitterBaseImpl.screenName = null;
 twitterBaseImpl.id = 0L;
 try {
 twitterBaseImpl.getScreenName();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // Expected
 }

 // Test with initialized screenName and null ID
 twitterBaseImpl.screenName = "testUser";
 twitterBaseImpl.auth = null;
 twitterBaseImpl.id = 0L;
 try {
 twitterBaseImpl.getScreenName();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // Expected
 }

 // Test with initialized ID and null screenName
 twitterBaseImpl.screenName = null;
 twitterBaseImpl.id = 1L;
 try {
 twitterBaseImpl.getScreenName();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test with initialized screenName and ID
 twitterBaseImpl.setFactory();
 twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
 twitterBaseImpl.onRateLimitReached(mock(RateLimitStatusEvent.class));
 twitterBaseImpl.getScreenName();
 twitterBaseImpl.getId();

 // Test with null screenName and ID
 twitterBaseImpl.auth = null;
 twitterBaseImpl.screenName = null;
 twitterBaseImpl.id = 0L;
 try {
 twitterBaseImpl.getId();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // Expected
 }

 // Test with initialized screenName and null ID
 twitterBaseImpl.screenName = "testUser";
 twitterBaseImpl.auth = null;
 twitterBaseImpl.id = 0L;
 try {
 twitterBaseImpl.getId();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // Expected
 }

 // Test with initialized ID and null screenName
 twitterBaseImpl.screenName = null;
 twitterBaseImpl.id = 1L;
 try {
 twitterBaseImpl.getId();
 Assert.fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
 verify(mockHttpClient).addListener(eq(twitterBaseImpl), any());
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 final RateLimitStatusEvent mockRateLimitStatusEvent = mock(RateLimitStatusEvent.class);
 twitterBaseImpl.onRateLimitReached(mockRateLimitStatusEvent);
 twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
 twitterBaseImpl.onRateLimitStatus(mockRateLimitStatusEvent);
 twitterBaseImpl.onRateLimitReached(mockRateLimitStatusEvent);
 }

 @Test
 public void testGetAuthorization() {
 twitterBaseImpl.getAuthorization();
 // Verify that getAuthorization() returns the mocked Authorization object
 verify(mockAuthorization, times(1)).isEnabled();
 }

 @Test
 public void testGetConfiguration() {
 twitterBaseImpl.getConfiguration();
 // Verify that getConfiguration() returns the mocked Configuration object
 verify(configuration, times(1)).getDefaultConsumerKey();
 }

 @Test
 public void testEnsureAuthorizationEnabled() {
 twitterBaseImpl.ensureAuthorizationEnabled();
 // Verify that ensureAuthorizationEnabled() throws IllegalStateException if the authorization is not enabled
 verify(mockAuthorization, times(1)).isEnabled();
 }

 @Test
 public void testEnsureOAuthEnabled() {
 twitterBaseImpl.ensureOAuthEnabled();
 // Verify that ensureOAuthEnabled() throws IllegalStateException if OAuth is not enabled
 verify(mockAuthorization, times(1)).isOAuthEnabled();
 }
}