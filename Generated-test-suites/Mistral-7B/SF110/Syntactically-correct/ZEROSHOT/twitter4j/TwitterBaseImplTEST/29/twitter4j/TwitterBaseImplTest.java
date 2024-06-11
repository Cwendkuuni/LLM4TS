package twitter4j.test;

import org.junit.Assert;
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
 private Authorization authorization;
 private OAuthSupport oAuth;

 @Before
 public void setUp() {
 configuration = new ConfigurationBuilder().build();
 authorization = mock(Authorization.class);
 oAuth = mock(OAuthSupport.class);
 twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Given
 twitterBaseImpl.setAuthorization(null, null);

 // When
 String screenName = twitterBaseImpl.getScreenName();

 // Then
 Assert.assertNull(screenName);
 }

 @Test
 public void testGetScreenNameWithBasicAuthorization() throws TwitterException, IllegalStateException {
 // Given
 when(authorization.isEnabled()).thenReturn(true);
 when(authorization.getUserId()).thenReturn("testUser");
 twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);

 // When
 String screenName = twitterBaseImpl.getScreenName();

 // Then
 Assert.assertEquals("testUser", screenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithNoAuthorization() throws TwitterException, IllegalStateException {
 // Given
 twitterBaseImpl.setAuthorization(null, null);

 // When
 twitterBaseImpl.getScreenName();

 // Then
 // IllegalStateException should be thrown
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Given
 when(authorization.isEnabled()).thenReturn(true);
 when(authorization.getUserId()).thenReturn(1L);
 twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);

 // When
 long id = twitterBaseImpl.getId();

 // Then
 Assert.assertEquals(1L, id);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithNoAuthorization() throws TwitterException, IllegalStateException {
 // Given
 twitterBaseImpl.setAuthorization(null, null);

 // When
 twitterBaseImpl.getId();

 // Then
 // IllegalStateException should be thrown
 }

 @Test
 public void testSetFactory() {
 // Given
 TwitterBaseImpl twitterBaseImplSpy = spy(twitterBaseImpl);
 JSONImplFactory jsonImplFactory = mock(JSONImplFactory.class);

 // When
 twitterBaseImplSpy.setFactory();
 twitterBaseImplSpy.getFactory();

 // Then
 verify(twitterBaseImplSpy).setFactory();
 verify(twitterBaseImplSpy).setFactory(); // Should be called twice (first time for initialization, second time for test)
 verify(jsonImplFactory).isSingleton();
 }

 @Test
 public void testOnRateLimitStatus() {
 // Given
 Consumer<RateLimitStatusEvent> rateLimitStatusConsumer = arg -> {};
 RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusConsumer);
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusConsumer);
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusEvent);

 // When
 RateLimitStatusEvent newRateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 twitterBaseImpl.onRateLimitStatus(newRateLimitStatusEvent);

 // Then
 verify(rateLimitStatusConsumer).accept(rateLimitStatusEvent);
 verify(rateLimitStatusConsumer).accept(newRateLimitStatusEvent);
 verify(rateLimitStatusEvent).handleRateLimitStatus(rateLimitStatusConsumer);
 verify(rateLimitStatusEvent).handleRateLimitStatus(rateLimitStatusConsumer);
 }

 @Test
 public void testGetAuthorization() {
 // Given

 // When
 Authorization auth = twitterBaseImpl.getAuthorization();

 // Then
 Assert.assertSame(authorization, auth);
 }

 @Test
 public void testGetConfiguration() {
 // Given

 // When
 Configuration conf = twitterBaseImpl.getConfiguration();

 // Then
 Assert.assertSame(configuration, conf);
 }

}