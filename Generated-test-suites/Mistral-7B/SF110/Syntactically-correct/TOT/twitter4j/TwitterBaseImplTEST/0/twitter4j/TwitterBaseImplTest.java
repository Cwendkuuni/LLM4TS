package twitter4j.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.RateLimitStatusListener;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration configuration;
 private Authorization auth;

 @Before
 public void setUp() {
 ConfigurationBuilder cb = new ConfigurationBuilder();
 configuration = new Configuration(cb.build());
 auth = mock(Authorization.class);
 twitterBaseImpl = new TwitterBaseImpl(configuration, auth);
 }

 @Test
 public void testGetScreenName() {
 // Test with basic authentication
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getUserId()).thenReturn("testUser");
 twitterBaseImpl.setFactory();
 assertEquals("testUser", twitterBaseImpl.getScreenName());

 // Test with OAuth authentication
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getOAuthAccessToken().getScreenName()).thenReturn("testUserOAuth");
 twitterBaseImpl.setFactory();
 assertEquals("testUserOAuth", twitterBaseImpl.getScreenName());

 // Test with no authentication
 when(auth.isEnabled()).thenReturn(false);
 assertNull(twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() {
 // Test with basic authentication
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getUserId()).thenReturn(123L);
 twitterBaseImpl.setFactory();
 assertEquals(123L, twitterBaseImpl.getId());

 // Test with OAuth authentication
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getOAuthAccessToken().getUserId()).thenReturn(456L);
 twitterBaseImpl.setFactory();
 assertEquals(456L, twitterBaseImpl.getId());

 // Test with no authentication
 when(auth.isEnabled()).thenReturn(false);
 assertNull(twitterBaseImpl.getId());
 }

 @Test
 public void testFillInIDAndScreenName() {
 User user = new User();
 user.setScreenName("testUser");
 user.setId(123L);
 when(auth.isEnabled()).thenReturn(true);
 when(http.get(anyString(), any(HttpParameter[].class), any(Authorization.class), eq(configuration)))
 .thenReturn(new ByteArrayInputStream(new byte[0]));
 twitterBaseImpl = new TwitterBaseImpl(configuration, auth);
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals("testUser", twitterBaseImpl.getScreenName());
 assertEquals(123L, twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthentication() {
 twitterBaseImpl = new TwitterBaseImpl(configuration, auth);
 assertThrows(IllegalStateException.class, twitterBaseImpl::getScreenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuthentication() {
 twitterBaseImpl = new TwitterBaseImpl(configuration, auth);
 assertThrows(IllegalStateException.class, twitterBaseImpl::getId);
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
 }

 @Test
 public void testOnRateLimitReached() {
 Consumer<RateLimitStatusEvent> action = mock(Consumer.class);
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 when(event.getRateLimitStatus().getLimit().getName()).thenReturn("limitName");
 when(event.getRateLimitStatus().getLimit().getLimitPerHour()).thenReturn(100L);
 when(event.getRateLimitStatus().getLimit().getRemaining()).thenReturn(50L);
 when(event.getRateLimitStatus().getLimit().getResetTime()).thenReturn(System.currentTimeMillis() + 3600000L);
 twitterBaseImpl.onRateLimitReached(action);
 assertEquals("limitName", action.getArguments()[0].getClass().getSimpleName());
 assertEquals(event, action.getArguments()[0]);
 }

 @After
 public void tearDown() {
 twitterBaseImpl = null;
 auth = null;
 configuration = null;
 }
}